package com.uit.myairquality;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.uit.myairquality.Interfaces.APIInterface;
import com.uit.myairquality.Interfaces.CallToken;
import com.uit.myairquality.Interfaces.CallWeather;
import com.uit.myairquality.Model.APIClient;
import com.uit.myairquality.Model.ChartResponse;
import com.uit.myairquality.Model.ConvertTime;
import com.uit.myairquality.Model.Cookie;
import com.uit.myairquality.Model.CustomChart;
import com.uit.myairquality.Model.TokenResponse;
import com.uit.myairquality.Model.User;
import com.uit.myairquality.Model.WeatherResponse;
import com.uit.myairquality.Model.datetimepicker;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class DashboardFragment extends Fragment implements datetimepicker.OnDateTimeSetListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_PARAM1 = "param1";
    private Button button;
    private boolean shouldShowDateTimePicker = true;

    private TextView dateFrom, dateTo;
    private Calendar calendar1, calendar2;

    private LineChart lineChart;
    private static final String ARG_PARAM2 = "param2";

    APIInterface apiInterface;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String[] temp = {"Temperature", "Humidity", "Rain Fall"};
    String[] time_frame = {"Day", "Month", "Year"};


    public DashboardFragment() {
        // Required empty public constructor


    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
//     TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
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
    Retrofit retrofit;
    User userCallApi;
    APIClient ApiClient;
    String token;
    private OkHttpClient okHttpClient;
    Spinner spinnerTemp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new Cookie())
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://uiot.ixxc.dev")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiInterface = retrofit.create(APIInterface.class);



        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_dashboard, container, false);
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        lineChart = view.findViewById(R.id.lineChart);
        button = view.findViewById(R.id.btnShow);
        spinnerTemp = view.findViewById(R.id.spinner_temp);

        dateFrom = view.findViewById(R.id.date_time_picker_from);
        dateTo = view.findViewById(R.id.date_time_picker_to);

        calendar1 = Calendar.getInstance();
        calendar2 = Calendar.getInstance();

//        showDateTimePickerButton.setOnClickListener(v -> showDateTimePicker());
//        ateTimePickerButton = view.findViewById(R.id.showDateTimePickerButton);
//        showDateTimePickerButton.setOnClickListener(v -> showDateTimePicker());


        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(1);
            }
        });
        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(2);
            }
        });

//        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
//                requireContext(),
//                R.array.options_array_1,
//                android.R.layout.simple_spinner_item
//        );



