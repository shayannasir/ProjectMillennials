package com.shayannasir.projectm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

public class RequestsActivity extends AppCompatActivity {

//    private View RequestsActivityView;
    private RecyclerView myRequestsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        InitializeFields();
    }

    private void InitializeFields() {

        myRequestsList = (RecyclerView)findViewById(R.id.chat_requests_list);
        myRequestsList.setLayoutManager(new LinearLayoutManager(this));

    }
}
