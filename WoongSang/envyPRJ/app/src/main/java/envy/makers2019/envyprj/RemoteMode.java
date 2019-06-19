package envy.makers2019.envyprj;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RemoteMode extends AppCompatActivity {
    BasicControl basicControlFragment = new BasicControl();
    PenMode penModeFragment = new PenMode();

    // Top 3 Buttons (MainMenu , F5 , ESC) Click Events
    private Button.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.F5:
                    Toast.makeText(getApplicationContext(), "F5 Button", Toast.LENGTH_LONG).show();
                    break;

                case R.id.ESC:
                    Toast.makeText(getApplicationContext(), "ESC Button", Toast.LENGTH_LONG).show();
                    break;

                case R.id.back:
                    Intent intent = new Intent();
                    intent.putExtra("name", "envy");
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
            }
        }
    };

    //Bottom menu items click event
    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.basic:
                    //Call Prev,Next Buttons
                    getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, basicControlFragment).commit();
                    Toast.makeText(getApplicationContext(), "Ctrl A Basic Mode", Toast.LENGTH_LONG).show();
                    return true;
                case R.id.pointer:
                    //Call TouchPad
                    Toast.makeText(getApplicationContext(), "Ctrl P to Pointer Mode", Toast.LENGTH_LONG).show();
                    return true;
                case R.id.pen:
                    //Call TouchPad with additional Button
                    getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, penModeFragment).commit();
                    Toast.makeText(getApplicationContext(), "Switched to Pen Mode", Toast.LENGTH_LONG).show();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_mode);

        //Call BasicControlFragment, when the First proccess
        getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, basicControlFragment).commit();

        findViewById(R.id.F5).setOnClickListener(btnClickListener);
        findViewById(R.id.ESC).setOnClickListener(btnClickListener);
        findViewById(R.id.back).setOnClickListener(btnClickListener);

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigator);
        navigationView.setOnNavigationItemSelectedListener(navigationListener);

        //Immersive mode (initialization)
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

   //Immersive mode (Focus Changed)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    //Events for system(HW) buttons  : Back , Volume Up , Volume down
    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                Toast.makeText(this, "Volume Down, Go to prev slide", Toast.LENGTH_LONG).show();
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                Toast.makeText(this, "Volume Up, Go to NextPage", Toast.LENGTH_LONG).show();
                break;
            case KeyEvent.KEYCODE_BACK:
                Toast.makeText(this, "You can't use Back button here. Use 'EXIT' button instead", Toast.LENGTH_LONG).show();
                break;
            default:
                return false;
        }
        return true;
    }
*/
}
