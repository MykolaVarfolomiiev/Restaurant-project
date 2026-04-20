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

        %% --- Added for REST API---
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
            +validateDates(PopUpEventDTO popupEvent) Collection~ValidationError~
            +validatePrice(PopUpEventDTO popupEvent) Collection~ValidationError~
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

        %% --- Added for REST API---
        class SeatingService {
            <<interface>>
            +getAll(Long eventId) Result~List~SeatingDTO~~
        }

        class SeatingRepository {
            <<interface>>
            +getAll(Long eventId) List~SeatingDTO~
        }

        class ReservationService {
            <<interface>>
            +getAllApproved(Long eventId) Result~List~ReservationDTO~~
        }

        class ReservationRepository {
            <<interface>>
            +getAllApproved(Long eventId) List~ReservationDTO~
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

        %% --- Added for REST requirements ---
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

PopUpEventService ..> Result : uses
MenuService ..> Result : uses
MenuItemService ..> Result : uses
SeatingService ..> Result : uses
ReservationService ..> Result : uses

PopUpEventService ..> PopUpEventValidationService : Is Validated By
MenuService ..> MenuValidationService : Is Validated By
MenuItemService ..> MenuItemValidationService : Is Validated By

%% Inheritance Relationships
Result~T~ <|-- ValidatedResult~T~ : extends
HasValidationErrors <|-- ValidatedResult~T~ : extends

%% Dependencies (Returns/Uses)
HasValidationErrors ..> ValidationError : uses
ValidatedResult~T~ ..> ValidationError : uses

PopUpEventRepository ..> PopUpEventDTO : uses
MenuRepository ..> MenuDTO : uses
MenuItemRepository ..> MenuItemDTO : uses
SeatingRepository ..> SeatingDTO : uses
ReservationRepository ..> ReservationDTO : uses

SeatingService --> SeatingRepository : uses
ReservationService --> ReservationRepository : uses