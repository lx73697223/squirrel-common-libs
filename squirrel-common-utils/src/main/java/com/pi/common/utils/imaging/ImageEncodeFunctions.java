package com.pi.common.utils.imaging;

import com.pi.common.utils.exceptions.PiRuntimeException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;

public class ImageEncodeFunctions {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageEncodeFunctions.class);

    private ImageEncodeFunctions() {
    }

    /**
     The {@link #getEncodedData()} will be created with the specified targetMimeType, with the default quality parameters in the
     JDK.
     <p>
     For better quality with JPG, see {@link #createForJpg(float)}
     @param targetMimeType see {@link ImageConstants.MimeTypes}
     */
    public static Function<BufferedImage, EncodedImageData> createForMimeType(String targetMimeType) {
        checkArgument(ImageConstants.MimeTypes.ALL.contains(targetMimeType),
                "precondition: ImageConstants.MimeTypes.ALL.contains(targetMimeType)");

        return (decodedImage) -> {
            return encodeWithDefaultSettings(decodedImage, targetMimeType);
        };
    }

    public static Function<BufferedImage, EncodedImageData> createForJpg(float imageQuality) {
        return (decodedImage) -> {
            return encodeJpg(decodedImage, imageQuality);
        };
    }

    private static EncodedImageData encodeWithDefaultSettings(BufferedImage decodedImage, String targetMimeType) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            String javaApiImageEncoderName = getJavaApiNameForEncodedDataMimeType(targetMimeType);

            ImageIO.write(decodedImage, javaApiImageEncoderName, outputStream);
            outputStream.flush();

            byte[] encoded = outputStream.toByteArray();
            EncodedImageData encodedImage = new ByteArrayImageData(encoded, targetMimeType);
            return encodedImage;

        } catch (Throwable th) {
            LOGGER.error("Unexpected exception when writing image data into byte array.  message:{}, stackTrace:{}",
                    ExceptionUtils.getMessage(th), ExceptionUtils.getStackTrace(th));
            throw new PiRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     Encode the specified image into jpg with the given imageQuality
     @param decodedImage
     @param imageQuality in the range (0, 1.0)
     */
    private static EncodedImageData encodeJpg(BufferedImage decodedImage, float imageQuality) {
        checkArgument(decodedImage != null, "precondition: decodedImage != null");

        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");
        ImageWriter writer = iter.next();
        ImageWriteParam writeParam = writer.getDefaultWriteParam();
        writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        writeParam.setCompressionQuality(imageQuality);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        writer.setOutput(new MemoryCacheImageOutputStream(outputStream));

        try {
            writer.write(null, new IIOImage(decodedImage, null, null), writeParam);
            outputStream.flush();

        } catch (IOException e) {
            LOGGER.error("Exception when encoding image into JPEG! imageQuality:{}, exception message:{}, stackTrace:{}",
                    imageQuality, ExceptionUtils.getMessage(e), ExceptionUtils.getStackTrace(e));
            throw new PiRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            writer.dispose();
        }

        byte[] encodedImageBytes = outputStream.toByteArray();
        ByteArrayImageData encodedImage = new ByteArrayImageData(encodedImageBytes, ImageConstants.MimeTypes.JPEG);
        return encodedImage;
    }

    /**
     From https://stackoverflow.com/questions/18614452/mimetypes-for-imageio-read-and-write
     <p>
     The list of names accepted by the Java 6 API: BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif
     <p>
     The equivalent MIME types: image/bmp image/jpeg image/png image/gif
     */
    private static String getJavaApiNameForEncodedDataMimeType(String imageMimeType) {
        switch (imageMimeType) {
            case ImageConstants.MimeTypes.JPEG:
                return "jpg";
            case ImageConstants.MimeTypes.BMP:
                return "bmp";
            case ImageConstants.MimeTypes.PNG:
                return "png";
            case ImageConstants.MimeTypes.GIF:
                return "gif";
            default:
                throw new PiRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
