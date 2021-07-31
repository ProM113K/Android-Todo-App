package com.dimas.uasandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UpdateActivity extends AppCompatActivity {
    EditText etTitleUpdate, etBodyUpdate;
    Button btnUpdate;
    Model model;
    DatabaseHelper dbhelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

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
                finish();
            }
        });
    }
}