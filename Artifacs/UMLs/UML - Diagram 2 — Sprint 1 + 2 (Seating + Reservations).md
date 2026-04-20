```mermaid
classDiagram
    namespace Domain {

        class Result~T~ {
            <<interface>>
            +getValue() T
            +isEmpty() boolean
            +hasValue() boolean
            +isSuccessful() boolean
            +isError() boolean
        }

        class HasValidationErrors {
            <<interface>>
            +getValidationErrors() Collection~ValidationError~
            +isInvalid() boolean
        }

        class ValidatedResult~T~ {
            <<interface>>
            +getValidationErrors() Collection~ValidationError~
            +isInvalid() boolean
            +isSuccessful() boolean
        }

        class ValidationError {
            +String field
            +String message
            +Object value
        }

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
            +LocalDateTime updatedAt
            +getTableIds() List~Long~
        }

        class ReservationRequest {
            +Long id
            +UUID uuid
            +String firstName
            +String lastName
            +String email
            +int groupSize
            +String status
            +getSeatingId() Long
            +getTableId() Long
        }
    }

    namespace Application {

        class DiningTableService {
            <<interface>>
            +getAll() Result~List~DiningTable~~
            +get(Long id) Result~DiningTable~
            +create(DiningTable table) Result~DiningTable~
            +update(DiningTable table) Result~DiningTable~
            +delete(Long id) Result~Void~
        }

        class DiningTableValidationService {
            +validate(DiningTable table) Collection~ValidationError~
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
            +getAll(Long eventId) Result~List~Seating~~
            +get(Long id) Result~Seating~
            +create(Seating seating) Result~Seating~
            +update(Seating seating) Result~Seating~
            +deleteOrArchive(Long id) Result~Void~
        }

        class SeatingValidationService {
            +validate(Seating seating) Collection~ValidationError~
            +validateNoOverlap(Seating seating) Collection~ValidationError~
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
            +getAll(Long eventId, String status) Result~List~ReservationRequest~~
            +get(Long id) Result~ReservationRequest~
            +getByUUID(UUID uuid) Result~ReservationRequest~
            +create(ReservationRequest request) Result~ReservationRequest~
            +approve(Long id, Long tableId) Result~ReservationRequest~
            +deny(Long id) Result~ReservationRequest~
        }

        class ReservationValidationService {
            +validate(ReservationRequest request) Collection~ValidationError~
            +validateTableAvailability(Long tableId, Long seatingId) Collection~ValidationError~
        }

        class ReservationRepository {
            <<interface>>
            +getAll(Long eventId, String status) List~ReservationRequest~
            +get(Long id) Optional~ReservationRequest~
            +getByUUID(UUID uuid) Optional~ReservationRequest~
            +create(ReservationRequest request) ReservationRequest
            +update(ReservationRequest request) ReservationRequest
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
            +LocalDateTime updatedAt
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

    Seating --> "0..*" DiningTable : contains
    ReservationRequest --> "1" Seating : belongsTo
    ReservationRequest --> "0..1" DiningTable : assignedTo
    SeatingEntity --> "0..*" DiningTableEntity : contains
    ReservationRequestEntity --> "1" SeatingEntity : belongsTo
    ReservationRequestEntity --> "0..1" DiningTableEntity : assignedTo

    DiningTableService ..> Result : uses
    SeatingService ..> Result : uses
    ReservationService ..> Result : uses

    DiningTableService ..> DiningTableValidationService : Is Validated By
    SeatingService ..> SeatingValidationService : Is Validated By
    ReservationService ..> ReservationValidationService : Is Validated By

    Result~T~ <|-- ValidatedResult~T~ : extends
    HasValidationErrors <|-- ValidatedResult~T~ : extends

    HasValidationErrors ..> ValidationError : uses
    ValidatedResult~T~ ..> ValidationError : uses

    DiningTableRepository ..> DiningTable : uses
    SeatingRepository ..> Seating : uses
    ReservationRepository ..> ReservationRequest : uses
    ```