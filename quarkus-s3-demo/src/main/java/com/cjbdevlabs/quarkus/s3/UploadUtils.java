package com.cjbdevlabs.quarkus.s3;

import java.util.UUID;

public class UploadUtils {

    public static String buildFileName(String fileNameHeader) {
        var fileName = isNotBlank(fileNameHeader) ? fileNameHeader : UUID.randomUUID().toString();
        return fileName + ".tar.gz";
    }

    public static boolean isNotBlank(String s) {
        return s != null && !s.isBlank();
    }
}
