package pl.mateam.marpg.core.modules.external.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import pl.mateam.marpg.api.utils.MathUtils;

public class MathUtilsImplementation implements MathUtils {
	@Override public double round(double number, int amountOfPlaces, RoundingMode mode){
		String format = "#.";
		for(int i = 0; i < amountOfPlaces; i++)
			format = format + "#";
		DecimalFormat df = new DecimalFormat(format);
		df.setRoundingMode(mode);
		return Double.parseDouble(df.format(number).replace(",", "."));
	}
	@Override public double round(double number, int amountOfPlaces)	{		return round(number, amountOfPlaces, RoundingMode.FLOOR);	}
}
