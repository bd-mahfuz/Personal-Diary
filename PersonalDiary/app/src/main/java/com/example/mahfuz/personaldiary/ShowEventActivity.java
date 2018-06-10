package com.example.mahfuz.personaldiary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ShowEventActivity extends AppCompatActivity {

    TextView showTitleTv, showDateTv, showDescriptionTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        showTitleTv = findViewById(R.id.showTitleTv);
        showDateTv = findViewById(R.id.showDateTv);
        showDescriptionTv = findViewById(R.id.showDescriptionTv);

        Intent intent = getIntent();
        Event event = (Event) intent.getSerializableExtra("event");
        showTitleTv.setText(event.getTitle());
        showDateTv.setText(event.getDate());
        showDescriptionTv.setText(event.getDescription());

    }

    public void backAction(View view) {
        Intent intent = new Intent(ShowEventActivity.this, EventListActivity.class);
        startActivity(intent);
    }
}
