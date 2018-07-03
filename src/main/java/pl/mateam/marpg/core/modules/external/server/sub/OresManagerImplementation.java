package pl.mateam.marpg.core.modules.external.server.sub;

import java.util.Random;

import pl.mateam.marpg.api.regular.modules.sub.server.OresManager;
import pl.mateam.marpg.core.MaCoreCommodoreEngine.Secret;

public class OresManagerImplementation implements OresManager {
	private int minimalTime;
	private int maximalTime;
	
	@Override public int getMinimalRegenerationTimeInSeconds() 	{	return minimalTime;		}
	@Override public int getMaximalRegenerationTimeInSeconds()	{	return maximalTime;		}

	@Override public void setMinimalRegenerationTimeInSeconds(int newValue) 	{	this.minimalTime = newValue;	}
	@Override public void setMaximalRegenerationTimeInSeconds(int newValue) 	{	this.maximalTime = newValue;	}
	
	@Secret public int getRandomTime() 	{	return new Random().nextInt(maximalTime - minimalTime) + minimalTime;	}
}
