package pl.mateam.marpg.api.regular.classes.inventories;

import org.bukkit.entity.Player;

import pl.mateam.marpg.api.regular.objects.users.AnyUser;

public abstract class CommodoreSharedInventory extends CommodoreInventory {
	protected final AnyUser object2;
	public CommodoreSharedInventory(AnyUser object1, AnyUser object2) {
		super(object1);
		this.object2 = object2;
	}
	
	public AnyUser getSecondUser(AnyUser relativeTo) {
		if(object == relativeTo)
			return object2;
		else return object;
	}
	
	public AnyUser getSecondUser(Player relativeTo) {
		if(object.getBukkitPlayer() == relativeTo)
			return object2;
		else return object;
	}
}
