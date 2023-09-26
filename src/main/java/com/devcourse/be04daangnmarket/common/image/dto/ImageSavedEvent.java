package com.devcourse.be04daangnmarket.common.image.dto;

public class ImageSavedEvent {
    private String fileName;

    public ImageSavedEvent(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
