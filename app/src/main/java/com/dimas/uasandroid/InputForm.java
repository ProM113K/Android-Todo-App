package com.dimas.uasandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InputForm extends AppCompatActivity {

    AlertDialog.Builder builderDialog;
    AlertDialog alertDialog;

    DatabaseHelper dbhelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_form);
        setTitle("Input Form");

        EditText etTitle = findViewById(R.id.etTitle);
        EditText etBody = findViewById(R.id.etBody);
        Button btnSbmtData = findViewById(R.id.btn_submit_data);

        dbhelp = new DatabaseHelper(this);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnSbmtData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbhelp.insertData(etTitle.getText().toString(), etBody.getText().toString());
                showAlertDialog(R.layout.custom_alert_input);
                etTitle.getText().clear();
                etBody.getText().clear();
            }
        });
    }

    private void showAlertDialog(int myLayout) {
        builderDialog = new AlertDialog.Builder(this);
        View layoutview = getLayoutInflater().inflate(myLayout, null);

        TextView okButton = layoutview.findViewById(R.id.tv_OK);
        builderDialog.setView(layoutview);
        alertDialog = builderDialog.create();
        alertDialog.show();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}