package com.mp.rena.craftersnote;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private FragmentManager fragmentManager;
    private String userName="";
    private String serverName="";
    TextView profileText;
    SharedPreferences sharedPreferences;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.deleteAll){

        } else if (item.getItemId() == R.id.profileChange){
           setProfile();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set profile text on the top
        sharedPreferences = getSharedPreferences("com.mp.rena.craftersnote", Context.MODE_PRIVATE);
        profileText = findViewById(R.id.profileTextView);
        userName = sharedPreferences.getString("userName", "");
        serverName = sharedPreferences.getString("serverName", "");
        profileText.setText ("Hello, " + userName + "!");

        if (isProfileComplete()){
            //download profile picture
        } else{
            setProfile();
        }

        // initial setting for bottom Navigation. inflate using navigaton xml
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.inflateMenu(R.menu.bottom_menu);

        // initial screen with todaysTask
        fragmentManager = getSupportFragmentManager();
        TodaysTask fragment = new TodaysTask();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment, "0").commit();


        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                final FragmentTransaction transaction = fragmentManager.beginTransaction();

                switch (id){
                    case R.id.action_today:
                        TodaysTask fragment = new TodaysTask();
                        transaction.replace(R.id.main_container, fragment, "0").commit();
                        break;
                    case R.id.action_task:
                        ManageTask fragment2 = new ManageTask();
                        transaction.replace(R.id.main_container, fragment2, "1").commit();
                        break;
                    case R.id.action_gather:
                        ManageGather fragment3 = new ManageGather();
                        transaction.replace(R.id.main_container, fragment3, "2").commit();
                        break;

                    case R.id.action_record:
                        RecordSales fragment4 = new RecordSales();
                        transaction.replace(R.id.main_container, fragment4, "3").commit();
                        break;
                }
                return true;
            }
        });
    }

    public void setProfile(){
        // Dialog box design
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText editName = new EditText(getApplicationContext());
        editName.setHint("Character Name");
        if (!userName.equals("")){
            editName.setText(userName);
        }
        layout.addView(editName);

        final EditText editServer = new EditText(getApplicationContext());
        editServer.setHint("Server Name");
        if (!serverName.equals("")){
            editServer.setText(serverName);
        }
        layout.addView(editServer);

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_edit)
                .setTitle("Profile Setting")
                .setView(layout)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        userName = editName.getText().toString();
                        sharedPreferences.edit().putString("userName", userName).apply();
                        profileText.setText ("Hello, " + userName + "!");

                        serverName = editServer.getText().toString();
                        sharedPreferences.edit().putString("serverName", serverName).apply();

                        if (isProfileComplete()){
                            //download profile picture
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public Boolean isProfileComplete(){
        if (userName.equals("") || serverName.equals("")){
            return false;
        }
        return true;
    }
}
