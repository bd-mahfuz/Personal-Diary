package com.example.mahfuz.personaldiary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText passwordEd;
    Button unLockBtn;
    TextView infoTitleTv, infoDesTv;

    UserDataSource userDataSource = new UserDataSource(this);
    List<User> users;

    private boolean loginStatus;
    //private boolean firstTimeLoggedIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        passwordEd = findViewById(R.id.passwordEd);
        unLockBtn = findViewById(R.id.unLockButton);
        infoDesTv = findViewById(R.id.infoDesTV);
        infoTitleTv = findViewById(R.id.infoTitleTV);
        User user = new User();
        user.setPassword("1234");
        user.setLoggedIn(false);

        String quoteList[] = getResources().getStringArray(R.array.quotesList);
        String quoteOwner[] = getResources().getStringArray(R.array.quotesOwner);

        //checking for data is already exist or not
        users = userDataSource.getAll();
        if (users.size() == 0) {
            boolean isSaved = userDataSource.save(user);
            if (isSaved) {
                Toast.makeText(this, "data saved and data is ",
                        Toast.LENGTH_SHORT).show();

                //Log.d("login status", logins.get(0).isLoggedIn()+"");
            }else {
                Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
            }

        } else {
            if (users.get(0).isLoggedIn() == true) {
                int randomValue = DateUtility.getRandomNumber(0, quoteList.length);
                infoTitleTv.setText("Popular Quote about Life -");
                infoDesTv.setText("\""+quoteList[randomValue]+"\"  --"+quoteOwner[randomValue]);
            }
        }

    }

    public void unlockAction(View view) {
        List<User> users = userDataSource.getAll();
        if (users.size() == 1) {
            boolean isUpdated = userDataSource.update(1, 1);
        }
        String userPassword = passwordEd.getText().toString();
        //Log.i("password from data ase ", users.get(0).getPassword());
        if (userPassword.equals(users.get(0).getPassword())) {
            loginStatus = true;
            Intent intent = new Intent(MainActivity.this, EventListActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Pin Code is Not Matched! Try Again!",
                    Toast.LENGTH_SHORT).show();
        }

    }
}
