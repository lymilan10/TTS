package com.example.ttsdemo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.github.paolorotolo.appintro.AppIntro;

public class AppIntroActivity extends AppIntro {
    @Override
    public void init(@Nullable Bundle savedInstanceState) {

        addSlide(SlideFragment.newInstance(R.layout.intro1));
        addSlide(SlideFragment.newInstance(R.layout.intro2));
        addSlide(SlideFragment.newInstance(R.layout.intro3));
//        addSlide(SlideFragment.newInstance(R.layout.intro4));
        setSeparatorColor(getResources().getColor(R.color.purple_200));
        setVibrateIntensity(30);
        setSkipText("跳过");
        setDoneText("完成");
        setSeparatorColor(Color.WHITE);
        //动画效果
//        setFadeAnimation();
//        setZoomAnimation();
        setFlowAnimation();
//        setSlideOverAnimation();
//        setDepthAnimation();

    }

    @Override
    public void onSkipPressed() {
        //当执行跳过动作时触发
        Intent intent = new Intent(AppIntroActivity.this, MainActivity.class);
        startActivity(intent);
        SpUtils.putBoolean(AppIntroActivity.this, AppConstants.FIRST_OPEN, true);
        finish();
    }

    @Override
    public void onNextPressed() {
        //当执行下一步动作时触发
    }

    @Override
    public void onDonePressed() {
        //当执行完成动作时触发
        Intent intent = new Intent(AppIntroActivity.this, MainActivity.class);
        startActivity(intent);
        SpUtils.putBoolean(AppIntroActivity.this, AppConstants.FIRST_OPEN, true);
        finish();
    }

    @Override
    public void onSlideChanged() {
        //当执行变化动作时触发
    }
}
