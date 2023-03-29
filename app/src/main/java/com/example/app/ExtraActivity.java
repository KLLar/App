package com.example.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class ExtraActivity extends AppCompatActivity implements View.OnClickListener {
    public static int flag = 0;
    private TextView filePathtextView;
    private Button btn;
    private PopupWindow mPopWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extract);
        filePathtextView = findViewById(R.id.path);
        findViewById(R.id.photo).setOnClickListener(this);
        btn = findViewById(R.id.extract_data);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0){
                    //文件选择器
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(Intent.createChooser(intent, "需要选择文件"), 1);
                    flag = 1;
                }else{
                    flag = 0;

                    //弹窗
                    initPopWindow(v);
                }
            }
        });

    }

    private void initPopWindow(View v) {
        View contentView = LayoutInflater.from(ExtraActivity.this).inflate(R.layout.pop_view,null);
        Button map = (Button)contentView.findViewById(R.id.map);
        Button exit = (Button)contentView.findViewById(R.id.exit);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, true);
        popupWindow.setAnimationStyle(R.xml.pop);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.showAtLocation(contentView,Gravity.CENTER,0,-100);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ExtraActivity.this,"转到高德地图",Toast.LENGTH_SHORT).show();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ExtraActivity.this,"确认退出",Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                btn.setText("选择所要提取的图片");
                btn.setBackground(getDrawable(R.drawable.old_button));
            }
        });
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //加载路径
        if (resultCode == Activity.RESULT_OK) {

            Uri uri = data.getData();
            String pathString = UriUtil.getPath(this,uri);
            filePathtextView.setText(pathString);
            btn.setBackground(getDrawable(R.drawable.simple_button));
            btn.setText("提取");
        }
    }
}
