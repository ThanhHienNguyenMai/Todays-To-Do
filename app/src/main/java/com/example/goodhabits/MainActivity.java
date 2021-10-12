package com.example.goodhabits;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<ExampleItem> mExampleList;

    private ArrayList<IndividualPage> listOfPages = new ArrayList<IndividualPage>();

    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createExampleList();
        buildRecyclerView();


//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);



        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                insertItem(position);
            }
        });
    }

    public void insertItem(int position){
        position = mExampleList.size();
        mExampleList.add(position, new ExampleItem(R.drawable.ic_android, "New Item At Position " + position, "This is Line 2"));

        IndividualPage newPage =  new IndividualPage();

        listOfPages.add(newPage);
        System.out.println(listOfPages.size());

        mAdapter.notifyItemInserted(position);
    }

    public void openNewHabit(){
//        Intent intent = new Intent(this, AddingHabits.class);
//        startActivity(intent);

//        LinearLayout buttonContainer = (LinearLayout) findViewById(R.id.buttonContainer);
//        Button button = new Button(this);
//
//
//        buttonContainer.addView(button);
    }

    public void createExampleList(){
        mExampleList = new ArrayList<>();
        mExampleList.add(new ExampleItem(R.drawable.image2vector, "Line 1", "Line 2"));
        mExampleList.add(new ExampleItem(R.drawable.ic_android, "Line 3", "Line 4"));
    }

    public  void  buildRecyclerView(){
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

}