package nbcc.resto.viewmodels;

import nbcc.resto.domain.dto.PopUpEvent;

import java.util.Collection;

public class PopUpEventListViewModel {

    private final boolean canAdd;
    private final boolean canEdit;
    private final boolean canDelete;

    private final String message;

    private  final Collection<PopUpEvent> popUpEvents;

    public PopUpEventListViewModel(Collection<PopUpEvent> popUpEvents, boolean canManage) {
        this(popUpEvents, canManage, null);
    }

    public PopUpEventListViewModel(Collection<PopUpEvent> popUpEvents, boolean canManage, String message) {
        this.popUpEvents = popUpEvents;
        this.canAdd = canManage;
        this.canEdit = canManage;
        this.canDelete = canManage;
        this.message = message;
    }

    public Collection<PopUpEvent> getPopUpEvents() {
        return popUpEvents;
    }

    public boolean isCanAdd() {
        return canAdd;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public String getMessage() {
        return message;
    }

    public boolean isEmpty() { return popUpEvents.isEmpty();}
}
