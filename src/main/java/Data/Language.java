package Data;

public enum Language {

    GEORGIAN("ქართული", "ქართ",
            "სავალდებულო ველი",
            "მონაცემები არასწორია",
            "დასაშვებია მხოლოდ 11 სიმბოლო"),

    ENGLISH("English", "Eng",
            "Required field",
            "Please make sure the entered details are correct",
            "Only 11 characters are allowed"),

    RUSSIAN("Русский", "Рус",
            "Обязательное поле",
            "Пожалуйста, убедитесь, что введенные данные верны.",
            "Допускается только 11 символов");

    private final String label;
    private final String shortLabel;
    private final String requiredFieldError;
    private final String invalidCredentialsError;
    private final String personalNumberLengthError;

    Language(String label, String shortLabel, String requiredFieldError,
             String invalidCredentialsError, String personalNumberLengthError) {
        this.label = label;
        this.shortLabel = shortLabel;
        this.requiredFieldError = requiredFieldError;
        this.invalidCredentialsError = invalidCredentialsError;
        this.personalNumberLengthError = personalNumberLengthError;
    }

    public String label() {
        return label;
    }

    public String shortLabel() {
        return shortLabel;
    }

    public String requiredFieldError() {
        return requiredFieldError;
    }

    public String invalidCredentialsError() {
        return invalidCredentialsError;
    }

    public String personalNumberLengthError() {
        return personalNumberLengthError;
    }

    @Override
    public String toString() {
        return label;
    }
}
