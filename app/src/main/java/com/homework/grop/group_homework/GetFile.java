package com.homework.grop.group_homework;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.homework.grop.group_homework.network.IMiniDouyinService;
import com.homework.grop.group_homework.network.PostVideoResponse;
import com.homework.grop.group_homework.network.ResourceUtils;
import com.homework.grop.group_homework.network.RetrofitManager;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetFile extends AppCompatActivity {

    private static final int IMAGE_MODE=1;
    private static final int VIDEO_MODE=2;

    private Button mImage;
    private Button mVidoe;
    private FloatingActionButton mPost;

    public Uri mSelectedImage;
    private Uri mSelectedVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getfile);

        mImage=(Button)findViewById(R.id.file_image);
        mVidoe=(Button)findViewById(R.id.file_video);
        mPost=(FloatingActionButton) findViewById(R.id.file_post);

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        mVidoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseVideo();
            }
        });
        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String toast = "Posting!";
                Toast.makeText(GetFile.this, toast, Toast.LENGTH_LONG).show();
                postVideo();
            }
        });

    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_MODE);
    }

    public void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,VIDEO_MODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && data!=null){
            if(requestCode==IMAGE_MODE){
                mImage.setText("ok");
                mSelectedImage=data.getData();
            }
            else if(requestCode==VIDEO_MODE){
                mSelectedVideo=data.getData();
                mVidoe.setText("ok");
            }
        }
    }

    private void postVideo() {
//        take_camera_file.setText("POSTING...");
        mPost.setEnabled(false);
        RetrofitManager.get(IMiniDouyinService.HOST).create(IMiniDouyinService.class).createVideo("1120170000", "test", getMultipartFromUri("cover_image", mSelectedImage), getMultipartFromUri("video", mSelectedVideo)).enqueue(new Callback<PostVideoResponse>() {
            @Override
            public void onResponse(Call<PostVideoResponse> call, Response<PostVideoResponse> response) {
                String toast;
                if (response.isSuccessful()) {
                    toast = "Post Success!";
                    mPost.setImageDrawable(getResources().getDrawable(R.drawable.upload));
//                    take_camera_file.setText(R.string.success_try_refresh);
                } else {
                    toast = "Post Failure...";
//                    take_camera_file.setText(R.string.post_it);
                }
                Toast.makeText(GetFile.this, toast, Toast.LENGTH_LONG).show();
                mPost.setEnabled(true);
            }

            @Override public void onFailure(Call<PostVideoResponse> call, Throwable t) {
                Toast.makeText(GetFile.this, t.getMessage(), Toast.LENGTH_LONG).show();
//                take_camera_file.setText(R.string.post_it);
                mPost.setEnabled(true);
            }
        });
    }

    private MultipartBody.Part getMultipartFromUri(String name, Uri uri) {
        // if NullPointerException thrown, try to allow storage permission in system settings
        File f = new File(ResourceUtils.getRealPath(GetFile.this, uri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        return MultipartBody.Part.createFormData(name, f.getName(), requestFile);
    }


}
