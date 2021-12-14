package com.example.notifyme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateEventActivity extends AppCompatActivity {

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

        //add the event listener to the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new intent
                Intent intent = new Intent();
                //add the event description to the intent
                intent.putExtra("event", eventInputText.getText().toString());
                //set the result for the activity
                CreateEventActivity.this.setResult(Activity.RESULT_OK, intent);
                //go back to main activity
                CreateEventActivity.this.finish();
            }
        });

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