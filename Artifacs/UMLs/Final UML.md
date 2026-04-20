```mermaid
classDiagram
    namespace Common {
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
    }

    namespace Domain {

        class Menu {
          +Long id
          +String name
          +String description
          +Long version
          +PopUpEvent event
          +List~MenuItem~ menuItems
        }

        class MenuItem {
          +Long id
          +String name
          +String description
          +Long version
          +Menu menu
        }

        class PopUpEvent {
          +Long id
          +String name
          +String eventDescription
          +LocalDate startDate
          +LocalDate endDate
          +Integer durationInMinutes
          +BigDecimal price
          +Boolean isActive
          +Boolean isArchived
          +Long version
          +LocalDateTime createdAt
          +LocalDateTime updatedAt
          +List~Seating~ seatings
          +Menu menu
        }

        class Seating {
          +Long id
          +Long eventId
          +String name
          +LocalDateTime startDateTime
          +int durationMinutes
          +LocalDateTime createdDate
          +LocalDateTime updatedAt
          +boolean archived
          +List~Long~ tableIds
        }

        class DiningTable {
          +Long id
          +String name
          +int capacity
          +LocalDateTime createdDate
        }

        class ReservationRequest {
          +Long id
          +UUID uuid
          +String firstName
          +String lastName
          +String email
          +Integer groupSize
          +Long seatingId
          +Long tableId
          +ReservationRequestStatus status
          +LocalDateTime createdAt
        }

        class ReservationGuestDetail {
          +UUID uuid
          +String firstName
          +String lastName
          +Integer groupSize
          +ReservationRequestStatus status
          +String eventName
          +LocalDateTime seatingStartDateTime
        }

        class ReservationRequestSummary {
          +Long id
          +Long seatingId
          +String firstName
          +String lastName
          +String email
          +int groupSize
          +ReservationRequestStatus status
          +String uuid
          +LocalDateTime createdAt
          +String seatingName
          +LocalDateTime seatingStartDateTime
          +Long eventId
          +String eventName
        }

        class ReservationRequestStatus {
          <<enum>>
          PENDING
          APPROVED
          DENIED
        }
}

    namespace Application {
        class MenuService {
          <<interface>>
          +getAll() Result~Collection~Menu~~
          +getAll(boolean loadMenuItems) Result~Collection~Menu~~
          +get(Long id) ValidatedResult~Menu~
          +get(Long id, boolean loadMenuItems) ValidatedResult~Menu~
          +create(Menu menu) ValidatedResult~Menu~
          +update(Menu menu) ValidatedResult~Menu~
          +delete(Long id) ValidatedResult~Void~
        }

        class MenuItemService {
          <<interface>>
          +getAll() Result~Collection~MenuItem~~
          +getAll(Long menuId) ValidatedResult~Collection~MenuItem~~
          +get(Long id) ValidatedResult~MenuItem~
          +create(MenuItem menuItem) ValidatedResult~MenuItem~
          +update(MenuItem menuItem) ValidatedResult~MenuItem~
          +delete(Long id) ValidatedResult~Void~
          +unlink(Long id) ValidatedResult~Void~ 
        }

        class PopUpEventService {
          <<interface>>
          +getAll() Result~Collection~PopUpEvent~~
          getAll(boolean loadSeatings, boolean loadMenu) Result~Collection~PopUpEvent~~
          search(String name, LocalDate startDate, LocalDate endDate) Result~Collection~PopUpEvent~~
          get(Long id) ValidatedResult~PopUpEvent~
          get(Long id, boolean loadSeatings, boolean loadMenu) ValidatedResult~PopUpEvent~
          create(PopUpEvent event) ValidatedResult~PopUpEvent~
          update(PopUpEvent event) ValidatedResult~PopUpEvent~
          delete(Long id) ValidatedResult~Void~
        }

        class SeatingService {
          <<interface>>
          +getAll() Result~Collection~Seating~~
          +get(Long id) ValidatedResult~Seating~
          +create(Seating seating) ValidatedResult~Seating~
          +update(Seating seating) ValidatedResult~Seating~
          +delete(Long id) ValidatedResult~Void~
        }

        class TableService {
          <<interface>>
          +getAll() Result~Collection~DiningTable~~
          +get(Long id) ValidatedResult~DiningTable~
          +create(DiningTable table) ValidatedResult~DiningTable~
          +update(DiningTable table) ValidatedResult~DiningTable~
          +delete(Long id) ValidatedResult~Void~
        }

        class ReservationRequestService {
          <<interface>>
          +getActiveEventsForReserve() ValidationResults~List~PopUpEvent~~
          +getActiveSeatingsForReserve() ValidationResults~List~Seating~~
          +create(ReservationRequest request) ValidationResults~ReservationRequest~
          +listForEmployee(Long eventId, ReservationRequestStatus status)       ValidationResults~List~ReservationRequestSummary~~
          +listApprovedForEmployee(Long eventId) ValidationResults~List~ReservationRequestSummary~~
          +get(Long id) Optional~ReservationRequest~
          +findGuestDetailByUuid(UUID uuid) Optional~ReservationGuestDetail~
          +approve(Long id, Long tableId) ValidationResults~ReservationRequest~
          +deny(Long id) ValidationResults~ReservationRequest~
        }

        class MenuValidationService {
          <<interface>>
          +validate(Menu menu) Collection~ValidationError~
        }

        class MenuItemValidationService {
          <<interface>>
          +validate(MenuItem menuItem) Collection~ValidationError~
        }

        class PopUpEventValidationService {
          <<interface>>
          +validate(PopUpEvent event) Collection~ValidationError~
        }

        class SeatingValidationService {
          <<interface>>
          +validate(Seating seating) Collection~ValidationError~
        }

        class MenuRepository {
          <<interface>>
          +getAll() List~Menu~
          +get(Long id) Optional~Menu~
          +create(Menu menu) Menu
          +update(Menu menu) Menu
          +delete(Long id) void
          +exists(String name) boolean
        }

        class MenuItemRepository {
          <<interface>>
          +getAll() List~MenuItem~
          +getAll(Long menuId) List~MenuItem~
          +get(Long id) Optional~MenuItem~
          +create(MenuItem menuItem) MenuItem
          +update(MenuItem menuItem) MenuItem
          +delete(Long id) void
          +exists(String name) boolean
        }

        class PopUpEventRepository {
          <<interface>>
          +getAll() List~PopUpEvent~
          +search(String name, LocalDate startDate, LocalDate endDate) List~PopUpEvent~
          +get(Long id) Optional~PopUpEvent~
          +create(PopUpEvent event) PopUpEvent
          +update(PopUpEvent event) PopUpEvent
          +delete(Long id) void
          +exists(String name) boolean
        }

        class SeatingRepository {
          <<interface>>
          +getAll() List~Seating~
          +get(Long id) Optional~Seating~
          +create(Seating seating) Seating
          +update(Seating seating) Seating
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

        class ReservationRequestRepository {
          <<interface>>
          +get(Long id) Optional~ReservationRequest~
          +getByUuid(UUID uuid) Optional~ReservationRequest~
          +create(ReservationRequest request) ReservationRequest
          +update(ReservationRequest request) ReservationRequest
          +delete(Long id) void
          +countBySeatingId(Long seatingId) long
        }
    }
    
    namespace Persistence {
      class MenuEntity {
        +Long id
        +String name
        +String description
        +LocalDateTime createdAt
        +Long version
        +List~MenuItemEntity~ menuItems
        +List~PopUpEventEntity~ events
      }
    
      class MenuItemEntity {
        +Long id
        +String name
        +String description
        +Long version
        +MenuEntity menu
      }
    
      class PopUpEventEntity {
        +Long id
        +String name
        +String eventDescription
        +LocalDate startDate
        +LocalDate endDate
        +Integer durationInMinutes
        +BigDecimal price
        +Boolean isActive
        +Boolean isArchived
        +LocalDateTime createdAt
        +LocalDateTime updatedAt
        +MenuEntity menu
        +Long version
      }
    
      class SeatingEntity {
        +Long id
        +Long eventId
        +String name
        +LocalDateTime startDateTime
        +int durationMinutes
        +LocalDateTime createdDate
        +LocalDateTime updatedAt
        +boolean archived
        +List~Long~ tableIds
      }

      class DiningTableEntity {
        +Long id
        +String name
        +int capacity
        +LocalDateTime createdDate
      }
    
      class ReservationRequestEntity {
        +Long id
        +UUID uuid
        +String firstName
        +String lastName
        +String email
        +Integer groupSize
        +Long seatingId
        +Long tableId
        +String status
        +LocalDateTime createdAt
      }    
}



Menu --> "0..1" PopUpEvent : contains
MenuItem --> "0..1" Menu : contains
PopUpEvent --> "0..*" Seating : contains
Seating --> "0..*" DiningTable : references
ReservationRequest --> "0..1" Seating : belongsTo
ReservationRequest --> "0..1" DiningTable : assignedTo
ReservationRequest --> ReservationRequestStatus : use
PopUpEventEntity --> "0..1" MenuEntity : contains
MenuEntity --> "0..*" MenuItemEntity : contains
PopUpEventEntity --> "0..*" SeatingEntity : contains
SeatingEntity --> "0..*" DiningTableEntity : references
ReservationRequestEntity --> "0..1" SeatingEntity : belongsTo
ReservationRequestEntity --> "0..1" DiningTableEntity : assignedT
Result~T~ <|-- ValidatedResult~T~ : extends
HasValidationErrors <|-- ValidatedResult~T~ : extends
HasValidationErrors ..> ValidationError : uses
ValidatedResult~T~ ..> ValidationError : use
PopUpEventService ..> PopUpEventRepository : uses
PopUpEventService ..> PopUpEventValidationService : validated b
MenuService ..> MenuRepository : uses
MenuService ..> MenuValidationService : validated b
MenuItemService ..> MenuItemRepository : uses
MenuItemService ..> MenuItemValidationService : validated b
SeatingService ..> SeatingRepository : uses
SeatingService ..> SeatingValidationService : validated b
DiningTableService ..> DiningTableRepository : use
ReservationRequestService ..> ReservationRequestRepository : uses