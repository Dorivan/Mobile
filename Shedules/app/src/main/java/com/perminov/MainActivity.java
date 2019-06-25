package com.perminov;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.perminov.adapters.Adapter;
import com.perminov.network.Repository;
import com.perminov.network.ResponseCallback;
import com.perminov.network.pojo.ScheduleFromServer;
import com.perminov.network.pojo.SchedulesResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String[] typesOfTransport = {"Поезда", "Самолеты", "Междугородние автобусы"};
    Calendar calendar = Calendar.getInstance();
    TextView dateTV;
    List<Schedule> schedules = new ArrayList<>();
    RecyclerView rv;
    Adapter adapterSchedules;
    Repository repository = Repository.getInstance();
    ProgressBar pBar;
    String currentStation = "s9612402";
    String currentDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pBar = findViewById(R.id.progress);
        dateTV = findViewById(R.id.dateTV);
        currentDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);

        setInitialDateTime();

        initSpinner();

        initRecyclerView();
    }

    private void initSpinner() {
        AppCompatSpinner spinner = findViewById(R.id.typesOfTransport);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typesOfTransport);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);

        //слушатель изменения выбора
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                pBar.setVisibility(ProgressBar.VISIBLE);
                // Получаем выбранный объект
                String item = (String) parent.getItemAtPosition(position);

                if (item.equals(typesOfTransport[0])) {
                    currentStation = "s9612402";
                    getSchedules(currentStation, currentDate);
                } else if (item.equals(typesOfTransport[1])) {
                    currentStation = "s9600181";
                    getSchedules(currentStation, currentDate);
                } else {
                    currentStation = "s9636941";
                    getSchedules(currentStation, currentDate);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }

    // инициализация RecyclerView
    private void initRecyclerView() {
        rv = findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        adapterSchedules = new Adapter(schedules);
        rv.setAdapter(adapterSchedules);
    }

    // отображаем диалоговое окно для выбора даты
    public void setDate(View v) {
        new DatePickerDialog(this, d,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // установка начальных даты и времени
    private void setInitialDateTime() {
        dateTV.setText(DateUtils.formatDateTime(this,
                calendar.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));

        getSchedules(currentStation, currentDate);
    }

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            currentDate = year + "-";

            calendar.set(Calendar.MONTH, monthOfYear);
            if (monthOfYear < 10)
                currentDate += "0" + (monthOfYear + 1) + "-";
            else
                currentDate += (monthOfYear + 1) + "-";

            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if (dayOfMonth < 10)
                currentDate += "0" + dayOfMonth;
            else
                currentDate += dayOfMonth;

            setInitialDateTime();
        }
    };

    private void getSchedules(String station, String date) {
        schedules.clear();

        repository.getSchedules(new ResponseCallback<SchedulesResponse>() {
            @Override
            public void onEnd(SchedulesResponse apiResponse) {
                List<ScheduleFromServer> schedulesFromServer = apiResponse.getSchedules();

                for (int i = 0; i < schedulesFromServer.size(); i++) {
                    ScheduleFromServer currentSchedule = schedulesFromServer.get(i);
                    schedules.add(new Schedule(
                            currentSchedule.getThread().getTypeOfTransport(),
                            currentSchedule.getThread().getTitle(),
                            currentSchedule.getThread().getNumber(),
                            currentSchedule.getThread().getCarrier().getTitle(),
                            currentSchedule.getThread().getVehicle(),
                            currentSchedule.getDays(),
                            currentSchedule.getDeparture().substring(11, 16),
                            new Station(currentStation),
                            currentDate
                    ));
                }

                adapterSchedules.notifyDataSetChanged();
                pBar.setVisibility(ProgressBar.GONE);
            }
        }, station, date);
    }

    // отображаем диалоговое окно для выбора даты
    public void openDB(View v) {
        Intent intent = new Intent(this, DBActivity.class);
        startActivity(intent);
    }
}
