package com.homework.grop.group_homework;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.homework.grop.group_homework.model.Message;
import com.homework.grop.group_homework.model.PullParser;
import com.homework.grop.group_homework.network.FeedResponse;
import com.homework.grop.group_homework.network.IMiniDouyinService;
import com.homework.grop.group_homework.network.RetrofitManager;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements MyAdapter.DetailInformation ,MessageAdapter.MyItemClickListener{
    private RecyclerView mNumbersListView;
    private MyAdapter myAdapter;
    private LinearLayoutManager layoutManager;
    private List<Feed>feeds;
    private  StandardGSYVideoPlayer standardGSYVideoPlayer;
    private RecyclerView myNumbersListView;
    private MessageAdapter messageAdapter;
    private List<Message> messages;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                   setMainScreeen();
                    return true;
                case R.id.navigation_dashboard:
                    setContentView(R.layout.activity_personal_page);
                    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
                    navigation.getMenu().getItem(1).setChecked(true);
                    Resources resource=(Resources)getBaseContext().getResources();
                    @SuppressLint("ResourceType") ColorStateList csl=(ColorStateList)resource.getColorStateList(R.drawable.navigation_menu_item_color);
                    navigation.setItemTextColor(csl);
                    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
                    return true;
                case R.id.navigation_notifications:
                    setMessageScreen();
                    return true;
            }
            return false;
        }
    };



    public void fetchFeed() {
        RetrofitManager.get(IMiniDouyinService.HOST).create(IMiniDouyinService.class).fetchFeed().enqueue(new Callback<FeedResponse>() {
            @Override
            public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
                //Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response.body() + "]");
                if (response.isSuccessful()) {
                    feeds.clear();
                    feeds.addAll(response.body().getFeeds());
                    mNumbersListView.getAdapter().notifyDataSetChanged();
                } else {

                    Toast.makeText(MainActivity.this, "fetch feed failure!", Toast.LENGTH_LONG).show();
                }
            }
            @Override public void onFailure(Call<FeedResponse> call, Throwable t) {
                Log.d("aaaaa", "onResponse: "+MainActivity.this+t.getMessage());
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }





    public void setfeeds(){
        feeds=new LinkedList<>();
        fetchFeed();
//        Feed feed=new Feed();
//        feed.setImageUrl("https://cdn2.thecatapi.com/images/2a1.jpg");
//        feed.setVideoUrl("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4");
//        feed.setStudentId("1120172710");
//        feed.setUserName("顾骁");
//        for(int i=0;i<20;i++)
//            feeds.add(feed);
    }
    public void setMainScreeen()
    {
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Resources resource=(Resources)getBaseContext().getResources();
        @SuppressLint("ResourceType") ColorStateList csl=(ColorStateList)resource.getColorStateList(R.drawable.navigation_menu_item_color);
        navigation.setItemTextColor(csl);
        //获取信息列表
        setfeeds();

        //设置recyclerview
        mNumbersListView = findViewById(R.id.my_list);
        myAdapter=new MyAdapter(feeds,this);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mNumbersListView.setLayoutManager(layoutManager);
        mNumbersListView.setHasFixedSize(true);
        mNumbersListView.setAdapter(myAdapter);
        //自动播放
        mNumbersListView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int firstVisibleItem, lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem   = layoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if(dy>=0) {//下滑
                    StandardGSYVideoPlayer mStandardGSYVideoPlayer = (StandardGSYVideoPlayer) recyclerView.getChildAt(lastVisibleItem - firstVisibleItem).findViewById(R.id.detail_player);
                    int[] screenPosition = new int[2];
                    mStandardGSYVideoPlayer.getLocationOnScreen(screenPosition);
                    //Log.d("ccc", "onScrolled: "+mStandardGSYVideoPlayer.getHeight());
                    //大概第二个视频居中时底下视频开始播放
                    if (screenPosition[1] <= 500 && !mStandardGSYVideoPlayer.isInPlayingState()) {
                        mStandardGSYVideoPlayer.startPlayLogic();
                        standardGSYVideoPlayer=mStandardGSYVideoPlayer;
                    }
                }else{//上划
                    if(lastVisibleItem==firstVisibleItem){
                        //上划刷新
                        setMainScreeen();
                    }else{
                        StandardGSYVideoPlayer mStandardGSYVideoPlayer = (StandardGSYVideoPlayer) recyclerView.getChildAt(lastVisibleItem - firstVisibleItem-1).findViewById(R.id.detail_player);
                        int[] screenPosition = new int[2];
                        mStandardGSYVideoPlayer.getLocationOnScreen(screenPosition);
                        if (screenPosition[1] >= -500 && !mStandardGSYVideoPlayer.isInPlayingState())
                            mStandardGSYVideoPlayer.startPlayLogic();
                        standardGSYVideoPlayer=mStandardGSYVideoPlayer;
                    }
                }
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //这里申请权限

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },0);
                //开启拍照模式
                startActivity(new Intent(MainActivity.this,TakeCamera.class));
            }
        });
    }
    public void setMessageScreen()
    {
        setContentView(R.layout.activity_tips);
        myNumbersListView = findViewById(R.id.rv_list);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.getMenu().getItem(2).setChecked(true);
        Resources resource=(Resources)getBaseContext().getResources();
        @SuppressLint("ResourceType") ColorStateList csl=(ColorStateList)resource.getColorStateList(R.drawable.navigation_menu_item_color);
        navigation.setItemTextColor(csl);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        try {
            InputStream assetInput = getAssets().open("data.xml");
            messages = PullParser.pull2xml(assetInput);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        messageAdapter =new MessageAdapter(messages,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myNumbersListView.setLayoutManager(layoutManager);
        myNumbersListView.setHasFixedSize(true);
        myNumbersListView.setAdapter(messageAdapter);
    }
    @Override
    public void onListItemClick(int position)
    {
        Intent intent=new Intent(this,ChatroomActivity.class);
        intent.putExtra("message",messages.get(position).getTitle());
        startActivity(intent);
        //Toast.makeText(this, "item"+position, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainScreeen();

    }


    @Override
    public void onStop()
    {super.onStop();
    if(standardGSYVideoPlayer!=null)
        standardGSYVideoPlayer.release();
    }
    @Override
    public void openDetailInformation(Feed feed) {
        Intent intent=new Intent(this,DetailVideoActivity.class);
        intent.putExtra("video_url",feed.getVideoUrl());
        intent.putExtra("image_url",feed.getImageUrl());
        intent.putExtra("username",feed.getUserName());
        intent.putExtra("student_id",feed.getStudentId());
        startActivity(intent);
    }
}
