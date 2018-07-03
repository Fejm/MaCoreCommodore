package pl.mateam.marpg.api.regular.classes.inventories;

import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import pl.mateam.marpg.api.CoreUtils.RemoteAccess;
import pl.mateam.marpg.api.regular.objects.users.AnyUser;

public abstract class CommodoreInventory implements RemoteAccess {
	protected final AnyUser object;
	public CommodoreInventory(AnyUser object)	{	this.object = object;	}

	public abstract void open();
	public abstract void handleClicking(InventoryClickEvent event);
	public abstract void close(@Nullable Player whoClosed);
	public boolean shouldCursorBeReturnedOnClose()	{	return true;	}	//Hook
	public boolean areShiftclicksCustomlyHandled()	{	return false;	}	//Hook
}
