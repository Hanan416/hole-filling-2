package com.hanan.utilities;

import com.hanan.config.AppConfig;
import com.hanan.models.PixelDTO;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.Set;

public class ImageUtil {

    public Mat getRGBImageByFileName(String filename) {

        Mat m = Imgcodecs.imread(filename, Imgcodecs.IMREAD_COLOR);
        Mat mNormalized = new Mat();
        m.convertTo(mNormalized, CvType.CV_32F, 1.f / 255);

        return mNormalized;
    }

    public Mat getGrayScaleImageByFileName(String filename) {
        Mat m = Imgcodecs.imread(filename, Imgcodecs.IMREAD_GRAYSCALE);
        Mat mNormalized = new Mat();
        m.convertTo(mNormalized, CvType.CV_32F, 1.f / 255);

        return mNormalized;
    }

    public Mat applyMask(Mat origImage, String maskFileName) {
        Mat maskingImg = getGrayScaleImageByFileName(maskFileName);
        return makeHoleByThreshold(origImage, maskingImg, AppConfig.HOLE_THRESHOLD);
    }

    public void initializePixelDTOsData(Mat holedImage, Set<PixelDTO> boundaryPixelDTOs, Set<PixelDTO> holePixelDTOs, String connectivityType) {
        for (int i = 0; i < holedImage.cols(); i++) {
            for (int j = 0; j < holedImage.rows(); j++) {
                double pixelData = holedImage.get(j, i)[0];
                if (pixelData == -1) {
                    holePixelDTOs.add(new PixelDTO(i, j, pixelData));

                    int up = j - 1;
                    int down = j + 1;
                    int left = i - 1;
                    int right = i + 1;

                    if (up >= 0 && holedImage.get(up, i)[0] != -1)
                        boundaryPixelDTOs.add(new PixelDTO(i, up, holedImage.get(up, i)[0]));
                    if (down < holedImage.rows() && holedImage.get(down, i)[0] != -1)
                        boundaryPixelDTOs.add(new PixelDTO(i, down, holedImage.get(down, i)[0]));
                    if (left >= 0 && holedImage.get(j, left)[0] != -1)
                        boundaryPixelDTOs.add(new PixelDTO(left, j, holedImage.get(j, left)[0]));
                    if (right < holedImage.cols() && holedImage.get(j, right)[0] != -1)
                        boundaryPixelDTOs.add(new PixelDTO(right, j, holedImage.get(j, right)[0]));
                    if (AppConfig.EIGHT_CONNECTED.equals(connectivityType)) {
                        if (up >= 0 && left >= 0 && holedImage.get(left, up)[0] != -1)
                            boundaryPixelDTOs.add(new PixelDTO(left, up, holedImage.get(up, left)[0]));
                        if (up >= 0 && right < holedImage.cols() && holedImage.get(right, up)[0] != -1)
                            boundaryPixelDTOs.add(new PixelDTO(right, up, holedImage.get(up, right)[0]));
                        if (down < holedImage.rows() && left >= 0 && holedImage.get(down, left)[0] != -1)
                            boundaryPixelDTOs.add(new PixelDTO(left, down, holedImage.get(down, left)[0]));
                        if (down < holedImage.rows() && right < holedImage.cols() && holedImage.get(down, right)[0] != -1)
                            boundaryPixelDTOs.add(new PixelDTO(right, down, holedImage.get(down, right)[0]));
                    }
                }
            }
        }
    }

    
    private Mat makeHoleByThreshold(Mat imageForMasking, Mat mask, double threshold) {

        for (int i = 0; i < imageForMasking.cols(); i++) {
            for (int j = 0; j < imageForMasking.rows(); j++) {
                double[] pixelData = imageForMasking.get(j, i);
                double[] pixelMaskData = mask.get(j, i);
                pixelData[0] = pixelMaskData[0] <= threshold ? -1 : pixelData[0];
                imageForMasking.put(j, i, pixelData);
            }
        }
        return imageForMasking;
    }


}
