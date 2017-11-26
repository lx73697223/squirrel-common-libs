package com.pi.common.utils.imaging;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Set;

public class ImageConstants {

    private ImageConstants() {
    }

    public static class MimeTypes {

        public static final String JPEG = "image/jpeg";

        public static final String BMP = "image/bmp";

        public static final String PNG = "image/png";

        public static final String GIF = "image/gif";

        public static final Set<String> ALL = Collections
                .unmodifiableSet(Sets.newConcurrentHashSet(Lists.newArrayList(JPEG, BMP, PNG, GIF)));
    }
}
