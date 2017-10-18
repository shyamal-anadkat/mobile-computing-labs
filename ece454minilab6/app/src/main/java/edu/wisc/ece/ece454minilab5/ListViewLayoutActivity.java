package edu.wisc.ece.ece454minilab5;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.Arrays;

public class ListViewLayoutActivity extends ListActivity {

    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> FOLKS = new ArrayList<String>();
    final String FILENAME = "students.txt";
    private static final String TAG = "Minilab6";
    private EditText editStudent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_custom_layout);

        //load students
        loadStudents();

        editStudent = (EditText) findViewById(R.id.editStudentName);
        mAdapter = new ArrayAdapter<String>(this,
                R.layout.activity_list_view_layout, FOLKS);

        // Assign the adapter to ListView
        setListAdapter(mAdapter);

        // Define the listener interface
        OnItemClickListener mListener = new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText
                        (ListViewLayoutActivity.this,
                                FOLKS.get(position)
                                        + " is removed from the list!", Toast.LENGTH_SHORT).show();
                FOLKS.remove(position);
                try {
                    FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                    fos.write(android.text.TextUtils.join(",", FOLKS).getBytes());
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mAdapter.notifyDataSetChanged();
            }
        };

        // Get the ListView and wired the listener
        ListView listView = getListView();
        listView.setOnItemClickListener(mListener);
    }

    /* Add Student Button Action */
    public void onAddClicked(View v) {
        saveStudent();
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    /* Append Student(string) to the file */
    private void saveStudent() {
        String mValue = editStudent.getText().toString();
        editStudent.getText().clear();
        if (mValue != null && mValue.length() > 0) {
            try {
                FOLKS.add(mValue);
                FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_APPEND);
                if (FOLKS.size() > 1) {
                    fos.write(",".getBytes());
                }
                fos.write(mValue.getBytes());
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* Read from internal storage file and update FOLKS */
    private void loadStudents() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            byte data[] = new byte[fis.available()];
            fis.read(data);
            fis.close();
            String strm = new String(data);
            FOLKS = new ArrayList(Arrays.asList(strm.split(",")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}