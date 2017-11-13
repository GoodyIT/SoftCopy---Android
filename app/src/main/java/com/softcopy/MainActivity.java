package com.softcopy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;
import com.afollestad.materialcamera.util.CameraUtil;
import com.softcopy.internal.BaseCameraFragment;
import com.softcopy.internal.Camera2Fragment;
import com.softcopy.util.SimpleGestureFilter;
import com.softcopy.util.SimpleGestureListener;

import java.io.File;

public class MainActivity extends AppCompatActivity implements SimpleGestureListener {

    private static final int CAMERA_RQ = 6969;
    private static final int PERMISSION_RQ = 84;

    private SimpleGestureFilter detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Request permission to save videos in external storage
            ActivityCompat.requestPermissions(
                    this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_RQ);
        }

//        setContentView(R.layout.activity_fragment);

        // Detect touched area
        detector = new SimpleGestureFilter(this,this);

        showCamera();
    }

    public void showCamera() {
        File saveDir = null;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            // Only use external storage directory if permission is granted, otherwise cache directory is used by default
            saveDir = new File(Environment.getExternalStorageDirectory(), "SoftCopy");
            saveDir.mkdirs();
        }
//        MaterialCamera materialCamera =
//                new MaterialCamera(this)
//                        .saveDir(saveDir)
//                        .showPortraitWarning(true)
//                        .allowRetry(true)
//                        .defaultToFrontFacing(false)
//                        .allowRetry(true)
//                        .autoSubmit(false);
//
//        materialCamera
//                .stillShot() // launches the Camera in stillshot mode
//                .labelConfirm(R.string.mcam_use_stillshot);
//        materialCamera.start(CAMERA_RQ);
        BaseCameraFragment fragment = null;
        if (CameraUtil.hasCamera2(this, true)) {
            fragment = Camera2Fragment.newInstance();
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Received recording or error from MaterialCamera
        if (requestCode == CAMERA_RQ) {

            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Saved to: " + data.getDataString(), Toast.LENGTH_LONG).show();
            } else if(data == null) {
                showCamera();
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me){
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }
    @Override
    public void onSwipe(int direction) {
        String str = "";

        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT : str = "Swipe Right";
//                Intent intent = new Intent(this, PreviewActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_from_left);
                break;
            case SimpleGestureFilter.SWIPE_LEFT :  str = "Swipe Left";
                break;
            case SimpleGestureFilter.SWIPE_DOWN :  str = "Swipe Down";
                break;
            case SimpleGestureFilter.SWIPE_UP :    str = "Swipe Up";
                break;

        }
//        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDoubleTap() {

    }

//    @Override
//    public void finish() {
//        super.finish();
//        overridePendingTransitionExit();
//    }

//    @Override
//    public void startActivity(Intent intent) {
//        super.startActivity(intent);
//        overridePendingTransitionEnter();
//    }
//
//    /**
//     * Overrides the pending Activity transition by performing the "Enter" animation.
//     */
//    protected void overridePendingTransitionEnter() {
//        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_from_left);
//    }
//
//    /**
//     * Overrides the pending Activity transition by performing the "Exit" animation.
//     */
//    protected void overridePendingTransitionExit() {
//        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
