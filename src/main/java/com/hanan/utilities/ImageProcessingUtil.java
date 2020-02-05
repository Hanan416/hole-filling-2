package com.hanan.utilities;

import com.hanan.config.AppConfig;
import com.hanan.models.PixelDTO;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.HashSet;
import java.util.Set;

public class ImageProcessingUtil {

    private Mat sourceImage;
    private Mat maskImage;
    private Mat holedImage;
    private String connectivityType;
    private Set<PixelDTO> boundaryPixelDTOs;
    private Set<PixelDTO> holePixelDTOs;

    public ImageProcessingUtil() {
        boundaryPixelDTOs = new HashSet<PixelDTO>();
        holePixelDTOs = new HashSet<PixelDTO>();
    }

    public void initImages(String sourceImageStr, String maskImageStr) {
        sourceImage = getGrayScaleImageByFileName(sourceImageStr);
        maskImage = getGrayScaleImageByFileName(maskImageStr);

        if (sourceImage.cols() != maskImage.cols() && sourceImage.rows() != maskImage.rows())
            throw new RuntimeException(AppConfig.IMAGE_SIZE_MISMATCH_ERROR);

        holedImage = getGrayScaleImageByFileName(sourceImageStr);
    }

    public Set<PixelDTO> getBoundaryPixelDTOs() {
        return boundaryPixelDTOs;
    }

    public Set<PixelDTO> getHolePixelDTOs() {
        return holePixelDTOs;
    }

    public void setHoledImage(Mat holedImage) {
        this.holedImage = holedImage;
    }

    public void setConnectivityType(String connectivityType) {
        this.connectivityType = connectivityType;
    }

    public Mat applyMask() {
        for (int i = 0; i < sourceImage.cols(); i++) {
            for (int j = 0; j < sourceImage.rows(); j++) {
                double[] pixelData = sourceImage.get(j, i);
                double[] pixelMaskData = maskImage.get(j, i);
                pixelData[0] = pixelMaskData[0] <= AppConfig.HOLE_THRESHOLD ? -1 : pixelData[0];
                holedImage.put(j, i, pixelData);
            }
        }
        initializePixelDTOsData();
        return holedImage;
    }

    public void writeResult() {
        Mat resultMat = new Mat();
        holedImage.convertTo(resultMat, CvType.CV_32F, 255.0);
        String path = System.getProperty("user.dir") + "\\result.jpg";
        System.out.println("Writing result to: " + path);
        Imgcodecs.imwrite(path, resultMat);
    }

    public void initializePixelDTOsData() {
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
                        if (up >= 0 && left >= 0 && holedImage.get(up, left)[0] != -1)
                            boundaryPixelDTOs.add(new PixelDTO(left, up, holedImage.get(up, left)[0]));
                        if (up >= 0 && right < holedImage.cols() && holedImage.get(up, right)[0] != -1)
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

    private Mat getGrayScaleImageByFileName(String filename) {
        Mat m = Imgcodecs.imread(filename, Imgcodecs.IMREAD_GRAYSCALE);
        Mat mNormalized = new Mat();
        m.convertTo(mNormalized, CvType.CV_32F, 1.f / 255);

        return mNormalized;
    }


}
