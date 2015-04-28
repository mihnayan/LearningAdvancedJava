package mihnayan.divetojava.utils;

public final class DtjHelper {

    public static String buildJSONParameter(String key, String value) {
        return "\"" + key + "\": " + "\"" + (value == null ? "" : value) + "\"";
    }
    
    public static String buildJSONParameter(String key, Number value) {
        return "\"" + key + "\": " + (value == null ? 0 : value);
    }
    
    public static String buildJSONParameter(String key, Object value) {
        return "\"" + key + "\": " + "\"" + (value == null ? "" : value.toString()) + "\"";
    }
    
    private DtjHelper() {
        throw new AssertionError("Can't create helper class");
    }
}