//        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.options_array,
                android.R.layout.simple_spinner_item

        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                requireContext(), R.array.items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTemp.setAdapter(adapter1);




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExecutorService networkExecutor = Executors.newSingleThreadExecutor();
                networkExecutor.execute(() -> {
                    Call<ResponseBody> StartSignIn = apiInterface.Login_for_chart(
                            "openremote",
                            "user",
                            "123",
                            "password");
                    try {
                        Response<ResponseBody> Response = StartSignIn.execute();
                        JSONObject AccessTokenJSON = new JSONObject(Response.body().string());
                        token = AccessTokenJSON.getString("access_token");
                        Log.i("Token: ",token);
                        System.out.println(token);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                });
//
//                ExecutorService networkExecutor = Executors.newSingleThreadExecutor();
//                networkExecutor.execute(() -> {
//                    Call<ResponseBody> StartSignIn = apiInterface.Login_for_chart(
//                            "openremote",
//                            "user",
//                            "123",
//                            "password");
//                    try {
//                        retrofit2.Response<ResponseBody> Response = StartSignIn.execute();
//                        JSONObject AccessTokenJSON = new JSONObject(Response.body().string());
//                        token = AccessTokenJSON.getString("access_token");
//                        Log.i("Token",token);
//                    } catch (Exception e) {
//                        System.out.println(e);
//                    }
//                });
                setupLineChart();
                generateChart();
            }
        });





        return view;
    }
 
    private void populateChart(List<ChartResponse> chartresponses, String Label) {
        ArrayList<Entry> entries = new ArrayList<>();

        for (ChartResponse chartresponse : chartresponses) {
            entries.add(new Entry(chartresponse.getX(), chartresponse.getY()));
        }
        LineDataSet dataSet = new LineDataSet(entries, Label);

        dataSet.setDrawValues(false);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setLineWidth(2.0f);
        dataSet.setColor(Color.BLUE);
        dataSet.setCircleColor(Color.BLUE);
        dataSet.setCircleRadius(5f);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }
    private void setupLineChart() {
        lineChart.getDescription().setEnabled(false);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setTouchEnabled(true);
        lineChart.setBackgroundColor(Color.TRANSPARENT);
        lineChart.getDescription().setTextColor(Color.WHITE);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.setDoubleTapToZoomEnabled(false);

        CustomChart markerView = new CustomChart(this, R.layout.customchart);
        lineChart.setMarker(markerView);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new ConvertTime());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setLabelRotationAngle(45f);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
    }
    private void showDateTimePickerDialog(boolean isStartTime) {
        datetimepicker dateTimePickerFragment = new datetimepicker(this, isStartTime);
        dateTimePickerFragment.show(getChildFragmentManager(), null);
    }

    private void generateChart() {
        if (spinnerTemp.getSelectedItem() != null && calendar1 != null && calendar2 != null) {
            ExecutorService networkExecutor = Executors.newSingleThreadExecutor();
            networkExecutor.execute(() -> {
                try {
                    String selectedMetrics = spinnerTemp.getSelectedItem().toString();
                    selectedMetrics = selectedMetrics.toLowerCase().split(" ")[0];

                    long startTimestamp = calendar1.getTime().getTime();
                    long endTimestamp = calendar2.getTime().getTime();

                    JSONObject Request = new JSONObject();
                    Request.put("type","lttb");
                    Request.put("fromTimestamp", startTimestamp);
                    Request.put("toTimestamp", endTimestamp);
                    Request.put("amountOfPoints", 100);
                    System.out.println(Request);

                    RequestBody requestBody = RequestBody.create(JSON, Request.toString());

                    retrofit2.Call<List<ChartResponse>> GetChart = apiInterface.CallChart("Bearer " + token, selectedMetrics, requestBody);
                    Response<List<ChartResponse>> ChartResponse = GetChart.execute();
                    List<ChartResponse> listPoint = ChartResponse.body();
                    if (!listPoint.isEmpty())
                        populateChart(listPoint, selectedMetrics);

                } catch (Exception e){
                    System.out.println(e);
                }
            });
        }
        else{
            Log.i("Data","Spinner Null");
        }
    }

    private void showDateTimePicker(int buttonNumber) {
        // Hiển thị DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        if (buttonNumber == 1) {
                            calendar1.set(Calendar.YEAR, year);
                            calendar1.set(Calendar.MONTH, month);
                            calendar1.set(Calendar.DAY_OF_MONTH, day);
                            showTimePicker(1);
                        } else if (buttonNumber == 2) {
                            calendar2.set(Calendar.YEAR, year);
                            calendar2.set(Calendar.MONTH, month);
                            calendar2.set(Calendar.DAY_OF_MONTH, day);
                            showTimePicker(2);
                        }
                    }
                },
                calendar1.get(Calendar.YEAR),
                calendar1.get(Calendar.MONTH),
                calendar1.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePicker(int buttonNumber) {
        // Hiển thị TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        // Set giờ và phút đã chọn vào Calendar
                        if (buttonNumber == 1) {
                            calendar1.set(Calendar.HOUR_OF_DAY, hour);
                            calendar1.set(Calendar.MINUTE, minute);
                            updateSelectedDateTimeTextView(1);
                        } else if (buttonNumber == 2) {
                            calendar2.set(Calendar.HOUR_OF_DAY, hour);
                            calendar2.set(Calendar.MINUTE, minute);
                            updateSelectedDateTimeTextView(2);
                        }
                    }
                },
                calendar1.get(Calendar.HOUR_OF_DAY),
                calendar1.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    private void updateSelectedDateTimeTextView(int buttonNumber) {
        // Định dạng ngày và giờ đã chọn
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String selectedDateTime;
        if (buttonNumber == 1) {
            selectedDateTime = sdf.format(calendar1.getTime());
            dateFrom.setText(selectedDateTime);
        } else if (buttonNumber == 2) {
            selectedDateTime = sdf.format(calendar2.getTime());
            dateTo.setText(selectedDateTime);
        }
    }


    @Override
    public void onDateTimeSet(Calendar dateTime, boolean isStartTime) {
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
//        String selectedDateTime;
//        if (buttonNumber == 1) {
//            selectedDateTime = sdf.format(calendar1.getTime());
//            dateFrom.setText(selectedDateTime);
//        } else if (buttonNumber == 2) {
//            selectedDateTime = sdf.format(calendar2.getTime());
//            dateTo.setText(selectedDateTime);
        }
    }
