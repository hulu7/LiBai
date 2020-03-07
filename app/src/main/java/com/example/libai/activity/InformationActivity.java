package com.example.libai.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.libai.R;
import com.example.libai.databases.DatabaseHelper;
import com.example.libai.databases.UserDao;
import com.example.libai.model.User;
import com.example.libai.utils.CacheUtils;
import com.example.libai.utils.LogUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class InformationActivity extends AppCompatActivity {

    @BindView(R.id.iv_portrait)
    ImageView ivPortrait;
    @BindView(R.id.btn_set_portrait)
    Button btnSetPortrait;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    private CacheUtils cacheUtils;
    private String phone;
    private UserDao userDao;

    private static final int REQUEST_CODE_SELECT_PHOTO = 1;// 从相册中选择
    private static final int REQUEST_CODE_TAKE_PHOTO = 2;// 拍照
    private static final int REQUEST_CODE_CUT_PHOTO = 3;// 结果
    private String IMAGE_NAME = "temp.jpeg";
    private String IMAGE_FILE_URI_PATH = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        ButterKnife.bind(this);

        IMAGE_FILE_URI_PATH = getExternalCacheDir().getPath();
//        IMAGE_FILE_URI_PATH = getExternalFilesDir(null).getPath();

        cacheUtils = new CacheUtils();
        phone = cacheUtils.getString(this, DatabaseHelper.PHONE);
        userDao = new UserDao(this);
        String path = userDao.checkPhone(phone).getPath();

        //设置头像
//        Glide.with(this).load(userDao.checkPhone(phone).getPath())
//                .error(R.drawable.portrait).into(ivPortrait);
        if (path.equals("") || path == null) {
            ivPortrait.setImageResource(R.drawable.portrait);
        } else {
            ivPortrait.setImageURI(Uri.fromFile(new File(path)));
        }


        //设置头像的按钮
        btnSetPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseImageDialog();
            }
        });

        //设置手机号
        tvPhone.setText(userDao.checkPhone(phone).getPhone());

        //设置名称
        etName.setText(userDao.checkPhone(phone).getName());

        //取消
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //修改名称
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(InformationActivity.this, "名称不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    User user = new User();
                    user.setPhone(phone);
                    user.setName(name);
                    if (userDao.updateName(user)) {
                        Toast.makeText(InformationActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(InformationActivity.this, "修改失败！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void showChooseImageDialog() {
        CharSequence[] items = {"相册", "相机"};
        new AlertDialog.Builder(this)
                .setTitle("选择图片来源")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            chooseImg();
                        } else {
                            takePhoto();
                        }
                    }
                })
                .create().show();
    }

    //打开相机
    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempPhotoFileUri());
        startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
    }

    //打开图库
    private void chooseImg() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);//Pick an item from the data
        intent.setType("image/*");//从所有图片中进行选择
        startActivityForResult(intent, REQUEST_CODE_SELECT_PHOTO);
    }

    //创建临时文件存储
    private Uri getTempPhotoFileUri() {
        File file = new File(IMAGE_FILE_URI_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        File imageFile = new File(file, IMAGE_NAME);
        return Uri.fromFile(imageFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SELECT_PHOTO) {
                if (data != null) {
                    Uri uri = data.getData();
                    crop(uri, getTempPhotoFileUri());
                }
            } else if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
                crop(getTempPhotoFileUri(), getTempPhotoFileUri());
            } else if (requestCode == REQUEST_CODE_CUT_PHOTO) {
                handleCropImg(data);
                try {
                    deleteFile(getTempPhotoFileUri().getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //处理裁剪的图片
    private void handleCropImg(Intent data) {
        if (data != null) {
            //有的手机返回的data中没有任何数据，可能和crop时Intent中使用了MediaStore.EXTRA_OUTPUT有关，所以直接从保存图片的uri中获取图片
//            Uri uri = data.getData();
            Bitmap bitmap = getBitmapFromUri(getTempPhotoFileUri());
            if (bitmap != null) {
                ivPortrait.setImageBitmap(bitmap);
            }
        }
    }

    //得到图片的Bitmap
    public Bitmap getBitmapFromUri(Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            writeFileByBitmap(bitmap);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 剪切图片
     *
     * @param uri 原始图片的uri
     */
    private void crop(Uri uri, Uri tempPhotoUri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");//设置为裁切
        intent.putExtra("aspectX", 1);//裁切的宽比例
        intent.putExtra("aspectY", 1);//裁切的高比例
        intent.putExtra("outputX", 250);//裁切的宽度
        intent.putExtra("outputY", 250);//裁切的高度
        intent.putExtra("scale", true);//支持缩放
        intent.putExtra("return-data", false);//剪切的图片不直接返回，返回uri,否则6.0可能会返回null
        //这里是剪切后图片保存的文件地址，加了这行，可能有些手机返回的intent是空的，所以最好直接使用这个uri获取剪切后的图片
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempPhotoUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//裁切成的图片的格式
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, REQUEST_CODE_CUT_PHOTO);
    }

    //删除临时文件
    public boolean deleteFile(String filepath) {
        boolean wasDeleted = false;
        try {
            File file = new File(filepath);
            wasDeleted = file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wasDeleted;
    }

    /**
     * 将bitmap写入文件
     *
     * @param bitmap
     */
    public void writeFileByBitmap(Bitmap bitmap) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();//手机设置的存储位置
        File file = new File(path);
        File imageFile = new File(file, phone + ".jpeg");

        User user = new User();
        user.setPhone(phone);
        user.setPath(String.valueOf(imageFile));
        userDao.updatePath(user);

        LogUtil.e("file" + imageFile);

        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            imageFile.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
