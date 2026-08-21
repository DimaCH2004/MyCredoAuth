package Data;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Locale;

public record TestCredentials(String caseName, String username, String password) {

    public static TestCredentials randomUnregistered() {
        return new TestCredentials(
                "random unregistered user",
                "QA" + RandomStringUtils.insecure().nextAlphanumeric(8).toUpperCase(Locale.ROOT),
                RandomStringUtils.insecure().nextAlphanumeric(12) + "!1");
    }

    public static TestCredentials whitespaceOnly() {
        return new TestCredentials("whitespace-only input", "   ", "   ");
    }

    public static TestCredentials sqlInjection() {
        return new TestCredentials("SQL injection payload", "' OR '1'='1", "' OR '1'='1' --");
    }

    public static TestCredentials overlong() {
        return new TestCredentials(
                "overlong input (300 characters)",
                RandomStringUtils.insecure().nextAlphanumeric(300),
                RandomStringUtils.insecure().nextAlphanumeric(300));
    }

    public static TestCredentials withBlank(BlankField blank) {
        TestCredentials filled = randomUnregistered();
        return new TestCredentials(
                blank.toString(),
                blank == BlankField.USERNAME ? "" : filled.username(),
                blank == BlankField.PASSWORD ? "" : filled.password());
    }

    @Override
    public String toString() {
        return caseName;
    }
}
