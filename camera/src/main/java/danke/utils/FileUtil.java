package danke.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import danke.camera.Constant;

/**
 * 功能：文件操作的工具类
 * Created by danke on 2017/2/7.
 */

public class FileUtil {

    /**
     * 保存文件
     *
     * @param dirName 文件路径
     * @param data    保存数据
     */
    public static String saveFile(String dirName, byte[] data) {
        FileOutputStream fos = null;
        String url = null;
        try {
            File file = getFile(dirName, TimeUtil.getFileNameNomal());
            fos = new FileOutputStream(file);
            fos.write(data);
            url = file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            url = "";
        } catch (IOException e) {
            e.printStackTrace();
            url = "";
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return url;
    }

    /**
     * 保存图片
     *
     * @param data
     */
    public static String saveFileImage(byte[] data) {
        return saveFile(Constant.IMAGE_SAVE_URL, data);
    }

    /**
     * 根据文件路径和文件名，获取对应的文件对象
     *
     * @param dirName
     * @param fileName
     * @return
     */
    public static File getFile(String dirName, String fileName) {
        File dir = new File(dirName);

        // 如果文件不存在，则创建
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return new File(dir, fileName);
    }
}
