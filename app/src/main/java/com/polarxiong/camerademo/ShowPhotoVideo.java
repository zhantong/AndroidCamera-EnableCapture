package com.polarxiong.camerademo;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

/**
 * Created by zhantong on 16/5/3.
 */
public class ShowPhotoVideo extends Activity{
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linLayout = new LinearLayout(this);
        LinearLayout.LayoutParams linLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        Uri uri=getIntent().getData();
        View view;
        if(getIntent().getType().equals("image/*")){
            view=new ImageView(this);
            ((ImageView)view).setImageURI(uri);
        }else{
            view=new VideoView(this);
            ((VideoView)view).setVideoURI(uri);
        }
        view.setLayoutParams(linLayoutParam);
        linLayout.addView(view);
        setContentView(linLayout, linLayoutParam);
    }
}
