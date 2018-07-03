package pl.mateam.marpg.api.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface DeveloperUtils {
	Object getFieldValue(Object object, String fieldName);
	Object getReflectiveFieldValue(Class<?> containingClass, Object subject, String reflectiveFieldPurpose);
	void overrideReflectiveFinalField(Class<?> containingClass, Object subject, String reflectiveFieldPurpose, Object value);
	
	@Target({ ElementType.FIELD })
	@Retention(value = RetentionPolicy.RUNTIME)
	public @interface ReflectiveField {
		String purpose();
	}
}	
