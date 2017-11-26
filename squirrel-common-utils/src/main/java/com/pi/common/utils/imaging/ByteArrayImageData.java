package com.pi.common.utils.imaging;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * An in-memory implementation of the {@link EncodedImageData} interface.
 *
 * See comments on {@link ImageData} for how this class is different than {@link BufferedImage}.
 */
public class ByteArrayImageData implements EncodedImageData {

    private final String mimeType;

    private final byte[] data;

    public ByteArrayImageData(byte[] data) throws IOException {
        checkArgument(data != null, "precondition: data != null");

        this.data = data;
        try (ByteArrayInputStream encodedImageDataStream = new ByteArrayInputStream(data)) {

            this.mimeType = URLConnection.guessContentTypeFromStream(encodedImageDataStream);
        }
    }

    public ByteArrayImageData(byte[] data, String mimeType) {
        checkArgument(data != null, "precondition: data != null");
        checkArgument(ImageConstants.MimeTypes.ALL.contains(mimeType),
                "precondition: ImageConstants.MimeTypes.ALL.contains(mimeType)");

        this.data = data;
        this.mimeType = mimeType;
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }

    @Override
    public InputStream createInputStream() {
        return new ByteArrayInputStream(data);
    }

    @Override
    public byte[] getBytes() {
        return data;
    }

    @Override
    public int getByteSize() {
        return data.length;
    }

}
