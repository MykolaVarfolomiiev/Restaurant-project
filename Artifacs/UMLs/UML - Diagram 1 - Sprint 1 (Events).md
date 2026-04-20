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
            %%+MenuDTO menu
            +Long version
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
    }

    %% Relationships
    PopUpEventService ..> Result : uses
    PopUpEventService ..> PopUpEventValidationService : Is Validated By
    PopUpEventRepository ..> PopUpEventDTO : uses

    Result~T~ <|-- ValidatedResult~T~ : extends
    HasValidationErrors <|-- ValidatedResult~T~ : extends

    HasValidationErrors ..> ValidationError : uses
    ValidatedResult~T~ ..> ValidationError : uses