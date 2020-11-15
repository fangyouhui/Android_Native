package com.gh.qiniushortvideo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.gh.qiniushortvideo.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class VideoPuzzleConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_config);
    }

    public void onClickPuzzle2Video(View v) {
        jumpToMediaSelectActivity(MediaSelectActivity.TYPE_VIDEO_PUZZLE_2);
    }

    public void onClickPuzzle3Video(View v) {
        jumpToMediaSelectActivity(MediaSelectActivity.TYPE_VIDEO_PUZZLE_3);
    }

    public void onClickPuzzle4Video(View v) {
        jumpToMediaSelectActivity(MediaSelectActivity.TYPE_VIDEO_PUZZLE_4);
    }

    public void onClickBack(View v) {
        finish();
    }

    private void jumpToMediaSelectActivity(int type) {
        Intent intent = new Intent(this, MediaSelectActivity.class);
        intent.putExtra(MediaSelectActivity.TYPE, type);
        startActivity(intent);
    }
}
