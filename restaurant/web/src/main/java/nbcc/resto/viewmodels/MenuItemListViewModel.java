package nbcc.resto.viewmodels;

import nbcc.resto.domain.dto.MenuItem;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class MenuItemListViewModel implements Iterable<MenuItem> {

    private final Long menuId;

    private final boolean showAdd;
    private final boolean showEdit;
    private final boolean showDelete;
    private boolean showMenu;

    private final Collection<MenuItem> menuItems;

    public MenuItemListViewModel(Collection<MenuItem> menuItems, Long menuId, boolean showManage) {
        this.menuItems = menuItems;
        this.menuId = menuId;
        this.showAdd = showManage;
        this.showEdit = showManage;
        this.showDelete = showManage;
        this.showMenu = false;
    }

    public MenuItemListViewModel(Collection<MenuItem> menuItems, Long menuId, boolean canManage, boolean showMenu) {
        this(menuItems, menuId, canManage);
        this.showMenu = showMenu;
    }

    public boolean isShowMenu() {
        return showMenu;
    }

    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }

    public boolean isShowAdd() {
        return showAdd;
    }

    public boolean isShowEdit() {
        return showEdit;
    }

    public boolean isShowDelete() {
        return showDelete;
    }

    public boolean isEmpty() {
        return menuItems.isEmpty();
    }

    public Long getMenuId() {
        return menuId;
    }

    public Collection<MenuItem> getMenuItems() {
        return menuItems != null ? menuItems : new ArrayList<>();
    }

    @Override
    @NonNull
    public Iterator<MenuItem> iterator() {
        return getMenuItems().iterator();
    }
}
