package com.pi.common.utils.imaging;

import java.io.InputStream;

/**
 * Represents some image data in a specific encoding (e.g. image/jpeg, image/png).
 * In contrast, {@link java.awt.image.BufferedImage} is the decoded image, hence is not specific to any encoding format information.
 */
public interface EncodedImageData {

    /**
     * @return the mime type in which the image data is encoded in.
     */
    public String getMimeType();

    /**
     * @return the image data encoded in the format indicated by {@link #getMimeType()}.
     */
    public InputStream createInputStream();

    /**
     * Caller code should not modify the content of this byte array!
     *
     * @return the image data encoded in the format indicated by {@link #getMimeType()}.
     */
    public byte[] getBytes();

    /**
     * @return the byte length of the data in {@link #createInputStream()}.
     */
    public int getByteSize();

    default public String getLoggingInfo() {
        return this.getClass().getSimpleName() + " [mimeType=" + getMimeType() + ", byteSize=" + getByteSize() + "]";
    }
}
