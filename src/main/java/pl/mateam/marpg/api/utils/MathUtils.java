package pl.mateam.marpg.api.utils;

import java.math.RoundingMode;

public interface MathUtils {
	double round(double number, int amountOfPlaces, RoundingMode mode);
	double round(double number, int amountOfPlaces);
}
