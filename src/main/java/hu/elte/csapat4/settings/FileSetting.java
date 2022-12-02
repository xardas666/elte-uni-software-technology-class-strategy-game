package hu.elte.csapat4.settings;

public enum FileSetting {
    MAPS_PATH("data/maps/")
    ,MAP_IMAGES_PATH("data/images/map/")
    ,UI_IMAGES_PATH("data/images/ui/")
    ,TEXTS_PATH("data/texts/")
    ,MAP_FILE_EXT(".txt")
    ,IMAGE_FILE_EXT(".PNG")
    ,TEXT_FILE_EXT(".txt")
    ;

    private final String value;

    FileSetting(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
