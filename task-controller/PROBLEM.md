# TaskController - Spring Boot REST API

## Description

Implement a `TaskController` class to expose CRUD operations on `Task` entities via HTTP using Spring Boot.

## Requirements

### Create one

- **URL:** `POST /tasks`
- **Payload:** `{"title":"Task 1","description":"Some description"}`
- **Response:** Status `200`, Body: the `id` of the newly created task (e.g. `13`)
- Only `title` and `description` can be set during creation.

### Read one

- **URL:** `GET /tasks/{id}`
- **Response:**
  - Found → Status `200`, Body: `{"id":"1","title":"Task 1","description":"Some description","status":"CREATED"}`
  - Not found → Status `204`

### Update one

- **URL:** `PUT /tasks/{id}`
- **Payload:** `{"title":"Task 1.1","description":"New description","status":"BLOCKED"}`
- **Response:**
  - Found → Status `200`
  - Not found → Status `204`
  - Invalid status → Status `400`, Body: `Available statuses are: CREATED, APPROVED, REJECTED, BLOCKED, DONE.`

### Delete one

- **URL:** `DELETE /tasks/{id}`
- **Response:**
  - Found → Status `200`
  - Not found → Status `204`

### Get all

- **URL:** `GET /tasks`
- **Response:** Status `200`, Body: array of all task DTOs

## Data Model

```java
// TaskStatus enum
public enum TaskStatus {
    CREATED, APPROVED, REJECTED, BLOCKED, DONE
}

// TaskDto
public class TaskDto {
    private String id;
    private String title;
    private String description;
    private String status;
    // getters ...
}

// Task entity
@Entity
public class Task {
    @Id @GeneratedValue
    private Long id;
    private String title;
    private String description;
    private TaskStatus status = CREATED;

    public Task(String title) { this.title = title; }
    public void setTitle(String title) { ... }
    public void setDescription(String description) { ... }
    public void setTaskStatus(TaskStatus status) { ... }
    public Long getId() { ... }
    public TaskDto toDto() { ... }
}

// TaskRepository
public interface TaskRepository extends CrudRepository<Task, Long> {}
```

## Notes

- A `TaskRepository` is already injected into `TaskController`.
- Use `TaskDto` to receive input and send responses.
- No security or transaction handling required.
