package pl.mateam.marpg.api.regular.enums.players;

import java.util.function.Supplier;

import org.bukkit.ChatColor;

import pl.mateam.marpg.api.Core;

public enum Rank {
	PLAYER(ChatColor.DARK_GRAY, Core.getServer().getRanks()::getDefaultRankArmorColorValue, ChatColor.WHITE.toString() + " - " + ChatColor.GRAY.toString() + "gracz", (byte) 0),
	PREMIUM(ChatColor.GOLD, Core.getServer().getRanks()::getPremiumArmorColorValue, ChatColor.WHITE.toString() + " - " + ChatColor.GOLD.toString() + "gracz premium", (byte) 1),
	NOBLEMAN(ChatColor.BLUE, Core.getServer().getRanks()::getNoblemanArmorColorValue, ChatColor.WHITE.toString() + " - " + ChatColor.BLUE.toString() + "szlachcic", (byte) 2);
	
	private final String nicknameColorValue;
	private final String explicitTitleSuffix;
	private final byte permissionsLevel;
	private final Supplier<Float> value;
	
	private Rank(ChatColor nicknameColorValue, Supplier<Float> value, String explicitTitleSuffix, byte permissionsLevel){
		this.nicknameColorValue 	= nicknameColorValue.toString();
		this.explicitTitleSuffix	= explicitTitleSuffix;
		this.permissionsLevel		= permissionsLevel;
		this.value 					= value;
	}
	
	public String getColoredNickname(String playername)	{	return nicknameColorValue + playername;		}
	public String getExplicitTitle(String playername) 	{	return playername + explicitTitleSuffix;	}

	public boolean isExactPremium(){
		return permissionsLevel == (byte) 1;
	}
	public boolean isAtLeastPremium(){
		return permissionsLevel >= 1;
	}
	public boolean isNoble(){
		return permissionsLevel == (byte) 2;
	}
	public float getHSVArmorColorValue(){
		return value.get();
	}
}
