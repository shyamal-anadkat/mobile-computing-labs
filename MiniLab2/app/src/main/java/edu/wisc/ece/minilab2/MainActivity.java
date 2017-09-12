package edu.wisc.ece.minilab2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    private static Stopwatch timer = new Stopwatch();
    private static final String TAG = "MainActivity";
    private static Thread t_thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button start_btn  = (Button) findViewById(R.id. button);
        final Button pause_btn  = (Button) findViewById(R.id. button2);
        final Button rst_btn    = (Button) findViewById(R.id. button3);
        final TextView txt_view = (TextView) findViewById(R.id. textView2);

        // Start button handler
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "### start button pressed ###");

                // Start timer
                timer.start();

                // non-UI thread for real-time display
                t_thread = new Thread(new Runnable() {
                    public void run() {
                        while(true) {
                            if(timer.isRunning()) {
                                try {
                                    txt_view.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            txt_view.setText(timer.getElapsed());
                                        }
                                    });
                                    Thread.sleep(10); //every 10 ms
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
                // Start the thread
                t_thread.start();
            }
        });

        // Pause button handler
        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "### pause button pressed ###");
                if(!timer.isPaused()) {
                    timer.pause();
                } else {    //resume the timer
                    timer.resume();
                }
            }
        });

        // Reset button handler
        rst_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_view.setText("0:0");
                Log.v(TAG, "### reset button pressed ###");
                timer.stop();
                timer = new Stopwatch();
            }
        });
    }
}