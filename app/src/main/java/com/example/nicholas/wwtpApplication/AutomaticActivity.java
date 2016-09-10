package com.example.nicholas.wwtpApplication;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;


public class AutomaticActivity extends ActionBarActivity {

    private static TextView currentPH;
    private static ProgressBar phBar;
    private static TextView status;
    private static TextView targetPh;
    private static Spinner targetSelector;
    private static Button start;

    private static boolean lockStatus = true;
    private static int currentTargetPH = -1;
    private static String currentStatus = "Status";
    private static int currentSelectorPosition = 1;
    private static int startingPH;

    private static int howManyTimesForBalancing = 5;
    private static Handler autoHandler = new Handler();
    private static int threshold = 30; // 0.3 ph +-
    private static int retry = 0;
    private static int maxRetry = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automatic);

        currentPH = (TextView) findViewById(R.id.currentPH);
        phBar = (ProgressBar) findViewById(R.id.currentPHBar);
        status = (TextView) findViewById(R.id.status);
        targetPh = (TextView) findViewById(R.id.targetPh);
        targetSelector = (Spinner) findViewById(R.id.targetPhSpinner);
        start = (Button) findViewById(R.id.start);

        Thread update = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(200);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //PH 0 to 14 prog 0-1400
                                float phF = Float.valueOf(Ardruino.PH) / Constant.PHMUL;
                                phBar.setProgress(Ardruino.PH);
                                currentPH.setText(String.valueOf(phF));
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        update.start();

        String[] items = new String[]{"4","5", "6", "7"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.phspinner, items);
        targetSelector.setAdapter(adapter);
        targetSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                currentSelectorPosition = position;
                int targetPHSelected = (position + 4);
                targetPh.setText(String.valueOf(targetPHSelected));
                currentTargetPH = targetPHSelected;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startingPH = Ardruino.PH;
                lockStatus = false;
                start.setEnabled(lockStatus);
                targetSelector.setEnabled(lockStatus);
                startProcess();


            }
        });

    }

    private static void startProcess() {
        int targetPH = currentTargetPH * 100;
        int currentPH = Ardruino.PH;
        int ab = 0;
        int phNow = Ardruino.PH;
        if (phNow <= targetPH) {
            ab = Constant.BASE;
        } else {
            ab = Constant.ACID;
        }

        int difference = Math.abs(targetPH - currentPH);
        if(difference > 40){
            howManyTimesForBalancing = 5;
        } else if (30 <= difference &&  difference <= 40) {
            howManyTimesForBalancing = 3;
        }

        Cycle cy = new Cycle(ab, howManyTimesForBalancing);
        cy.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (status != null && targetPh != null && targetSelector != null && start != null  ){
            status.setText(currentStatus);
            targetPh.setText(String.valueOf(currentTargetPH));
            targetSelector.setSelection(currentSelectorPosition);
            start.setEnabled(lockStatus);
            targetSelector.setEnabled(lockStatus);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_automatic, menu);
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

    public static void append(final String toAppend) {
        autoHandler.post(new Runnable() {
            @Override
            public void run() {
                float startingPHF = Float.valueOf(startingPH) / Constant.PHMUL;
               currentStatus = "Starting PH: " + startingPHF + "\nProcess : " + (retry +1) + "\n" + toAppend;
               status.setText(currentStatus);;
            }
        });
    }

    public static void cycleDone() {
        autoHandler.post(new Runnable() {
            @Override
            public void run() {

                float startingPHF = Float.valueOf(startingPH) / Constant.PHMUL;
                int targetPH = currentTargetPH * 100;
                int phNow = Ardruino.PH ;
                float phNowF = Float.valueOf(Ardruino.PH) / Constant.PHMUL;
                float difference = Math.abs(startingPHF - phNowF);

                if (Math.abs(phNow - targetPH) > threshold && retry < maxRetry) {
                    retry ++ ;
                    startProcess();
                } else if (retry == maxRetry){
                    retry = 0;
                    lockStatus = true;
                    start.setEnabled(lockStatus);
                    targetSelector.setEnabled(lockStatus);
                    currentStatus = "Target PH could not be balanced after " + (maxRetry + 1) + " processes" +
                                 "\nPH changed " + String.format("%.2f", difference) + " from " + startingPHF + " to " + phNowF;
                    status.setText(currentStatus);
                } else {
                    retry = 0;
                    lockStatus = true;
                    start.setEnabled(lockStatus);
                    targetSelector.setEnabled(lockStatus);
                    currentStatus = "PH balanced to  " + phNowF + " with  " + (retry +1) + " processes" +
                                 "\nPH changed " + String.format("%.2f", difference)   +  " from " + startingPHF + " to " + phNowF;
                    status.setText(currentStatus);
                }
            }
        });
    }
}
