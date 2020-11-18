package com.example.androidassignments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class WeatherForecast extends Activity {
    private final String ACTIVITY_NAME = "WeatherForecastActivity";

    ProgressBar progressBar;
    ImageView imageView;
    TextView current_temp;
    TextView min_temp;
    TextView max_temp;
    List<String> cityList;
    TextView cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        current_temp = findViewById(R.id.current_temp);
        min_temp = findViewById(R.id.min_temp);
        max_temp = findViewById(R.id.max_temp);
        imageView = findViewById(R.id.image_forecast);
        cityName = findViewById(R.id.cityName);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        get_a_city();
    }

    public void get_a_city() {
        cityList = Arrays.asList(getResources().getStringArray(R.array.cities));

        final Spinner citySpinner = findViewById(R.id.citySpinner);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.cities, android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> adapterView, View view, int i, long l) {
                new ForecastQuery(cityList.get(i)).execute("this will go to background");
                cityName.setText(cityList.get(i) + " Weather");
            }

            @Override
            public void onNothingSelected(AdapterView <?> adapterView) {
            }
        });
    }

    private class ForecastQuery extends AsyncTask <String, Integer, String> {
        private String currentTemp;
        private String minTemp;
        private String maxTemp;
        private Bitmap picture;
        protected String city;

        ForecastQuery(String city) {
            this.city = city;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(
                        "https://api.openweathermap.org/data/2.5/weather?" +
                                "q=" + this.city + "," + "ca&" +
                                "APPID=79cecf493cb6e52d25bb7b7050ff723c&" +
                                "mode=xml&" +
                                "units=metric");

                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setReadTimeout(10000);
                httpsURLConnection.setConnectTimeout(15000);
                httpsURLConnection.setRequestMethod("GET");
                httpsURLConnection.setDoInput(true);
                httpsURLConnection.connect();

                InputStream in = httpsURLConnection.getInputStream();

                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                int type;
                while ((type = parser.getEventType()) != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() == XmlPullParser.START_TAG) {
                        if (parser.getName().equals("temperature")) {
                            currentTemp = parser.getAttributeValue(null, "value");
                            publishProgress(25);
                            minTemp = parser.getAttributeValue(null, "min");
                            publishProgress(50);
                            maxTemp = parser.getAttributeValue(null, "max");
                            publishProgress(75);
                        } else if (parser.getName().equals("weather")) {
                            String iconName = parser.getAttributeValue(null, "icon");
                            String fileName = iconName + ".png";

                            Log.i(ACTIVITY_NAME, "Looking for file: " + fileName);
                            if (fileExistance(fileName)) {
                                FileInputStream fis = null;
                                try {
                                    fis = openFileInput(fileName);

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                Log.i(ACTIVITY_NAME, "Found the file locally");
                                picture = BitmapFactory.decodeStream(fis);
                            } else {
                                String iconUrl = "https://openweathermap.org/img/w/" + fileName;
                                picture = getImage(new URL(iconUrl));

                                FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                                picture.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                Log.i(ACTIVITY_NAME, "Downloaded the file from the Internet");
                                outputStream.flush();
                                outputStream.close();
                            }
                            publishProgress(100);
                        }
                    }
                    parser.next();
                }
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }

            return null;
        }

        public boolean fileExistance(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        public Bitmap getImage(URL url) {
            HttpsURLConnection connection = null;
            try {
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) { // == 200) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else
                    return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(String a) {
            progressBar.setVisibility(View.INVISIBLE);
            imageView.setImageBitmap(picture);
            current_temp.setText(currentTemp + "C\u00b0");
            min_temp.setText(minTemp + "C\u00b0");
            max_temp.setText(maxTemp + "C\u00b0");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }
    }
}
