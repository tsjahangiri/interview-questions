import java.util.*;

public class EventRegistrationSystem {

    // ─────────────────────────────────────────────
    //  Inner Data Classes
    // ─────────────────────────────────────────────

    enum EventType { CONFERENCE, WORKSHOP, WEBINAR }

    static class Event {
        String id;
        EventType type;
        int capacity;
        boolean requiresBusinessEmail;
        String requiredSkillLevel; // null if not required

        Set<String> registered = new LinkedHashSet<>();
        Queue<String[]> waitlist = new LinkedList<>(); // [participantId, email, skillLevel]
        Set<String> allParticipants = new HashSet<>(); // registered + waitlisted (duplicate prevention)

        Event(String id, EventType type, int capacity,
              boolean requiresBusinessEmail, String requiredSkillLevel) {
            this.id = id;
            this.type = type;
            this.capacity = capacity;
            this.requiresBusinessEmail = requiresBusinessEmail;
            this.requiredSkillLevel = requiredSkillLevel;
        }
    }

    // ─────────────────────────────────────────────
    //  State
    // ─────────────────────────────────────────────

    private final Map<String, Event> events = new HashMap<>();
    private final Map<String, Set<String>> participantEvents = new HashMap<>();

    // ─────────────────────────────────────────────
    //  1. addEvent
    // ─────────────────────────────────────────────

    public boolean addEvent(String eventId, String eventType, int capacity, String[] requirements) {

        if (eventId == null || eventId.isEmpty()) return false;
        if (capacity <= 0) return false;
        if (events.containsKey(eventId)) return false;

        EventType type;
        try {
            type = EventType.valueOf(eventType);
        } catch (Exception e) {
            return false;
        }

        boolean requiresBusinessEmail = false;
        String requiredSkillLevel = null;

        if (requirements != null) {
            for (String token : requirements) {
                if (token == null || token.trim().isEmpty()) continue;
                token = token.trim();

                if (token.equals("business_email")) {
                    requiresBusinessEmail = true;
                } else if (token.startsWith("skill_level:")) {
                    String level = token.substring("skill_level:".length());
                    if (!isValidSkillLevel(level)) return false;
                    requiredSkillLevel = level;
                } else {
                    return false;
                }
            }
        }

        events.put(eventId, new Event(eventId, type, capacity,
                requiresBusinessEmail, requiredSkillLevel));
        return true;
    }

    // ─────────────────────────────────────────────
    //  2. registerParticipant
    // ─────────────────────────────────────────────

    public int registerParticipant(String eventId, String participantId,
                                   String email, String skillLevel) {

        if (eventId == null || eventId.isEmpty()) return -1;
        if (participantId == null || participantId.isEmpty()) return -1;
        if (email == null || email.isEmpty()) return -1;
        if (skillLevel == null || skillLevel.isEmpty()) return -1;

        Event event = events.get(eventId);
        if (event == null) return -1;

        if (!isValidSkillLevel(skillLevel)) return -1;

        if (event.allParticipants.contains(participantId)) return -1;

        if (event.type == EventType.CONFERENCE) {
            if (event.requiresBusinessEmail && !isBusinessEmail(email)) return -1;
        }

        if (event.type == EventType.WORKSHOP) {
            if (event.requiredSkillLevel != null
                    && !event.requiredSkillLevel.equals(skillLevel)) return -1;
        }

        event.allParticipants.add(participantId);

        if (event.registered.size() < event.capacity) {
            event.registered.add(participantId);
            participantEvents
                    .computeIfAbsent(participantId, k -> new HashSet<>())
                    .add(eventId);
            return 0;
        } else {
            event.waitlist.add(new String[]{participantId, email, skillLevel});
            return getWaitlistPosition(event, participantId);
        }
    }

    // ─────────────────────────────────────────────
    //  3. cancelRegistration
    // ─────────────────────────────────────────────

    public boolean cancelRegistration(String eventId, String participantId) {

        if (eventId == null || eventId.isEmpty()) return false;
        if (participantId == null || participantId.isEmpty()) return false;

        Event event = events.get(eventId);
        if (event == null) return false;
        if (!event.allParticipants.contains(participantId)) return false;

        event.allParticipants.remove(participantId);

        boolean wasRegistered = event.registered.remove(participantId);

        if (!wasRegistered) {
            event.waitlist.removeIf(entry -> entry[0].equals(participantId));
            return true;
        }

        Set<String> pEvents = participantEvents.get(participantId);
        if (pEvents != null) pEvents.remove(eventId);

        promoteFromWaitlist(event);

        return true;
    }

    // ─────────────────────────────────────────────
    //  4. findRegisteredEvents
    // ─────────────────────────────────────────────

    public List<String> findRegisteredEvents(String participantId) {
        if (participantId == null || participantId.isEmpty()) return Collections.emptyList();

        Set<String> pEvents = participantEvents.get(participantId);
        if (pEvents == null || pEvents.isEmpty()) return Collections.emptyList();

        List<String> result = new ArrayList<>(pEvents);
        Collections.sort(result);
        return result;
    }

    // ─────────────────────────────────────────────
    //  Private Helpers
    // ─────────────────────────────────────────────

    private void promoteFromWaitlist(Event event) {
        if (event.waitlist.isEmpty()) return;
        if (event.registered.size() >= event.capacity) return;

        String[] next = event.waitlist.poll();
        String promotedId = next[0];

        event.registered.add(promotedId);
        participantEvents
                .computeIfAbsent(promotedId, k -> new HashSet<>())
                .add(event.id);
    }

    private int getWaitlistPosition(Event event, String participantId) {
        int pos = 0;
        for (String[] entry : event.waitlist) {
            pos++;
            if (entry[0].equals(participantId)) return pos;
        }
        return -1;
    }

    private boolean isBusinessEmail(String email) {
        if (email == null) return false;
        String lower = email.toLowerCase();
        return lower.contains("@company") || lower.contains("@business");
    }

    private boolean isValidSkillLevel(String level) {
        return "BEGINNER".equals(level)
                || "INTERMEDIATE".equals(level)
                || "ADVANCED".equals(level);
    }

    // ─────────────────────────────────────────────
    //  Main – stdin command processor
    // ─────────────────────────────────────────────

    public static void main(String[] args) {
        EventRegistrationSystem system = new EventRegistrationSystem();
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            String command = parts[0];

            switch (command) {

                case "addEvent": {
                    if (parts.length < 4) { System.out.println("false"); break; }
                    String eventId   = parts[1];
                    String eventType = parts[2];
                    int capacity;
                    try { capacity = Integer.parseInt(parts[3]); }
                    catch (NumberFormatException e) { System.out.println("false"); break; }

                    String[] requirements = parts.length > 4
                            ? Arrays.copyOfRange(parts, 4, parts.length)
                            : new String[0];

                    System.out.println(system.addEvent(eventId, eventType, capacity, requirements));
                    break;
                }

                case "registerParticipant": {
                    if (parts.length < 5) { System.out.println(-1); break; }
                    System.out.println(system.registerParticipant(
                            parts[1], parts[2], parts[3], parts[4]));
                    break;
                }

                case "cancelRegistration": {
                    if (parts.length < 3) { System.out.println("false"); break; }
                    System.out.println(system.cancelRegistration(parts[1], parts[2]));
                    break;
                }

                case "findRegisteredEvents": {
                    if (parts.length < 2) { System.out.println("[]"); break; }
                    List<String> result = system.findRegisteredEvents(parts[1]);
                    System.out.println(result);
                    break;
                }

                default:
                    System.out.println("Unknown command: " + command);
            }
        }
        scanner.close();
    }
}