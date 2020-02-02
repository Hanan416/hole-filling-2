package com.hanan.application;

import com.hanan.models.PixelDTO;
import com.hanan.utilities.implementations.DefaultWeightFunctionsUtil;
import org.junit.Assert;
import org.junit.Test;

public class DefaultWeightFunctionsUtilTest {


    private DefaultWeightFunctionsUtil defaultWeightFunctionsUtil = new DefaultWeightFunctionsUtil();
    private PixelDTO u;
    private PixelDTO v;
    private double zValue;
    private double epsilonValue;


    @Test
    public void testValueCalculation_positiveZ() {
        zValue = 2.0;
        epsilonValue = 0.5;
        u = new PixelDTO(3, 4, 0);
        v = new PixelDTO(5, 4, 0);

        double expectedValue = 1 / 4.5;
        double result = defaultWeightFunctionsUtil.calculateValue(u, v, zValue, epsilonValue);
        Assert.assertEquals(expectedValue, result, 0);
    }

    @Test
    public void testValueCalculation_negativeZ() {
        zValue = -5.0;
        epsilonValue = 0.005;
        u = new PixelDTO(3, 4, 0);
        v = new PixelDTO(8, 4, 0);

        double expectedValue = 1 / 0.00532;
        double result = defaultWeightFunctionsUtil.calculateValue(u, v, zValue, epsilonValue);
        Assert.assertEquals(expectedValue, result, 0);
    }
}
