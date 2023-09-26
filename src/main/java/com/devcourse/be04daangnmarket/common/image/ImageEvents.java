package com.devcourse.be04daangnmarket.common.image;

import org.springframework.context.ApplicationEventPublisher;

public class ImageEvents {
    private static ApplicationEventPublisher publisher;

    public static void setPublisher(ApplicationEventPublisher publisher) {
        ImageEvents.publisher = publisher;
    }

    public static void raise(Object event) {
        if (publisher != null) {
            publisher.publishEvent(event);
        }
    }
}
