package com.polarxiong.camerademo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

/**
 * Created by zhantong on 16/4/28.
 */
public class MainActivity extends Activity {
    private boolean isRecording=false;
    private CameraPreview mPreview;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initCamera();
        Button buttonSettings = (Button) findViewById(R.id.button_settings);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.camera_preview, new SettingsFragment()).addToBackStack(null).commit();
            }
        });
        final ImageView imageView  = (ImageView)findViewById(R.id.taken_photo);
        final VideoView videoView  = (VideoView)findViewById(R.id.taken_video);
        final Button buttonCapturePhoto = (Button) findViewById(R.id.button_capture_photo);
        buttonCapturePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView view=(ImageView)findViewById(R.id.taken_photo);
                mPreview.takePicture(view);
                imageView.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.INVISIBLE);
            }
        });
        final Button buttonCaptureVideo = (Button) findViewById(R.id.button_capture_video);
        buttonCaptureVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording) {
                    VideoView view=(VideoView)findViewById(R.id.taken_video);
                    mPreview.stopRecording(view);
                    buttonCaptureVideo.setText("录像");
                    isRecording=false;
                    videoView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.INVISIBLE);
                }else{
                    if(mPreview.startRecording()) {
                        buttonCaptureVideo.setText("停止");
                        isRecording = true;
                    }
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ShowPhotoVideo.class);
                intent.setDataAndType(mPreview.getOutputMediaFileUri(),"image/*");
                startActivityForResult(intent,0);
            }
        });
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ShowPhotoVideo.class);
                intent.setDataAndType(mPreview.getOutputMediaFileUri(),"video/*");
                startActivityForResult(intent,0);
            }
        });
    }
    private void initCamera(){
        mPreview = new CameraPreview(this);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        SettingsFragment.passCamera(mPreview.getCameraInstance());
        if (PreferenceManager.getDefaultSharedPreferences(this).getString(SettingsFragment.KEY_PREF_PREV_SIZE, null) == null) {
            getFragmentManager().beginTransaction().replace(R.id.camera_preview, new SettingsFragment()).addToBackStack(null).commit();
            getFragmentManager().executePendingTransactions();
        }
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SettingsFragment.init(PreferenceManager.getDefaultSharedPreferences(this));
    }

    public void onPause(){
        super.onPause();
        mPreview=null;
    }
    public void onResume(){
        super.onResume();
        if(mPreview==null) {
            initCamera();
        }
    }
}
