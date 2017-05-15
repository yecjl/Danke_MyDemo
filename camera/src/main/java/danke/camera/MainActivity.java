package danke.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import danke.utils.BitmapUtil;
import danke.utils.MatrixBuilder;

public class MainActivity extends Activity {

    @Bind(R.id.bt_camera)
    Button btCamera;

    @Bind(R.id.iv_photo)
    ImageView ivPhoto;
    @Bind(R.id.bt_camera2)
    Button btCamera2;
    @Bind(R.id.activity_main)
    LinearLayout activityMain;
    @Bind(R.id.bt_camera3)
    Button btCamera3;
    private String imageFile;

//    public static final int REQUEST_EXTERNAL_STORAGE = 1;
//    private static String[] PERMISSIONS_STORAGE = {
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.CAMERA
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        imageFile = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "danke.jpg";

//        verifyStoragePermissions(this);
    }

    /**
     * 从外部存储中读取文件 需要权限
     */
//    public static void verifyStoragePermissions(Activity activity) {
//        // Check if we have write permission
//        int permission = ActivityCompat.checkSelfPermission(activity,
//                Manifest.permission.CAMERA);
//
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            // We don't have permission so prompt the user
//            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
//                    REQUEST_EXTERNAL_STORAGE);
//        }
//    }
    @OnClick(R.id.bt_camera)
    public void openCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Constant.REQUEST_CAMERA_1);
    }

    @OnClick(R.id.bt_camera2)
    public void openCamera2(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = Uri.fromFile(new File(imageFile));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, Constant.REQUEST_CAMERA_2);
    }

    @OnClick(R.id.bt_camera3)
    public void openCamera3(View view) {
        CaptureActivity.start(this, Constant.REQUEST_CAMERA_3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.REQUEST_CAMERA_1:
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        Bitmap bitmap = (Bitmap) extras.get("data");
                        ivPhoto.setImageBitmap(bitmap);
                    }
                    break;
                case Constant.REQUEST_CAMERA_2:
                    BitmapUtil.decodeFileShow(imageFile, ivPhoto);
                    break;
                case Constant.REQUEST_CAMERA_3:
                    if (data != null) {
                        String url = data.getStringExtra("url");
                        BitmapUtil.decodeFileShow(url, new MatrixBuilder().setRotate90().build(), ivPhoto);
                    }
                    break;
            }
        }
    }
}
