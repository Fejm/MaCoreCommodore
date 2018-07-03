package pl.mateam.marpg.api.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.server.v1_12_R1.Packet;

import org.bukkit.entity.Player;

public interface NMSUtils {
    void sendPacket(final Player player, final Object packet);
    void setValue(Object obj, String name, Object value);
    Object getValue(Object obj, String name);
    void sendPacket(Object playerConnection, Object packet) throws Exception;
    void sendPacket(Packet<?> packet, Player player);
    void sendPacket(Packet<?> packet);
    Field getField(Class<?> clazz, String fname) throws Exception;
    <T> Field getField(Class<?> target, String name, Class<T> fieldType, int index);
    Object getObject(Object obj, String fname) throws Exception;
    Object getObject(Class<?> clazz, Object obj, String fname) throws Exception;
	void setObject(Object obj, String fname, Object value) throws Exception;
	void setObject(Class<?> clazz, Object obj, String fname, Object value) throws Exception;
	Method getMethod(Class<?> clazz, String mname) throws Exception;
	Method getMethod(Class<?> clazz, String mname, Class<?>... args) throws Exception;
	Constructor<?> getConstructor(Class<?> clazz, Class<?>... args) throws Exception;
	Enum<?> getEnum(Class<?> clazz, String enumname, String constant) throws Exception;
	Enum<?> getEnum(Class<?> clazz, String constant) throws Exception;
	Class<?> getNMSClass(String clazz) throws Exception;
	Class<?> getBukkitClass(String clazz) throws Exception;
	Object invokeMethod(Class<?> clazz, Object obj, String method, Class<?>[] args, Object... initargs) throws Exception;
	Object invokeMethod(Class<?> clazz, Object obj, String method) throws Exception;
	Object invokeMethod(Class<?> clazz, Object obj, String method, Object... initargs) throws Exception;
	Object invokeMethod(Object obj, String method) throws Exception;
	Object invokeMethod(Object obj, String method, Object[] initargs) throws Exception;
	Object invokeConstructor(Class<?> clazz, Class<?>[] args, Object... initargs) throws Exception;
}
