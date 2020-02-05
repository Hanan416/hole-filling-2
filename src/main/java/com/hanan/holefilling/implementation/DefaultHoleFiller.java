package com.hanan.holefilling.implementation;

import com.hanan.holefilling.interfaces.IHoleFillerBase;
import com.hanan.models.PixelDTO;
import com.hanan.weightfunctions.interfaces.IWeightFunctionsBase;
import org.opencv.core.Mat;

import java.util.Set;

public class DefaultHoleFiller implements IHoleFillerBase {


    public void fillHole(Mat holedImage, Set<PixelDTO> boundaryPixelDTOs, Set<PixelDTO> holePixelDTOs, IWeightFunctionsBase weightFunction) {
        double weightValue;
        double numeratorSum = 0;
        double denominatorSum = 0;

        for (PixelDTO holePixelDTO : holePixelDTOs) {
            for (PixelDTO boundaryPixelDTO : boundaryPixelDTOs) {
                weightValue = weightFunction.calculateValue(holePixelDTO, boundaryPixelDTO);
                numeratorSum += weightValue * boundaryPixelDTO.getValue();
                denominatorSum += weightValue;
            }
            double[] holePixelData = holedImage.get(holePixelDTO.getYCoordinate(), holePixelDTO.getXCoordinate());
            holePixelData[0] = numeratorSum / denominatorSum;
            holedImage.put(holePixelDTO.getYCoordinate(), holePixelDTO.getXCoordinate(), holePixelData);
        }
    }
}
