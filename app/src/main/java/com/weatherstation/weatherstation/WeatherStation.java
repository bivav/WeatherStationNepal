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

public class WeatherStation extends Fragment {

    private static final String TAG = "DATA_RECEIVED --> " ;
    String resultTime;
    ProgressDialog progressDialog;
    String rainfallWeather, temperatureWeather, humidityWeather, timeWeather, windDirectionWeather, windSpeedWeather;
    TextView temperatureValue, temperatureValueF, humidityValue, rainfallValue, timeValue, windDirectionValue,
    windSpeedValue, latestUpdate, dateValue;

    // Get the database
    FirebaseDatabase database;
    DatabaseReference myRef;

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_station, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Fetching Data. Please Wait...");
        progressDialog.setCancelable(false);

        temperatureValue = (TextView)view.findViewById(R.id.temperatureValue);
        temperatureValueF = (TextView)view.findViewById(R.id.temperatureValueF);
        rainfallValue = (TextView)view.findViewById(R.id.rainfallValue);
        humidityValue = (TextView)view.findViewById(R.id.humidityValue);
        timeValue = (TextView)view.findViewById(R.id.timeValue);
        windDirectionValue = (TextView)view.findViewById(R.id.windDirectionValue);
        windSpeedValue = (TextView)view.findViewById(R.id.windSpeedValue);
        dateValue = (TextView)view.findViewById(R.id.dateValue);
        latestUpdate = (TextView)view.findViewById(R.id.latestUpdate);

        progressDialog.show();

        GetDataFromServer getDataFromServer = new GetDataFromServer();
        getDataFromServer.execute();

        return view;
    }

    private class GetDataFromServer extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            GetData();
            Log.i(TAG, "Data Fetched.." );
            return null;
        }
    }

    public void GetData() {

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("weatherStationData/weatherStation");
        myRef.keepSynced(true);

        myRef.addValueEventListener(new ValueEventListener() {

            private HashMap hashMap;
            JSONObject object;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                hashMap = (HashMap)dataSnapshot.getValue();
                object = new JSONObject(hashMap);

                for (int i = 0; i < object.length(); i++) {

                    try {
                        rainfallWeather = object.getString("rain");
                        temperatureWeather = object.getString("temperature");
                        humidityWeather = object.getString("humidity");
                        windDirectionWeather = object.getString("windDirection");
                        timeWeather = object.getString("currentTime");
                        windSpeedWeather = object.getString("windSpeed");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.i("gotTheData", "Rainfall: " + rainfallWeather + "\n"
                        + "Temperature_Sta: " + temperatureWeather + "\n"
                        + "Humidity: " + humidityWeather);

                if (hashMap != null && isAdded()) {
                    double far = Double.parseDouble(temperatureWeather);
                    far = far * 1.8 + 32;

                    temperatureValue.setText(String.valueOf(Math.round(Float.parseFloat(temperatureWeather))));
                    temperatureValueF.setText(String.valueOf(Math.round(far)));
                    timeWeather = timeWeather.substring(0, Math.min(timeWeather.length(), 19));

                    humidityValue.setText(String.valueOf(Math.round(Float.parseFloat(humidityWeather)) + getString(R.string.percent)));
                    windDirectionValue.setText(windDirectionWeather);
                    windSpeedValue.setText(String.valueOf(Math.round(Float.parseFloat(windSpeedWeather)) + getString(R.string.kph)));
                    rainfallValue.setText(String.valueOf(Math.round(Float.parseFloat(rainfallWeather)) + getString(R.string.mm)));


                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                    try {

                        Date parseDate = simpleDateFormat.parse(timeWeather);
                        Log.i("finalTime", String.valueOf(parseDate));

                        resultTime = new SimpleDateFormat("yyyy-MM-dd\nHH:mm:ss", Locale.US).format(parseDate);
                        long newTime = parseDate.getTime();

                        String[] time_date_split = resultTime.split("\n");

                        Date currentDate = new Date();
                        long currentTime = currentDate.getTime();

                        long diff = currentTime - newTime;
                        diff = diff/(60*1000);

                        latestUpdate.setText(String.valueOf(diff + " " + getString(R.string.minutes_ago)));
                        dateValue.setText(time_date_split[0]);
                        timeValue.setText(time_date_split[1]);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    progressDialog.dismiss();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value", databaseError.toException());
            }
        });
    }
}