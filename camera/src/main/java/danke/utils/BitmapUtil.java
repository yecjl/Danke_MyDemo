package danke.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.widget.ImageView;

/**
 * 功能：图片工具类
 * Created by danke on 2017/2/7.
 */

public class BitmapUtil {

    /**
     * 从文件中加载bitmap
     *
     * @param url
     * @param matrix 对图像的操作
     * @return
     */
    public static Bitmap decodeFile(String url, Matrix matrix) {
        // 从文件中加载图片
        Bitmap bitmap = BitmapFactory.decodeFile(url);
        if (matrix != null) {
            // 根据 matrix 对图片进行操作
            Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            // 释放之前的图片资源
            bitmap.recycle();
            return newBitmap;
        } else {
            return bitmap;
        }
    }

    /**
     * 从文件中加载bitmap
     *
     * @param url
     * @return
     */
    public static Bitmap decodeFile(String url) {
        return decodeFile(url, null);
    }

    /**
     * 从像素点中加载bitmap
     * @param width
     * @param height
     * @param pixels
     * @return
     */
    public static Bitmap decodePixels(int width, int height, int[] pixels) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 根据bitmap 获取像素集合
     *
     * @param bitmap
     * @return
     */
    public static int[] getPixels(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        return pixels;
    }

    /**
     * 将文件中加载的bitmap，显示到ImageView
     * @param url
     * @param matrix
     * @param imageView
     */
    public static void decodeFileShow(String url, Matrix matrix, ImageView imageView) {
        Bitmap bitmap = decodeFile(url, matrix);
        show(bitmap, imageView);
    }

    /**
     * 将文件中加载的bitmap，显示到ImageView
     * @param url
     * @param imageView
     */
    public static void decodeFileShow(String url, ImageView imageView) {
        Bitmap bitmap = decodeFile(url);
        show(bitmap, imageView);
    }

    /**
     * 将bitmap显示到ImageView
     * @param bitmap
     * @param imageView
     */
    public static void show(Bitmap bitmap, ImageView imageView) {
        if (bitmap != null && imageView != null) {
            imageView.setImageBitmap(bitmap);
        }
    }


    // FIXME (zxing) 如果没有使用到二维码识别 请去掉这里的内容 begin-------------------------------------------

//    /**
//     * 解析BitMatrix到Bitmap中 二维码使用(zxing)
//     *
//     * @param matrix
//     * @return
//     */
//    public static Bitmap bitMatrix2Bitmap(BitMatrix matrix) {
//        int w = matrix.getWidth();
//        int h = matrix.getHeight();
//        int[] rawData = new int[w * h];
//        for (int i = 0; i < w; i++) {
//            for (int j = 0; j < h; j++) {
//                int color = Color.WHITE;
//                if (matrix.get(i, j)) {
//                    color = Color.BLACK; // 自定义其他颜色会导致解析不出来
//                }
//                rawData[i + (j * w)] = color;
//            }
//        }
//
//        return decodePixels(w, h, rawData);
//    }
//
//    /**
//     *  根据bitmap 解析二维码（zxing）
//     * @param bitmap
//     * @return
//     */
//    public static Result scanningImage(Bitmap bitmap) {
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
//        RGBLuminanceSource source = new RGBLuminanceSource(width, height, getPixels(bitmap));
//        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
//        Map<DecodeHintType, String> hints = new HashMap<>();
//        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
//        Result result = null;
//        try {
//            result = new MultiFormatReader().decode(binaryBitmap, hints);
//        } catch (NotFoundException e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }
//
//    /**
//     * 根据图片路径 解析二维码（zxing）
//     * @param path
//     * @return
//     */
//    public static Result scanningImage(String path) {
//        return scanningImage(BitmapUtil.decodeFile(path));
//    }

    // FIXME (zxing) 如果没有使用到二维码识别 请去掉这里的内容 end-------------------------------------------
}
