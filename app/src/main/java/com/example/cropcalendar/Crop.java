package com.example.cropcalendar;

public class Crop {
    private String name;
    private cropType type;
    private int daysToPlantBeforeLastFrost;
    private int daysToGerm;
    private int daysToHarvest;
    private sunLevel sun;
    private String notes;

    private static int frostMonth = 1;
    private static int frostDay = 1;

    public enum cropType {
        FRUITS,
        VEGETABLES,
        ROOTS,
        GRAINS,
        SPICES,
        OTHER
    }

    public enum sunLevel {
        FULL_SUN,
        PART_SUN,
        PART_SHADE,
        FULL_SHADE
    }

    public Crop(String name, cropType type, int daysToPlantBeforeLastFrost, int daysToGerm,
                int daysToHarvest, sunLevel sun, String notes) {
        this.name = name;
        this.type = type;
        this.daysToPlantBeforeLastFrost = daysToPlantBeforeLastFrost;
        this.daysToGerm = daysToGerm;
        this.daysToHarvest = daysToHarvest;
        this.sun = sun;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public cropType getType() {
        return type;
    }

    public void setType(cropType type) {
        this.type = type;
    }

    public int getDaysToPlantBeforeLastFrost() {
        return daysToPlantBeforeLastFrost;
    }

    public void setDaysToPlantBeforeLastFrost(int daysToPlantBeforeLastFrost) {
        this.daysToPlantBeforeLastFrost = daysToPlantBeforeLastFrost;
    }

    public int getDaysToGerm() {
        return daysToGerm;
    }

    public void setDaysToGerm(int daysToGerm) {
        this.daysToGerm = daysToGerm;
    }

    public int getDaysToHarvest() {
        return daysToHarvest;
    }

    public void setDaysToHarvest(int daysToHarvest) {
        this.daysToHarvest = daysToHarvest;
    }

    public sunLevel getSun() {
        return sun;
    }

    public void setSun(sunLevel sun) {
        this.sun = sun;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public static int getFrostMonth() {
        return frostMonth;
    }

    public static int getFrostDay() {
        return frostDay;
    }

    public static void setFrostDate(int month, int day) {
        frostMonth = month;
        frostDay = day;
    }

    public static void setFrostDate(String date) {
        String[] intValues = date.split("/");
        frostMonth = Integer.parseInt(intValues[0]);
        frostDay = Integer.parseInt(intValues[1]);
    }

    /**
     * Returns String of crop type based on int value
     *
     * @return Crop type String
     */
    public String getTypeText() {
        switch (this.type) {
            case FRUITS:
                return "Fruit";
            case VEGETABLES:
                return "Vegetable";
            case ROOTS:
                return "Root";
            case GRAINS:
                return "Grain";
            case SPICES:
                return "Spice";
            default:
                return "Other";
        }
    }

    /**
     * Returns String of sun level based on int value
     *
     * @return Sun level String
     */
    public String getSunText() {
        switch (this.sun) {
            case FULL_SUN:
                return "Full Sun";
            case PART_SUN:
                return "Partial Sun";
            case PART_SHADE:
                return "Partial Shade";
            default:
                return "Full Shade";
        }
    }

    /**
     * Converts a String sun level to a sun level object
     *
     * @param sunString String of sun level
     * @return sunLevel object that corresponds to given String
     */
    public static sunLevel getSunLevelFromText(String sunString) {
        switch (sunString) {
            case "Full Sun":
                return sunLevel.FULL_SUN;
            case "Partial Sun":
                return sunLevel.PART_SUN;
            case "Partial Shade":
                return sunLevel.PART_SHADE;
            default:
                return sunLevel.FULL_SHADE;
        }
    }

    /**
     * Return frost date as a string formatted as month/day
     *
     * @return Frost date
     */
    public static String getFrostDateString() {
        return frostMonth + "/" + frostDay;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s\nDays to plant before frost: %d\nDays to germ: %d\n" +
                        "Days to harvest: %d\n%s", name, getTypeText(), getSunText(),
                daysToPlantBeforeLastFrost, daysToGerm, daysToHarvest, notes);

    }
}
