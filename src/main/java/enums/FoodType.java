package enums;

public enum FoodType {
    APPETIZERS("A"),
    MAIN_COURSE("M");

    public final String label;

    FoodType(String label) {
        this.label = label;
    }
}
