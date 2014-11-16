package com.example.student.lightingapp1;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;
import android.widget.Chronometer;
import java.io.FileWriter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Vector;

public class Record extends Activity implements View.OnClickListener {
    private Camera myCamera;
    private MyCameraSurfaceView myCameraSurfaceView;
    private MediaRecorder mediaRecorder;

    FileWriter saveFile;

    JSONObject song = new JSONObject();
    JSONObject times = new JSONObject();


    Button myButton, bSplit, bExit;
    TextView tvTime, tvFileName, tvSplitNumber;
    SurfaceHolder surfaceHolder;
    Chronometer mChronometer;
    boolean recording;
    Vector <Long> timesVector;
    int trackNumber;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        initialize();


    }

    private void initialize() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        recording = false;

        setContentView(R.layout.activity_record);



        //Get Camera for preview
        myCamera = getCameraInstance();
        if(myCamera == null){
            Toast.makeText(Record.this,
                    "Fail to get Camera",
                    Toast.LENGTH_LONG).show();
        }

        myCameraSurfaceView = new MyCameraSurfaceView(this, myCamera);
        FrameLayout myCameraPreview = (FrameLayout)findViewById(R.id.videoview);
        myCameraPreview.addView(myCameraSurfaceView);
        addWidgets();

    }

    private void addWidgets() {
        // Declare JSON objects
        // Declare TV
        //tvTime = (TextView) findViewById(R.id.tvTime);
        tvFileName = (TextView) findViewById(R.id.tvFileName);
        tvSplitNumber = (TextView) findViewById(R.id.tvSplitNumber);

        //Declare Buttons
        myButton = (Button) findViewById(R.id.mybutton);
        bSplit = (Button) findViewById(R.id.bSplit);
        bExit = (Button)findViewById(R.id.bExit);

        //Declare StopWatch
        mChronometer = (Chronometer) findViewById(R.id.cTimer1);

        //Declare Vectors
        timesVector = new Vector<Long>();
        trackNumber = 0;

        tvSplitNumber.setText("" + trackNumber);

        //Bring to Front
        //tvTime.bringToFront();
        tvFileName.bringToFront();
        tvSplitNumber.bringToFront();
        myButton.bringToFront();
        bSplit.bringToFront();
        bExit.bringToFront();
        mChronometer.bringToFront();

        //Set on Click Listeners
        myButton.setEnabled(true);
        myButton.setOnClickListener(this);//changed this line
        bSplit.setOnClickListener(this);
        bExit.setOnClickListener(this);

    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.mybutton:
                // TODO Auto-generated method stub
                Toast.makeText(Record.this,
                        "Click",
                        Toast.LENGTH_LONG).show();
                if (recording) {

                    /*// stop recording and release camera
                    mediaRecorder.stop();  // stop the recording
                    long time = SystemClock.elapsedRealtime() - mChronometer.getBase();
                    Toast.makeText(Record.this, "" + time, Toast.LENGTH_LONG).show();
                    mChronometer.stop();
                    releaseMediaRecorder(); // release the MediaRecorder object

                    //Exit after saved
                    finish();*/
                } else {

                    //Release Camera before MediaRecorder start
                    releaseCamera();

                    if (!prepareMediaRecorder()) {
                        Toast.makeText(Record.this,
                                "Fail in prepareMediaRecorder()!\n - Ended -",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                     //start timer
                    mChronometer.setBase(SystemClock.elapsedRealtime());
                    mChronometer.start();
                    long timeElapsed = getTime();
                    timesVector.add(timeElapsed);
                    mediaRecorder.start();
                    recording = true;
                    //myButton.setText("STOP");
                    myButton.setEnabled(false);

                    //data storage, why is this in a try catch block???
                    try {
                        song.put("showName: ", "twists and turns");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                break;

            case R.id.bExit:
                if (recording) {
                // stop recording and release camera
                mediaRecorder.stop();  // stop the recording
                long time = SystemClock.elapsedRealtime() - mChronometer.getBase();
                //Toast.makeText(Record.this, "" + timesVector, Toast.LENGTH_LONG).show();
                try {

                    saveFile.write(song.toString());
                    saveFile.flush();
                    saveFile.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.print(song);


                mChronometer.stop();
                    //why do I have a try, catch?
                    try {
                        song.put("times: ", times);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                Toast.makeText(Record.this, "" + song, Toast.LENGTH_LONG).show();
                releaseMediaRecorder(); // release the MediaRecorder object

                //Exit after saved
                finish();
                break;}
                else {
                    finish();
                    break;
                }

            case R.id.bSplit:
                bookmark();
                break;


        }
    }

    private void bookmark() {
        long timeElapsed = getTime();
        String trackNumberString = Integer.toString(trackNumber);
        //why do I have a try catch block again?
        try {
            times.put(trackNumberString, addTimes(null, timeElapsed));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //timesVector.add(timeElapsed);

    }
    private static JSONArray addTimes(  String songName, long time) {
        // TODO Auto-generated method stub
        double longToDouble = time *.001;
        String time2 = Double.toString(longToDouble);
        JSONArray label = new JSONArray();
        label.put(songName);
        label.put(time2);
        return label;
}

    private long getTime() {
        long time = 0;
        time = SystemClock.elapsedRealtime() - mChronometer.getBase();
        Toast.makeText(Record.this, "" + time, Toast.LENGTH_SHORT).show();
        trackNumber = trackNumber +1;
        tvSplitNumber.setText( "" + trackNumber);
        return time;
    }

    private Camera getCameraInstance(){
        // TODO Auto-generated method stub
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance

        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private boolean prepareMediaRecorder(){
        myCamera = getCameraInstance();
        mediaRecorder = new MediaRecorder();

        myCamera.unlock();
        mediaRecorder.setCamera(myCamera);

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        Random rand = new Random();
        int x = rand.nextInt(51);
        if (isExternalStorageWritable() == true) {
            mediaRecorder.setOutputFile("/sdcard/myvideo324.mp4");//need to change this
            try {
                saveFile = new FileWriter("/sdcard/lightingAppData.json");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(Record.this,
                    "Saved",
                    Toast.LENGTH_SHORT).show();


        }
        else{
            Toast.makeText(Record.this,
                    "This is going to be saved to your device. Please cancel if this is not okay. ",
                    Toast.LENGTH_SHORT).show();

        }
        mediaRecorder.setMaxDuration(999999999); // Set max duration 60 sec.
        mediaRecorder.setMaxFileSize(999999999); // Set max file size 5M
        mediaRecorder.setPreviewDisplay(myCameraSurfaceView.getHolder().getSurface());
        // mediaRecorder.setOrientationHint(-90);

        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            releaseMediaRecorder();
            return false;
        }
        return true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseMediaRecorder(){
        if (mediaRecorder != null) {
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            myCamera.lock();           // lock camera for later use
        }
    }
    private void releaseCamera(){
        if (myCamera != null){
            myCamera.release();        // release the camera for other applications
            myCamera = null;
        }
    }

    public class MyCameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

        private SurfaceHolder mHolder;
        private Camera mCamera;

        public MyCameraSurfaceView(Context context, Camera camera) {
            super(context);
            mCamera = camera;

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int weight,
                                   int height) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.

            if (mHolder.getSurface() == null){
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            try {
                mCamera.stopPreview();
            } catch (Exception e){
                // ignore: tried to stop a non-existent preview
            }

            // make any resize, rotate or reformatting changes here

            // start preview with new settings
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();

            } catch (Exception e){
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub

        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }}

