package pl.mateam.marpg.core.internal.utils;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.objects.items.CommodoreItem;
import pl.mateam.marpg.core.objects.items.special.CommodoreArmorElementImplementation;

public class InternalIngameUtils {
	public static ItemStack setArmorValidColor(ItemStack item, float colorValue) {
		CommodoreItem result = Core.getServer().getItems().getCommodoreLayer(item);
		if(result instanceof CommodoreArmorElementImplementation){
			CommodoreArmorElementImplementation armor = (CommodoreArmorElementImplementation) result;
			LeatherArmorMeta lam = (LeatherArmorMeta) item.getItemMeta();
			int itemLevel = armor.getItemLevel();
			double saturation = 2/9D * (1 + armor.getUpgradementLevel() / 9D) * (1 + armor.getTier().getID() * 0.25);
			int[] list = CoreUtils.parsing.convertHSVtoRGB((short) (itemLevel * 2), saturation, colorValue);
			lam.setColor(Color.fromRGB(list[0], list[1], list[2]));
			item.setItemMeta(lam);
		}
		return item;
	}
}
