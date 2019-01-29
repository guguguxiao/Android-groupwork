package com.homework.grop.group_homework;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    MainActivity activity;
    ImageView imageView;
    OrientationUtils orientationUtils;
    List<Feed>feeds;
    public MyAdapter(List<Feed>feeds,MainActivity activity) {
        this.feeds=feeds;
        this.activity=activity;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.init(position);
    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private StandardGSYVideoPlayer videoPlayer;
        private FloatingActionButton button;
        private LottieAnimationView animationView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        videoPlayer=itemView.findViewById(R.id.detail_player);
            button=itemView.findViewById(R.id.button2);
            animationView=itemView.findViewById(R.id.animation_view);

        }
        private void init(int position) {
            animationView.setOnClickListener((v)->{
                animationView.playAnimation();
            });
            animationView.setProgress(0 );
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailInformation detailInformation=activity;
                    detailInformation.openDetailInformation(feeds.get(position));
                }
            });

            String source1 = feeds.get(position).getVideoUrl();
            imageView=new ImageView(activity);
            Glide.with(activity).load(feeds.get(position).getImageUrl()).into(imageView);
            videoPlayer.setUp(source1, true, "测试视频");
            videoPlayer.setThumbImageView(imageView);
            videoPlayer.getTitleTextView().setVisibility(View.INVISIBLE);
            videoPlayer.getBackButton().setVisibility(View.INVISIBLE);//感觉不加标题看起来跟好看些
            orientationUtils = new OrientationUtils(activity, videoPlayer);
            //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
            videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orientationUtils.resolveByClick();
                }
            });
            //是否可以滑动调整
            videoPlayer.setIsTouchWiget(true);
            videoPlayer.startPlayLogic();
        }


    }
interface DetailInformation{
        public void openDetailInformation(Feed feed);
}

}
