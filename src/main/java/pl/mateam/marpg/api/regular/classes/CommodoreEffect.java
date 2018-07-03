package pl.mateam.marpg.api.regular.classes;

import org.bukkit.Location;

import pl.mateam.marpg.api.CoreUtils.RemoteAccess;

public abstract class CommodoreEffect implements RemoteAccess {
	protected final Location location;
	public CommodoreEffect(Location location)	{	this.location = location;	}

	public abstract void play(Object... parameters);
}
