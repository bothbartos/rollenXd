package page;

public enum URLS {
    SIGN_UP("http://localhost:5173/signUp"),
    LOGIN("http://localhost:5173/login"),
    MAIN_PAGE("http://localhost:5173/");

    private final String url;
    URLS(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }
}
