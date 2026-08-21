package Data;

import java.time.Duration;

public final class Constants {

    private Constants() {
    }

    public static final String CREDO_URL =
            System.getProperty("baseUrl", "https://mycredo.ge/landing/main/auth");

    public static final boolean HEADLESS =
            Boolean.parseBoolean(System.getProperty("headless", "false"));

    public static final Duration EXPLICIT_TIMEOUT =
            Duration.ofSeconds(Long.getLong("timeout.explicit", 20L));

    public static final Duration FLUENT_TIMEOUT =
            Duration.ofSeconds(Long.getLong("timeout.fluent", 25L));

    public static final Duration POLL_INTERVAL =
            Duration.ofMillis(Long.getLong("timeout.poll", 300L));

    public static final Duration PAGE_LOAD_TIMEOUT =
            Duration.ofSeconds(Long.getLong("timeout.pageLoad", 60L));
}
