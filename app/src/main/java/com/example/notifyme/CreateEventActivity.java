package com.example.notifyme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class CreateEventActivity extends AppCompatActivity {
    int notificationID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        //add back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //edit text to get the event input from the user
        EditText eventInputText = findViewById(R.id.eventInputText);

        //button the save the event
        Button saveButton = findViewById(R.id.saveButton);

        //edit text for the hour inserted by user
        EditText hourEditText = findViewById(R.id.hourEditText);

        //edit text for the minute inserted by the user
        EditText minuteEditText = findViewById(R.id.minuteEditText);

        //spinner that lets the user select am or pm
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinnerValues, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //createNotificationChannel();

        //add the event listener to the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new intent
                Intent intent = new Intent();
                //add the event description to the intent
                intent.putExtra("event", eventInputText.getText().toString());
                //add the time to the intent
                String time = hourEditText.getText().toString() + " : " +
                        minuteEditText.getText().toString() + "  " + spinner.getSelectedItem().toString();
                intent.putExtra("time", time);
                CreateEventActivity.this.setResult(Activity.RESULT_OK, intent);
                //go back to main activity
                CreateEventActivity.this.finish();

                //remaining code for setting up notifications
                createNotificationChannel();
                NotificationCompat.Builder builder = new NotificationCompat.Builder(CreateEventActivity.this, "CHANNEL_ID")
                    .setSmallIcon(R.drawable.calendar_image)
                    .setContentTitle("NEW EVENT ADDED!")
                    .setContentText(eventInputText.getText().toString())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(CreateEventActivity.this);

                notificationManager.notify(notificationID, builder.build());
                notificationID++;
            }
        });

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "final project";
            String description = "final project";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
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