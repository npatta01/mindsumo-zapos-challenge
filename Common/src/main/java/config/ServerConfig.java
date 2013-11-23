package config;

/**
 * Server Config
 */
public class ServerConfig {
    //host name
    private String baseUrl;

    /**
     * Constructor
     * @param base base url
     */
    public ServerConfig(String base) {
        baseUrl = base;
    }

    /**
     * Get base url
     * @return     url
     */
    public String getBASE_URL() {
        return baseUrl;
    }
}
