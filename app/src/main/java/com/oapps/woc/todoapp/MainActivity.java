package com.oapps.woc.todoapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oapps.woc.todoapp.DB.CountData;
import com.oapps.woc.todoapp.DB.ToDoRepository;
import com.oapps.woc.todoapp.UI.MainAdapter;
import com.oapps.woc.todoapp.UI.TextWithDrawableData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        Date d = Utils.getCalenderDayForDate(c.getTime());
        ToDoRepository repo = new ToDoRepository(getApplication());
        LiveData<CountData> countsOfTasks = repo.getTasksCounts(d.getTime());
        countsOfTasks.observe(this, counts -> {
            listElements.get(0).countPrimary = counts.tasks_today;
            listElements.get(1).countPrimary = counts.starred_tasks;
            listElements.get(2).countPrimary = counts.pending_tasks;
            listElements.get(3).countPrimary = counts.all_tasks;
            mainAdapter.notifyDataSetChanged();
        });
    }

    void initializeMainList(){
        listElements.add(new TextWithDrawableData(TasksActivity.TODAY, R.drawable.ic_sun_24dp, R.color.today_icon));
//        listElements.add(new TextWithDrawableData("Work", R.drawable.ic_work_24dp));
        listElements.add(new TextWithDrawableData(TasksActivity.IMPORTANT, R.drawable.ic_star_24dp, R.color.star_yellow));
        listElements.add(new TextWithDrawableData(TasksActivity.PENDING, R.drawable.ic_assignment_late_24dp, R.color.pending_icon));
        listElements.add(new TextWithDrawableData(TasksActivity.ALL_TASKS, R.drawable.ic_all_tasks_24dp, R.color.all_tasks_icon));
//        listElements.add(new TextWithDrawableData("Snoozed", R.drawable.ic_snooze_24dp));
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
