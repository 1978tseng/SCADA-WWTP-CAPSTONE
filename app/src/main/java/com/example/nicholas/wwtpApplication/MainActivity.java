    package com.example.nicholas.wwtpApplication;

    import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//For intent


    public class MainActivity extends ActionBarActivity {

        public static final int CONNECTED = 1;
        public static final int DISCONNECTED = 2;
        private static final int WRITE = 1;
        private static final int READ = 2;

        private Button testBtn;
        private Button guiBtn;
        private EditText value;
        private TextView status;
        public static String debugString ="" ;
        private static TextView debug;
        public static PLCReadingControler prc;
        public static PLCWritingControler pwc;
        public static ArduinoReadingControler arc;



        private static Handler mainHandler = new Handler();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            prc = new PLCReadingControler();
            prc.start();
            pwc = new PLCWritingControler();
            pwc.start();
            arc = new ArduinoReadingControler();
            arc.start();

        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
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

        public void creditScreen(View view) {
            Intent CreditIntent = new Intent(this, CreditActivity.class);
            startActivity(CreditIntent);
        }

        public void informationScreen(View view) {
            Intent informationIntent = new Intent(this, InformationActivity.class);
            startActivity(informationIntent);
        }

        public void manAutoScreen(View view) {
            Intent manAutoIntent = new Intent(this, manAutoActivity.class);
            startActivity(manAutoIntent);
        }


        public static void write(final int value,final int address) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    pwc.requestWriting(value, address);
                }
            });
        }
    }