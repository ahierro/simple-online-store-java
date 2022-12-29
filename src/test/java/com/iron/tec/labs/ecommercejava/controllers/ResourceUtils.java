package com.iron.tec.labs.ecommercejava.controllers;

import org.springframework.core.io.Resource;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

public class ResourceUtils {
    public static String readFile(Resource resource) {
        try {
            return FileUtils.readFileToString(resource.getFile(), "UTF-8");
        } catch (Exception e) {
            return "";
        }
    }
}
