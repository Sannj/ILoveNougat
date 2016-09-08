package com.example.sbadam2.pricechecker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showResults(View view) {
        EditText editText = (EditText) findViewById(R.id.searchBox);
        String searchTerm = editText.getText().toString();
        if (searchTerm.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter the search term", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, ProductResults.class);
            intent.putExtra("SEARCH_TERM", searchTerm);
            startActivity(intent);
        }
    }
}
