package com.pi.common.utils.qrcode;

import com.google.common.base.Charsets;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class QRCodeUtils {

    // 默认二维码宽度
    public static final int WIDTH = 200;

    // 默认二维码高度
    public static final int HEIGHT = 200;

    // 默认二维码文件格式
    public static final String FORMAT = "png";

    // 二维码参数
    private static final Map<EncodeHintType, Object> HINTS = new HashMap<>();

    static {
        HINTS.put(EncodeHintType.CHARACTER_SET, Charsets.UTF_8.name());
        // 容错等级 L、M、Q、H 其中 L 为最低, H 为最高
        HINTS.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 二维码与图片边距
        HINTS.put(EncodeHintType.MARGIN, 1);
    }

    public static BufferedImage generateBufferedImage(String content, int width, int height) {
        try {
            return MatrixToImageWriter.toBufferedImage(encodeContent(content, width, height));
        } catch (WriterException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 将二维码图片输出到流中
     * @param content 二维码内容
     * @param outputstream  输出流
     * @param width   宽
     * @param height  高
     */
    public static void writeToStream(String content, OutputStream outputstream, int width, int height) {
        try {
            MatrixToImageWriter.writeToStream(encodeContent(content, width, height), FORMAT, outputstream);
        } catch (WriterException | IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * @param content 二维码内容
     * @param path    文件保存路径
     * @param width   宽
     * @param height  高
     */
    public static void createQRCodeImage(String content, String path, int width, int height) {
        try {
            MatrixToImageWriter.writeToPath(encodeContent(content, width, height), FORMAT, new File(path).toPath());
        } catch (IOException | WriterException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static BitMatrix encodeContent(String content, int width, int height) throws WriterException {
        return new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, HINTS);
    }

}
