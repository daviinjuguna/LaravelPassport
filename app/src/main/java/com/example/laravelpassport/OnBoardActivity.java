package com.example.laravelpassport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.laravelpassport.adapters.ViewPagerAdapter;
import com.example.laravelpassport.model.ScreenItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class OnBoardActivity extends AppCompatActivity {

    private ViewPager screenPager;
    ViewPagerAdapter viewPagerAdapter ;
    TabLayout tabIndicator;
    Button btnNext;
    int position = 0 ;
    Animation btnAnim ;
    Button tvSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_on_board);

        // hide the action bar

        getSupportActionBar().hide();

        // ini views
        btnNext = findViewById(R.id.btn_next);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);
        tvSkip = findViewById(R.id.tv_skip);

        // fill list screen

        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Fresh Food","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit",R.drawable.img1));
        mList.add(new ScreenItem("Fast Delivery","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit",R.drawable.img2));
        mList.add(new ScreenItem("Easy Payment","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit",R.drawable.img3));

        // setup viewpager
        screenPager =findViewById(R.id.screen_viewpager);
        viewPagerAdapter = new ViewPagerAdapter(this,mList);
        screenPager.setAdapter(viewPagerAdapter);

        // setup tablayout with viewpager

        tabIndicator.setupWithViewPager(screenPager);

        btnNext.setOnClickListener(v->{
            //if button is next go to next
            position = screenPager.getCurrentItem();
            if (position < mList.size()) {

                position++;
                screenPager.setCurrentItem(position);


            }
            if (position == mList.size()-1){//last page reached
                loadLastScreen();
            }
            if (btnNext.getText().toString().equals("Finish")){
                //start AuthActivity
                startActivity(new Intent(OnBoardActivity.this,AuthActivity.class));
                finish();
            }
        });

        tvSkip.setOnClickListener(v ->{
            //if btn skip is click go to last page
            screenPager.setCurrentItem(mList.size());
        });

        tabIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == mList.size()-1) {
                    loadLastScreen();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void loadLastScreen() {
        tvSkip.setVisibility(View.GONE);
        tvSkip.setEnabled(false);
        btnNext.setText("Finish");
    }


    /*private ViewPager.OnPageChangeListener listener =  new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            //change buttone of skip if page 3
            //skip is hidden in page one

            if(position==0){
                tvSkip.setVisibility(View.VISIBLE);
                tvSkip.setEnabled(true);
                btnNext.setText("Next");

            }
            else if (position==1){
                tvSkip.setVisibility(View.GONE);
                tvSkip.setEnabled(false);
                btnNext.setText("Next");

            }
            else {
                tvSkip.setVisibility(View.GONE);
                tvSkip.setEnabled(false);
                btnNext.setText("Finish");

            }
        }


        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
*/
}
