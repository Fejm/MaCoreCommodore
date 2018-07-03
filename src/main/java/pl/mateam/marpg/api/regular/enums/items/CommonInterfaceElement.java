package pl.mateam.marpg.api.regular.enums.items;

public enum CommonInterfaceElement {
	MENU_DEFAULT_ELEMENT_CENTER("domyslnyElementMenuSrodek"),
	MENU_DEFAULT_ELEMENT_LEFT("domyslnyLewyElementMenu"),
	MENU_DEFAULT_ELEMENT_RIGHT("domyslnyPrawyElementMenu"),
	
	MENU_TRADE_ARROWS("handelStrzalki"),
	MENU_TRADE_MONEY_1("handelPoteflon1"),
	MENU_TRADE_MONEY_10("handelPoteflon10"),
	MENU_TRADE_MONEY_100("handelPoteflon100"),
	MENU_TRADE_MONEY_1000("handelPoteflon1000"),
	MENU_TRADE_MONEY_PLUS("handelPoteflonPlus"),
	MENU_TRADE_MONEY_MINUS("handelPoteflonMinus"),
	MENU_TRADE_ACCEPT_SINGLE("handelAkceptujJeden"),
	MENU_TRADE_ACCEPT_BOTH("handelAkceptujObaj"),
	MENU_TRADE_ACCEPTED("handelZaakceptowal"),
	MENU_TRADE_ACCEPT_UNABLE("handelAkceptujBlad"),
	MENU_TRADE_DISCARD("handelNieAkceptuj"),
	
	MENU_ANVIL("kowadlo"),
	MENU_ANVIL_UNABLE("kowadloNiedostatek"),
	MENU_ANVIL_ABLE("kowadloGotowe"),
	
	MENU_ENCHANTER("zaklinacz"),
	MENU_ENCHANTER_UNABLE("zaklinaczNiedostatek"),
	MENU_ENCHANTER_ABLE("zaklinaczGotowe"),
	MENU_ENCHANTER_QUESTION_MARK("zaklinaczPytajnik"),

	MENU_MERCHANT("kupiec"),
	MENU_MERCHANT_SELL("kupiecSprzedaj"),
	
	MENU_TRADER_CHANGE_QUALITY("zmienJakosc"),
	MENU_TRADER_QUALITY_POOR("jakoscSlaboWykonane"),
	MENU_TRADER_QUALITY_NORMAL("jakoscZwyczajne"),
	MENU_TRADER_QUALITY_GOOD("jakoscWzmocnione"),
	
	
	
	EMPTY_NECKLACE_SLOT("pustyNaszyjnik"),
	EMPTY_RING_SLOT("pustyPierscien"),
	EMPTY_WEAPON_SLOT("pustaBron"),
	
	
	
	SKILL_EMPTY("pustySkill"),
	
	SKILL_WARRIOR_READY_1("skill1wojownik"),
	SKILL_WARRIOR_READY_2("skill2wojownik"),
	SKILL_WARRIOR_READY_3("skill3wojownik"),
	SKILL_WARRIOR_READY_4("skill4wojownik"),
	SKILL_WARRIOR_READY_5("skill5wojownik"),
	SKILL_WARRIOR_READY_6("skill6wojownik"),
	SKILL_WARRIOR_CASTING_1("skill1wojownikSzary"),
	SKILL_WARRIOR_CASTING_2("skill2wojownikSzary"),
	SKILL_WARRIOR_CASTING_3("skill3wojownikSzary"),
	SKILL_WARRIOR_CASTING_4("skill4wojownikSzary"),
	SKILL_WARRIOR_CASTING_5("skill5wojownikSzary"),
	SKILL_WARRIOR_CASTING_6("skill6wojownikSzary"),

	SKILL_BARBARIAN_READY_3("skill3barbarzynca"),
	SKILL_BARBARIAN_READY_4("skill4barbarzynca"),
	SKILL_BARBARIAN_READY_5("skill5barbarzynca"),
	SKILL_BARBARIAN_READY_6("skill6barbarzynca"),
	SKILL_BARBARIAN_CASTING_3("skill3barbarzyncaSzary"),
	SKILL_BARBARIAN_CASTING_4("skill4barbarzyncaSzary"),
	SKILL_BARBARIAN_CASTING_5("skill5barbarzyncaSzary"),
	SKILL_BARBARIAN_CASTING_6("skill6barbarzyncaSzary"),
	
