package com.example.app;


import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button take_photo;
    private ImageView picture;
    private static int REQUEST_CAMERA_2 = 2;
    private String mFilePath;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        findViewById(R.id.extraction).setOnClickListener(this);
        // 初始化控件
        init();
        // 控件绑定点击事件
        bindClick();
        //动态申请拍照权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

    }

    // 初始化控件和变量
    private void init() {

        take_photo = (Button) findViewById(R.id.take_photo);
        picture = (ImageView) findViewById(R.id.picture);

    }

    // 控件绑定点击事件
    private void bindClick() {

        take_photo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.take_photo:
                // 拍照后存储并显示图片
                openCamera_2();
                break;
            case R.id.extraction:
                Intent intent = new Intent(this,ExtraActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }



    // 拍照后存储并显示图片
    private void openCamera_2() {

        File fileDir = new File(Environment.getExternalStorageDirectory(),"Pictures");
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        fileName = "IMG_" + System.currentTimeMillis() + ".jpg";

        mFilePath = fileDir.getAbsolutePath()+"/"+ fileName;
        Uri uri = null;
        ContentValues contentValues = new ContentValues();
        //设置文件名

        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/Pictures");
        }else {

            contentValues.put(MediaStore.Images.Media.DATA, mFilePath);
        }
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/JPEG");
        uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQUEST_CAMERA_2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回数据
            if (requestCode == REQUEST_CAMERA_2) {
                try {
                    //查询的条件语句
                    String selection = MediaStore.Images.Media.DISPLAY_NAME + "=? ";
                    //查询的sql
                    //Uri：指向外部存储Uri
                    //projection：查询那些结果
                    //selection：查询的where条件
                    //sortOrder：排序
                    Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media._ID},selection,new String[]{fileName},null);
                    if (cursor != null && cursor.moveToFirst()) {
                        do {
                            Uri uri =  ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getLong(0));
                            Log.i("luingssd","@"+uri);
                            InputStream inputStream = getContentResolver().openInputStream(uri);
                            Bitmap bitmap =  BitmapFactory.decodeStream(inputStream);
                            picture.setImageBitmap(bitmap);// 显示图片
                        }while (cursor.moveToNext());
                    }else {
                        Toast.makeText(this,"no photo",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}




