package pl.mateam.marpg.core.data.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import pl.mateam.marpg.core.MaCoreCommodoreEngine;

public abstract class AbstractCommodoreTask extends BukkitRunnable {
	public final void register() 	{	runTaskTimer(MaCoreCommodoreEngine.getReference(), 0, delayInTicks());	}
	public abstract int delayInTicks();
}
