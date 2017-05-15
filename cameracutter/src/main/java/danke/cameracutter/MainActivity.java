package danke.cameracutter;

import android.Manifest;
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
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    private static final int PHOTO_REQUEST_CUT = 0x1006;
    private static final int GET_IMAGE_VIA_GALLERY = 0x1004;
    @Bind(R.id.btn_open)
    Button btnOpen;
    @Bind(R.id.iv_show)
    ImageView ivShow;
    public static final String IMAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "kakasure" + File.separator + "image" + File.separator;
    private Uri imageUri;
    private Uri newUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_open)
    public void openCamera(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//打开系统的相册
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, GET_IMAGE_VIA_GALLERY);
    }

    @NeedsPermission(Manifest.permission.MANAGE_DOCUMENTS)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GET_IMAGE_VIA_GALLERY: //从相册选取图片

                if (data == null) {
                    return;
                }

                imageUri = data.getData();

                if (resultCode == RESULT_OK) {
                    //从相册选取成功后，需要从Uri中拿出图片的绝对路径，再调用剪切
                    newUri = Uri.parse("file:///" + CropUtils.getPath(this, data.getData()));
                    if (newUri != null) {
                        CropUtils.cropImageUri(this, newUri, imageUri, 400, 400, PHOTO_REQUEST_CUT);
                    } else {
                        Toast.makeText(this, "没有得到相册图片", Toast.LENGTH_SHORT).show();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "从相册选取取消", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "从相册选取失败", Toast.LENGTH_SHORT).show();
                }

                break;
            case PHOTO_REQUEST_CUT:
                //接收裁剪好的图片信息并保存到本地文件夹
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = decodeUriAsBitmap(this, imageUri);
                    ivShow.setImageBitmap(bitmap);
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "取消剪切图片", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "剪切失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //从Uri中获取Bitmap格式的图片
    private static Bitmap decodeUriAsBitmap(Context context, Uri uri) {
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
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
