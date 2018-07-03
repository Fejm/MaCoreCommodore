package pl.mateam.marpg.api.regular.objects.worlds.tectonic;

import org.bukkit.block.Block;

public interface TectonicPlateOresManager {
	int[] getOreChances(boolean relative);
	void setOreChances(int[] proportionInfo, boolean relative);
	void replenishOre(Block block, boolean playEffect, boolean updateSubplates);
	void replenishAllOres(boolean playEffect, boolean updateSubplates);
}
