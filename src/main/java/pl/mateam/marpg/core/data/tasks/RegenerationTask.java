package pl.mateam.marpg.core.data.tasks;

import org.bukkit.entity.Player;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.objects.users.ExtendedPlayer;

public class RegenerationTask extends AbstractCommodoreTask {
	@Override public void run() {
		for(ExtendedPlayer extPlayer : Core.getServer().getUsers().getAllExtendedPlayers()){
			Player player = extPlayer.getBukkitPlayer();
			if(player.isDead())
				continue;

				double regenerationValue = Core.getServer().getMechanics().getRegenerationValue();
				extPlayer.changeHPLevel((1 + extPlayer.getHpRegeneration()/100D) * regenerationValue);
				extPlayer.changeEPLevel((1 + extPlayer.getEpRegeneration()/100D) * regenerationValue);

			double currentHP = player.getHealth();
			
			if(currentHP < 2)		CoreUtils.ingame.playSoundPrivate(player, false, "other.very_low_hp");
			else if(currentHP < 6) 	CoreUtils.ingame.playSoundPrivate(player, false, "other.low_hp");
		}
	}

	@Override public int delayInTicks() 	{	return 20;	}
}
