package com.devcourse.be04daangnmarket.common.image;

import com.devcourse.be04daangnmarket.common.image.dto.ImageSavedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class ImageSavedEventHandler {
    private final ImageIOService imageIOService;

    public ImageSavedEventHandler(ImageIOService imageIOService) {
        this.imageIOService = imageIOService;
    }

    @Async
    @TransactionalEventListener(
            classes = ImageSavedEvent.class,
            phase = TransactionPhase.AFTER_ROLLBACK
    )
    public void deleteHandle(ImageSavedEvent event) {
        imageIOService.delete(event.getFileName());
    }
}
