package com.example.nicholas.wwtpApplication;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;


public class Data extends ActionBarActivity {

    private static final int DATA = 0;
    private static Handler valueSetter = new Handler();

    private static TextView ph;
    private static ProgressBar phBar ;
    private static TextView dO;
    private static ProgressBar doBar ;
    private static TextView orp;
    private static ProgressBar orpBar ;
    private static TextView flow;
    private static ProgressBar flowBar ;
    private static TextView waterLevel;
    private static ProgressBar waterLvlBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        ph = (TextView) findViewById(R.id.phLvl);
        phBar = (ProgressBar) findViewById(R.id.currentPHBar);
        dO = (TextView) findViewById(R.id.doLvl);
        doBar = (ProgressBar) findViewById(R.id.doBar);
        orp = (TextView) findViewById(R.id.orpLvl);
        orpBar = (ProgressBar) findViewById(R.id.orpBar);
        flow = (TextView) findViewById(R.id.flowLvl);
        flowBar = (ProgressBar) findViewById(R.id.flowBar);
        waterLevel = (TextView) findViewById(R.id.waterLvl);
        waterLvlBar = (ProgressBar) findViewById(R.id.waterBar);

        Thread update = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(200);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                    //Flow 0 to 20  prog 0-2000
                                    float flowF = Float.valueOf(Ardruino.FLOW) / Constant.FLOWMULT;
                                    flowBar.setProgress(Ardruino.FLOW);
                                    flow.setText(String.valueOf(flowF));

                                    //PH 0 to 14 prog 0-1400
                                    float phF = Float.valueOf(Ardruino.PH) / Constant.PHMUL;
                                    phBar.setProgress(Ardruino.PH);
                                    ph.setText(String.valueOf(phF));

                                    //ORP -900 to 900 prog 0-18000
                                    float orpF = Float.valueOf(Ardruino.ORP) / Constant.ORPMULT;
                                    orpBar.setProgress(Ardruino.ORP + 9000); // convert to 0-18000
                                    orp.setText(String.valueOf(orpF));

                                    //DO 0 to 100 prog 0 -1000
                                    float doF = Float.valueOf(Ardruino.DO) / Constant.DOMULT;
                                    doBar.setProgress(Ardruino.DO);
                                    dO.setText(String.valueOf(doF));

                                    //Water Level  0 to 5  prog 0 -5
                                    waterLvlBar.setProgress(Ardruino.WATERLEVEL);
                                    waterLevel.setText(String.valueOf(Ardruino.WATERLEVEL));

                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        update.start();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data, menu);
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



}
