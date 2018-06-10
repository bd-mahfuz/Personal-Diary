package com.example.mahfuz.personaldiary;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateEventActivity extends AppCompatActivity {

    private EditText titleEd, dateEd, descriptionEd;
    private TextView titleErrorTv, dateErrorTv, descriptionErrorTv;
    private boolean isValidTitle, isValidDate, isValidDescription;
    int id;
    private EventDataSource eventDataSource = new EventDataSource(this);

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);

        titleEd = findViewById(R.id.uTitleEd);
        dateEd = findViewById(R.id.uEventDateEd);
        descriptionEd = findViewById(R.id.uEventDescriptionEd);

        titleErrorTv = findViewById(R.id.uTitleErrorTv);
        dateErrorTv = findViewById(R.id.uDateErrorTv);
        descriptionErrorTv = findViewById(R.id.uDescriptionErrorTv);

        Intent intent = getIntent();
        Event event = (Event) intent.getSerializableExtra("event");
        id = event.getId();
        titleEd.setText(event.getTitle());
        dateEd.setText(event.getDate());
        descriptionEd.setText(event.getDescription());

        dateEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                dateEd.setText(day+"/"+month+"/"+year);
                            }
                        },2018, 4, 27);
                datePickerDialog.show();
            }
        });

        // validating title
        if (!(titleEd.getText().toString().isEmpty())) {
            isValidTitle = true;
            titleEd.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,
                    R.drawable.ic_check_black_24dp, 0);
        }

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
        if (!(dateEd.getText().toString().isEmpty())) {
            isValidDate = true;
            dateEd.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,
                    R.drawable.ic_check_black_24dp, 0);
        }
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
        if (!(descriptionEd.getText().toString().isEmpty())) {
            isValidDescription = true;
            descriptionEd.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,
                    R.drawable.ic_check_black_24dp, 0);
        }
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

    public void updateEventAction(View view) {
        if (isValidDate && isValidDescription && isValidTitle) {
            Event event = new Event();
            event.setId(id);
            event.setTitle(titleEd.getText().toString());
            event.setDate(dateEd.getText().toString());
            event.setDescription(descriptionEd.getText().toString());

            boolean isUpdated = eventDataSource.update(event);
            if (isUpdated) {
                Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateEventActivity.this, EventListActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Update Failed!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Validation Failed!", Toast.LENGTH_SHORT).show();
        }

    }
}
