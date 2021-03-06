package pl.mateam.marpg.core.modules.external.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.minecraft.server.v1_12_R1.Packet;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import pl.mateam.marpg.api.utils.NMSUtils;

public class NMSUtilsImplementation implements NMSUtils {
	public static final String serverVersion = null;
	{
		try {
			String name = Bukkit.getServer().getClass().getPackage().getName();

			setObject(NMSUtilsImplementation.class, null, "serverVersion", name.substring(name.lastIndexOf(".") + 1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle", (Class<?>[])new Class[0]).invoke(player, new Object[0]);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

	public void setValue(Object obj, String name, Object value) {
		try {
			Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(obj, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Object getValue(Object obj, String name) {
		try {
			Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void sendPacket(Object playerConnection, Object packet) throws Exception {
		invokeMethod(playerConnection.getClass(), playerConnection, "sendPacket",
				new Class<?>[] { getNMSClass("Packet") }, new Object[] { packet });
	}
	
	public void sendPacket(Packet<?> packet, Player player) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

	public void sendPacket(Packet<?> packet) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			sendPacket(packet, player);
		}
	}
	
	public Field getField(Class<?> clazz, String fname) throws Exception {
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

    public <T> Field getField(Class<?> target, String name, Class<T> fieldType, int index) {
        for (Field field : target.getDeclaredFields()) {
            if ((name == null || field.getName().equals(name)) && fieldType.isAssignableFrom(field.getType()) && index-- <= 0) {
                field.setAccessible(true);
                return field;
            }
        }

        if (target.getSuperclass() != null)
            return getField(target.getSuperclass(), name, fieldType, index);
        throw new IllegalArgumentException("Cannot find field with type " + fieldType);
    }
    
	public Object getObject(Object obj, String fname) throws Exception {
		return getField(obj.getClass(), fname).get(obj);
	}

	public Object getObject(Class<?> clazz, Object obj, String fname) throws Exception {
		return getField(clazz, fname).get(obj);
	}

	public void setObject(Object obj, String fname, Object value) throws Exception {
		getField(obj.getClass(), fname).set(obj, value);
	}

	public void setObject(Class<?> clazz, Object obj, String fname, Object value) throws Exception {
		getField(clazz, fname).set(obj, value);
	}

	public Method getMethod(Class<?> clazz, String mname) throws Exception {
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

	public Method getMethod(Class<?> clazz, String mname, Class<?>... args) throws Exception {
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

	public Constructor<?> getConstructor(Class<?> clazz, Class<?>... args) throws Exception {
		Constructor<?> c = clazz.getConstructor(args);
		c.setAccessible(true);
		return c;
	}

	public Enum<?> getEnum(Class<?> clazz, String enumname, String constant) throws Exception {
		Class<?> c = Class.forName(clazz.getName() + "$" + enumname);
		Enum<?>[] econstants = (Enum<?>[]) c.getEnumConstants();
		for (Enum<?> e : econstants) {
			if (e.name().equalsIgnoreCase(constant))
				return e;
		}
		throw new Exception("Enum constant not found " + constant);
	}

	public Enum<?> getEnum(Class<?> clazz, String constant) throws Exception {
		Class<?> c = Class.forName(clazz.getName());
		Enum<?>[] econstants = (Enum<?>[]) c.getEnumConstants();
		for (Enum<?> e : econstants) {
			if (e.name().equalsIgnoreCase(constant))
				return e;
		}
		throw new Exception("Enum constant not found " + constant);
	}

	public Class<?> getNMSClass(String clazz) throws Exception {
		return Class.forName("net.minecraft.server." + serverVersion + "." + clazz);
	}

	public Class<?> getBukkitClass(String clazz) throws Exception {
		return Class.forName("org.bukkit.craftbukkit." + serverVersion + "." + clazz);
	}

	public Object invokeMethod(Class<?> clazz, Object obj, String method, Class<?>[] args, Object... initargs)
			throws Exception {
		return getMethod(clazz, method, args).invoke(obj, initargs);
	}

	public Object invokeMethod(Class<?> clazz, Object obj, String method) throws Exception {
		return getMethod(clazz, method).invoke(obj, new Object[] {});
	}

	public Object invokeMethod(Class<?> clazz, Object obj, String method, Object... initargs) throws Exception {
		return getMethod(clazz, method).invoke(obj, initargs);
	}

	public Object invokeMethod(Object obj, String method) throws Exception {
		return getMethod(obj.getClass(), method).invoke(obj, new Object[] {});
	}

	public Object invokeMethod(Object obj, String method, Object[] initargs) throws Exception {
		return getMethod(obj.getClass(), method).invoke(obj, initargs);
	}

	public Object invokeConstructor(Class<?> clazz, Class<?>[] args, Object... initargs) throws Exception {
		return getConstructor(clazz, args).newInstance(initargs);
	}
}
