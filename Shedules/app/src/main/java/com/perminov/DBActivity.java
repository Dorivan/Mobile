package com.perminov;

import android.app.DatePickerDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.perminov.adapters.AdapterDB;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class DBActivity extends AppCompatActivity {
    List<String> stations = new ArrayList<>();
    Realm realm;
    Calendar calendar = Calendar.getInstance();
    RecyclerView rv;
    TextView dateTVDB;
    AdapterDB adapterSchedules;
    List<Schedule> schedules = new ArrayList<>();
    String currentStation = "";
    String currentDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        setTitle("Сохранённые расписания");

        realm = Realm.getDefaultInstance();

        RealmResults<Station> stationsFromDB = realm.where(Station.class).findAll();//берем станции из бд

        for (int i = 0; i < stationsFromDB.size(); i++)
            stations.add(stationsFromDB.get(i).getName());//заполняем выпадающий список значениями имён станций

        stations = new ArrayList<>(new HashSet<>(stations));
        currentDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        dateTVDB = findViewById(R.id.dateTVDB);
        dateTVDB.setText("сегодня");

        initRecyclerView();
        initSpinner();
    }

    private void initSpinner() {
        AppCompatSpinner spinner = findViewById(R.id.typesOfTransportDB);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stations);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);

        //слушатель изменения выбора
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                String item = (String) parent.getItemAtPosition(position);

                //получаем расписания по выбранной станции
                if (item.equals("Киров Пасс (Вокзал)")) {
                    currentStation = "s9612402";
                    getSchedulesFromDB(currentStation, currentDate);
                } else if (item.equals("Аэропорт Победилово")) {
                    currentStation = "s9600181";
                    getSchedulesFromDB(currentStation, currentDate);
                } else {
                    currentStation = "s9636941";
                    getSchedulesFromDB(currentStation, currentDate);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
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
        dateTVDB.setText(DateUtils.formatDateTime(this,
                calendar.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
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

            getSchedulesFromDB(currentStation, currentDate);

            setInitialDateTime();
        }
    };

    // инициализация RecyclerView
    private void initRecyclerView() {
        rv = findViewById(R.id.rvDB);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        adapterSchedules = new AdapterDB(schedules);
        rv.setAdapter(adapterSchedules);
    }

    public void clearDB(View v) {
        try {
            final RealmResults<Schedule> schedulesFromDB = realm.where(Schedule.class)
                    .equalTo("station.code", currentStation)
                    .equalTo("date", currentDate).findAll();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    schedulesFromDB.deleteAllFromRealm();
                }
            });

            Toast.makeText(this, "Данные успешно удалены", Toast.LENGTH_LONG).show();
            getSchedulesFromDB(currentStation, currentDate);
        } catch (Exception e) {
            Toast.makeText(this, "Во время удаления данных произошла ошибка", Toast.LENGTH_LONG).show();
        }
    }

    private void getSchedulesFromDB(String stationCode, String date) {
        schedules.clear();

        RealmResults<Schedule> schedulesFromDB = realm.where(Schedule.class)
                .equalTo("station.code", stationCode)
                .equalTo("date", date).findAll();
        //выбираем расписания с заданной станции и по выбранному времени

        schedules.addAll(schedulesFromDB);

        adapterSchedules.notifyDataSetChanged();
    }
}
