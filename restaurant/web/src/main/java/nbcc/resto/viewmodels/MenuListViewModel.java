package nbcc.resto.viewmodels;

import nbcc.resto.domain.dto.Menu;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class MenuListViewModel implements Iterable<Menu>{

    private final Long eventId;

    private final boolean showAdd;
    private final boolean showEdit;
    private final boolean showDelete;
    private boolean showEvent;

   private final Collection<Menu> menus;

    public MenuListViewModel(Collection<Menu> menus, Long eventId, boolean showManage) {
        this.menus = menus;
        this.eventId = eventId;
        this.showAdd = showManage;
        this.showEdit = showManage;
        this.showDelete = showManage;
        this.showEvent = false;
    }

    public MenuListViewModel(Collection<Menu> menus, Long eventId, boolean canManage, boolean showEvent) {
        this(menus, eventId, canManage);
        this.showEvent = showEvent;
    }

    public boolean isShowEvent() { return showEvent; }

    public void setShowEvent(boolean showEvent) { this.showEvent = showEvent; }

    public boolean isShowAdd() { return showAdd; }

    public boolean isShowEdit() { return showEdit; }

    public boolean isShowDelete() { return showDelete; }

    public boolean isEmpty() { return menus.isEmpty(); }

    public Long getPopUpEventId() { return eventId; }

    public Collection<Menu> getMenus() {
        return menus != null ? menus : new ArrayList<>();
    }

    @Override
    @NonNull
    public Iterator<Menu> iterator() {
        return getMenus().iterator();
    }
}
