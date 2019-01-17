package com.mp.rena.craftersnote;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    static BottomNavigationView bottomNavigation;
    private FragmentManager fragmentManager;
    private String userName="";
    private String serverName="";
    TextView profileText;
    ImageView profilePicImageView;
    static SharedPreferences sharedPreferences;
    static DatabaseHelper db;
    private final String appkey = "3e1f649be24c4f8bb33b9a42";
    String [] nameSplited;
    String lastEdited;


    public class DownloadProfilePicture extends AsyncTask <String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection= null;
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                String result = "";
                int data = reader.read();
                while (data != -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                JSONObject profileJsonAll = new JSONObject(result);
                String resultString = profileJsonAll.getString("Results");
                JSONArray arr = new JSONArray(resultString);
                JSONObject profileJson = arr.getJSONObject(0);
                String imageURL = profileJson.getString("Avatar");

                return imageURL;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

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
            // delete all items from the list
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
        profileText.setText ("Hello!");

        profilePicImageView = findViewById(R.id.profilePicture);
        if (isProfileComplete()){
            downloadProfilePic();
        } else{
            setProfile();
        }

        // initial setting for bottom Navigation. inflate using navigation xml
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.inflateMenu(R.menu.bottom_menu);

        // initial screen with todaysTask
        fragmentManager = getSupportFragmentManager();
        TodaysTask fragment = new TodaysTask();
        ManageTask manageF = new ManageTask();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, manageF, "1").commit();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment, "0").commit();

        db = new DatabaseHelper(getApplicationContext());

        lastEdited = sharedPreferences.getString("date", "");
        SimpleDateFormat isoFormat = new SimpleDateFormat("MM-dd-yyyy");
        isoFormat.setTimeZone(TimeZone.getDefault());
        String date = isoFormat.format(new Date());

        // if lastEdited date is empty or lastEdited date is not equal to today, we want to reset today's list with everyday task
        if (lastEdited.equals("") || !lastEdited.equals(date)){
           db.deleteAllToday();
           db.copyFromEveryToToday();
        }

        saveSharedPreference();

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

                    case R.id.action_search:
                        SearchItems fragment5 = new SearchItems();
                        transaction.replace(R.id.main_container, fragment5, "4").commit();
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

        // alert box
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
                            downloadProfilePic();
                        } else if (userName.equals("")){
                            profileText.setText ("Hello!");
                            profilePicImageView.setImageBitmap(null);
                        } else if (serverName.equals("") || nameSplited.length != 2){
                            profilePicImageView.setImageBitmap(null);
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public Boolean isProfileComplete(){
        nameSplited = userName.split("\\s+");
        if (userName.equals("") || serverName.equals("") || nameSplited.length != 2){
            return false;
        }
        return true;
    }

    public void downloadProfilePic(){
        profileText.setText ("Hello, " + userName + "!");
        DownloadProfilePicture dpp = new DownloadProfilePicture();
        try {
            String imageURL = dpp.execute("https://xivapi.com/character/search?name="+ nameSplited[0] + "+" + nameSplited[1] + "&server="+ serverName + "&key=" + appkey).get();
            Glide.with(MainActivity.this)
                    .load(imageURL)
                    .apply(RequestOptions.placeholderOf(new ColorDrawable(Color.BLACK)))
                    .apply(RequestOptions.overrideOf(200, 200))
                    .apply(RequestOptions.circleCropTransform())
                    .into(profilePicImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveSharedPreference(){
        SimpleDateFormat isoFormat = new SimpleDateFormat("MM-dd-yyyy");
        isoFormat.setTimeZone(TimeZone.getDefault());
        String date = isoFormat.format(new Date());

        sharedPreferences.edit().putString("date", date).apply();
    }
}
