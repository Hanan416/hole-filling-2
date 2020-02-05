package com.hanan.config;

import java.util.Arrays;
import java.util.List;

public class AppConfig {

    public static final String FOUR_CONNECTED = "4-connected";
    public static final String EIGHT_CONNECTED = "8-connected";
    public static final List<String> CONNECTIVITY_TYPES = Arrays.asList(FOUR_CONNECTED, EIGHT_CONNECTED);
    public static final double HOLE_THRESHOLD = 0.05f;

    public static final String IMAGE_SIZE_MISMATCH_ERROR = "source image and mask image mismatch size, expected to be similar";


}
