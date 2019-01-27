package com.homework.grop.group_homework;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

//    private List<String> urls;
//    public MyAdapter(List<String> urls) {
//        this.urls=urls;
//    }
    MainActivity activity;
    ImageView imageView;
    OrientationUtils orientationUtils;

    public MyAdapter(MainActivity activity) {
      // this.imageView=new ImageView(activity);
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
            holder.init();
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private StandardGSYVideoPlayer videoPlayer;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        videoPlayer=itemView.findViewById(R.id.detail_player);
        }
        private void init() {

           // Glide.with(activity).load("").into(imageView);
            String source1 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";//可以改成从string数组内获取url
            imageView=new ImageView(activity);
            imageView.setImageResource(R.mipmap.xxx1);
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


}
