package danke.camera;

import android.os.Environment;

import java.io.File;

/**
 * 功能：常量
 * Created by danke on 2017/2/7.
 */

public class Constant {

    // 保存默认路径
    public static final String BASE_SAVE_URL = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Danke" + File.separator;
    // 图片保存路径
    public static final String IMAGE_SAVE_URL = BASE_SAVE_URL + "image";

    /**
     * request code
     */
    public static final int REQUEST_CAMERA_1 = 1;
    public static final int REQUEST_CAMERA_2 = 2;
    public static final int REQUEST_CAMERA_3 = 3;



}
