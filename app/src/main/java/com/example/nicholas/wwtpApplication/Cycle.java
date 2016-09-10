package com.example.nicholas.wwtpApplication;

/**
 * Created by Chun-Wei Tseng on 2015/6/11.
 */

import android.util.Log;

/**
 * Created by Chun-Wei Tseng on 2015/6/11.
 */
public class Cycle extends Thread {

    private static final String DEBUGTAG = "Cycle";
    private static final int PUMP = Constant.PUMP;
    private static final int VAVLE = Constant.VALVE;
    private long delay = 100;
    private int TYPE = 0;
    private int tries = 0;
    private static final int ON = 1;
    private static final int OFF = 0;

    public Cycle(int type, int times) {
        this.TYPE = type;
        this.tries = times;
    }

    @Override
    public void run() {

        try {
            writeLog("Cycle starts ...");
            AutomaticActivity.append("Automation start ");
            Thread.sleep(1000);
            AutomaticActivity.append("Pumping Water... ");
            MainActivity.write(ON, PUMP);
            Thread.sleep(29000);
            MainActivity.write(OFF, PUMP);
            AutomaticActivity.append("Pumping Finished");
            Thread.sleep(delay);
            AutomaticActivity.append("Filtering Start");
            MainActivity.write(ON, VAVLE);
            Thread.sleep(delay);

            for (int shot = 0 ; shot < tries; shot ++){
                String solType = "";
                if (TYPE == Constant.ACID){
                    solType = "acid";
                } else if (TYPE == Constant.BASE) {
                    solType = "base";
                } else {
                    return;
                }
                AutomaticActivity.append(String.valueOf("Adding " + solType +" :  " + (shot+1)));
                Thread.sleep(delay);
                MainActivity.write(ON, TYPE);
                Thread.sleep(500);
                MainActivity.write(OFF, TYPE);
                Thread.sleep(5000);
            }

            AutomaticActivity.append("Finishing Process... ");
            Thread.sleep(35000 - (5 * tries * 1000));
            MainActivity.write(OFF, VAVLE);
            Thread.sleep(delay);
            MainActivity.write(ON, VAVLE);
            Thread.sleep(35000);
            MainActivity.write(OFF, VAVLE);
            AutomaticActivity.cycleDone();
            writeLog("Cycle done ...");
        }  catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void writeLog(String msg) {
        Log.e(DEBUGTAG, msg);
    }
}




