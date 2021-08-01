package com.dimas.uasandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Model> list = new ArrayList<>();
    DatabaseHelper dbhelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbhelp = new DatabaseHelper(this);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onResume() {
        super.onResume();
        list = dbhelp.getAll();
        adapter = new ListAdapter(this, list);
        recyclerView.setAdapter(adapter);

        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_action_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_action_add:
                Intent intent = new Intent(this, InputForm.class);
                startActivity(intent);
                break;
            case R.id.menu_logout:
                logOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logOut(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListHolder>{
        Context context;
        List<Model> list;

        public ListAdapter(Context context, List<Model> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data, parent, false);
            return new ListHolder(v);
        }

        @Override
        public void onBindViewHolder(ListHolder holder, int position) {
            final Model model = list.get(position);
            holder.title.setText(model.getTitle());
            holder.body.setText(model.getBody());
            holder.update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                    intent.putExtra("ID", model.getId());
                    startActivity(intent);
                }
            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Yakin Menghapus Data?");
                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dbhelp.deleteData(model.getId());
                            dialogInterface.dismiss();
                            list = dbhelp.getAll();
                            adapter = new ListAdapter(MainActivity.this, list);
                            recyclerView.setAdapter(adapter);
                            notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ListHolder extends RecyclerView.ViewHolder{
            TextView title, body;
            ImageView update, delete;
            public ListHolder(View itemView) {
                super(itemView);
                title = (TextView)itemView.findViewById(R.id.txtTitle);
                body = (TextView)itemView.findViewById(R.id.txtBody);
                update = (ImageView)itemView.findViewById(R.id.ivEdit);
                delete = (ImageView) itemView.findViewById(R.id.ivDelete);
            }
        }
    }
}