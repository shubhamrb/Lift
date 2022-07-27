package com.liftPlzz.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.liftPlzz.R;
import com.liftPlzz.utils.Constants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YoutubeActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    String strToken = "";

    @BindView(R.id.toolBarTitle)
    AppCompatTextView toolBarTitle;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.youtube_player)
    YouTubePlayerView youtube_player;
    private YouTubePlayer youtubePlayer;
    private String video_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        ButterKnife.bind(this);
        sharedPreferences = this.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        toolBarTitle.setText("Help & Support");
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
        try {
            String url = getIntent().getStringExtra("url");
            if (url != null) {
                video_id = url.substring(url.lastIndexOf("/") + 1);
            }
            youtube_player.setEnableAutomaticInitialization(false);
            youtube_player.getPlayerUiController().showMenuButton(true);
            youtube_player.getPlayerUiController().showCustomAction1(true);
            youtube_player.getPlayerUiController().showCustomAction2(true);
            youtube_player.initialize(new YouTubePlayerListener() {
                @Override
                public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                    youtubePlayer = youTubePlayer;
                    youtubePlayer.loadVideo(video_id, 0);
                }

                @Override
                public void onStateChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerState playerState) {

                }

                @Override
                public void onPlaybackQualityChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlaybackQuality playbackQuality) {

                }

                @Override
                public void onPlaybackRateChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlaybackRate playbackRate) {

                }

                @Override
                public void onError(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerError playerError) {

                }

                @Override
                public void onCurrentSecond(@NotNull YouTubePlayer youTubePlayer, float v) {

                }

                @Override
                public void onVideoDuration(@NotNull YouTubePlayer youTubePlayer, float v) {

                }

                @Override
                public void onVideoLoadedFraction(@NotNull YouTubePlayer youTubePlayer, float v) {

                }

                @Override
                public void onVideoId(@NotNull YouTubePlayer youTubePlayer, @NotNull String s) {

                }

                @Override
                public void onApiChange(@NotNull YouTubePlayer youTubePlayer) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        imageViewBack.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (youtube_player != null) {
            youtube_player.release();
        }
    }
}

