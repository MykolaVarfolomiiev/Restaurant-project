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

        class PopUpEventDTO {
            +Long id
            +String name
            +String eventDescription
            +Date startDate
            +Date endDate
            +Integer durationInMinutes
            +Double price
            +Boolean isActive
            +LocalDateTime eventCreatedAt
            +LocalDateTime eventUpdatedAt
            +MenuDTO menu
            +Long version
        }

        class MenuDTO {
            +Long id
            +String name
            +String menuDescription
            +LocalDateTime menuCreatedAt
            +LocalDateTime menuUpdatedAt
            +List~MenuItemDTO~ items
            +PopUpEventDTO event
            +getEventId() Long
            +Long version
        }

        class MenuItemDTO {
            +Long id
            +String name
            +String itemDescription
            +MenuDTO menu
            +getMenuId() Long
            +Long version
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

        class SeatingDTO {
            +Long id
            +Long eventId
            +String name
            +LocalDateTime startDateTime
            +int duration
            +LocalDateTime createdDate
            +boolean archived
        }

        class ReservationDTO {
            +Long id
            +Long eventId
            +String firstName
            +String lastName
            +String Email
            +Integer groupSize
            +LocalDateTime reservedAt
            +ReservationStatus status
            +Boolean approved
            +Boolean confirmed
        }
    }

    namespace Application {

        class PopUpEventService {
            <<interface>>
            +getAll() Result~List~PopUpEventDTO~~
            +get(Long id) Result~PopUpEventDTO~
            +create(PopUpEventDTO popupEvent) Result~PopUpEventDTO~
            +update(PopUpEventDTO popupEvent) Result~PopUpEventDTO~
            +delete(Long id) Result~Void~
            +exists(String name) Result~Boolean~
        }

        class PopUpEventValidationService {
            +validate(PopUpEventDTO popupEvent) Collection~ValidationError~
            +validateDates(PopUpEventDTO popupEvent)    Collection~ValidationError~
            +validatePrice(PopUpEventDTO popupEvent)    Collection~ValidationError~
        }

        class PopUpEventRepository {
            <<interface>>
            +getAll() List~PopUpEventDTO~
            +get(Long id) Optional~PopUpEventDTO~
            +create(PopUpEventDTO eventDTO) PopUpEventDTO
            +update(PopUpEventDTO eventDTO) PopUpEventDTO
            +delete(Long id) void
            +exists(String name) boolean
        }

        class MenuService {
            <<interface>>
            +getAll() Result~List~MenuDTO~~
            +getAll(Long eventId) Result~List~MenuDTO~~
            +get(Long id) Result~MenuDTO~
            +create(MenuDTO menu) Result~MenuDTO~
            +update(MenuDTO menu) Result~MenuDTO~
            +delete(Long id) Result~Void~
            +exists(String name) Result~Boolean~
        }

        class MenuValidationService {
            +validate(MenuDTO menu) Collection~ValidationError~
        }

        class MenuRepository {
            <<interface>>
            +getAll() List~MenuDTO~
            +getAll(Long eventId) List~MenuDTO~
            +get(Long id) Optional~MenuDTO~
            +create(MenuDTO menu) MenuDTO
            +update(MenuDTO menu) MenuDTO
            +delete(Long id) void
            +exists(String name) boolean
        }

        class MenuItemService {
            <<interface>>
            +getAll() Result~List~MenuItemDTO~~
            +getAll(Long menuId) Result~List~MenuItemDTO~~
            +get(Long id) Result~MenuItemDTO~
            +create(MenuItemDTO item) Result~MenuItemDTO~
            +update(MenuItemDTO item) Result~MenuItemDTO~
            +delete(Long id) Result~Void~
            +exists(String name) Result~Boolean~
        }

        class MenuItemValidationService {
            +validate(MenuItemDTO item) Collection~ValidationError~
        }

        class MenuItemRepository {
            <<interface>>
            +getAll() List~MenuItemDTO~
            +getAll(Long menuId) List~MenuItemDTO~
            +get(Long id) Optional~MenuItemDTO~
            +create(MenuItemDTO item) MenuItemDTO
            +update(MenuItemDTO item) MenuItemDTO
            +delete(Long id) void
            +exists(String name) boolean
        }

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
            +getAll(Long eventId) Result~List~SeatingDTO~~
            +get(Long id) Seating
            +create(Seating seating) Seating
            +update(Seating seating) Seating
            +deleteOrArchive(Long id) void
        }

        class SeatingRepository {
            <<interface>>
            +getAll(Long eventId) List~SeatingDTO~
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
            +getAllApproved(Long eventId) Result~List~ReservationDTO~~
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
            +create(Long eventId, Long seatingId, String firstName, String  lastName, String email, int groupSize) ReservationRequest
        }
    }

    namespace Persistence {

        class PopUpEventEntity {
            +Long id
            +String name
            +String eventDescription
            +Date startDate
            +Date endDate
            +Integer durationInMinutes
            +Double price
            +Boolean isActive
            +LocalDateTime eventCreatedAt
            +LocalDateTime eventUpdatedAt
            +MenuEntity menu
            +Long version
        }
    
        class MenuEntity {
            +Long id
            +String name
            +String menuDescription
            +LocalDateTime menuCreatedAt
            +LocalDateTime menuUpdatedAt
            +List~MenuItemEntity~ items
            +PopUpEventEntity event
            +Long version
        }
    
        class MenuItemEntity {
            +Long id
            +String name
            +String itemDescription
            +MenuEntity menu
            +Long version
        }
    
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
    
        class ReservationEntity {
            +Long id
            +PopUpEventEntity event
            +String firstName
            +String lastName
            +String Email
            +Integer groupSize
            +LocalDateTime reservedAt
            +ReservationStatus status
            +Boolean approved
            +Boolean confirmed
            +Long version
        }
    }

