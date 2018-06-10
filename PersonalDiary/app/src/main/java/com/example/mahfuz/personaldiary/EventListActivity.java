package com.example.mahfuz.personaldiary;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class EventListActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView eventRv;
    private TextView messageTv;
    private FloatingActionButton addEventBt;

    private EditText previousPassEd, newPassEd;
    private TextView newPasswordErrorTv, prePassErrorTv;

    private EventDataSource eventDataSource = new EventDataSource(this);
    private UserDataSource userDataSource = new UserDataSource(this);
    private List<Event> events;

    private ActionMode mActionMode;
    boolean isNewPassValid, isPrePassValid;

    EventRecyclerViewAdapter eventRecyclerViewAdapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        eventRv = findViewById(R.id.eventRecyclerView);
        messageTv = findViewById(R.id.message);
        addEventBt = findViewById(R.id.addEvent);
        addEventBt.setOnClickListener(this);
        searchView = findViewById(R.id.searchView);
        searchView.setQueryHint("Use date or title");
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                eventRecyclerViewAdapter.getFilter().filter(newText);
                return true;
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        eventRv.setLayoutManager(layoutManager);


        // fetching all event from database
        events = eventDataSource.getAllEvent();
        if (events.size() == 0) {
            messageTv.setVisibility(View.VISIBLE);
        } else {
            eventRecyclerViewAdapter = new EventRecyclerViewAdapter(events);
            eventRv.setAdapter(eventRecyclerViewAdapter);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.lockMenu :
                Toast.makeText(this, "Locked! ",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EventListActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.changePasswordMenu :
                showCustomDialog();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

       /* builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });*/

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.password_changed_layout, null);
        Button cancel, change;
        cancel = v.findViewById(R.id.cancelButton);
        change = v.findViewById(R.id.loginButton);
        previousPassEd = v.findViewById(R.id.previousPassEd);
        newPassEd = v.findViewById(R.id.newPassEd);
        newPasswordErrorTv = v.findViewById(R.id.newPassError);
        prePassErrorTv = v.findViewById(R.id.prePassError);

        previousPassEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password = charSequence.toString();
                if (password.matches("\\d+") && password.length()==4) {
                    isPrePassValid = true;
                    prePassErrorTv.setVisibility(View.GONE);

                }
                else if (password.equals("")){
                    isPrePassValid = false;
                    prePassErrorTv.setText("Password should not be empty!");
                    prePassErrorTv.setVisibility(View.VISIBLE);
                }
                else if (password.length() > 4 || password.length()<4){
                    isPrePassValid = false;
                    prePassErrorTv.setText("Password must be contain only 4 digit");
                    prePassErrorTv.setVisibility(View.VISIBLE);
                }
                else {
                    isPrePassValid = false;
                    prePassErrorTv.setText("All digits are not number! Please enter only number!");
                    prePassErrorTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //validation new password password
        newPassEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password = charSequence.toString();
                if (password.matches("\\d+") && password.length()==4) {
                    isNewPassValid = true;
                    newPasswordErrorTv.setVisibility(View.GONE);
                    newPassEd.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,
                    R.drawable.ic_check_black_24dp, 0);
                }
                else if (password.equals("")){
                    isNewPassValid = false;
                    newPasswordErrorTv.setText("Password should not be empty!");
                    newPasswordErrorTv.setVisibility(View.VISIBLE);
                }
                else if (password.length() > 4 || password.length()<4){
                    isNewPassValid = false;
                    newPasswordErrorTv.setText("Password must be contain only 4 digit!");
                    newPasswordErrorTv.setVisibility(View.VISIBLE);
                }
                else {
                    isNewPassValid = false;
                    newPasswordErrorTv.setText("All digits are not number! Please enter only number!");
                    newPasswordErrorTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        final AlertDialog dialog = builder.create();
        dialog.setTitle("Change Password");
        dialog.setView(v);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                Toast.makeText(EventListActivity.this, "cancel", Toast.LENGTH_SHORT).show();
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNewPassValid && isPrePassValid) {
                    String previousPassword = previousPassEd.getText().toString();
                    String newPassword = newPassEd.getText().toString();

                    String passFromDb = userDataSource.getPasswordFromDB(1);
                    if (previousPassword.equals(passFromDb)) {
                        boolean isUpdated = userDataSource.update(1, newPassword);
                        if (isUpdated) {
                            Toast.makeText(EventListActivity.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(EventListActivity.this, "Update failed!", Toast.LENGTH_SHORT).show();
                        }
                        // if update successful close the alert dialog
                        dialog.dismiss();
                    } else {
                        Toast.makeText(EventListActivity.this, "Previous password does not matched!", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(EventListActivity.this, "Validation failed!", Toast.LENGTH_LONG).show();
                }
            }
        });
        dialog.show();
    }

   /* public void addEventAction(View view) {

    }*/

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(EventListActivity.this, AddEventActivity.class);
        startActivity(intent);
    }
}
