package Data;

public enum BlankField {

    USERNAME("username left empty"),
    PASSWORD("password left empty");

    private final String caseName;

    BlankField(String caseName) {
        this.caseName = caseName;
    }

    public BlankField other() {
        return this == USERNAME ? PASSWORD : USERNAME;
    }

    @Override
    public String toString() {
        return caseName;
    }
}
