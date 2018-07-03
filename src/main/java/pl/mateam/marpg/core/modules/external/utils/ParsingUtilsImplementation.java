package pl.mateam.marpg.core.modules.external.utils;

import pl.mateam.marpg.api.utils.ParsingUtils;

public class ParsingUtilsImplementation implements ParsingUtils {
	@Override public String timeInSecondsToText(long timeInSeconds){
		int days = 0;
		byte hours = 0;
		byte minutes = 0;
		days = (int) (timeInSeconds/86400);
		timeInSeconds -= days*86400;
		hours = (byte) (timeInSeconds/3600);
		timeInSeconds -= hours*3600;
		minutes = (byte) (timeInSeconds/60);
		String dayForm, hoursForm, minutesForm;
		dayForm = days == 1? " dzieñ " : " dni ";
		
		if(hours == 1) hoursForm = " godzina ";
		else if(hours > 4 && hours <20) hoursForm = " godzin ";
		else if(hours % 10 > 1 && hours % 10 < 5) hoursForm = " godziny ";
		else hoursForm = " godzin ";
		
		if(minutes == 1) minutesForm = " minuta ";
		else if(minutes > 4 && minutes <20) minutesForm = " minut ";
		else if(minutes % 10 > 1 && minutes % 10 < 5) minutesForm = " minuty ";
		else minutesForm = " minut ";
		if(days > 0)
			return days + dayForm + hours + hoursForm + minutes + minutesForm;
		if(hours > 0)
			return hours + hoursForm + minutes + minutesForm;
		timeInSeconds -= minutes*60;
		minutesForm = minutesForm + " ";
		String sekundonator;
		if(timeInSeconds == 1) sekundonator = " sekunda";
		else if(timeInSeconds > 4 && timeInSeconds <20) sekundonator = " sekund";
		else if(timeInSeconds % 10 > 1 && timeInSeconds % 10 < 5) sekundonator = " sekundy";
		else sekundonator = " sekund";
		if(minutes > 0)
			return minutes + minutesForm + timeInSeconds + sekundonator;
		return timeInSeconds + sekundonator;
	}
	
	@Override
	public int[] convertHSVtoRGB(short hue, double saturation, double value){
		int[] result = new int[3];
		int i = (int) Math.floor(hue/60); //hue should be within range 0-359
		double f = Double.valueOf(hue)/60 - i;
		double p = value * (1 - saturation);
		double q = value * (1 - (saturation*f));
		double t = value * (1 - (saturation*(1-f)));
		int r = 0;
		int g = 0;
		int b = 0;
		switch(i%6){
			case 0:
			r = (int) (value * 255);
			g =  (int) (t * 255);
			b = (int) (p * 255);
			break;
		case 1:
			r = (int) (q * 255);
			g =  (int) (value * 255);
			b = (int) (p * 255);
			break;
		case 2:
			r = (int) (p * 255);
			g =  (int) (value * 255);
			b = (int) (t * 255);
			break;
		case 3:
			r = (int) (p * 255);
			g =  (int) (q * 255);
			b = (int) (value * 255);
			break;
		case 4:
			r = (int) (t * 255);
			g =  (int) (p * 255);
			b = (int) (value * 255);
			break;
		case 5:
			r = (int) (value * 255);
			g =  (int) (p * 255);
			b = (int) (q * 255);
			break;
		}
		result[0] = r;
		result[1] = g;
		result[2] = b;
		return result;
	}
}
