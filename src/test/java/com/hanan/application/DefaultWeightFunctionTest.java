package com.hanan.application;

import com.hanan.models.PixelDTO;
import com.hanan.weightfunctions.implementation.DefaultWeightFunction;
import org.junit.Assert;
import org.junit.Test;

public class DefaultWeightFunctionTest {

    private PixelDTO u;
    private PixelDTO v;
    private double zValue;
    private double epsilonValue;
    private DefaultWeightFunction defaultWeightFunction;

    @Test
    public void testValueCalculation_positiveZ() {
        zValue = 2.0;
        epsilonValue = 0.5;

        defaultWeightFunction = new DefaultWeightFunction(zValue, epsilonValue);
        u = new PixelDTO(3, 4, 0);
        v = new PixelDTO(5, 4, 0);

        double expectedValue = 1 / 4.5;
        double result = defaultWeightFunction.calculateValue(u, v);
        Assert.assertEquals(expectedValue, result, 0);
    }

    @Test
    public void testValueCalculation_negativeZ() {
        zValue = -5.0;
        epsilonValue = 0.005;
        defaultWeightFunction = new DefaultWeightFunction(zValue, epsilonValue);
        u = new PixelDTO(3, 4, 0);
        v = new PixelDTO(8, 4, 0);

        double expectedValue = 1 / 0.00532;
        double result = defaultWeightFunction.calculateValue(u, v);
        Assert.assertEquals(expectedValue, result, 0);
    }
}
