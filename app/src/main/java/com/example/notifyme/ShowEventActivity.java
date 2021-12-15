package com.example.notifyme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ShowEventActivity extends AppCompatActivity {

    //create a list to hold events
    List<Event> eventList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        //get the text view that will show the date
        TextView textView = findViewById(R.id.textView2);

        //add back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //grab the intent sent from main activity
        Intent intent = getIntent();

        //set the text to show the date
        textView.setText("Events for " + intent.getStringExtra("date"));

        //get the event string into the array
        String[] temp = intent.getStringArrayExtra("eventList");

        //get the time array into an array
        String[] timeTemp = intent.getStringArrayExtra("timeList");

        //we want to now create a list of events
//        for (String s : temp) {
//            Event e = new Event(s, intent.getStringExtra("date"));
//            eventList.add(e);
//        }
        for (int i = 0; i < temp.length; i++) {
            Event e = new Event(temp[i], intent.getStringExtra("date"), timeTemp[i]);
            eventList.add(e);
        }

        //get the recycler view
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        //set the layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //set up the adapter
        CustomAdapter adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);
    }

    //create the custom adapter
    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            TextView myText1;

            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);
                //get the text view
                myText1 = itemView.findViewById(R.id.myText1);

                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            public void updateView(Event e) {
                //set the text for the cards
                myText1.setText(e.getTitle());
            }

            @Override
            public void onClick(View view) {

            }

            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        }

        @NonNull
        @Override
        public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ShowEventActivity.this)
                    .inflate(R.layout.card_view_list_item, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
            Event e = eventList.get(position);
            holder.updateView(e);
        }

        @Override
        public int getItemCount() {
            return eventList.size();
        }
    }

    //function to deal with selected buttons in the menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //switch statement to execute correct code based on which item selected
        switch (item.getItemId()) {
            //this is the back button to go back to main
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
