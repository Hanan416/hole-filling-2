package com.hanan.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class LoadLibrary {
    public static void loadOpenCV() {
        try {
            InputStream inputStream = null;
            File fileOut = null;
            String osName = System.getProperty("os.name");

            if (osName.startsWith("Windows")) {
                int bitness = Integer.parseInt(System.getProperty("sun.arch.data.model"));
                if (bitness == 32) {
                    inputStream = LoadLibrary.class.getResourceAsStream("/opencv/opencv_java300.dll");
                    fileOut = File.createTempFile("lib", ".dll");
                } else if (bitness == 64) {
                    inputStream = LoadLibrary.class.getResourceAsStream("/opencv/opencv_java300.dll");
                    fileOut = File.createTempFile("lib", ".dll");
                } else {
                    inputStream = LoadLibrary.class.getResourceAsStream("/opencv/opencv_java300.dll");
                    fileOut = File.createTempFile("lib", ".dll");
                }
            }

            if (fileOut != null) {
                OutputStream outputStream = new FileOutputStream(fileOut);
                byte[] buffer = new byte[1024];
                int length;

                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                inputStream.close();
                outputStream.close();
                System.load(fileOut.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}