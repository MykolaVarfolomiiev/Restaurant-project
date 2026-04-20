```mermaid
classDiagram
    namespace Domain {

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
        }

        class MenuDTO {
            +Long id
            +String name
            +String menuDescription
            +LocalDateTime menuCreatedAt
            +LocalDateTime menuUpdatedAt
            +Long eventId
            %%+PopUpEventDTO eventDTO
            +items List~ItemDTO~
            +getItemId() Long
        }

        class ItemDTO {
            +Long id
            +String name
            +String itemDescription
        }

        class Result~T~ {
            <<interface>>
            +getValue() T
            +getValidationErrors() Collection~ValidationError~
            +isEmpty() boolean
            +hasValue() boolean
            +hasValidationError() boolean
            +isSuccessful() boolean
            +isError() boolean
        }

         class ValidationError {
            +String field
            +String message
            +Object value
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
            +exists(String name) boolean
        }

        class CommonValidator~T~ {
            +validate(T) Collection~ValidationError~
        }

        %%class PopUpEventValidationService {
        %%    <<interface>>
        %%    +validate(PopUpEvent popupEvent) Collection~ValidationError~
        %%}

        class MenuService{
            <<interface>>
            +getAll() Result~List~MenuDTO~~
            +getAll(Long eventId) Result~List~MenuDTO~~
            +get(Long id) Result~MenuDTO~
            +create(MenuDTO menu) Result~MenuDTO~
            +update(MenuDTO menu) Result~MenuDTO~
            +delete(Long id) Result~Void~
        }

        class ItemService {
            <<interface>>
            +getAll() Result~List~ItemDTO~~
            +get(Long id) Result~ItemDTO~
            +create(ItemDTO item) Result~ItemDTO~
            +update(ItemDTO item) Result~ItemDTO~
            +delete(Long id) Result~Void~
        }

        class PopUpEventRepository {
            <<interface>>
            +getAll() List~PopUpEventDTO~
            +get(Long id) Optional~PopUpEventDTO~
            +create(PopUpEventDTO eventDTO) PopUpEventDTO
            +update(PopUpEventDTO eventDTO) PopUpEventDTO
            +delete(Long id) void
            +searchEvent(String name) Result~List~PopUpEventDTO~~
            +exists(String name) boolean
        }

        class MenuRepository {
            <<interface>>
            +getAll() List~MenuDTO~
            +getAll(Long eventId) List~MenuDTO~
            +get(Long id) Optional~MenuDTO~
            +create(MenuDTO menu) MenuDTO
            +update(MenuDTO menu) MenuDTO
            +delete(Long id) void
            +searchMenu(String name) Result~List~MenuDTO~~
        }

        class ItemRepository {
            <<interface>>
            +getAll() List~ItemDTO~
            +get(Long id) Optional~ItemDTO~
            +create(ItemDTO item) ItemDTO
            +update(ItemDTO item) ItemDTO
            +delete(Long id) void
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
            +menu MenuEntity
        }

        class MenuEntity {
            +Long id
            +String name
            +String menuDescription
            +LocalDateTime menuCreatedAt
            +LocalDateTime menuUpdatedAt
            +items List~ItemEntity~
            +getItemId() Long
        }

        class ItemEntity {
            +Long id
            +String name
            +String itemDescription
        }
    }

    %% Relationships
    PopUpEventEntity "1" --> "1" MenuEntity: contains
    MenuEntity "1" --> "0..*" ItemEntity: contains
    PopUpEventDTO "1" --> "1" MenuDTO: contains
    MenuDTO "1" --> "0..*" ItemDTO: contains
    PopUpEventRepository ..> PopUpEventDTO: Uses
    PopUpEventService ..> PopUpEventDTO: Uses
    PopUpEventService ..> Result~T~: Uses
    MenuService ..> MenuDTO: Uses
    MenuRepository ..> MenuDTO: Uses
    MenuService ..> Result~T~: Uses
    ItemService ..> ItemDTO: Uses
    ItemRepository ..> ItemDTO: Uses
    ItemService ..> Result~T~: Uses
    Result~T~ ..> ValidationError: Uses
    CommonValidator~T~ ..> ValidationError: Uses

````