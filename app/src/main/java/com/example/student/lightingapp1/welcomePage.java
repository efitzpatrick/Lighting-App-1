package com.example.student.lightingapp1;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class welcomePage extends Activity implements View.OnClickListener {

    Button bSelectRecord, bSelectSplit, bSelectReview;

    TextView tvWelcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        initialize();
    }

    private void initialize() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        bSelectRecord = (Button) findViewById(R.id.bSelectRecord);
        bSelectSplit = (Button) findViewById(R.id.bSelectSplit);
        bSelectReview = (Button) findViewById(R.id.bSelectReveiw);

        bSelectRecord.setOnClickListener(this);
        bSelectSplit.setOnClickListener(this);
        bSelectReview.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bSelectRecord:
                Intent basketRecord = new Intent(this, Record.class);

                startActivity(basketRecord);
                break;

            case R.id.bSelectSplit:
                Intent basketSplit = new Intent(this, Split.class);

                startActivity(basketSplit);
                break;

            case R.id.bSelectReveiw:
                Intent basketReview = new Intent(this, Review.class);

                startActivity(basketReview);
                break;

        }
    }
}
