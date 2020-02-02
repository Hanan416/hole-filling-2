package com.hanan.utilities.implementations;

import com.hanan.models.PixelDTO;
import com.hanan.utilities.interfaces.IWeightFunctionsUtil;

public class DefaultWeightFunctionsUtil implements IWeightFunctionsUtil {

    public double calculateValue(PixelDTO u, PixelDTO v, double zValue, double epsilonValue) {
        return (1 / (Math.pow(getEuclideanDistance(u, v), zValue) + epsilonValue));
    }

    private double getEuclideanDistance(PixelDTO u, PixelDTO v) {
        double a = Math.pow((v.getXCoordinate() - u.getXCoordinate()), 2);
        double b = Math.pow((v.getYCoordinate() - u.getYCoordinate()), 2);
        return Math.sqrt(a + b);
    }
}
