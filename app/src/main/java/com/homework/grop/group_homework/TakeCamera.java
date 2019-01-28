package com.homework.grop.group_homework;

import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.homework.grop.group_homework.Utils.MEDIA_TYPE_IMAGE;
import static com.homework.grop.group_homework.Utils.MEDIA_TYPE_VIDEO;
import static com.homework.grop.group_homework.Utils.getOutputMediaFile;


public class TakeCamera extends AppCompatActivity implements SurfaceHolder.Callback {

    private SurfaceHolder mSurfaceHolder;
    private SurfaceView mSurdaceView;
    private Camera mCamera;
    private MediaRecorder mMediaRecorder;

    private int CAMERA_TYPE=Camera.CameraInfo.CAMERA_FACING_BACK;
    private boolean isRecording=false;//录像
    private boolean isOpen=true;//手电
    private Camera.Parameters parameters;
    private int rotationDeree=0;

    private FloatingActionButton take_picture;
    private FloatingActionButton take_record;
    private FloatingActionButton take_file;
    private FloatingActionButton facing;
    private FloatingActionButton Iflash;

    private static final int IMAGE_CODE=1;
    private static final int VIEDO_MODE=2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_take_camera);

        //初始化相机
        initCamera();

        take_picture=(FloatingActionButton) findViewById(R.id.take_camera_picture);
        take_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(null,null,mPicture);
            }
        });

        take_record=(FloatingActionButton) findViewById(R.id.take_camera_record);
        take_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecording){
                    //停止录制
                    releaseMediaRecorder();
                    //变换图标以提示
                    take_record.setImageDrawable(getResources().getDrawable(R.drawable.record_light));
                    isRecording=false;
                } else{
                    //开始录制
                    if(prepareVideoRecorder()){
                        mMediaRecorder.start();
                        //变换图标以提示
                        take_record.setImageDrawable(getResources().getDrawable(R.drawable.recording));
                        isRecording=true;
                    }else {
                        //异常，退出
                        releaseMediaRecorder();
                    }
                }
            }
        });

        facing=(FloatingActionButton)findViewById(R.id.take_camera_facing);
        facing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //后置转前置
                if(CAMERA_TYPE==Camera.CameraInfo.CAMERA_FACING_BACK){
                    mCamera=getCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
                    try{
                        mCamera.setPreviewDisplay(mSurfaceHolder);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    mCamera.startPreview();
                }
                //前置转后置
                else {
                    mCamera=getCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
                    try{
                        mCamera.setPreviewDisplay(mSurfaceHolder);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    mCamera.startPreview();
                }
            }
        });

        Iflash=(FloatingActionButton)findViewById(R.id.take_camera_light);
        Iflash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpen){
                    //手电已关闭
                    parameters=mCamera.getParameters();
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    mCamera.setParameters(parameters);
                    isOpen=false;
                }else{
                    //手电已开启
                    parameters=mCamera.getParameters();
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    mCamera.setParameters(parameters);
                    isOpen=true;
                }
            }
        });

        mSurdaceView=(SurfaceView)findViewById(R.id.take_camrea_img);
        mSurdaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAutoFocus();
            }
        });

        take_file=(FloatingActionButton)findViewById(R.id.take_camera_file);
        take_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转提交页面
                startActivity(new Intent(TakeCamera.this,GetFile.class));

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mCamera==null){
            mCamera=getCamera(CAMERA_TYPE);
            if(mSurfaceHolder!=null){
                startPreview(mSurfaceHolder);
            }
        }
    }

    private void initCamera(){
        mSurdaceView=findViewById(R.id.take_camrea_img);
        mCamera=getCamera(CAMERA_TYPE);
        mSurdaceView=findViewById(R.id.take_camrea_img);
        mSurfaceHolder=mSurdaceView.getHolder();
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    mCamera.setPreviewDisplay(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mCamera.startPreview();
                mCamera.cancelAutoFocus();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if(success){
                            mCamera.cancelAutoFocus();
                            doAutoFocus();
                            //改变位置即自动对焦
                        }
                    }
                });
                try {
                    mCamera.setPreviewDisplay(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mCamera.startPreview();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera=null;
            }
        });
    }


    public Camera getCamera(int position){
        CAMERA_TYPE=position;
        if(mCamera!=null){
            releaseCameraAndPreview();
        }
        Camera cam=Camera.open(position);
        //转换角度
        rotationDeree=getCameraDisplayOrientation(CAMERA_TYPE);
        cam.setDisplayOrientation(rotationDeree);
        return cam;
    }

    private static final int DEGREE_90 = 90;
    private static final int DEGREE_180 = 180;
    private static final int DEGREE_270 = 270;
    private static final int DEGREE_360 = 360;

    private int getCameraDisplayOrientation(int cameraId) {
        //旋转相机角度
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = DEGREE_90;
                break;
            case Surface.ROTATION_180:
                degrees = DEGREE_180;
                break;
            case Surface.ROTATION_270:
                degrees = DEGREE_270;
                break;
            default:
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % DEGREE_360;
            result = (DEGREE_360 - result) % DEGREE_360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + DEGREE_360) % DEGREE_360;
        }
        return result;
    }

    private void doAutoFocus(){
        //自动对焦
        parameters=mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.setParameters(parameters);
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if(success){
                    mCamera.cancelAutoFocus();
                    if(!Build.MODEL.equals("KORIDY H30")){
                        parameters=mCamera.getParameters();
                        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                        mCamera.setParameters(parameters);
                    }else {
                        parameters=mCamera.getParameters();
                        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                        mCamera.setParameters(parameters);
                    }
                }
            }
        });
    }

    private void releaseCameraAndPreview() {
        //释放camera资源

        if(mCamera!=null){
            mCamera.release();
            mCamera=null;
        }
    }

    private void startPreview(SurfaceHolder holder) {
        try {
            //绑定surfaceview
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();//开始预览
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean prepareVideoRecorder() {
        //准备MediaRecorder

        mMediaRecorder=new MediaRecorder();
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        mMediaRecorder.setOutputFile(getOutputMediaFile(TakeCamera.this,MEDIA_TYPE_VIDEO).toString());

        mMediaRecorder.setPreviewDisplay(mSurdaceView.getHolder().getSurface());
        mMediaRecorder.setOrientationHint(rotationDeree);

        try{
            mMediaRecorder.prepare();
        }catch (Exception e){
            releaseMediaRecorder();
            return false;
        }
        return true;
    }


    private void releaseMediaRecorder() {
        //释放MediaRecorder
        if(mMediaRecorder!=null){
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder=null;
            mCamera.lock();
        }
    }

    Camera.Size Size;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startPreview(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    Camera.Size size;

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //释放Camera和MediaRecorder资源
        mCamera.release();
        mCamera=null;
        mMediaRecorder.release();
        mMediaRecorder=null;
    }

    private Camera.PictureCallback mPicture=(data,camera)->{
        File pictureFile=getOutputMediaFile(TakeCamera.this,MEDIA_TYPE_IMAGE);

        if(pictureFile==null){
            return;
        }
        try{
            FileOutputStream fos=new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
        }catch (IOException e){
            Log.d("mPicture","Error accessing file: "+e.getMessage());
        }
        mCamera.startPreview();
    };

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = Math.min(w, h);

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

}
