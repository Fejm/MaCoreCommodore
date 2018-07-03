package pl.mateam.marpg.core.internal.utils;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_12_R1.NBTBase;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import pl.mateam.marpg.api.CoreUtils;

public class Parsers {
	public static String getProperForm(int count, String element, String elementy, String elementow){
		if(count != 1){
			if(count % 10 > 1 && count % 10 < 5 && (count > 20 || count < 10))
				return elementy;
			else
				return elementow;
		} else return element;
	}
	
	public static String getHash(String givenPassword, String decryptorKey){
		try {
			java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();
			crc32.update(decryptorKey.getBytes(Charset.forName("UTF-8")));
			String eNICK = Long.toHexString(crc32.getValue());
			MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
			sha512.reset();
			sha512.update(givenPassword.getBytes(Charset.forName("UTF-8")));
			byte bPASS[] = sha512.digest();
			StringBuffer sbPASS = new StringBuffer();
			for(int i=0; i< bPASS.length ;i++){
				sbPASS.append(Integer.toString((bPASS[i] & 0xff) + 0x100, 16).substring(1));
			}
			String ePASS = sbPASS.toString();String eVERYTHING = eNICK + ePASS;
			sha512.reset();
			sha512.update(eVERYTHING.getBytes(Charset.forName("UTF-8")));
			byte bVERYTHING[] = sha512.digest();
			StringBuffer sbVERYTHING = new StringBuffer();
			for(int i=0; i< bVERYTHING.length ;i++){
				sbVERYTHING.append(Integer.toString((bVERYTHING[i] & 0xff) + 0x100, 16).substring(1));
			}
			return sbVERYTHING.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getKickBanReason(String reason, String whobanned, Date banned, Date expiration) {
		StringBuilder banFinalReason = new StringBuilder(ChatColor.DARK_RED.toString()).append(ChatColor.BOLD.toString()).append("Twoje konto zostało zablokowane! :(\n");
		if(reason != null)
			banFinalReason.append("\n\n").append(ChatColor.RED.toString()).append(ChatColor.BOLD.toString())
			.append("Pow�d na�o�enia blokady").append("\n").append(ChatColor.RED.toString()).append(reason);
		if(whobanned != null)
			banFinalReason.append("\n\n").append(ChatColor.RED.toString()).append(ChatColor.BOLD.toString())
			.append("Blokad� na�o�y�").append("\n").append(ChatColor.RED.toString()).append(reason);
		banFinalReason.append("\n\n").append(ChatColor.RED.toString()).append(ChatColor.BOLD.toString()).append("Data nałożenia blokady\n")
		.append(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(banned));

		if(expiration != null){
			if(banned != null){
				banFinalReason.append("\n\n").append(ChatColor.RED.toString()).append(ChatColor.BOLD.toString()).append("Data zdjęcia blokady\n")
				.append(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(expiration)).append("\n\n\n").append(ChatColor.GREEN);
			}
			long banExpireTime = expiration.getTime();
			long banDayTime = banned.getTime();
			long length = banExpireTime - banDayTime;
			long passed = new Date().getTime() - banDayTime;
			float percent = (float) CoreUtils.math.round((Double.valueOf(passed)/length)*100, 2);
			int iterator = 25;
			for(int i = 0; i< Math.floor(percent/4); i++){
				banFinalReason.append("\u258c");
				iterator--;
			}
			banFinalReason.append(ChatColor.GRAY.toString());
			for(int i = 0; i<iterator; i++)
				banFinalReason.append("\u258c");
			banFinalReason.append(" ").append(ChatColor.GOLD.toString()).append("[").append(ChatColor.YELLOW.toString())
			.append(percent).append("%").append(ChatColor.GOLD.toString()).append("]");
		}
		return banFinalReason.toString();
	}
	
	public static Object getTagValue(NBTTagCompound compound, String tagName){
		NBTBase tag = compound.get(tagName);
		if(tag == null)
			return null;
		switch(tag.getTypeId()){
			//Byte - 1
			//Short - 2
			//Int - 3
			//Long - 4
			//Float - 5
			//Double - 6
			//Byte[] - 7
			//String - 8
			//List - 9
			//Compound - 10
			//Int[] - 11
			//Long[] - 12
			case 1:		return compound.getByte(tagName);
			case 2:		return compound.getShort(tagName);
			case 3:		return compound.getInt(tagName);
			case 4:		return compound.getLong(tagName);
			case 5:		return compound.getFloat(tagName);
			case 6:		return compound.getDouble(tagName);
			case 8:		return compound.getString(tagName);
			default:	throw new RuntimeException();
		}
	}
	
	public static String genderToString(boolean isWoman) {
		return isWoman? "Kobieta" : "Mężczyzna";
	}
}
