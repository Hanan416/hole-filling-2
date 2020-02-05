package com.hanan.application;

import com.hanan.config.AppConfig;
import com.hanan.utilities.ImageUtil;
import com.hanan.utilities.implementations.DefaultHoleFillerUtil;
import com.hanan.utilities.implementations.DefaultWeightFunctions;
import com.hanan.utilities.interfaces.IHoleFillerUtil;
import com.hanan.utilities.interfaces.IWeightFunctionsUtil;
import org.apache.commons.lang3.StringUtils;
import org.opencv.core.Core;
import org.opencv.core.Mat;

public class App {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static String sourceImage = null;
    private static String maskImage = null;
    private static String connectivityType = null;
    private static String epsilonValueStr = null;
    private static String zValueStr = null;

    public static void main(String[] args) {

        readArgs(args);
        validateArguments();

        ImageUtil imageUtil = new ImageUtil(sourceImage, maskImage, connectivityType);
        Mat holedImage = imageUtil.applyMask();

        float zValue = getFloatValue(zValueStr, "zValue");
        float epsilonValue = getFloatValue(epsilonValueStr, "epsilonValue");
        IWeightFunctionsUtil defaultWeightFunction = new DefaultWeightFunctions(zValue, epsilonValue);

        IHoleFillerUtil defaultHoleFiller = new DefaultHoleFillerUtil();
        defaultHoleFiller.fillHole(holedImage, imageUtil.getBoundaryPixelDTOs(), imageUtil.getHolePixelDTOs(), defaultWeightFunction);

        imageUtil.writeResult();

    }

    private static void readArgs(String[] inputArg) {
        if (inputArg.length > 0 && inputArg[0].equals("-h")) {
            usagePrint();
        } else {
            for (int i = 0; i < inputArg.length; i++) {
                if ((inputArg[i].equals("-i") || inputArg[i].equals("-ip")) && sourceImage == null)
                    sourceImage = inputArg[++i];
                if ((inputArg[i].equals("-m") || inputArg[i].equals("-mp")) && maskImage == null)
                    maskImage = inputArg[++i];
                if (inputArg[i].equals("-k") && connectivityType == null)
                    connectivityType = inputArg[++i];
                if (inputArg[i].equals("-z") && zValueStr == null)
                    zValueStr = inputArg[++i];
                if (inputArg[i].equals("-ep") && epsilonValueStr == null)
                    epsilonValueStr = inputArg[++i];
            }
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
        } /*else if (!checkDoubleParsing(epsilonValue)) {
            throw new RuntimeException("Invalid epsilonValue, expected double, got: " + epsilonValue);
        } else if (!checkDoubleParsing(zValue)) {
            throw new RuntimeException("Invalid zValue, expected double, got: " + zValue);
        }*/
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