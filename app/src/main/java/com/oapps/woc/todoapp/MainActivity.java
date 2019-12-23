package com.oapps.woc.todoapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oapps.woc.todoapp.UI.MainAdapter;
import com.oapps.woc.todoapp.UI.TextWithDrawableData;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MainAdapter mainAdapter;
    ArrayList<TextWithDrawableData> listElements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.main_recycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        listElements = new ArrayList<>();
        initializeMainList();
        mainAdapter = new MainAdapter(this, listElements);
        recyclerView.setAdapter(mainAdapter);
    }

    void initializeMainList(){
        listElements.add(new TextWithDrawableData("Today", R.drawable.ic_sun_24dp));
        listElements.add(new TextWithDrawableData("Work", R.drawable.ic_work_24dp));
        listElements.add(new TextWithDrawableData("Home", R.drawable.ic_home_24dp));
        listElements.add(new TextWithDrawableData("Pending", R.drawable.ic_assignment_late_24dp));
        listElements.add(new TextWithDrawableData("Snoozed", R.drawable.ic_snooze_24dp));
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
