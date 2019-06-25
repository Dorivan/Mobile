package com.perminov;

import io.realm.RealmObject;

public class Schedule extends RealmObject {

    private String typeOfTransport;
    private String title;
    private String number;
    private String titleOfCarrier;
    private String vehicle;
    private String days;
    private String departure;
    private Station station;
    private String date;

    public Schedule() {
    }

    public Schedule(String typeOfTransport, String title, String number, String titleOfCarrier, String vehicle, String days, String departure, Station station, String date) {
        this.typeOfTransport = typeOfTransport;
        this.title = title;
        this.number = number;
        this.titleOfCarrier = titleOfCarrier;
        this.vehicle = vehicle;
        this.days = days;
        this.departure = departure;
        this.station = station;
        this.date = date;
    }

    public String getTypeOfTransport() {
        return typeOfTransport;
    }

    public void setTypeOfTransport(String typeOfTransport) {
        this.typeOfTransport = typeOfTransport;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitleOfCarrier() {
        return titleOfCarrier;
    }

    public void setTitleOfCarrier(String titleOfCarrier) {
        this.titleOfCarrier = titleOfCarrier;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
