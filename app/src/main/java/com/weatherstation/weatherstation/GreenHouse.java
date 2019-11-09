package com.weatherstation.weatherstation;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Bivav on 4/25/2017.
 */

@SuppressWarnings("ALL")
public class GreenHouse extends Fragment {

    private static final String TAG = "DATA_RECEIVED --> " ;
    private Gauge gauge1, gauge2, gauge3;
    ProgressDialog progressDialog;
    String moistureGreen, temperatureGreen, humidityGreen, relayStatusGreen, timeGreen, soilTempGreen;
    TextView temperatureValue, temperatureValueF, moistureValue, humidityValue, relayStatValue,
            timeValue, latestUpdate, dateValue, soilTempValue, soildTempFaren;

    String relayFan, relayLight, relayPump;
    private HashMap hashMap;

    // Get the database
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.green_house, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Fetching Data. Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        temperatureValue = (TextView)view.findViewById(R.id.temperatureValue);
        temperatureValueF = (TextView)view.findViewById(R.id.temperatureValueF);
        moistureValue = (TextView)view.findViewById(R.id.moistureValue);
        humidityValue = (TextView)view.findViewById(R.id.humidityValue);
        relayStatValue = (TextView)view.findViewById(R.id.relayStatValue);
        soilTempValue = (TextView)view.findViewById(R.id.soilTempValue);
        soildTempFaren = view.findViewById(R.id.soildTempFaren);

        timeValue = (TextView)view.findViewById(R.id.timeValue);
        dateValue = (TextView)view.findViewById(R.id.dateValue);

        latestUpdate = (TextView)view.findViewById(R.id.latestUpdate);

        relayFan = "";
        relayLight = "";
        relayPump = "";

        GetDataFromServer getDataFromServer = new GetDataFromServer();
        getDataFromServer.execute();

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private class GetDataFromServer extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            GetData();
            Log.i(TAG, "Data Fetched.." );
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public void GetData() {

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("greenHouseData/greenhouse");
        myRef.keepSynced(true);
        myRef.addValueEventListener(new ValueEventListener() {



            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                long diff;

                try {
                    hashMap = (HashMap)dataSnapshot.getValue();
                    JSONObject object = new JSONObject(hashMap);

                    for (int i = 0; i < object.length(); i++) {
                        moistureGreen = object.getString("moisture");
                        temperatureGreen = object.getString("temperature");
                        humidityGreen = object.getString("humidity");
                        relayStatusGreen = object.getString("relay_status");
                        timeGreen = object.getString("currentTime");
                        soilTempGreen = object.getString("soilTemperature");
                    }

                    if (hashMap != null && isAdded()) {

                        if (temperatureGreen.equals("nan") || temperatureGreen.equals("")) {
                            Log.i("arraylistTrue", "Temperature: " + temperatureGreen);
                            temperatureValue.setText(temperatureGreen);
                            temperatureValueF.setText(temperatureGreen);
                        } else {
                            Log.i("arraylistFalse", "Temperature: " + temperatureGreen);
                            double far = Double.parseDouble(temperatureGreen);
                            far = far * 1.8 + 32;
                            temperatureValue.setText(String.valueOf(Math.round(Float.parseFloat(temperatureGreen))));
                            temperatureValueF.setText(String.valueOf(Math.round(far)));
                        }

                        if (soilTempGreen.equals("nan")|| soilTempGreen.equals("")) {
                            Log.i("arraylistTrue", "Soil Temperature: " + soilTempGreen);
                            soildTempFaren.setText(soilTempGreen);
                            soilTempValue.setText(String.format(Locale.US, "%.4s", soilTempGreen));
                        } else {
                            Log.i("arraylistFalse", "Soil Temperature: " + soilTempGreen);
                            double flo = Double.parseDouble(soilTempGreen);
                            flo = flo * 1.8 + 32;
                            soildTempFaren.setText(String.format(Locale.US, "%.1f", flo));
                            soilTempValue.setText(String.format(Locale.US, "%.4s", soilTempGreen));
                        }

                        if (moistureGreen.equals("nan")||moistureGreen.equals("")) {
                            Log.i("arraylistTrue", "Moisture: " + moistureGreen);
                            moistureValue.setText(moistureGreen + getString(R.string.m));
                        } else {
                            Log.i("arraylistFalse", "Moisture: " + moistureGreen);
                            moistureValue.setText(String.valueOf(Math.round(Float.parseFloat(moistureGreen)) + getString(R.string.m)));
                        }

                        if (humidityGreen.equals("nan")||humidityGreen.equals("")) {
                            Log.i("arraylistTrue", "Humidity: " + humidityGreen);
                            humidityValue.setText(humidityGreen + getString(R.string.percent));
                        } else {
                            Log.i("arraylistFalse", "Humidity: " + humidityGreen);
                            humidityValue.setText(String.valueOf(Math.round(Float.parseFloat(humidityGreen)) + getString(R.string.percent)));
                        }

                        // relay status display
                        for(int i = 0; i < relayStatusGreen.length(); i++) {
                            if (String.valueOf(relayStatusGreen.charAt(i)).equals("L")) {
                                relayLight = "Light";
                                Log.i("relayLight", relayLight + "\n" + relayPump + "\n" + relayFan);

                            } else if (String.valueOf(relayStatusGreen.charAt(i)).equals("F")) {
                                relayFan = "Fan";
                                Log.i("relayFan", relayFan + " " + relayPump + " " + relayFan);

                            }
                            else if (String.valueOf(relayStatusGreen.charAt(i)).equals("P")){
                                relayPump = "Pump";
                                Log.i("relayPump", relayFan + " " + relayPump + " " + relayFan);
                            }
                        }

                        Log.i("relayTest", relayLight + "\n" + relayPump + "\n" + relayFan);

                        for (int i = 0; i < relayStatusGreen.length(); i++) {
                            if (String.valueOf(relayStatusGreen.charAt(i)).equals("L") ||
                                    String.valueOf(relayStatusGreen.charAt(i)).equals("F") ||
                                    String.valueOf(relayStatusGreen.charAt(i)).equals("P")) {

                                relayStatValue.setText(relayLight + " " + relayFan + " " + relayPump);

                            }
                        }


                        // Time conversion from GMT to Local Time Nepal and last updated time conversion

                        timeGreen = timeGreen.substring(0, Math.min(timeGreen.length(), 19));

                        String[] timeSplit = timeGreen.split(" ");
                        Log.i("timeGreenTest", timeSplit[1]);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                        simpleDateFormat.setTimeZone(TimeZone.getDefault());

                        try {
                            Date convertedDate = simpleDateFormat.parse(timeGreen);
                            String newDateTime= new SimpleDateFormat("yyyy-MM-dd\nHH:mm:ss",
                                    Locale.US).format(convertedDate);
                            long newTime = convertedDate.getTime();

                            String[] time_split = newDateTime.split("\n");
                            Log.i("testNewDate", time_split[0] + "\t" + time_split[1]);

                            Date currentDate = new Date();
                            long currentTime = currentDate.getTime();

                            diff = Math.abs(currentTime - newTime);
                            diff = diff/(60*1000);

                            Log.i("testCurrent", String.valueOf(diff));

                            dateValue.setText(time_split[0]);
                            timeValue.setText(time_split[1]);

                            latestUpdate.setText(String.valueOf(diff + " " + getString(R.string.minutes_ago)));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();

                    }
                } catch (JSONException e) {
                        e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("arraylistData", "Failed to read value", databaseError.toException());
            }

        });
    }
}