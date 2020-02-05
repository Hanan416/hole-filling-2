package com.hanan.holefilling.interfaces;

import com.hanan.models.PixelDTO;
import com.hanan.weightfunctions.interfaces.IWeightFunctionsBase;
import org.opencv.core.Mat;

import java.util.Set;

public interface IHoleFillerBase {

    void fillHole(Mat holedImage, Set<PixelDTO> boundaryPixelDTOs, Set<PixelDTO> holePixelDTOs, IWeightFunctionsBase weightFunction);
}
