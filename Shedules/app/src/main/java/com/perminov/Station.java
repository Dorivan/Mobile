package com.perminov;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Station extends RealmObject {
    private String code;
    private String name;
    private RealmList<Schedule> schedules;

    public Station() {
        schedules = new RealmList();
    }

    public Station(String code) {
        this.code = code;
        if (code.equals("s9612402"))
            this.name = "Киров Пасс (Вокзал)";
        else if (code.equals("s9600181"))
            this.name = "Аэропорт Победилово";
        else
            this.name = "Автовокзал Киров";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;

        if (code.equals("s9612402"))
            this.name = "Киров Пасс (Вокзал)";
        else if (code.equals("s9600181"))
            this.name = "Аэропорт Победилово";
        else
            this.name = "Автовокзал Киров";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(RealmList<Schedule> schedules) {
        this.schedules = schedules;
    }
}
