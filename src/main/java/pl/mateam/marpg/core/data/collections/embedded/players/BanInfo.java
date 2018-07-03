package pl.mateam.marpg.core.data.collections.embedded.players;

import java.util.Date;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

import pl.mateam.marpg.core.internal.hardcoded.database.players.GlobalBanHardcodedNames;

@Embedded
public class BanInfo {
	@Property(GlobalBanHardcodedNames.DATE)				public Date banDate;
	@Property(GlobalBanHardcodedNames.EXPIRATION)		public Date banExpirationTime;
	@Property(GlobalBanHardcodedNames.WHOBANNED)		public String whoBanned;
	@Property(GlobalBanHardcodedNames.REASON)			public String banReason;
	
	public void clear() {
		banDate = null;
		banExpirationTime = null;
		banReason = null;
		whoBanned = null;
	}
}
