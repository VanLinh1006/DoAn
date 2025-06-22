package com.prepify.be.utils;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class FileUtils {
    private static final List<String> IMAGE_CONTENT_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/bmp", "image/webp", "image/svg+xml"
    );

    private static final List<String> AUDIO_CONTENT_TYPES = Arrays.asList(
            "audio/mpeg", "audio/wav", "audio/ogg"
    );

    public static boolean isImageFile(MultipartFile file) {
        return file.getContentType() != null && IMAGE_CONTENT_TYPES.contains(file.getContentType());
    }

    public static boolean isAudioFile(MultipartFile file) {
        return file.getContentType() != null && AUDIO_CONTENT_TYPES.contains(file.getContentType());
    }
}

