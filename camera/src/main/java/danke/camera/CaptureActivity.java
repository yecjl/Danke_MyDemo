package danke.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import danke.utils.FileUtil;

/**
 * 功能：照相机显示界面
 * Created by danke on 2017/2/7.
 */

public class CaptureActivity extends Activity implements SurfaceHolder.Callback {
    @Bind(R.id.bt_capture)
    Button btCapture;
    @Bind(R.id.surfaceView)
    SurfaceView surfaceView;

    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        ButterKnife.bind(this);

        mSurfaceHolder = surfaceView.getHolder();
        mSurfaceHolder.addCallback(this);

        btCapture.setOnClickListener(onClickListener);
        surfaceView.setOnClickListener(onClickListener);
    }

    /**
     * 自动对焦结果回调：拍照完成的回调
     */
    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        /**
         * @param data 完整的图像拍摄的数据，不是缩略图
         * @param camera
         */
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // 将拍完的照片保存到指定目录中
            String url = FileUtil.saveFileImage(data);
            Intent intent = getIntent();
            intent.putExtra("url", url);
            setResult(RESULT_OK, intent);
            finish();
        }
    };

    View.OnClickListener onClickListener =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_capture: // 拍照
                    Camera.Parameters parameters = mCamera.getParameters();
                    // 设置拍照格式
                    parameters.setPictureFormat(ImageFormat.JPEG);
                    // 设置预览大小
                    parameters.setPreviewSize(800, 600);
                    // 设置自动对焦(相机需要支持自动对焦)
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                    // 当对焦完成，拍照
                    mCamera.autoFocus(new Camera.AutoFocusCallback() {
                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {
                            if (success) {
                                // 拍照
                                mCamera.takePicture(null, null, pictureCallback);
                            }
                        }
                    });
                    break;

                case R.id.surfaceView: // 点击surfaceView自动对焦功能:
                    mCamera.autoFocus(null); // 不需要对自动对焦的结果进行回调
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null) {
            mCamera = getCamera();

            if (mCamera != null && mSurfaceHolder != null) {
                startPreview(mCamera, mSurfaceHolder);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    /**
     * 获取系统照相机
     *
     * @return
     */
    public Camera getCamera() {
        Camera camera;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            camera = null;
            e.printStackTrace();
        }

        return camera;
    }

    /**
     * 开启相机预览
     *
     * @param camera
     * @param surfaceHolder
     */
    public void startPreview(Camera camera, SurfaceHolder surfaceHolder) {
        try {
            // 将相机预览和SurfaceView绑定
            camera.setPreviewDisplay(surfaceHolder);
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放系统相机资源
     */
    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (mCamera != null) {
            startPreview(mCamera, surfaceHolder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if (mCamera != null) {
            mCamera.stopPreview();
            startPreview(mCamera, surfaceHolder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        releaseCamera();
    }

    public static void start(Context context, int requestCode) {
        Intent intent = new Intent(context, CaptureActivity.class);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }
}
