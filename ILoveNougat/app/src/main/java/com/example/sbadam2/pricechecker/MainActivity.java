package com.example.sbadam2.pricechecker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v(TAG,"Main Activity is triggered");
    }


    public void showResults(View view) {
        editText = (EditText) findViewById(R.id.searchBox);
        String searchTerm = editText.getText().toString();
        Log.v(TAG,"The search term is "+searchTerm);
        Log.v(TAG,"Submit button is clicked.");
        if (searchTerm.isEmpty()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string
                    .toast_string_main_activity), Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, ProductResults.class);
            // make explicit which key holds the value
            intent.putExtra("SEARCH_TERM", searchTerm);
            startActivity(intent);
        }
    }
}
