package com.pi.common.utils.imaging;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A pair of encoded and decoded data for the same image.
 */
public class ImageData {

    private final BufferedImage decodedImage;

    private EncodedImageData encodedImage;

    private Function<BufferedImage, EncodedImageData> encodeFunction;

    /**
     *
     * @param encodeFunction See {@link ImageEncodeFunctions}
     */
    public ImageData(BufferedImage decodedImage, Function<BufferedImage, EncodedImageData> encodeFunction) {
        checkArgument(decodedImage != null, "precondition: decodedImage != null");
        checkArgument(encodeFunction != null, "precondition: encodeFunction != null");

        this.decodedImage = decodedImage;
        this.encodeFunction = encodeFunction;
    }

    public ImageData(EncodedImageData encodedImage, BufferedImage decodedImage) {
        checkArgument(encodedImage != null, "precondition: encodedImage != null");
        checkArgument(decodedImage != null, "precondition: decodedImage != null");

        this.decodedImage = decodedImage;
        this.encodedImage = encodedImage;
    }

    /**
     * Constructs an {@link ImageData} that keeps the specified encodedImageData for {@link #getEncodedData()}.
     */
    public static ImageData decode(byte[] encodedImageData) throws IOException {
        checkArgument(encodedImageData != null, "precondition: encodedImageData != null");

        ByteArrayImageData encodedImage = new ByteArrayImageData(encodedImageData);
        BufferedImage decodedImage = decodeImageAndCloseStream(encodedImage.createInputStream());

        return new ImageData(encodedImage, decodedImage);
    }

    public static ImageData decode(EncodedImageData encodedImage) throws IOException {
        checkArgument(encodedImage != null, "precondition: encodedImage != null");

        BufferedImage decodedImage = decodeImageAndCloseStream(encodedImage.createInputStream());

        return new ImageData(encodedImage, decodedImage);
    }

    /**
     * @return the width dimension of the image.
     */
    public int getWidth() {
        return getDecodedImage().getWidth();
    }

    /**
     * @return the height dimension of the image.
     */
    public int getHeight() {
        return getDecodedImage().getHeight();
    }

    /**
     * @return the image data encoded in a particular format, e.g. jpeg.
     */
    public EncodedImageData getEncodedData() {
        if (encodedImage == null) {
            encodedImage = encodeFunction.apply(decodedImage);
        }

        return encodedImage;
    }

    /**
     * Gets the decoded image data.  Note that caller code should not modify the returned
     * {@link BufferedImage}, and the effect on other properties in this object is undefined.
     *
     * This property is exposed in order to avoid having to decode a given piece of image data more than once...
     */
    public BufferedImage getDecodedImage() {
        return decodedImage;
    }

    private static BufferedImage decodeImageAndCloseStream(InputStream inputStream) throws IOException {
        BufferedImage decodedImage = ImageUtils.decodeImageAndCloseStream(inputStream);

        if (decodedImage != null) {
            return decodedImage;
        } else {
            throw new IOException("No registered ImageReader claims to be able to decode the image in the specified stream.");
        }
    }

    @Override
    public String toString() {
        return "ImageData [decodedImage="
                + (decodedImage != null ? decodedImage.getWidth() + "x" + decodedImage.getHeight() : "null") + ", encodedImage="
                + (encodedImage != null ? encodedImage.getLoggingInfo() : "null") + ", encodeFunction=" + encodeFunction + "]";
    }

}
