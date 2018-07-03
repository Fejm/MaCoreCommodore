package pl.mateam.marpg.core.data.tasks;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.regular.objects.users.AnyUser;

public class CharacterDataSaverTask extends AbstractCommodoreTask {	
	@Override public void run() {		
		for(AnyUser user : Core.getServer().getUsers().getAllUsersObjects())
			user.saveCurrentStateToDatabase();
	}

	@Override public int delayInTicks() 	{	return Core.getServer().getEnvironment().getCharacterInfoSavingFrequencyInTicks();	}
}
