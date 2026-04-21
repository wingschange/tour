package com.tour.moderation;

public interface ContentModerationService {

    /**
     * Check whether the given text content is allowed.
     */
    boolean isContentAllowed(String content);

    /**
     * Check whether the image at the given URL is allowed.
     */
    boolean isImageAllowed(String imageUrl);
}
