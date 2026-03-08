# Event Registration System

Implement an Event Registration System that manages different types of events, handles participant registrations, manages waitlists, and provides analytics. The system processes commands through standard input and outputs results to standard output.

---

## EVENT TYPES

The system supports three types of events with different registration requirements:

**1. CONFERENCE**
- Requires business email addresses
- Business email must contain `@company` OR `@business` in the domain
- Example: `john@company.com`, `jane@business.org`

**2. WORKSHOP**
- Requires matching skill levels
- Valid skill levels: `BEGINNER`, `INTERMEDIATE`, `ADVANCED`
- Participant skill level must match the workshop requirement

**3. WEBINAR**
- No special requirements
- Anyone can register regardless of email or skill level

---

## METHODS TO IMPLEMENT

**`addEvent(eventId, eventType, capacity, requirements)`**
- Returns `true` if successful, `false` if duplicate ID or invalid parameters

**`registerParticipant(eventId, participantId, email, skillLevel)`**
- Returns `0` for success, positive number for waitlist position, `-1` for failure

**`cancelRegistration(eventId, participantId)`**
- Returns `true` if successful, `false` otherwise

**`findRegisteredEvents(participantId)`**
- Returns sorted array of event IDs where the participant is registered successfully (not waitlisted)

---

## INPUT FORMAT

Each line contains one command with space-separated parameters:

```
addEvent <eventId> <eventType> <capacity> <requirements>
registerParticipant <eventId> <participantId> <email> <skillLevel>
cancelRegistration <eventId> <participantId>
findRegisteredEvents <participantId>
```

**Requirements Parameter (String array):**
- Empty: `new String[]{}`
- Business email: `new String[]{"business_email"}`
- Skill level: `new String[]{"skill_level:BEGINNER"}`
- Multiple: `new String[]{"business_email", "skill_level:INTERMEDIATE"}`

---

## SAMPLE INPUT/OUTPUT

**Input:**
```
addEvent CONF001 CONFERENCE 100 business_email
addEvent WORK001 WORKSHOP 20 skill_level:BEGINNER
addEvent WEB001 WEBINAR 500
registerParticipant CONF001 P001 john@company.com BEGINNER
```

**Output:**
```
true
true
true
0
```

---

## BUSINESS RULES

1. No duplicate registrations for the same participant in the same event
2. Waitlist management: FIFO (first in, first out) promotion
3. Automatic promotion when capacity becomes available
4. Validation occurs before registration or waitlisting

---

## CONSTRAINTS

- Event IDs and participant IDs: non-empty strings
- Event capacity: positive integers only
- Event types: exactly `CONFERENCE`, `WORKSHOP`, `WEBINAR`
- Skill levels: exactly `BEGINNER`, `INTERMEDIATE`, `ADVANCED`
- Email addresses: non-empty strings
- Performance: handle up to 10,000 events and 100,000 participants
- All operations must complete within reasonable time limits

---

## ERROR HANDLING

- Invalid event types return `false`
- Non-existent events return appropriate error responses
- Invalid skill levels or empty required fields return `-1`
- Null or empty required parameters are handled gracefully
