package com.pi.common.utils.imaging;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Note: If we need to do more complicated image manipulation, consider using this library:
 * https://github.com/rkalla/imgscalr
 * http://mvnrepository.com/artifact/org.imgscalr/imgscalr-lib
 */
public class ImageUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageUtils.class);

    /**
     * If no registered <code>ImageReader</code> claims to be able to read the resulting stream, <code>null</code> is returned
     * @throws IOException
     */
    public static BufferedImage decodeImageAndCloseStream(InputStream encodedImageStream) throws IOException {

        try (InputStream inputStream = encodedImageStream) {

            return ImageIO.read(inputStream);

        } catch (IOException ex) {
            LOGGER.error("Exception when decoding image data from byte array. exception message:{}, stackTrace:{}",
                    ExceptionUtils.getMessage(ex), ExceptionUtils.getStackTrace(ex));
            throw ex;
        }
    }

    public static ImageData cropToCenter(ImageData source, int desiredWidth, int desiredHeight,
            Function<BufferedImage, EncodedImageData> imageEncodeFunc) {
        checkArgument(source != null, "precondition: source != null");
        checkArgument(desiredWidth >= 0, "precondition: desiredWidth >= 0");
        checkArgument(desiredHeight >= 0, "precondition: desiredHeight >= 0");

        int cropX = Math.max(0, (source.getWidth() - desiredWidth) / 2);
        int cropY = Math.max(0, (source.getHeight() - desiredHeight) / 2);
        int cropWidth = Math.min(source.getWidth() - cropX, desiredWidth);
        int cropHeight = Math.min(source.getHeight() - cropY, desiredHeight);

        if (cropX == 0 && cropY == 0 && cropWidth == source.getWidth() && cropHeight == source.getHeight()) {
            return source;
        } else {
            BufferedImage original = source.getDecodedImage();
            BufferedImage cropped = original.getSubimage(cropX, cropY, cropWidth, cropHeight);
            return new ImageData(cropped, imageEncodeFunc);
        }
    }

    /**
     * If scale-down is required, the returned image data will be encoded in JPEG using default settings in the JDK.
     *
     * @return never returns null
     * @throws IOException
     */
    public static ImageData scaleDownKeepingAspectRatio(byte[] source, int widthMax, int heightMax) throws IOException {
        checkArgument(source != null, "precondition: source != null");

        ImageData imageData = ImageData.decode(source);
        return scaleDownKeepingAspectRatio(imageData, widthMax, heightMax,
                ImageEncodeFunctions.createForMimeType(ImageConstants.MimeTypes.JPEG), ImageScaleDownMode.BOUND_BY_LARGER_SIDE);
    }

    /**
     * If scale-down is required, the returned image data will be encoded in JPEG.
     *
     * @param imageEncodeFunc see {@link ImageEncodeFunctions}
     * @return never returns null
     * @throws IOException
     */
    public static ImageData scaleDownKeepingAspectRatio(ImageData source, int widthMax, int heightMax,
            Function<BufferedImage, EncodedImageData> imageEncodeFunc, ImageScaleDownMode scaleDownMode) throws IOException {
        Preconditions.checkArgument(source != null, "precondition: source != null");
        Preconditions.checkArgument(imageEncodeFunc != null, "precondition: imageEncodeFunc != null");

        boolean isDownSizingNeeded = source.getWidth() > widthMax || source.getHeight() > heightMax;
        if (isDownSizingNeeded) {
            BufferedImage decodedSourceImage = source.getDecodedImage();
            BufferedImage scaledDownImage = scaleDownKeepingAspectRatio(decodedSourceImage, widthMax, heightMax, scaleDownMode);
            return new ImageData(scaledDownImage, imageEncodeFunc);

        } else {
            return source;
        }
    }

    /**
     * If the specified source image has a larger dimension than maxWidth or maxHeight,
     * returns a downscaled copy while keeping the image's aspect ratio.
     *
     * Else, simply returns the specified source.
     */
    public static BufferedImage scaleDownKeepingAspectRatio(BufferedImage source, int widthMax, int heightMax,
            ImageScaleDownMode scaleDownMode) {
        checkArgument(source != null, "precondition: source != null");

        double scaleDownFactor = calculateScaleDownFactor(source.getWidth(), source.getHeight(), widthMax, heightMax,
                scaleDownMode);
        if (scaleDownFactor < 1.0) { // if downsizing is needed
            return getScaledInstance(source, scaleDownFactor);
        } else {
            return source;
        }
    }

    /**
     * Calculates the scale-down factor, in the range of [0.0, 1.0), for scaling down imageWidth and imageHeight to
     * fit within widthMax and heightMax while keeping the image's aspect ratio.
     */
    private static double calculateScaleDownFactor(int imageWidth, int imageHeight, int widthMax, int heightMax,
            ImageScaleDownMode scaleDownMode) {
        checkArgument(scaleDownMode != null, "precondition: scaleDownMode != null");

        if (imageWidth == 0 || imageHeight == 0) {
            return 1.0;
        }

        // To keep the existing aspect ratio of the image, determine the necessary scaling factor
        // on each width and height, select the more constraining scale factor, and apply it on both dimensions.
        double widthScaleFactor = (widthMax > 0) ? (((double) widthMax) / imageWidth) : 1.0;
        double heightScaleFactor = (heightMax > 0) ? (((double) heightMax) / imageHeight) : 1.0;

        double targetScaleFactor;
        switch (scaleDownMode) {
            case BOUND_BY_LARGER_SIDE:
                targetScaleFactor = Math.min(widthScaleFactor, heightScaleFactor);
                break;
            case BOUND_BY_SMALLER_SIDE:
                targetScaleFactor = Math.max(widthScaleFactor, heightScaleFactor);
                break;
            default:
                throw new IllegalArgumentException("Unhandled ImageScaleDownMode: " + scaleDownMode);
        }

        return targetScaleFactor;
    }

    private static BufferedImage getScaledInstance(BufferedImage source, double scaleFactor) {
        int imageWidth = source.getWidth();
        int imageHeight = source.getHeight();

        int targetWidth = (int) Math.floor(imageWidth * scaleFactor);
        int targetHeight = (int) Math.floor(imageHeight * scaleFactor);

        LOGGER.info("Going to scale-down the image from width:{}Xheight:{} to width:{}Xheight:{}", imageWidth, imageHeight,
                targetWidth, targetHeight);

        return getScaledInstance(source, targetWidth, targetHeight, RenderingHints.VALUE_INTERPOLATION_BILINEAR,
                /* higherQuality */ true);
    }

    /**
     * Convenience method that returns a scaled instance of the
     * provided {@code BufferedImage}.
     *
     * Uses {@link BufferedImage} instead of {@link Image#getScaledInstance(int, int, int)}
     * for much better speed performance.
     *
     * @implNote https://community.oracle.com/docs/DOC-983611
     * "For years, the Java 2D team has been encouraging developers to move away from JDK-1.0-isms
     * like Image.getScaledInstance() and onto more modern APIs.
     * ...
     * Image.getScaledInstance() isn't the fastest route; nor does it necessarily offer the best quality.
     * ...
     * UsingImage.getScaledInstance(SCALE_AREA_AVERAGING) does produce the nice "filtered" results
     * that many developers have grown to expect, but it incurs a severe performance penalty, up
     * to 50 times slower than a one-step BILINEAR operation. Finally, the last box shows the
     * multi-step BILINEAR technique advocated earlier in this article. Notice that the visual
     * results are on par with those produced bySCALE_AREA_AVERAGING, but at a fraction of the
     * performance cost."
     *
     * @implNote https://community.oracle.com/docs/DOC-98361 also related to the multi-step downscaling algorithm:
     * "... When downscaling an image, the choice is slightly more complex.
     * The same advice regarding RenderingHints given for upscaling is generally
     * applicable to downscaling as well. However, be aware that if you try to
     * downscale an image by a factor of more than two (i.e., the scaled instance
     * is less than half the size of the original), and you are using the BILINEAR
     * or BICUBIC hint, the quality of the scaled instance may not be as smooth as you
     * might like. If you are familiar with the quality of the old Image.SCALE_AREA_AVERAGING
     * (orImage.SCALE_SMOOTH) hint, then you may be especially dismayed. The reason for this
     * disparity in quality is due to the different filtering algorithms in use. If downscaling
     * by more than two times, the BILINEAR and BICUBIC algorithms tend to lose information due
     * to the way pixels are sampled from the source image; the older AreaAveragingFilter algorithm
     * used by Image.getScaledInstance() is quite different and does not suffer from this problem as
     * much, but it requires much more processing time in general.
     *
     * To combat this issue, you can use a multi-step approach when downscaling by more than two times;
     * this helps prevent the information loss issue and produces a much higher quality result that is
     * visually quite close to that produced byImage.SCALE_AREA_AVERAGING. Despite the fact that there
     * may be multiple temporary images created, and multiple calls made to Graphics.drawImage() in the
     * process, this approach can be significantly faster than using the older,
     * slowerImage.getScaledInstance() method. The basic idea here is to repeatedly scale the image by
     * half (usingBILINEAR filtering), and then, once the target size is near, perform one final scaling
     * step to reach the target dimensions."
     *
     * @param img the original image to be scaled
     * @param targetWidth the desired width of the scaled instance, in pixels
     * @param targetHeight the desired height of the scaled instance, in pixels
     * @param hint one of the rendering hints that corresponds to
     * {@code RenderingHints.KEY_INTERPOLATION} (e.g.
     * {@code RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR},
     * {@code RenderingHints.VALUE_INTERPOLATION_BILINEAR},
     * {@code RenderingHints.VALUE_INTERPOLATION_BICUBIC})
     * @param higherQuality if true, this method will use a multi-step
     * scaling technique that provides higher quality than the usual
     * one-step technique (only useful in downscaling cases, where
     * {@code targetWidth} or {@code targetHeight} is
     * smaller than the original dimensions, and generally only when
     * the {@code BILINEAR} hint is specified)
     *
     * @return a scaled version of the original {@code BufferedImage}
     */
    public static BufferedImage getScaledInstance(BufferedImage img, int targetWidth, int targetHeight, Object hint,
            boolean higherQuality) {

        int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = img;
        int w, h;
        if (higherQuality) {
            // Use multi-step technique: start with original size, then
            // scale down in multiple passes with drawImage()
            // until the target size is reached
            w = img.getWidth();
            h = img.getHeight();
        } else {
            // Use one-step technique: scale directly from original
            // size to target size with a single drawImage() call
            w = targetWidth;
            h = targetHeight;
        }

        do {
            if (higherQuality && w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }
            if (higherQuality && h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }

            BufferedImage tmp = new BufferedImage(w, h, type);
            Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose();
            ret = tmp;
        } while (w != targetWidth || h != targetHeight);

        return ret;
    }
}
