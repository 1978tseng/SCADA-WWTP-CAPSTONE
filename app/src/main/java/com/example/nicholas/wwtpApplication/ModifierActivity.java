package com.example.nicholas.wwtpApplication;

        import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

public class ModifierActivity extends ActionBarActivity {

    private final int PUMP_ADDRESS = 0;
    private final int ACID_ADDRESS = 1;
    private final int BASE_ADDRESS = 2;
    private final int SHOTOFF_ADDRESS = 3;

    private final int TURN_ON = 1;
    private final int TURN_OFF = 0;

    private static TextView Register0;
    private static TextView Register1;
    private static TextView Register2;
    private static TextView Register3;

    private Switch switch0;
    private Switch switch1;
    private Switch switch2;
    private Switch switch3;


    private Button refreshBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modifier, menu);
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

