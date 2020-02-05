package com.hanan.application;

import com.hanan.config.AppConfig;
import com.hanan.holefilling.implementation.DefaultHoleFiller;
import com.hanan.holefilling.interfaces.IHoleFillerBase;
import com.hanan.utilities.ImageProcessingUtil;
import com.hanan.utilities.LoadLibrary;
import com.hanan.weightfunctions.implementation.DefaultWeightFunction;
import com.hanan.weightfunctions.interfaces.IWeightFunctionsBase;
import org.apache.commons.lang3.StringUtils;
import org.opencv.core.Mat;

public class App {

    private static String sourceImage = null;
    private static String maskImage = null;
    private static String connectivityType = null;
    private static String epsilonValueStr = null;
    private static String zValueStr = null;

    public static void main(String[] args) {
        LoadLibrary.loadOpenCV();
        if (readArgs(args)) {
            validateArguments();
            ImageProcessingUtil imageProcessingUtil = initImageProcessingUtil(sourceImage, maskImage, connectivityType);
            Mat holedImage = imageProcessingUtil.applyMask();

            float zValue = getFloatValue(zValueStr, "zValue");
            float epsilonValue = getFloatValue(epsilonValueStr, "epsilonValue");
            IWeightFunctionsBase defaultWeightFunction = new DefaultWeightFunction(zValue, epsilonValue);

            IHoleFillerBase defaultHoleFiller = new DefaultHoleFiller();
            defaultHoleFiller.fillHole(holedImage, imageProcessingUtil.getBoundaryPixelDTOs(), imageProcessingUtil.getHolePixelDTOs(), defaultWeightFunction);

            imageProcessingUtil.writeResult();
        }
    }

    private static ImageProcessingUtil initImageProcessingUtil(String sourceImage, String maskImage, String connectivityType) {
        ImageProcessingUtil imageProcessingUtil = new ImageProcessingUtil();
        imageProcessingUtil.initImages(sourceImage, maskImage);
        imageProcessingUtil.setConnectivityType(connectivityType);

        return imageProcessingUtil;
    }

    private static boolean readArgs(String[] inputArg) {
        if (inputArg.length > 0 && inputArg[0].equals("-h")) {
            usagePrint();
            return false;
        } else {
            for (int i = 0; i < inputArg.length; i++) {
                if (inputArg[i].equals("-i") && sourceImage == null)
                    sourceImage = inputArg[++i];
                if (inputArg[i].equals("-m") && maskImage == null)
                    maskImage = inputArg[++i];
                if (inputArg[i].equals("-k") && connectivityType == null)
                    connectivityType = inputArg[++i];
                if (inputArg[i].equals("-z") && zValueStr == null)
                    zValueStr = inputArg[++i];
                if (inputArg[i].equals("-ep") && epsilonValueStr == null)
                    epsilonValueStr = inputArg[++i];
            }
            return true;
        }
    }

    private static float getFloatValue(String inputArg, String argName) {
        try {
            Float parsedFloat = Float.parseFloat(inputArg);
            return parsedFloat.floatValue();
        } catch (Exception e) {
            throw new RuntimeException("Invalid " + argName + ", expected float value, got: " + inputArg);
        }
    }

    private static void validateArguments() {
        if (StringUtils.isEmpty(sourceImage) || StringUtils.isEmpty(maskImage) || StringUtils.isEmpty(connectivityType) || StringUtils.isEmpty(epsilonValueStr) || StringUtils.isEmpty(zValueStr)) {
            throw new RuntimeException("Invalid app usage, expected sourceImage, maskImage, connectivityType, epsilonValue, zValue\n" +
                    "got: sourceImage: " + sourceImage + ", maskImage: " + maskImage + ", connectivityType:" + connectivityType + ", epsilonValue:" + epsilonValueStr + ", zValue:" + zValueStr);
        } else if (!AppConfig.CONNECTIVITY_TYPES.contains(connectivityType)) {
            throw new RuntimeException("Invalid connectivityType, expected on of: " + AppConfig.CONNECTIVITY_TYPES.toString() + ", got:" + connectivityType);
        }
    }

    private static void usagePrint() {
        System.out.println("usage: holeFilling [options]\n" +
                "where options are:\n" +
                "\t-i <value>\t: source image full path\n" +
                "\t-m <value>\t: mask image full path\n" +
                "\t-k <value>\t: the connectivity type for each pixel, the supported types are: " + AppConfig.CONNECTIVITY_TYPES.toString() + "\n" +
                "\t-z <value>\t: z value for the weighting function power\n" +
                "\t-ep <value>\t: epsilon small double value for the weighting function zero division escape\n" +
                "\n" +
                "example:\n" +
                "\tholeFilling -i imageInCurrentFolder.png -mp C:\\some\\long\\path\\to\\mask\\maskImage.png -k 4-connected -z 10 -ep 0.000001\n");
    }
}