package com.dimas.uasandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {
    EditText etTitleUpdate, etBodyUpdate;
    Button btnUpdate;
    Model model;
    DatabaseHelper dbhelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        etTitleUpdate = (EditText)findViewById(R.id.etTitleUpdate);
        etBodyUpdate = (EditText)findViewById(R.id.etBodyUpdate);
        btnUpdate = (Button)findViewById(R.id.btn_update);
        dbhelp = new DatabaseHelper(this);
        Bundle bundle = getIntent().getExtras();

        if(bundle!=null){
            model = dbhelp.getData(bundle.getInt("ID"));
            etTitleUpdate.setText(model.getTitle());
            etBodyUpdate.setText(model.getBody());
        }
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbhelp.updateData(model.getId(), etTitleUpdate.getText().toString(), etBodyUpdate.getText().toString());
                Toast.makeText(getApplicationContext(), "Data berhasil diubah", Toast.LENGTH_LONG).show();
                finish();
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