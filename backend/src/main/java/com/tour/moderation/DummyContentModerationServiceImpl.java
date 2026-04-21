package com.tour.moderation;

import org.springframework.stereotype.Service;

@Service
public class DummyContentModerationServiceImpl implements ContentModerationService {

    @Override
    public boolean isContentAllowed(String content) {
        return true;
    }

    @Override
    public boolean isImageAllowed(String imageUrl) {
        return true;
    }
}
