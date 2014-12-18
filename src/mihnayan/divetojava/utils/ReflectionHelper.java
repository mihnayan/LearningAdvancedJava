package mihnayan.divetojava.utils;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import mihnayan.divetojava.frontend.GameFrontend;

public class ReflectionHelper {
	
	private static Logger log = Logger.getLogger(ReflectionHelper.class.getName());

	public static Object createInstance(String className) {
		try {
			return Class.forName(className).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void setFieldValue(Object object, String fieldName, String value) {
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
			log.info("Setting has been missed for the " 
					+ fieldName + " field of " 
					+ object.getClass().getName() + " object.");
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
}
