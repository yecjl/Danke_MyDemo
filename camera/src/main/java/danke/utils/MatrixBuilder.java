package danke.utils;

import android.graphics.Matrix;

/**
 * 功能：图片样式处理工具类
 * 设计模式：采用构建者设计模式
 * Created by danke on 2017/2/8.
 */

public class MatrixBuilder {

    private Matrix mMatrix = null;

    public MatrixBuilder() {
        mMatrix = new Matrix();
    }

    /**
     * 旋转90操作
     *
     * @return
     */
    public MatrixBuilder setRotate90() {
        mMatrix.setRotate(90); // 旋转90度
        return this;
    }

    /**
     * 返回Matrix
     *
     * @return
     */
    public Matrix build() {
        return mMatrix;
    }
}
