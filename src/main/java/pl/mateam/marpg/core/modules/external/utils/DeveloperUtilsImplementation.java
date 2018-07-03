package pl.mateam.marpg.core.modules.external.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;

import net.minecraft.server.v1_12_R1.Packet;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import pl.mateam.marpg.api.utils.DeveloperUtils;
import pl.mateam.marpg.core.MaCoreCommodoreEngine.Secret;

public class DeveloperUtilsImplementation implements DeveloperUtils {
	@Override public Object getFieldValue(Object object, String fieldName){
		try {
			Class<?> clazz = object.getClass();
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
		    Object result = field.get(object);
		    field.setAccessible(false);
		    return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private Field getReflectiveField(Class<?> containingClass, String reflectiveFieldPurpose){
		for(Field field : containingClass.getDeclaredFields()){
			if(field.isAnnotationPresent(ReflectiveField.class)){
	    	   	ReflectiveField reflectiveField = field.getAnnotation(ReflectiveField.class);
	    	   	if(reflectiveField.purpose().equals(reflectiveFieldPurpose)){
	            	field.setAccessible(true);
	            	return field;
	           	}
			}
		}
		return null;
	}
	
	@Override
	public Object getReflectiveFieldValue(Class<?> containingClass, Object subject, String reflectiveFieldPurpose) {
		try {
			Field field = getReflectiveField(containingClass, reflectiveFieldPurpose);
			Object object = field.get(subject);
			field.setAccessible(false);
			return object;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override public void overrideReflectiveFinalField(Class<?> containingClass, Object subject, String reflectiveFieldPurpose, Object value){
		try {
			Field field = getReflectiveField(containingClass, reflectiveFieldPurpose);
	        Field modifiersField = Field.class.getDeclaredField("modifiers");

	        AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
			    modifiersField.setAccessible(true);
			    return null;
			});

	        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
	        field.set(subject, value);
	        field.setAccessible(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	public static final String serverVersion = null;
	{
		try {
			Class.forName("org.bukkit.Bukkit");
			final String name = Bukkit.getServer().getClass().getPackage().getName();

			setObject(DeveloperUtilsImplementation.class, null, "serverVersion", name.substring(name.lastIndexOf(".") + 1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Secret public void sendPacket(final Player player, final Object packet) {
        try {
            final Object handle = player.getClass().getMethod("getHandle", (Class<?>[])new Class[0]).invoke(player, new Object[0]);
            final Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

	@Secret public void setValue(Object obj, String name, Object value) {
		try {
			Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(obj, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Secret public Object getValue(Object obj, String name) {
		try {
			Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Secret public void sendPacket(Object playerConnection, Object packet) throws Exception {
		invokeMethod(playerConnection.getClass(), playerConnection, "sendPacket",
				new Class<?>[] { getNMSClass("Packet") }, new Object[] { packet });
	}
	
	@Secret public void sendPacket(Packet<?> packet, Player player) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

	@Secret public void sendPacket(Packet<?> packet) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			sendPacket(packet, player);
		}
	}
	
	@Secret public Field getField(Class<?> clazz, String fname) throws Exception {
		Field f = null;
		try {
			f = clazz.getDeclaredField(fname);
		} catch (Exception e) {
			f = clazz.getField(fname);
		}
		f.setAccessible(true);
		Field modifiers = Field.class.getDeclaredField("modifiers");
		modifiers.setAccessible(true);
		modifiers.setInt(f, f.getModifiers() & ~Modifier.FINAL);
		return f;
	}

	@Secret public <T> Field getField(Class<?> target, String name, Class<T> fieldType, int index) {
        for (final Field field : target.getDeclaredFields()) {
            if ((name == null || field.getName().equals(name)) && fieldType.isAssignableFrom(field.getType()) && index-- <= 0) {
                field.setAccessible(true);
                return field;
            }
        }

        if (target.getSuperclass() != null)
            return getField(target.getSuperclass(), name, fieldType, index);
        throw new IllegalArgumentException("Cannot find field with type " + fieldType);
    }
    
	@Secret public Object getObject(Object obj, String fname) throws Exception {
		return getField(obj.getClass(), fname).get(obj);
	}

	@Secret public Object getObject(Class<?> clazz, Object obj, String fname) throws Exception {
		return getField(clazz, fname).get(obj);
	}

	@Secret public void setObject(Object obj, String fname, Object value) throws Exception {
		getField(obj.getClass(), fname).set(obj, value);
	}

	@Secret public void setObject(Class<?> clazz, Object obj, String fname, Object value) throws Exception {
		getField(clazz, fname).set(obj, value);
	}

	@Secret public Method getMethod(Class<?> clazz, String mname) throws Exception {
		Method m = null;
		try {
			m = clazz.getDeclaredMethod(mname);
		} catch (Exception e) {
			try {
				m = clazz.getMethod(mname);
			} catch (Exception ex) {
				for (Method me : clazz.getDeclaredMethods()) {
					if (me.getName().equalsIgnoreCase(mname))
						m = me;
					break;
				}
				if (m == null)
					for (Method me : clazz.getMethods()) {
						if (me.getName().equalsIgnoreCase(mname))
							m = me;
						break;
					}
			}
		}
		m.setAccessible(true);
		return m;
	}

	@Secret public Method getMethod(Class<?> clazz, String mname, Class<?>... args) throws Exception {
		Method m = null;
		try {
			m = clazz.getDeclaredMethod(mname, args);
		} catch (Exception e) {
			try {
				m = clazz.getMethod(mname, args);
			} catch (Exception ex) {
				for (Method me : clazz.getDeclaredMethods()) {
					if (me.getName().equalsIgnoreCase(mname))
						m = me;
					break;
				}
				if (m == null)
					for (Method me : clazz.getMethods()) {
						if (me.getName().equalsIgnoreCase(mname))
							m = me;
						break;
					}
			}
		}
		m.setAccessible(true);
		return m;
	}

	@Secret public Constructor<?> getConstructor(Class<?> clazz, Class<?>... args) throws Exception {
		Constructor<?> c = clazz.getConstructor(args);
		c.setAccessible(true);
		return c;
	}

	@Secret public Enum<?> getEnum(Class<?> clazz, String enumname, String constant) throws Exception {
		Class<?> c = Class.forName(clazz.getName() + "$" + enumname);
		Enum<?>[] econstants = (Enum<?>[]) c.getEnumConstants();
		for (Enum<?> e : econstants) {
			if (e.name().equalsIgnoreCase(constant))
				return e;
		}
		throw new Exception("Enum constant not found " + constant);
	}

	@Secret public Enum<?> getEnum(Class<?> clazz, String constant) throws Exception {
		Class<?> c = Class.forName(clazz.getName());
		Enum<?>[] econstants = (Enum<?>[]) c.getEnumConstants();
		for (Enum<?> e : econstants) {
			if (e.name().equalsIgnoreCase(constant))
				return e;
		}
		throw new Exception("Enum constant not found " + constant);
	}

	@Secret public Class<?> getNMSClass(String clazz) throws Exception {
		return Class.forName("net.minecraft.server." + serverVersion + "." + clazz);
	}

	@Secret public Class<?> getBukkitClass(String clazz) throws Exception {
		return Class.forName("org.bukkit.craftbukkit." + serverVersion + "." + clazz);
	}

	@Secret public Object invokeMethod(Class<?> clazz, Object obj, String method, Class<?>[] args, Object... initargs)
			throws Exception {
		return getMethod(clazz, method, args).invoke(obj, initargs);
	}

	@Secret public Object invokeMethod(Class<?> clazz, Object obj, String method) throws Exception {
		return getMethod(clazz, method).invoke(obj, new Object[] {});
	}

	@Secret public Object invokeMethod(Class<?> clazz, Object obj, String method, Object... initargs) throws Exception {
		return getMethod(clazz, method).invoke(obj, initargs);
	}

	@Secret public Object invokeMethod(Object obj, String method) throws Exception {
		return getMethod(obj.getClass(), method).invoke(obj, new Object[] {});
	}

	@Secret public Object invokeMethod(Object obj, String method, Object[] initargs) throws Exception {
		return getMethod(obj.getClass(), method).invoke(obj, initargs);
	}

	@Secret public Object invokeConstructor(Class<?> clazz, Class<?>[] args, Object... initargs) throws Exception {
		return getConstructor(clazz, args).newInstance(initargs);
	}
}
