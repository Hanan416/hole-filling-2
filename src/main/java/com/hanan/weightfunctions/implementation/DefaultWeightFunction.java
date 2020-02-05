package com.hanan.weightfunctions.implementation;

import com.hanan.models.PixelDTO;
import com.hanan.weightfunctions.interfaces.IWeightFunctionsBase;

public class DefaultWeightFunction implements IWeightFunctionsBase {

    private double zValue;
    private double epsilonValue;

    public DefaultWeightFunction(double zValue, double epsilonValue) {
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
