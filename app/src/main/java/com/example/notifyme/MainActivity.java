package com.example.notifyme;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //ActivityResultLauncher
    ActivityResultLauncher<Intent> launcher;
    //variable to held the date that was selected by the user from the calendar
    String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //"Selected Date" text view
        TextView textView = findViewById(R.id.dateTextView);

        //calendar view
        CalendarView calendarView = findViewById(R.id.calendarView);

        //text view that shows the date that was selected
        TextView actualDateText = findViewById(R.id.actualDateText);

        //button to see the events in a recycler view
        Button seeEventsButton = findViewById(R.id.seeEventsButton);

        //button to create a new event
        Button createEventButton = findViewById(R.id.createEventButton);

        //create a hash map to store the month names
        HashMap<Integer, String> monthMap = createMonthMap();

        //create a hash map the holds the events based on the date
        //HashMap<String, List<Event>> eventMap = new HashMap<>();
        HashMap<String, List<Event>> eventMap = createInitialData();

        //this is the calendar view listener that helps us get the date selected by user
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                //create the data string to look nice
                selectedDate = monthMap.get(i1) + " " + i2 + ", " + i;
                //set the text for the user to see what date they have selected
                actualDateText.setText(selectedDate);
                //enable the buttons for the user to do something
                seeEventsButton.setEnabled(true);
                createEventButton.setEnabled(true);
            }
        });

        //add an event listener for the create event button
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new intent
                Intent intent = new Intent(MainActivity.this, CreateEventActivity.class);
                //go to the CreateEventActivity class
                launcher.launch(intent);
            }
        });

        //add an event listener for the see events button
        seeEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new intent
                Intent intent;
                intent = new Intent(MainActivity.this, ShowEventActivity.class);
                //put the selected date in the intent
                intent.putExtra("date", selectedDate);
                //create a temporary list
                List<Event> temp;
                //create arrays of strings to hold the descriptions and times
                String[] itemsArray;
                String[] timeArray;
                //check to see of there is a list for the selected date
                if (eventMap.containsKey(selectedDate)) {
                    //grab the list of events
                    temp = eventMap.get(selectedDate);
                    //make the size of the array the size of the list
                    itemsArray = new String[temp.size()];
                    timeArray = new String [temp.size()];
                    //counter
                    int i = 0;
                    //for each event in the list, copy the description to the array
                    for (Event e : temp) {
                        itemsArray[i] = e.getTitle();
                        timeArray[i] = e.getTime();
                        i++;
                    }
                }
                //if there is no list, make the array empty
                else {
                    itemsArray = new String[0];
                    timeArray = new String[0];
                }
                //add the array to the intent
                intent.putExtra("eventList", itemsArray);
                intent.putExtra("timeList", timeArray);
                launcher.launch(intent);
            }
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Log.d("hello", "hello");
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            //intent to hold data from user in CreateEventActivity
                            Intent data = result.getData();
                            //string to get the event data
                            String event = data.getStringExtra("event");
                            //string for the time
                            String time = data.getStringExtra("time");
                            //create a new event object
                            Event newEvent = new Event(event, selectedDate, time);
                            //check to see if that map already holds events for the selected date,
                            //if not, we need to do work to add the key to the map
                            //this if statement takes care if the data is already on map, then we jsut add the event
                            if (eventMap.get(selectedDate) != null) {
                                //grab the list from the map for the selected date
                                List<Event> temp = eventMap.get(selectedDate);
                                //add the event to the list
                                temp.add(newEvent);
                                //update the eventMap
                                eventMap.put(selectedDate, temp);
                            }
                            //in this case, there are no events for the selected date, which means there is no list,
                            //so we will get a null object reference error if we try to grab the list
                            else {
                                //create a new list
                                List<Event> newList = new ArrayList<>();
                                //add the event to the new list
                                newList.add(newEvent);
                                //add the key value pair to the eventMap
                                eventMap.put(selectedDate, newList);
                            }
                        }
                    }
                });
    }

    //return a hash map that has all the month names
    public HashMap<Integer, String> createMonthMap() {
        //create new hash map
        HashMap<Integer, String> monthMap = new HashMap<>();

        //fill the hash map with the names and the correct month number
        monthMap.put(0, "January");
        monthMap.put(1, "February");
        monthMap.put(2, "March");
        monthMap.put(3, "April");
        monthMap.put(4, "May");
        monthMap.put(5, "June");
        monthMap.put(6, "July");
        monthMap.put(7, "August");
        monthMap.put(8, "September");
        monthMap.put(9, "October");
        monthMap.put(10, "November");
        monthMap.put(11, "December");

        //return the complete hashmap
        return monthMap;
    }

    //create initial events for demo purposes
    public HashMap<String, List<Event>> createInitialData() {
        HashMap <String, List<Event>> tempMap = new HashMap<>();
        List<Event> tempList = new ArrayList<>();

        Event e4 = new Event("Senior Design Meeting", "December 15, 2021", "10 : 00 AM");
        Event e10 = new Event("Clean House", "December 15, 2021", "11 : 00 AM");
        Event e1 = new Event("Lunch", "December 15, 2021", "12 : 00 PM");
        Event e11 = new Event("Study for Exams", "December 15, 2021", "5 : 30 PM");
        Event e12 = new Event("Meet with math group", "December 15, 2021", "2 : 00 PM");
        Event e2 = new Event("Math Exam", "December 15, 2021", "3 : 30 PM");
        Event e13 = new Event("Dinner", "December 15, 2021", "6 : 30 PM");
        Event e14 = new Event("Get groceries", "December 15, 2021", "7 : 30 PM");
        Event e5 = new Event("Android Presentation", "December 15, 2021", "1 : 00 PM");
        Event e3 = new Event("Finish English Essay", "December 15, 2021", "11 : 59 PM");

        tempList.add(e4);
        tempList.add(e10);
        tempList.add(e1);
        tempList.add(e5);
        tempList.add(e12);
        tempList.add(e2);
        tempList.add(e11);
        tempList.add(e13);
        tempList.add(e14);
        tempList.add(e3);

        tempMap.put("December 15, 2021", tempList);

        Event e6 = new Event("CS Exam", "December 16, 2021", "8 : 00 AM");
        Event e7 = new Event("Advisor Meeting", "December 16, 2021", "1 : 00 PM");
        Event e8 = new Event("Basketball Game", "December 16, 2021", "6 : 00 PM");

        List<Event> tempList2 = new ArrayList<>();

        tempList2.add(e6);
        tempList2.add(e7);
        tempList2.add(e8);

        tempMap.put("December 16, 2021", tempList2);
        return tempMap;
    }
}