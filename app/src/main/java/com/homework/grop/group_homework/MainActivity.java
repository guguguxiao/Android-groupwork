package com.homework.grop.group_homework;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView mNumbersListView;
    private MyAdapter myAdapter;
    private LinearLayoutManager layoutManager;
    private List<Feed>feeds;

    public void setfeeds(){
        feeds=new LinkedList<>();
        Feed feed=new Feed();
        feed.setImageUrl("https://cdn2.thecatapi.com/images/2a1.jpg");
        feed.setVideoUrl("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4");
        feed.setStudentId("1120172710");
        feed.setUserName("顾骁");
        for(int i=0;i<20;i++)
            feeds.add(feed);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    if (screenPosition[1] <= 1200 && !mStandardGSYVideoPlayer.isInPlayingState())
                        mStandardGSYVideoPlayer.startPlayLogic();
                }else{//上划
                    if(lastVisibleItem==firstVisibleItem){
                        //上划刷新




                    }else{
                    StandardGSYVideoPlayer mStandardGSYVideoPlayer = (StandardGSYVideoPlayer) recyclerView.getChildAt(lastVisibleItem - firstVisibleItem-1).findViewById(R.id.detail_player);
                    int[] screenPosition = new int[2];
                    mStandardGSYVideoPlayer.getLocationOnScreen(screenPosition);
                    if (screenPosition[1] >= -1200 && !mStandardGSYVideoPlayer.isInPlayingState())
                        mStandardGSYVideoPlayer.startPlayLogic();
                    }
                }
            }
        });















        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
