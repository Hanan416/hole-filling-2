package com.hanan.utilities.implementations;

import com.hanan.models.PixelDTO;
import com.hanan.utilities.interfaces.IWeightFunctionsUtil;

public class DefaultWeightFunctions implements IWeightFunctionsUtil {

    private double zValue;
    private double epsilonValue;

    public DefaultWeightFunctions(double zValue, double epsilonValue) {
        this.zValue = zValue;
        this.epsilonValue = epsilonValue;
    }

    public double calculateValue(PixelDTO u, PixelDTO v) {
        return (1 / (Math.pow(getEuclideanDistance(u, v), zValue) + epsilonValue));
    }

    private double getEuclideanDistance(PixelDTO u, PixelDTO v) {
        double a = Math.pow((v.getXCoordinate() - u.getXCoordinate()), 2);
        double b = Math.pow((v.getYCoordinate() - u.getYCoordinate()), 2);
        return Math.sqrt(a + b);
    }
}
