package pl.mateam.marpg.api.regular.modules.sub.server;

public interface RanksManager {
	float getDefaultRankArmorColorValue();
	float getPremiumArmorColorValue();
	float getNoblemanArmorColorValue();
	void setDefaultRankArmorColorValue(float ArmorColorValue);
	void setPremiumArmorColorValue(float ArmorColorValue);
	void setNoblemanArmorColorValue(float ArmorColorValue);
}