MenuDTO --> "0..1" PopUpEventDTO : contains
MenuItemDTO --> "0..1" MenuDTO : contains
MenuEntity --> "0..1" PopUpEventEntity : contains
MenuItemEntity --> "0..1" MenuEntity : contains

PopUpEventDTO --> "0..*" SeatingDTO : contains
SeatingEntity ..> DiningTableEntity : contains
ReservationEntity --> "0..1" PopUpEventEntity : contains

Seating --> "0..*" DiningTable : uses
ReservationRequest --> "1" Seating : belongsTo
ReservationRequest --> "0..1" DiningTable : assignedTo
SeatingEntity --> "0..*" DiningTableEntity : uses
ReservationRequestEntity --> "1" SeatingEntity : belongsTo

PopUpEventService ..> Result : uses
MenuService ..> Result : uses
MenuItemService ..> Result : uses
SeatingService ..> Result : uses
ReservationService ..> Result : uses

PopUpEventService ..> PopUpEventValidationService : Is Validated By
MenuService ..> MenuValidationService : Is Validated By
MenuItemService ..> MenuItemValidationService : Is Validated By

Result~T~ <|-- ValidatedResult~T~ : extends
HasValidationErrors <|-- ValidatedResult~T~ : extends

HasValidationErrors ..> ValidationError : uses
ValidatedResult~T~ ..> ValidationError : uses

PopUpEventRepository ..> PopUpEventDTO : uses
MenuRepository ..> MenuDTO : uses
MenuItemRepository ..> MenuItemDTO : uses
SeatingRepository ..> SeatingDTO : uses
ReservationRepository ..> ReservationDTO : uses

DiningTableRepository ..> DiningTable : uses
SeatingRepository ..> Seating : uses
ReservationRepository ..> ReservationRequest : uses

SeatingService --> SeatingRepository : uses
ReservationService --> ReservationRepository : uses
ReservationService ..> EmailService : uses