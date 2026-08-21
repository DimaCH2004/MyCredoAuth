package Data;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.DataProvider;

import java.util.List;
import java.util.function.Supplier;

public class DataSets {

    private static final List<Supplier<TestCredentials>> REJECTED_CREDENTIALS = List.of(
            TestCredentials::randomUnregistered,
            TestCredentials::whitespaceOnly,
            TestCredentials::sqlInjection,
            TestCredentials::overlong);

    @DataProvider(name = "languages")
    public static Object[][] languages() {
        return List.of(Language.values()).stream()
                .map(language -> new Object[]{language})
                .toArray(Object[][]::new);
    }

    @DataProvider(name = "blankFieldPerLanguage")
    public static Object[][] blankFieldPerLanguage() {
        return List.of(Language.values()).stream()
                .flatMap(language -> List.of(BlankField.values()).stream()
                        .map(blank -> new Object[]{
                                language, blank, TestCredentials.withBlank(blank)}))
                .toArray(Object[][]::new);
    }

    @DataProvider(name = "rejectedCredentialsPerLanguage")
    public static Object[][] rejectedCredentialsPerLanguage() {
        return List.of(Language.values()).stream()
                .flatMap(language -> REJECTED_CREDENTIALS.stream()
                        .map(supplier -> new Object[]{language, supplier.get()}))
                .toArray(Object[][]::new);
    }

    @DataProvider(name = "shortPersonalNumberPerLanguage")
    public static Object[][] shortPersonalNumberPerLanguage() {
        return List.of(Language.values()).stream()
                .map(language -> new Object[]{
                        language, RandomStringUtils.insecure().nextNumeric(5)})
                .toArray(Object[][]::new);
    }

    @DataProvider(name = "personalNumberPerLanguage")
    public static Object[][] personalNumberPerLanguage() {
        return List.of(Language.values()).stream()
                .map(language -> new Object[]{
                        language, RandomStringUtils.insecure().nextNumeric(11)})
                .toArray(Object[][]::new);
    }
}