	SKILL_MAGE_READY_1("skill1mag"),
	SKILL_MAGE_READY_2("skill2mag"),
	SKILL_MAGE_READY_3("skill3mag"),
	SKILL_MAGE_READY_4("skill4mag"),
	SKILL_MAGE_READY_5("skill5mag"),
	SKILL_MAGE_READY_6("skill6mag"),
	SKILL_MAGE_CASTING_1("skill1magSzary"),
	SKILL_MAGE_CASTING_2("skill2magSzary"),
	SKILL_MAGE_CASTING_3("skill3magSzary"),
	SKILL_MAGE_CASTING_4("skill4magSzary"),
	SKILL_MAGE_CASTING_5("skill5magSzary"),
	SKILL_MAGE_CASTING_6("skill6magSzary"),
	
	
	
	CHAT_MAIN_MUTED("czatGlownyWyciszony"),
	CHAT_MAIN_MUTED_BANNED("czatGlownyWyciszonyZablokowany"),
	CHAT_MAIN_ACTIVE("czatGlownySluchanie"),
	CHAT_MAIN_ACTIVE_BANNED("czatGlownySluchanieZablokowany"),
	CHAT_MAIN_TYPING("czatGlownyAktywny"),
	
	CHAT_TRADE_MUTED("czatHandlowyWyciszony"),
	CHAT_TRADE_MUTED_BANNED("czatHandlowyWyciszonyZablokowany"),
	CHAT_TRADE_ACTIVE("czatHandlowySluchanie"),
	CHAT_TRADE_ACTIVE_BANNED("czatHandlowySluchanieZablokowany"),
	CHAT_TRADE_TYPING("czatHandlowyAktywny"),
	
	CHAT_MODERATORS_ACTIVE("czatModeratorowSluchanie"),
	CHAT_MODERATORS_TYPING("czatModeratorowAktywny"),
	
	CHAT_GAMEMASTERS_MUTED("czatMistrzowWyciszony"),
	CHAT_GAMEMASTERS_ACTIVE("czatMistrzowSluchanie"),
	CHAT_GAMEMASTERS_TYPING("czatMistrzowAktywny"),


	CHAT_PRIVATE_EMPTY("czatPrywatnyPusty"),
	CHAT_PRIVATE_ACTIVE("czatPrywatnySluchanie"),
	CHAT_PRIVATE_TYPING("czatPrywatnyAktywny"),

	CHAT_PRIVATE_OPTIONS("czatPrywatnyOpcje"),
	CHAT_PRIVATE_ADDING("czatPrywatnyDodawanie"),
	CHAT_PRIVATE_REMOVING("czatPrywatnyUsuwanie"),
	CHAT_PRIVATE_BLOCKING("czatPrywatnyBlokowanie"),
	CHAT_PRIVATE_PARDONNING("czatPrywatnyOdblokowywanie"),
	
	
	PAGE_PREVIOUS("poprzedniaStrona"),
	PAGE_NEXT("nastepnaStrona"),
	
	
	
	PROPOSE_TRADE_SEND("zaproponujHandel"),
	PROPOSE_TRADE_SENT("zaproponowalHandel"),
	PROPOSE_TRADE_RECEIVED("zaproponowanoHandel"),
	PROPOSE_TRADE_UNABLE("zajetyHandel"),

	PROPOSE_PVP_SEND("zaproponujPojedynek"),
	PROPOSE_PVP_SENT("zaproponowalPojedynek"),
	PROPOSE_PVP_RECEIVED("zaproponowanoPojedynek"),
	PROPOSE_PVP_UNABLE("zajetyPojedynek"),
	PROPOSE_PVP_SENDER_NO_LEVEL("pojedynekWypowiadajacyNiskiPoziom"),
	PROPOSE_PVP_RECEIVER_NO_LEVEL("pojedynekWypowiadanyNiskiPoziom"),
	PROPOSE_PVP_ALREADY_IN("pojedynekWTrakcie"),

	
	
	VIPONLY_5_IN_PRIVATE_CHAT("upTo5inPrivateChatVIP");
	
	
	public String getExploratoryName()	{	return exploratoryName;		}
	private final String exploratoryName;
	private CommonInterfaceElement(String exploratoryName){
		this.exploratoryName = exploratoryName;
	}
}
