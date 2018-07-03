package pl.mateam.marpg.core.objects.worlds.tectonic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.regular.enums.items.Tier;
import pl.mateam.marpg.api.regular.objects.worlds.tectonic.TectonicPlate;
import pl.mateam.marpg.api.regular.objects.worlds.tectonic.TectonicPlateMobsManager;
import pl.mateam.marpg.core.MaCoreCommodoreEngine;
import pl.mateam.marpg.core.MaCoreCommodoreEngine.Secret;
import pl.mateam.marpg.core.internal.hardcoded.TierCompartment;
import pl.mateam.marpg.core.modules.external.server.sub.OresManagerImplementation;
import pl.mateam.marpg.core.objects.effects.OreRegenerationCommodoreEffect;

public class TectonicPlateMobsManagerImplementation extends AbstractTectonicPlateManager<TectonicPlateMobsManager> implements TectonicPlateMobsManager {
	public TectonicPlateMobsManagerImplementation(TectonicPlate parentPlate) {
		super(parentPlate, TectonicPlate::getMobsManager);
	}
	
	private final static int[] coreOresAppearChance = {TierCompartment.DAMAGED, TierCompartment.POORLY_MADE, TierCompartment.CASUAL, TierCompartment.STRENGHENED,
			TierCompartment.ELITE, TierCompartment.EPIC, TierCompartment.LEGENDARY, TierCompartment.DIVINE};
	private final static int coreSum = TierCompartment.SUM;


	private final int[] oresAppearChance = Arrays.copyOf(coreOresAppearChance, coreOresAppearChance.length);
	private boolean isRelative;
	private int sum = coreSum;
	
	private HashMap<Block, BukkitRunnable> minedBlocks = new HashMap<>();
	
	@Override public int[] getOreChances(boolean relative)	{
		if(relative || parent == null)
			return Arrays.copyOf(oresAppearChance, oresAppearChance.length);
		else {
			int[] result = new int[oresAppearChance.length];
			for(int i = 0; i < result.length; i++)
				result[i] = ((TectonicPlateMobsManagerImplementation) parent).oresAppearChance[i] * oresAppearChance[i];
			return result;
		}
	}
	
	@Override public void setOreChances(int[] proportionInfo, boolean relative) {
		for(int i = 0; i < proportionInfo.length && i < proportionInfo.length; i++) {
			sum -= oresAppearChance[i];
			oresAppearChance[i] = proportionInfo[i];
			sum += proportionInfo[i];
		}
		this.isRelative = relative;
	}

	@SuppressWarnings("deprecation")
	@Override public void replenishOre(Block block, boolean playEffect, boolean updateSubplates) {
		BukkitRunnable task = minedBlocks.get(block);
		if(task != null) {
			task.cancel();
			if(playEffect)
				new OreRegenerationCommodoreEffect(block.getLocation()).playInternal();
			int tierNumber = 0;
			
			if(isRelative) {
				int[] parentValues = parent == null? coreOresAppearChance : ((TectonicPlateMobsManagerImplementation) parent).oresAppearChance;
				int sum = 0;
				for(int i = 0; i < parentValues.length; i++)
					sum += parentValues[i] * oresAppearChance[i];
				int number = (int) (Math.random() * sum + 1);
				for(int i = 0; i < oresAppearChance.length && number > 0; i++, tierNumber++)
					number -= oresAppearChance[i] * parentValues[i];
					
			} else {
				int number = (int) (Math.random() * sum + 1);
				for(int i = 0; i < oresAppearChance.length && number > 0; i++, tierNumber++)
					number -= oresAppearChance[i];
			}
			
			Tier tier = Tier.getUsingID(tierNumber);
			Material material = tier.getOreMaterial();
			byte durability = tier.getOreDurability();
			block.setType(material);
			block.setData(durability);
		} else
			updateSubplates(plate -> replenishOre(block, playEffect, true));
	}

	@Override public void replenishAllOres(boolean playEffect, boolean updateSubplates) {
		for(Entry<Block, BukkitRunnable> entry : minedBlocks.entrySet())
			replenishOre(entry.getKey(), playEffect, false);

		minedBlocks.clear();
		
		if(updateSubplates)
			updateSubplates(plate -> plate.getOresManager().replenishAllOres(playEffect, true));
	}
	
	@Secret public void notifyThatOreGotMined(Block block) {
		if(minedBlocks.containsKey(block))
			return;
		BukkitRunnable runnable = new BukkitRunnable() {
			@Override public void run() {
				replenishOre(block, true, false);
				minedBlocks.remove(block);
			}
		};
		minedBlocks.put(block, runnable);
		runnable.runTaskLater(MaCoreCommodoreEngine.getReference(), ((OresManagerImplementation) Core.getServer().getOres()).getRandomTime() * 20);
	}
}
