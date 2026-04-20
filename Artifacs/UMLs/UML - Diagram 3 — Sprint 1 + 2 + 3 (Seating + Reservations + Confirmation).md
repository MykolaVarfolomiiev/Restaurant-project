
```mermaid
classDiagram
    namespace Domain {

        class DiningTable {
            +Long id
            +String name
            +int capacity
            +LocalDateTime createdDate
        }

        class Seating {
            +Long id
            +Long eventId
            +String name
            +LocalDateTime startDateTime
            +int duration
            +LocalDateTime createdDate
            +boolean archived
        }

        class ReservationRequest {
            +Long id
            +UUID uuid
            +String firstName
            +String lastName
            +String email
            +int groupSize
            +String status
        }
    }

    namespace Application {

        class DiningTableService {
            <<interface>>
            +getAll() List~DiningTable~
            +get(Long id) DiningTable
            +create(DiningTable table) DiningTable
            +update(DiningTable table) DiningTable
            +delete(Long id) void
        }

        class DiningTableRepository {
            <<interface>>
            +getAll() List~DiningTable~
            +get(Long id) Optional~DiningTable~
            +create(DiningTable table) DiningTable
            +update(DiningTable table) DiningTable
            +delete(Long id) void
        }

        class SeatingService {
            <<interface>>
            +getAll(Long eventId) List~Seating~
            +get(Long id) Seating
            +create(Seating seating) Seating
            +update(Seating seating) Seating
            +deleteOrArchive(Long id) void
        }

        class SeatingRepository {
            <<interface>>
            +getAll(Long eventId) List~Seating~
            +get(Long id) Optional~Seating~
            +create(Seating seating) Seating
            +update(Seating seating) Seating
            +delete(Long id) void
        }

        class ReservationService {
            <<interface>>
            +getAll(Long eventId, String status) List~ReservationRequest~
            +get(Long id) ReservationRequest
            +getByUUID(UUID uuid) ReservationRequest
            +create(ReservationRequest request) ReservationRequest
            +approve(Long id, Long tableId) ReservationRequest
            +deny(Long id) ReservationRequest
            +getAllApproved(Long eventId) List~ReservationRequest~
        }

        class ReservationRepository {
            <<interface>>
            +getAll(Long eventId, String status) List~ReservationRequest~
            +get(Long id) Optional~ReservationRequest~
            +getByUUID(UUID uuid) Optional~ReservationRequest~
            +create(ReservationRequest request) ReservationRequest
            +update(ReservationRequest request) ReservationRequest
            +getAllApproved(Long eventId) List~ReservationRequest~
        }

        class EmailService {
            <<interface>>
            +sendReservationReceived(ReservationRequest request) void
            +sendStatusUpdate(ReservationRequest request) void
        }

        class ReservationApiService {
            <<interface>>
            +create(Long eventId, Long seatingId, String firstName, String lastName, String email, int groupSize) ReservationRequest
        }
    }

    namespace Persistence {

        class DiningTableEntity {
            +Long id
            +String name
            +int capacity
            +LocalDateTime createdDate
        }

        class SeatingEntity {
            +Long id
            +Long eventId
            +String name
            +LocalDateTime startDateTime
            +int duration
            +LocalDateTime createdDate
            +boolean archived
            +List~DiningTableEntity~ tables
        }

        class ReservationRequestEntity {
            +Long id
            +UUID uuid
            +String firstName
            +String lastName
            +String email
            +int groupSize
            +String status
            +SeatingEntity seating
            +DiningTableEntity table
        }
    }

    Seating --> "0..*" DiningTable : uses
    ReservationRequest --> "1" Seating : belongsTo
    ReservationRequest --> "0..1" DiningTable : assignedTo
    SeatingEntity --> "0..*" DiningTableEntity : uses
    ReservationRequestEntity --> "1" SeatingEntity : belongsTo
    ReservationService ..> EmailService : uses
    DiningTableRepository ..> DiningTable : uses
    SeatingRepository ..> Seating : uses
    ReservationRepository ..> ReservationRequest : uses
    ```