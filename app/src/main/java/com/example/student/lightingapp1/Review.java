package com.example.student.lightingapp1;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import com.example.student.lightingapp1.R;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

public class Review extends Activity implements View.OnClickListener {

    VideoView vvPlayVideo;
    Chronometer cTimer1, cTimer2, cTotalTime;
    Button bStart1, bClear1, bStart2, bClear2, bPause1, bPause2;
    ListView lvBookmarks;
    MediaController vidControl;
    long time1Pause, time2Pause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_review);

        initialize();
    }

    private void initialize() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        cTimer1 = (Chronometer) findViewById(R.id.cTimer1);
        cTimer2 = (Chronometer) findViewById(R.id.cTimer2);

        bClear1 = (Button) findViewById(R.id.bClear1);
        bClear2 = (Button) findViewById(R.id.bClear2);
        bStart1 = (Button) findViewById(R.id.bStart1);
        bStart2 = (Button) findViewById(R.id.bStart2);
        bPause1 = (Button) findViewById(R.id.bPause1);
        bPause2 = (Button) findViewById(R.id.bPause2);

        lvBookmarks = (ListView) findViewById(R.id.lvBookmarks);

        vvPlayVideo = (VideoView) findViewById(R.id.vvPlayVideo);

        vidControl = new MediaController(this);

        time1Pause = 0;
        time2Pause =0;

        bClear1.setOnClickListener(this);
        bClear2.setOnClickListener(this);
        bStart1.setOnClickListener(this);
        bStart2.setOnClickListener(this);
        bPause1.setOnClickListener(this);
        bPause2.setOnClickListener(this);
        vidControl.setAnchorView(vvPlayVideo);

        videoPlayer();
    }

    private void videoPlayer() {
        String address = "/storage/emulated/0/myvideo324.mp4";
        Uri vidUri = Uri.parse(address);
        vvPlayVideo.setVideoURI(vidUri);
        vvPlayVideo.setMediaController(vidControl);
        vvPlayVideo.start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bStart1:
                cTimer1.setBase(SystemClock.elapsedRealtime());
                cTimer1.stop();
                cTimer1.setBase(SystemClock.elapsedRealtime() + time1Pause);
                cTimer1.start();
                break;
            case R.id.bPause1:
                time1Pause = cTimer1.getBase() - SystemClock.elapsedRealtime();
                cTimer1.stop();
                break;
            case R.id.bClear1:
                cTimer1.setBase(SystemClock.elapsedRealtime());
                time1Pause = 0;
                cTimer1.stop();
                break;

            case  R.id.bStart2:
                cTimer2.setBase(SystemClock.elapsedRealtime());
                cTimer2.stop();
                cTimer2.setBase(SystemClock.elapsedRealtime() + time2Pause);
                cTimer2.start();
                break;
            case R.id.bPause2:
                time2Pause = cTimer2.getBase() - SystemClock.elapsedRealtime();
                cTimer2.stop();
                break;
            case R.id.bClear2:
                cTimer2.setBase(SystemClock.elapsedRealtime());
                time2Pause = 0;
                cTimer2.stop();
                break;
        }
    }
}
