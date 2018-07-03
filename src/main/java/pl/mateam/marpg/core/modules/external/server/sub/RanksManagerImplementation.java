package pl.mateam.marpg.core.modules.external.server.sub;

import pl.mateam.marpg.api.regular.modules.sub.server.RanksManager;

public class RanksManagerImplementation implements RanksManager {
	private float defaultRankArmorColorValue = 0.5F;
	private float premiumArmorColorValue = 0.75F;
	private float noblemanArmorColorValue = 1F;

	@Override public float getDefaultRankArmorColorValue()							{	return defaultRankArmorColorValue;				}
	@Override public float getPremiumArmorColorValue()								{	return premiumArmorColorValue;					}
	@Override public float getNoblemanArmorColorValue()								{	return noblemanArmorColorValue;					}
	@Override public void setDefaultRankArmorColorValue(float ArmorColorValue)		{	defaultRankArmorColorValue = ArmorColorValue;	}
	@Override public void setPremiumArmorColorValue(float ArmorColorValue)			{	premiumArmorColorValue = ArmorColorValue;		}
	@Override public void setNoblemanArmorColorValue(float ArmorColorValue)			{	noblemanArmorColorValue = ArmorColorValue;		}
}
