package com.uit.myairquality;

import static android.content.Intent.getIntent;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.uit.myairquality.Interfaces.CallToken;
import com.uit.myairquality.Interfaces.CallWeather;
import com.uit.myairquality.Model.APIClient;
import com.uit.myairquality.Model.TokenResponse;
import com.uit.myairquality.Model.User;
import com.uit.myairquality.Model.WeatherResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    EditText username;
TextView nhietdo, gio, mua, doam, thoigian,time;
    User userCallApi;
    private static final String DATE_FORMAT = "dd/MM";
    private static final String TIME_FORMAT_24 = "HH:mm";
    APIClient ApiClient;
    Date date = new Date();
    public static String getDateString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return format.format(date);
    }
//    public static String getTime24String(Date date) {
//        SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT_24);
//        return format.format(date);
//    }
private void updateDayOfWeek() {
    // Lấy thứ của ngày hiện tại
    Calendar calendar = Calendar.getInstance();
    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

    // Chuyển định dạng thứ từ số sang chuỗi
    String[] daysOfWeek = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    String dayOfWeekString = daysOfWeek[dayOfWeek - 1];

    // Hiển thị thứ trong TextView
    time.setText(dayOfWeekString);
}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        nhietdo = view.findViewById(R.id.nhietdo);
        gio = view.findViewById(R.id.gio);
        mua =  view.findViewById(R.id.mua);
        doam = view.findViewById(R.id.doam);
        thoigian = view.findViewById(R.id.n_gay);
        time = view.findViewById(R.id.thoigian);


        updateDayOfWeek();
//        ten = view.findViewById(R.id.textView3);
//        username = view.findViewById(R.id.email);
//        ten = String.valueOf(username.getText());
        userCallApi = new User("user", "123", "");

//        String receivedData = intent.getStringExtra("key");
//        Intent intent = getIntent();
//        User user = (User) intent.getSerializableExtra("user");

        CallToken callToken = ApiClient.CallToken();
        CallWeather callWeather = ApiClient.CallWeather();

        Call<TokenResponse> userCallToken = callToken.sendRequest(
                "password",
                "openremote",
                userCallApi.getUsername(),
                userCallApi.getPassword()
        );
        userCallToken.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.body() != null) {
                    String accessToken = response.body().getAccessToken();
                    userCallApi.setToken(accessToken);
                    Log.d("response call", userCallApi.getToken());

                    Call<WeatherResponse> userCallWeather = callWeather.callWeather(
                            "5zI6XqkQVSfdgOrZ1MyWEf",
                            "Bearer " + userCallApi.getToken()
                    );
                    userCallWeather.enqueue(new Callback<WeatherResponse>() {
                        @Override
                        public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                            if (response.body() != null) {
                                WeatherResponse weatherResponse = response.body();
                                setAttributeValues(weatherResponse);
                            } else {
                                Log.d("response call", "lỗi");
                            }
                        }
                        @Override
                        public void onFailure(Call<WeatherResponse> call, Throwable t) {
                            Log.d("response call", t.getMessage().toString());
                        }
                    });
                } else {
                    Log.d("response call", "lỗi");
                }
            }
            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Log.d("response call", t.getMessage().toString());
            }
        });

        return view;
    }


    private void setAttributeValues(WeatherResponse weatherResponse) {
        if (weatherResponse != null) {

            WeatherResponse.Attributes attributes = weatherResponse.getAttributes();
            if (attributes != null) {
                WeatherResponse.Attribute humidity = attributes.getHumidity();
                WeatherResponse.Attribute temperature = attributes.getTemperature();
                WeatherResponse.Attribute rainfall = attributes.getRainfall();
                WeatherResponse.Attribute windSpeed = attributes.getWindSpeed();

                if(humidity != null) {
                    String value = humidity.getValue();
                    doam.setText(value );
                }
                if(temperature != null) {
                    String value = temperature.getValue();
                    long timestamp = temperature.getTimestamp();
                    String formattedTime = convertTimestampToTime(timestamp);
                    thoigian.setText(getDateString(date));
                    nhietdo.setText(value + "°C");
                }
                if(rainfall != null) {
                    String value = rainfall.getValue();
                    mua.setText(value );
                }
                if(windSpeed != null) {
                    String value = windSpeed.getValue();
                    gio.setText(value);
                }
            }
        }
    }
    public static String convertTimestampToTime(long timestamp) {
        // Chuyển đổi timestamp thành Date
        Date date = new Date(timestamp);

        // Định dạng ngày để chỉ hiển thị giờ và phút
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        // Trả về chuỗi thời gian đã định dạng
        return sdf.format(date);
    }
}