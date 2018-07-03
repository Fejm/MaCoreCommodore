package pl.mateam.marpg.api.utils;

public interface ParsingUtils {
	String timeInSecondsToText(long timeInSeconds);
	int[] convertHSVtoRGB(short hue, double saturation, double value);
}
