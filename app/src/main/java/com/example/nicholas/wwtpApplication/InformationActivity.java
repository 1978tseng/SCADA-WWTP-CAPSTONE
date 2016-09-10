package com.example.nicholas.wwtpApplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class InformationActivity extends ActionBarActivity {

    private static final int INFOMATION = 1;
    private static Handler valueSetter = new Handler();

    private static TextView flowInfo;
    private static TextView phInfo;
    private static TextView orpInfo;
    private static TextView doInfo;
    private static TextView waterInfo;

    private static TextView solInfo;
    private static ImageView solImg;
    private static  TextView pumpInfo;
    private static ImageView pumpImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        flowInfo = (TextView) findViewById(R.id.flowInfo);
        phInfo = (TextView) findViewById(R.id.phInfo);
        orpInfo = (TextView) findViewById(R.id.orpInfo);
        doInfo = (TextView) findViewById(R.id.doInfo);
        waterInfo = (TextView) findViewById(R.id.waterInfo);

        solInfo = (TextView) findViewById(R.id.solInfo);
        solImg = (ImageView) findViewById(R.id.solImg);
        pumpInfo = (TextView) findViewById(R.id.pumpInfo);
        pumpImg = (ImageView) findViewById(R.id.pumpImg);

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
                                flowInfo.setText(String.valueOf(flowF));

                                //PH 0 to 14 prog 0-1400
                                float phF = Float.valueOf(Ardruino.PH) / Constant.PHMUL;
                                phInfo.setText(String.valueOf(phF));

                                //ORP -900 to 900 prog 0-18000
                                float orpF = Float.valueOf(Ardruino.ORP) / Constant.ORPMULT;
                                orpInfo.setText(String.valueOf(orpF));

                                //DO 0 to 100 prog 0 -1000
                                float doF = Float.valueOf(Ardruino.DO) / Constant.DOMULT;
                                doInfo.setText(String.valueOf(doF));

                                //Water Level  0 to 5  prog 0 -5
                                waterInfo.setText(String.valueOf(Ardruino.WATERLEVEL));

                                //shotoff
                                if(PLC.VALVE == 1){
                                    solInfo.setText("ON");
                                    solImg.setImageResource(R.drawable.greenbutton);
                                } else {
                                    solInfo.setText("OFF");
                                    solImg.setImageResource(R.drawable.redbutton);
                                }

                                //pump
                                if(PLC.PUMP == 1){
                                    pumpInfo.setText("ON");
                                    pumpImg.setImageResource(R.drawable.greenbutton);
                                } else {
                                    pumpInfo.setText("OFF");
                                    pumpImg.setImageResource(R.drawable.redbutton);
                                }

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
        getMenuInflater().inflate(R.menu.menu_information, menu);
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

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
//        InfomationReadingControler drc = new InfomationReadingControler();
//        drc.start();
    }

    public void processScreen(View view){
        Intent processIntent = new Intent(this, Process.class);
        startActivity(processIntent);
    }

    public void dataScreen(View view){
        Intent dataIntent = new Intent(this, Data.class);
        startActivity(dataIntent);
    }

}
