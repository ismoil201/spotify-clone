package kr.dev.spofity;

import static kr.dev.spofity.ConstantKt.MEDIA_PLAYER_BACK;
import static kr.dev.spofity.ConstantKt.MEDIA_PLAYER_NEXT;
import static kr.dev.spofity.ConstantKt.MEDIA_PLAYER_PLAY;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kr.dev.spofity.databinding.ActivityPleyer2Binding;

public class PlayerActivity2 extends AppCompatActivity {

    TextView fulName;
    ImageView iv_home;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ActivityPleyer2Binding binding;

    private MediaPlayer mediaPlayer;

    public static List<Music> musicList;
    private int currentMusic = 0;
    private boolean isPlaying = false;

    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPleyer2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initUI();
        clicks();

//        loadMusic(currentMusic);

        binding.nextMusic.setOnClickListener(view -> {

            Intent intent = new Intent(this, MediaPlayerService.class);
            intent.setAction(MEDIA_PLAYER_NEXT);
            startService(intent);
//            if (mediaPlayer != null) {
//                mediaPlayer.stop();
//                mediaPlayer.release();
//            }
//            currentMusic++;
//            if (currentMusic >= musicList.size()) {
//                currentMusic = 0;
//            }
//            loadMusic(currentMusic);
//            mediaPlayer.start();
//            isPlaying = true;
//            binding.tvFulName.setText(musicList.get(currentMusic).musicName);
//            binding.play.setImageResource(R.drawable.pause_circle_svgrepo_com);
        });

        binding.backMusic.setOnClickListener(view -> {

            Intent intent = new Intent(this, MediaPlayerService.class);
            intent.setAction(MEDIA_PLAYER_BACK);
            startService(intent);

//            if (mediaPlayer != null) {
//                mediaPlayer.stop();
//                mediaPlayer.release();
//            }
//            currentMusic--;
//            if (currentMusic < 0) {
//                currentMusic = musicList.size() - 1;
//            }
//            loadMusic(currentMusic);
//            mediaPlayer.start();
//            isPlaying = true;
//            binding.tvFulName.setText(musicList.get(currentMusic).musicName);
//            binding.play.setImageResource(R.drawable.pause_circle_svgrepo_com);
        });

        binding.play.setOnClickListener(view -> {

            Intent  intent = new Intent(this, MediaPlayerService.class);
            intent.setAction(MEDIA_PLAYER_PLAY);
            startService(intent);


//            if (mediaPlayer != null) {
//                if (!isPlaying) {
//                    mediaPlayer.start();
//                    updateSeekBar();
//                    binding.tvFulName.setText(musicList.get(currentMusic).musicName);
//                    binding.play.setImageResource(R.drawable.pause_circle_svgrepo_com);
//                    isPlaying = true;
//                } else {
//                    mediaPlayer.pause();
//                    binding.tvFulName.setText(musicList.get(currentMusic).musicName);
//                    binding.play.setImageResource(R.drawable.play_circle_svgrepo_com__1_);
//                    isPlaying = false;
//                }
//            }
        });

//        binding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if (fromUser && mediaPlayer != null) {
//                    int newPosition = (mediaPlayer.getDuration() * progress) / 100;
//                    mediaPlayer.seekTo(newPosition);
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {}
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {}
//        });
    }
//
//    private void loadMusic(int index) {
//        if (mediaPlayer != null) {
//            mediaPlayer.release();
//        }
//
//        Music music = musicList.get(index);
//        mediaPlayer = MediaPlayer.create(PlayerActivity2.this, music.getMusic());
//        binding.ivImage1.setImageResource(music.getImage());
//
//        mediaPlayer.setOnPreparedListener(mp -> {
//            binding.seekbar.setMax(100);
//            updateSeekBar();
//        });
//
//        mediaPlayer.setOnCompletionListener(mp -> {
//            binding.play.setImageResource(R.drawable.play_circle_svgrepo_com__1_);
//            isPlaying = false;
//            handler.removeCallbacks(runnable);
//        });
//    }

//    private void updateSeekBar() {
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//                    int currentPosition = mediaPlayer.getCurrentPosition();
//                    int duration = mediaPlayer.getDuration();
//                    if (duration > 0) {
//                        int progress = (int) (((float) currentPosition / duration) * 100);
//                        binding.seekbar.setProgress(progress);
//                    }
//                    handler.postDelayed(this, 500);
//                }
//            }
//        };
//        handler.postDelayed(runnable, 0);
//    }

    private void initUI() {
        fulName = findViewById(R.id.tv_fulName);
        iv_home = findViewById(R.id.iv_home);

        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        fulName.setText(sharedPreferences.getString("email", ""));

        musicList = new ArrayList<>();
        musicList.add(new Music(R.raw.birinchi, "사랑은 힘든가 봐 (Love Is Difficult)", R.drawable.img_7));
        musicList.add(new Music(R.raw.cukur, "두사랑 (Feat. 매드클라운)", R.drawable.img_8));

        musicList.add(new Music(R.raw.yor_yor_mix, "Yor Yor mix", R.drawable.img_2));
        musicList.add(new Music(R.raw.fake_love, "Fake love", R.drawable.img_3));
        musicList.add(new Music(R.raw.barbie_girl, "Barbie Girl", R.drawable.img_4));
        musicList.add(new Music(R.raw.cukur, "CUKUR TURKIYE", R.drawable.img_5));
        musicList.add(new Music(R.raw.monica, "Monica  mix", R.drawable.img));
        musicList.add(new Music(R.raw.michael_changed, "Michael changed", R.drawable.img_6));

        musicList.add(new Music(R.raw.birinchi, "사랑은 힘든가 봐 (Love Is Difficult)", R.drawable.img_7));
        musicList.add(new Music(R.raw.cukur, "두사랑 (Feat. 매드클라운)", R.drawable.img_8));

        musicList.add(new Music(R.raw.yor_yor_mix, "Yor Yor mix", R.drawable.img_2));
        musicList.add(new Music(R.raw.fake_love, "Fake love", R.drawable.img_3));
        musicList.add(new Music(R.raw.barbie_girl, "Barbie Girl", R.drawable.img_4));
        musicList.add(new Music(R.raw.cukur, "CUKUR TURKIYE", R.drawable.img_5));
        musicList.add(new Music(R.raw.monica, "Monica  mix", R.drawable.img));
        musicList.add(new Music(R.raw.michael_changed, "Michael changed", R.drawable.img_6));
    }



    private void clicks() {
        binding.ivHome.setOnClickListener(view -> {
            editor.putBoolean("isLoggedIn", false);
            editor.apply();
            startActivity(new Intent(PlayerActivity2.this, MainActivity.class));
            finish();
        });
    }


    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            handler.removeCallbacks(runnable);
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }


}
