package com.polarxiong.camerademo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

/**
 * Created by zhantong on 16/4/28.
 */
public class MainActivity extends Activity {
    private boolean isRecording=false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final CameraPreview mPreview = new CameraPreview(this);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        SettingsFragment.passCamera(mPreview.getCameraInstance());
        if (PreferenceManager.getDefaultSharedPreferences(this).getString(SettingsFragment.KEY_PREF_PREV_SIZE, null) == null) {
            getFragmentManager().beginTransaction().replace(R.id.camera_preview, new SettingsFragment()).addToBackStack(null).commit();
            getFragmentManager().executePendingTransactions();
        }
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SettingsFragment.init(PreferenceManager.getDefaultSharedPreferences(this));
        Button buttonSettings = (Button) findViewById(R.id.button_settings);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.camera_preview, new SettingsFragment()).addToBackStack(null).commit();
            }
        });
        final Button buttonCapturePhoto = (Button) findViewById(R.id.button_capture_photo);
        buttonCapturePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPreview.takePicture();
            }
        });
        final Button buttonCaptureVideo = (Button) findViewById(R.id.button_capture_video);
        buttonCaptureVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording) {
                    mPreview.stopRecording();
                    buttonCaptureVideo.setText("录像");
                    isRecording=false;
                }else{
                    if(mPreview.startRecording()) {
                        buttonCaptureVideo.setText("停止");
                        isRecording = true;
                    }
                }
            }
        });
    }

    public void onPause() {
        finish();
        super.onPause();
    }
}
