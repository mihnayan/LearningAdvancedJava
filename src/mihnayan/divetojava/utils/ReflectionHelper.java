package mihnayan.divetojava.utils;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper class for working with Java Reflection API.
 * @author Mikhail Mangushev (Mihnayan)
 *
 */
public final class ReflectionHelper {

    private static Logger log = Logger.getLogger(ReflectionHelper.class
            .getName());

    /**
     * Creates class instance by class name.
     * @param className The name of class which must be created.
     * @return instance of the class or <strong>null</strong> if an error occurred.
     */
    public static Object createInstance(String className) {
        try {
            return Class.forName(className).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            log.log(Level.SEVERE, "Not found class '" + className + "'!");
        }
        return null;
    }

    /**
     * Sets field value in real object.
     * @param object The object field which should be set to the required value.
     * @param fieldName The field which should be set to the required value.
     * @param value Required value.
     */
    public static void setFieldValue(Object object, String fieldName,
            String value) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            try {
                if (field.getType().equals(String.class)) {
                    field.set(object, value);
                } else if (field.getType().equals(int.class)) {
                    field.set(object, Integer.decode(value));
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } finally {
                field.setAccessible(false);
            }

        } catch (NoSuchFieldException e) {
            log.info("Setting has been missed for the " + fieldName
                    + " field of " + object.getClass().getName() + " object.");
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private ReflectionHelper() {
        throw new AssertionError("Can't create helper class");
    }
}
