package edu.wisc.ece.ece454minilab5;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ScrollViewLayoutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scroll_view_layout);


        Toast.makeText(getApplicationContext(),
                "ScrollView is here",
                Toast.LENGTH_LONG).show();

    }

    public void onGoBackClicked(View v) {

        Intent intent = new Intent(ScrollViewLayoutActivity.this,
                MainLayoutActivity.class);
        startActivity(intent);
    }

    public void onSaveClicked(View v) {

        Toast.makeText(getApplicationContext(),
                getString(R.string.save_message), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ScrollViewLayoutActivity.this,
                MainLayoutActivity.class);
        startActivity(intent);
    }

    public void onCancelClicked(View v) {

        Toast.makeText(getApplicationContext(),
                getString(R.string.cancel_message), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ScrollViewLayoutActivity.this,
                MainLayoutActivity.class);
        startActivity(intent);
    }

}
