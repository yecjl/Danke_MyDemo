package danke.cameracutter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int CUT_OK = 0x1006;
    private static final int GET_IMAGE_VIA_GALLERY = 0x1004;
    @Bind(R.id.btn_open)
    Button btnOpen;
    @Bind(R.id.iv_show)
    ImageView ivShow;
    public static final String IMAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "kakasure" + File.separator + "image" + File.separator;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_open)
    public void openCamera(View view) {
        Intent albumIntent = new Intent(Intent.ACTION_PICK);//打开系统的相册
        albumIntent.setType("image/*");
        startActivityForResult(albumIntent, GET_IMAGE_VIA_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

        switch (requestCode) {
            case GET_IMAGE_VIA_GALLERY: //从相册选取图片
                uri = data.getData();
//                getClipPhotoByPickPicture(uri);

                //从相册选取头像
                Bitmap bitmapfromgallery = getImageBitmap(this, data);
                String uriString = MediaStore.Images.Media.insertImage(getContentResolver(), bitmapfromgallery, null, null);
                if (uriString != null && !"".equals(uriString)) {
                    cropImage(Uri.parse(uriString), 300, 300, CUT_OK);
                }
                break;
            case CUT_OK:
                //接收裁剪好的图片信息并保存到本地文件夹
//                if (uri == null) {
//                    break;
//                }
//                Bitmap cropBitmap = getBitmapFromUri(uri, this);
//                if (cropBitmap != null) {
//                    ivShow.setImageBitmap(cropBitmap);
//                }


                Bitmap photo = null;
                try {

                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M && data.getData() != null) {
                        photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));

                    } else {
                        // in android version lower than M your method must work
                        photo = data.getParcelableExtra("data");
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ivShow.setImageBitmap(photo);
                break;
        }
    }

    /**
     * 取得返回的照片信息
     *
     * @param uri 传进返回数据的uri
     */
    private void getClipPhotoByPickPicture(Uri uri) {

        if (uri == null) {
            return;

        } else {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1); // aspectX是宽高的比例，这里设置的是正方形（长宽比为1:1）// 输出是X方向的比例
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 400); // outputX outputY 是裁剪图片宽高
            intent.putExtra("outputY", 400); // 不知怎么了，我设置不能设太大，<640
            intent.putExtra("scale", true); // 去黑边
            intent.putExtra("scaleUpIfNeeded", true); // 去黑边
            intent.putExtra("return-data", false); // 设置为不返回数据
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, CUT_OK);
        }
    }

    /**
     * 截取图片
     *
     * @param uri
     * @param outputX
     * @param outputY
     * @param requestCode
     */
    public void cropImage(Uri uri, int outputX, int outputY, int requestCode) {
        if (uri == null) {
            return;
        }

        //裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        //裁剪框的比例，1：1  // aspectX是宽高的比例，这里设置的是正方形（长宽比为1:1）// 输出是X方向的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", outputX);  // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputY", outputY);
        //图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("scale", true); // 去黑边
        intent.putExtra("scaleUpIfNeeded", true); // 去黑边
        intent.putExtra("return-data", true);  // 设置为不返回数据
        startActivityForResult(intent, requestCode);
    }

    public static Bitmap getBitmapFromUri(Uri uri, Context mContext) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap getImageBitmap(Context context, Intent intent) {
        Bitmap bitmap = null;
        if (intent.getExtras() != null) {
            bitmap = (Bitmap) intent.getExtras().get("data");
        } else {
            Uri uri = intent.getData();
            try {
                bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
        return bitmap;
    }
}
