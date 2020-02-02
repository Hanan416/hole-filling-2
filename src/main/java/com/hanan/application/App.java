package com.hanan.application;

import com.hanan.config.AppConfig;
import com.hanan.models.PixelDTO;
import com.hanan.utilities.ImageUtil;
import com.hanan.utilities.implementations.DefaultHoleFillerUtil;
import com.hanan.utilities.interfaces.IHoleFillerUtil;
import org.apache.commons.lang3.StringUtils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.HashSet;
import java.util.Set;

public class App {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {

        if (args.length > 0 && args[0].equals("-h")) {
            usagePrint();
        } else {
            String sourceImage = null;
            String maskImage = null;
            String connectivityType = null;
            String epsilonValue = null;
            String zValue = null;

            for (int i = 0; i < args.length; i++) {
                if ((args[i].equals("-i") || args[i].equals("-ip")) && sourceImage == null)
                    sourceImage = args[++i];
                if ((args[i].equals("-m") || args[i].equals("-mp")) && maskImage == null)
                    maskImage = args[++i];
                if (args[i].equals("-k") && connectivityType == null)
                    connectivityType = args[++i];
                if (args[i].equals("-z") && zValue == null)
                    zValue = args[++i];
                if (args[i].equals("-ep") && epsilonValue == null)
                    epsilonValue = args[++i];
            }
            validateArguments(sourceImage, maskImage, connectivityType, epsilonValue, zValue);

            ImageUtil imageUtil = new ImageUtil();

            Mat greyscaleImage = imageUtil.getGrayScaleImageByFileName(sourceImage);
            Mat holedImage = imageUtil.applyMask(greyscaleImage, maskImage);

            Set<PixelDTO> boundaryPixelDTOs = new HashSet<PixelDTO>();
            Set<PixelDTO> holePixelDTOs = new HashSet<PixelDTO>();
            imageUtil.initializePixelDTOsData(holedImage, boundaryPixelDTOs, holePixelDTOs, connectivityType);

            IHoleFillerUtil holeFillerUtil = new DefaultHoleFillerUtil();
            holeFillerUtil.fillHole(holedImage, boundaryPixelDTOs, holePixelDTOs, Double.parseDouble(zValue), Double.parseDouble(epsilonValue));

            Mat resultMat = new Mat();
            holedImage.convertTo(resultMat, CvType.CV_32F, 255.0);
            Imgcodecs.imwrite("C:\\Users\\hananp\\Desktop\\Projects\\Hole-Filling\\code\\hole-filling-2\\src\\main\\resources\\result.jpg", resultMat);
        }
    }

    private static boolean checkDoubleParsing(String inputArg) {
        try {
            Double.parseDouble(inputArg);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static void validateArguments(String sourceImage, String maskImage, String connectivityType, String epsilonValue, String zValue) {
        if (StringUtils.isEmpty(sourceImage) || StringUtils.isEmpty(maskImage) || StringUtils.isEmpty(connectivityType) || StringUtils.isEmpty(epsilonValue) || StringUtils.isEmpty(zValue)) {
            throw new RuntimeException("Invalid app usage, expected sourceImage, maskImage, connectivityType, epsilonValue, zValue\n" +
                    "got: sourceImage: " + sourceImage + ", maskImage: " + maskImage + ", connectivityType:" + connectivityType + ", epsilonValue:" + epsilonValue + ", zValue:" + zValue);
        } else if (!AppConfig.CONNECTIVITY_TYPES.contains(connectivityType)) {
            throw new RuntimeException("Invalid connectivityType, expected on of: " + AppConfig.CONNECTIVITY_TYPES.toString() + ", got:" + connectivityType);
        } else if (!checkDoubleParsing(epsilonValue)) {
            throw new RuntimeException("Invalid epsilonValue, expected double, got: " + epsilonValue);
        } else if (!checkDoubleParsing(zValue)) {
            throw new RuntimeException("Invalid zValue, expected double, got: " + zValue);
        }
    }

    private static void usagePrint() {
        System.out.println("usage: holeFilling [options]\n" +
                "where options are:\n" +
                "\t-i <value>\t: source image file name, use this flag when the image is withing the app folder\n" +
                "\t-ip <value>\t: source image full path, use this flag when the image is outside the app folder\n" +
                "\t-m <value>\t: mask image file name, use this flag when the mask image is withing the app folder\n" +
                "\t-mp <value>\t: mask image full path, use this flag when the mask image is outside the app folder\n" +
                "\t-k <value>\t: the connectivity type for each pixel, the supported types are: " + AppConfig.CONNECTIVITY_TYPES.toString() + "\n" +
                "\t-z <value>\t: z value for the weighting function power\n" +
                "\t-ep <value>\t: epsilon small double value for the weighting function zero division escape\n" +
                "\n" +
                "example:\n" +
                "\tholeFilling -i imageInCurrentFolder.png -mp C:\\some\\long\\path\\to\\mask\\maskImage.png -k 4-connected -z 10 -ep 0.000001\n");
    }
}