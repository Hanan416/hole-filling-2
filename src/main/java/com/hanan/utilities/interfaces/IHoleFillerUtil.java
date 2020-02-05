package com.hanan.utilities.interfaces;

import com.hanan.models.PixelDTO;
import org.opencv.core.Mat;

import java.util.Set;

public interface IHoleFillerUtil {

    void fillHole(Mat holedImage, Set<PixelDTO> boundaryPixelDTOs, Set<PixelDTO> holePixelDTOs, IWeightFunctionsUtil weightFunctionsUtil);
}
