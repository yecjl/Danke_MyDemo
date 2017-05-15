package danke.cameracutter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 使用第三方截图软件
 */
public class MainActivity2 extends AppCompatActivity {

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
        Crop.pickImage(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Crop.REQUEST_PICK: //从相册选取图片

                if (resultCode == RESULT_OK) {
                    beginCrop(data.getData());
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "从相册选取取消", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "从相册选取失败", Toast.LENGTH_SHORT).show();
                }

                break;

            case Crop.REQUEST_CROP:
                if (resultCode == RESULT_OK) {
                    ivShow.setImageDrawable(null);
                    ivShow.setImageURI(Crop.getOutput(data));
                } else if (resultCode == Crop.RESULT_ERROR) {
                    Toast.makeText(this, Crop.getError(data).getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

}
