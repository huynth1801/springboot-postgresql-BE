package started.local.startedjava.constant;

public interface SecurityConstants {
    String[] ADMIN_API_PATHS = {
            "/api/auth/info"
    };

    String[] CLIENT_API_PATHS = {
        "/api/users/**",
         "/api/orders/**",
    };

    String[] IGNORING_API_PATHS = {
            "/api/auth/**",
            "/api/orders/**",
            "/error/**",
            "/log-in"
    };

    interface Role {
        String ADMIN = "ADMIN";

        String EMPLOYEE = "EMPLOYEE";

        String CUSTOMER = "CUSTOMER";
    }
}
