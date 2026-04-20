package nbcc.common.viewmodel;

public class NavViewModel {

    private final String username;
    private final boolean showLoginLink;
    private final boolean showLogoutButton;
    private final boolean showRegisterLink;
    private final boolean showUserName;
    private final boolean showReservations;
    private final boolean showEventLink;

    public NavViewModel(boolean isLoggedIn, String username) {
        this.username = username;
        this.showUserName = username != null && !username.isBlank();
        this.showLoginLink = !isLoggedIn;
        this.showLogoutButton = isLoggedIn;
        this.showRegisterLink = !isLoggedIn;
        this.showReservations = isLoggedIn;
        this.showEventLink = isLoggedIn;
    }

    public boolean isShowUserName() {
        return showUserName;
    }

    public String getUsername() {
        return username;
    }

    public boolean isShowLoginLink() {
        return showLoginLink;
    }

    public boolean isShowLogoutButton() {
        return showLogoutButton;
    }

    public boolean isShowRegisterLink() {
        return showRegisterLink;
    }

    public boolean isShowReservations() {
        return showReservations;
    }

    public boolean isShowEventLink() {
        return showEventLink;
    }
}
