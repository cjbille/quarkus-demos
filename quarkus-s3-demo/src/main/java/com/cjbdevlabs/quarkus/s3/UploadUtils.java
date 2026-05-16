package com.cjbdevlabs.quarkus.s3;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class UploadUtils {

    public static String buildFileName(String fileNameHeader) {
        var fileName = StringUtils.isNotBlank(fileNameHeader) ? fileNameHeader : UUID.randomUUID().toString();
        return fileName + ".tar";
    }
}
