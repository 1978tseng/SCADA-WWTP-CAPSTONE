package com.example.nicholas.wwtpApplication;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class manualActivity extends ActionBarActivity {

    private static final int ON =1;
    private static final int OFF =0;
//    private static final int PUMP = 4;
//    private static final int BASE =5;
//    private static final int ACID =6;
//    private static final int SHOTOFF =7;
    private static final int BALANCE = 1;
//    private static final int PHCAL = 18;
//    private static final int ORPCAL = 19;
//    private static final int DOCAL = 20;


    private Switch baseSW;
    private TextView base;
    private Switch acidSW;
    private TextView acid;
    private Switch solSW;
    private TextView sol;
    private Switch pumpSW;
    private TextView pump;

    private Button phBal;
    private Button orpBal;
    private Button doBal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        baseSW = (Switch) findViewById(R.id.baseSW);
        base = (TextView) findViewById(R.id.base);
        acidSW = (Switch) findViewById(R.id.acidSW);
        acid = (TextView) findViewById(R.id.acid);
        solSW = (Switch) findViewById(R.id.solSW);
        sol = (TextView) findViewById(R.id.sol);
        pumpSW = (Switch) findViewById(R.id.pumpSW);
        pump = (TextView) findViewById(R.id.pump);

        baseSW.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked){
                   MainActivity.write(ON,Constant.BASE);
                    base.setText("ON");
                }else{
                    MainActivity.write(OFF,Constant.BASE);
                    base.setText("OFF");
                }
            }
        });

        acidSW.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    MainActivity.write(ON,Constant.ACID);
                    acid.setText("ON");
                } else {
                    MainActivity.write(OFF, Constant.ACID);
                    acid.setText("OFF");
                }
            }
        });

        solSW.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    MainActivity.write(ON, Constant.VALVE);
                    sol.setText("ON");
                } else {
                    MainActivity.write(OFF, Constant.VALVE);
                    sol.setText("OFF");
                }
            }
        });

        pumpSW.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    MainActivity.write(ON, Constant.PUMP);
                    pump.setText("ON");
                } else {
                    MainActivity.write(OFF, Constant.PUMP);
                    pump.setText("OFF");
                }
            }
        });

        phBal = (Button) findViewById(R.id.phBal);

        phBal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.write(ON, Constant.PHCAL);
            }
        });

        orpBal = (Button) findViewById(R.id.orpBal);
        orpBal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.write(ON, Constant.ORPCAL);
            }
        });

        doBal = (Button) findViewById(R.id.doBal);
        doBal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.write(ON, Constant.DOCAL);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manual, menu);
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
