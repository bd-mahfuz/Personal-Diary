package com.example.mahfuz.personaldiary;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddEventActivity extends AppCompatActivity {

    private EditText titleEd, dateEd, descriptionEd;
    private TextView titleErrorTv, dateErrorTv, descriptionErrorTv;
    EventDataSource eventDataSource = new EventDataSource(this);
    private boolean isValidTitle, isValidDate, isValidDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        titleEd = findViewById(R.id.titleEd);
        dateEd = findViewById(R.id.eventDateEd);
        descriptionEd = findViewById(R.id.eventDescriptionEd);
        titleErrorTv = findViewById(R.id.titleErrorTv);
        dateErrorTv = findViewById(R.id.dateErrorTv);
        descriptionErrorTv = findViewById(R.id.descriptionErrorTv);

        dateEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                dateEd.setText(day+"/"+(month+1)+"/"+year);
                            }
                        },DateUtility.getCurrentYear(), DateUtility.getCurrentMonth(),
                                DateUtility.getCurrentDay());
                datePickerDialog.show();
            }
        });

        // validating title
        titleEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() <= 2) {
                    isValidTitle = false;
                    titleErrorTv.setText("Not valid! Title should be more than 3 character!");
                    titleEd.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_title_black_24dp,0,
                            0, 0);
                    titleErrorTv.setVisibility(View.VISIBLE);
                    //Log.i("title status ", String.valueOf(validTitle));

                } else {
                    isValidTitle = true;
                    titleErrorTv.setVisibility(View.GONE);
                    titleEd.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_title_black_24dp,0,
                            R.drawable.ic_check_black_24dp, 0);
                    //Log.i("title status ", String.valueOf(validTitle));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // validating date
        dateEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Validator.isThisDateValid(charSequence.toString(), "dd/MM/yyyy")) {
                    isValidDate = true;
                    dateErrorTv.setVisibility(View.GONE);
                    dateEd.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_date_range_black_24dp,0,
                            R.drawable.ic_check_black_24dp, 0);
                } else {
                    isValidDate = false;
                    dateErrorTv.setText("Date is not valid! Valid format is dd/MM/yyyy !");
                    dateEd.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_date_range_black_24dp,0,
                            0, 0);
                    dateErrorTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //validating description
        descriptionEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() < 3) {
                    isValidDescription = false;
                    descriptionErrorTv.setText("Not valid! Story should not be less than 3 character!");
                    descriptionEd.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,
                            0, 0);
                    descriptionErrorTv.setVisibility(View.VISIBLE);
                } else {
                    isValidDescription = true;
                    descriptionEd.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,
                            R.drawable.ic_check_black_24dp, 0);
                    descriptionErrorTv.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



    }

    public void addEventAction(View view) {
        String title = titleEd.getText().toString();
        String date = dateEd.getText().toString();
        String description = descriptionEd.getText().toString();

        if (isValidTitle && isValidDate && isValidDescription) {
            Event event = new Event();
            event.setTitle(title);
            event.setDate(date);
            event.setDescription(description);

            //saving event to database
            boolean isSaved = eventDataSource.save(event);
            if (isSaved) {
                Toast.makeText(this, "New Story is Saved!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddEventActivity.this, EventListActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Story is not Saved!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Validation failed !", Toast.LENGTH_LONG).show();
        }


    }



}
