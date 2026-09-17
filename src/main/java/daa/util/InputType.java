package daa.util;

public enum InputType {
    RANDOM("random"),
    SORTED("sorted"),
    DUPLICATES("duplicates");

    private final String label;

    InputType(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
