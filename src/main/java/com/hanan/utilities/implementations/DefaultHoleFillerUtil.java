package com.hanan.utilities.implementations;

import com.hanan.models.PixelDTO;
import com.hanan.utilities.interfaces.IHoleFillerUtil;
import com.hanan.utilities.interfaces.IWeightFunctionsUtil;
import org.opencv.core.Mat;

import java.util.Set;

public class DefaultHoleFillerUtil implements IHoleFillerUtil {


    public void fillHole(Mat holedImage, Set<PixelDTO> boundaryPixelDTOs, Set<PixelDTO> holePixelDTOs, IWeightFunctionsUtil weightFunctionsUtil) {
        double weightValue;
        double numeratorSum = 0;
        double denominatorSum = 0;

        for (PixelDTO holePixelDTO : holePixelDTOs) {
            for (PixelDTO boundaryPixelDTO : boundaryPixelDTOs) {
                weightValue = weightFunctionsUtil.calculateValue(holePixelDTO, boundaryPixelDTO);
                numeratorSum += weightValue * boundaryPixelDTO.getValue();
                denominatorSum += weightValue;
            }
            double[] holePixelData = holedImage.get(holePixelDTO.getYCoordinate(), holePixelDTO.getXCoordinate());
            holePixelData[0] = numeratorSum / denominatorSum;
            holedImage.put(holePixelDTO.getYCoordinate(), holePixelDTO.getXCoordinate(), holePixelData);
        }
    }
}
