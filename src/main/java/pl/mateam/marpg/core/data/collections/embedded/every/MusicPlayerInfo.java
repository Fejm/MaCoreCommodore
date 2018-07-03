package pl.mateam.marpg.core.data.collections.embedded.every;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

import pl.mateam.marpg.core.internal.hardcoded.database.everyacc.MusicPlayerHardcodedNames;

@Embedded
public class MusicPlayerInfo {
	@Property(MusicPlayerHardcodedNames.MUTED)		public boolean muted = false;
}
