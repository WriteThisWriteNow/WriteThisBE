package ua.com.writethis.wsapi.config.security;

public enum AuthList {
    SWAGGER("/swagger-resources/**", "/swagger-ui.html", "/v2/api-docs", "/webjars/**", "/swagger-ui/**"),
    WHITE_LIST("/login", "/healthcheck");

    private final String[] authentications;

    AuthList(String... authentications) {
        this.authentications = authentications;
    }

    public String[] get() {
        return authentications;
    }
}
