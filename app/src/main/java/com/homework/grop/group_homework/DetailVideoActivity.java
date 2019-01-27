package com.homework.grop.group_homework;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shuyu.gsyvideoplayer.GSYBaseActivityDetail;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

public class DetailVideoActivity extends GSYBaseActivityDetail<StandardGSYVideoPlayer> {
    StandardGSYVideoPlayer detailPlayer;

    private String video_url = "https://res.exexm.com/cw_145225549855002";
    private String image_url;
    private String username;
    private String student_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_video);
        Bundle bundle=this.getIntent().getExtras();
        video_url =bundle.getString("video_url");
        image_url=bundle.getString("image_url");
        username=bundle.getString("username");
        student_id=bundle.getString("student_id");
        //name.setText(bundle.getString("message"));
        detailPlayer = (StandardGSYVideoPlayer) findViewById(R.id.detail_player);
        //增加title
        detailPlayer.getTitleTextView().setVisibility(View.GONE);
        detailPlayer.getBackButton().setVisibility(View.GONE);

        initVideoBuilderMode();

    }

    @Override
    public StandardGSYVideoPlayer getGSYVideoPlayer() {
        return detailPlayer;
    }

    @Override
    public GSYVideoOptionBuilder getGSYVideoOptionBuilder() {
        //内置封面可参考SampleCoverVideo
        ImageView imageView = new ImageView(this);
        loadCover(imageView,image_url);
        return new GSYVideoOptionBuilder()
                .setThumbImageView(imageView)
                .setUrl(video_url)
                .setCacheWithPlay(true)
                .setVideoTitle(" ")
                .setIsTouchWiget(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setShowFullAnimation(false)//打开动画
                .setNeedLockFull(true)
                .setSeekRatio(1);
    }

    @Override
    public void clickForFullScreen() {

    }
    /**
     * 是否启动旋转横屏，true表示启动
     */
    @Override
    public boolean getDetailOrientationRotateAuto() {
        return true;
    }

    private void loadCover(ImageView imageView, String url) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
       // imageView.setImageResource(R.mipmap.xxx1);
        Glide.with(this).load(image_url).into(imageView);
//        Glide.with(this.getApplicationContext())
//                .setDefaultRequestOptions(
//                        new RequestOptions()
//                                .frame(3000000)
//                                .centerCrop()
//                                .error(R.mipmap.xxx1)
//                                .placeholder(R.mipmap.xxx1))
//                .load(url)
//                .into(imageView);
    }

}
