package com.pai8.ke.qrcode;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.util.Hashtable;

/**
 * @Description: QR 扫描工具类
 */
public class QRCodeTools {
    private static final int IMAGE_HALFWIDTH = 40;
    private static final int QR_IMAGE_WIDTH = 600;
    public final static String QR_HEAD = "https://bill.s1.natapp.cc/givinggift/userCrowd/twoDimensionaCodes/";

    /**
     * Check if this device has a camera
     */
    public static boolean checkCameraHardware(Context context) {
        if (android.os.Build.MODEL.equals("vivo")) { //不检查vivo手机是否支持camera
            return true;
        }
        if (context == null) {
            return false;
        }
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FRONT)) {
            // this device has a front camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * 扫描内置图片二维码
     *
     * @param bmp
     * @param listener
     * @return
     */
    public static String qrReaderBitmap(Bitmap bmp, QRCodeReaderView.OnQRCodeReadListener listener) {
        String res = null;
        if (bmp != null) {
            /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
			byte[] bmpSource = baos.toByteArray();*/
            int width = bmp.getWidth();
            int height = bmp.getHeight();
            int[] bmpSource = new int[width * height];
            bmp.getPixels(bmpSource, 0, width, 0, 0, width, height);
			/*PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(
					bmpSource, width, height, 0, 0, width, height, false);*/
            LuminanceSource source = new RGBLuminanceSource(width, height, bmpSource);
            HybridBinarizer hybBin = new HybridBinarizer(source);
            BinaryBitmap bitmap = new BinaryBitmap(hybBin);
            QRCodeReader mQRCodeReader = new QRCodeReader();
            try {
                Result result = mQRCodeReader.decode(bitmap);
                res = result.getText();
                if (listener != null /*&& result != null */ && result.getText() != null) {
                    listener.onQRCodeRead(result.getText(), null);
                }
            } catch (ChecksumException e) {
                e.printStackTrace();
            } catch (NotFoundException e) {
                // Notify QR not found
                if (listener != null) {
                    listener.QRCodeNotFoundOnCamImage();
                }
            } catch (com.google.zxing.FormatException e) {
                e.printStackTrace();
            } finally {
                mQRCodeReader.reset();
                mQRCodeReader = null;
                bmpSource = null;
            }
        }
        return res;
    }

    public static Bitmap decodeFile(final String path) {
        Bitmap bmp = null;
        Options opts = new Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);
        int scale = 1;
        if (opts.outWidth > 1080) {
            scale = opts.outWidth / 1080;
        }
        if (opts.outHeight > 1080) {
            scale = opts.outHeight / 1080 > scale ? opts.outHeight / 1080 : scale;
        }
        opts.inJustDecodeBounds = false;
        opts.inSampleSize = scale;
        opts.inPreferredConfig = Config.RGB_565;
        try {
            bmp = BitmapFactory.decodeFile(path, opts);
        } catch (OutOfMemoryError e) {
        }
        return bmp;
    }

    /**
     * 生成二维码
     *
     * @param string  二维码中包含的文本信息
     * @param mBitmap logo图片
     *                [url=home.php?mod=space&uid=309376]@return[/url] Bitmap 位图
     */
    public static Bitmap createCode(String string, Bitmap mBitmap) throws WriterException {
        Matrix m = new Matrix();
        float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
        float sy = (float) 2 * IMAGE_HALFWIDTH
                / mBitmap.getHeight();
        m.setScale(sx, sy);//设置缩放信息
        //将logo图片按martix设置的信息缩放
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0,
                mBitmap.getWidth(), mBitmap.getHeight(), m, false);
        MultiFormatWriter writer = new MultiFormatWriter();
        Hashtable<EncodeHintType, String> hst = new Hashtable<EncodeHintType, String>();
        hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");//设置字符编码
        BitMatrix matrix = writer.encode(string, BarcodeFormat.QR_CODE, QR_IMAGE_WIDTH, QR_IMAGE_WIDTH, hst);//生成二维码矩阵信息
        int width = matrix.getWidth();//矩阵高度
        int height = matrix.getHeight();//矩阵宽度
        int halfW = width / 2;
        int halfH = height / 2;
        int[] pixels = new int[width * height];//定义数组长度为矩阵高度*矩阵宽度，用于记录矩阵中像素信息
        for (int y = 0; y < height; y++) {//从行开始迭代矩阵
            for (int x = 0; x < width; x++) {//迭代列
                if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
                        && y > halfH - IMAGE_HALFWIDTH
                        && y < halfH + IMAGE_HALFWIDTH) {//该位置用于存放图片信息
//记录图片每个像素信息
                    pixels[y * width + x] = mBitmap.getPixel(x - halfW
                            + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
                } else {
                    if (matrix.get(x, y)) {//如果有黑块点，记录信息
                        pixels[y * width + x] = 0xff000000;//记录黑块信息
                    }
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Config.ARGB_8888);
        // 通过像素数组生成bitmap
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }


    /**
     * 创建个人二维码
     *
     * @param userId
     * @return
     */
    public static Bitmap createCodeForUser(String userId, int size) {
        /*规则修改，不要这个前缀 */
        String value = /*QRCodeTools.QR_HEAD + */"user?" + "userId=" + userId;
        return createCode(value, size, false);
    }

    /**
     * 生成二维码
     *
     * @param string      二维码中包含的文本信息
     * @param deleteWhite
     */
    public static Bitmap createCode(String string, int size, boolean deleteWhite) {
        try {
            if (TextUtils.isEmpty(string) || size < 0) {
                return null;
            }
            int width = QR_IMAGE_WIDTH;//矩阵高度
            int height = QR_IMAGE_WIDTH;//矩阵宽度
            if (size > 0) {
                width = size;
                height = size;
            }
            MultiFormatWriter writer = new MultiFormatWriter();
            Hashtable<EncodeHintType, String> hst = new Hashtable<EncodeHintType, String>();
            hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");//设置字符编码
            BitMatrix matrix = writer.encode(string, BarcodeFormat.QR_CODE, width, height, hst);//生成二维码矩阵信息

            if (deleteWhite) {
                matrix = deleteWhite(matrix);
                width = matrix.getWidth();
                height = matrix.getHeight();
            }
            int[] pixels = new int[width * height];//定义数组长度为矩阵高度*矩阵宽度，用于记录矩阵中像素信息
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
            // 通过像素数组生成bitmap
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }
}