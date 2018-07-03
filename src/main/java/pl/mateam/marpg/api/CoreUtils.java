package pl.mateam.marpg.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import pl.mateam.marpg.api.utils.ChatUtils;
import pl.mateam.marpg.api.utils.DeveloperUtils;
import pl.mateam.marpg.api.utils.DeveloperUtils.ReflectiveField;
import pl.mateam.marpg.api.utils.IOUtils;
import pl.mateam.marpg.api.utils.IngameUtils;
import pl.mateam.marpg.api.utils.MathUtils;
import pl.mateam.marpg.api.utils.NMSUtils;
import pl.mateam.marpg.api.utils.ParsingUtils;

public class CoreUtils {
	//These fields are overwritten on engine startup using reflection system.
	@ReflectiveField(purpose = "initialize chat utils")				public static final ChatUtils chat = null;
	@ReflectiveField(purpose = "initialize developer utils")		public static final DeveloperUtils developer = null;
	@ReflectiveField(purpose = "initialize ingame utils") 			public static final IngameUtils ingame = null;
	@ReflectiveField(purpose = "initialize io utils")				public static final IOUtils io = null;
	@ReflectiveField(purpose = "initialize math utils") 			public static final MathUtils math = null;
	@ReflectiveField(purpose = "initialize nms utils")				public static final NMSUtils nms = null;
	@ReflectiveField(purpose = "initialize parsing utils") 			public static final ParsingUtils parsing = null;
	
	@Target(ElementType.TYPE_USE)	 @Retention(RetentionPolicy.SOURCE)
	public @interface ErrorProne{}
	
	@Target(ElementType.TYPE)	 @Retention(RetentionPolicy.RUNTIME)
	public @interface RemoteAccessName 		{	String name();	}
	public static interface RemoteAccess {}		//Interface marker - implementations of such marked types should be signed by RemoteAccessName key.
}
