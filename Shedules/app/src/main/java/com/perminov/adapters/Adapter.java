package com.perminov.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.perminov.R;
import com.perminov.Schedule;
import com.perminov.Station;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Realm realm;
    private List<Schedule> schedules;

    public Adapter(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder viewHolder, int i) {
        final int iCopy = i;

        viewHolder.title.setText(schedules.get(i).getTitle());
        viewHolder.number.setText(schedules.get(i).getNumber());
        viewHolder.company.setText(schedules.get(i).getTitleOfCarrier());

        if (schedules.get(i).getVehicle() != null) {
            viewHolder.vehicle.setVisibility(View.VISIBLE);
            viewHolder.vehicle.setText(schedules.get(i).getVehicle());
        } else {
            viewHolder.vehicle.setVisibility(View.GONE);
        }

        switch (schedules.get(i).getTypeOfTransport()) {
            case "suburban":
                viewHolder.transport.setImageResource(R.drawable.ic_suburban);
                break;
            case "train":
                viewHolder.transport.setImageResource(R.drawable.ic_train);
                break;
            case "bus":
                viewHolder.transport.setImageResource(R.drawable.ic_bus);
                break;
            default:
                viewHolder.transport.setImageResource(R.drawable.ic_plane);
                break;
        }

        viewHolder.days.setText(schedules.get(i).getDays());
        viewHolder.departure.setText(schedules.get(i).getDeparture());

        viewHolder.save.setOnClickListener(new View.OnClickListener() {//обработчик нажатия на кнопку сохранения расписания

            @Override
            public void onClick(View v) {

                //Realm.deleteRealm(Realm.getDefaultConfiguration());
                realm = Realm.getDefaultInstance();


                realm.executeTransaction(new Realm.Transaction() {
                    Station station;

                    @Override
                    public void execute(Realm realm) {
                        RealmResults<Station> results = realm.where(Station.class).equalTo("code", schedules.get(iCopy).getStation().getCode()).findAll();//находим нужную станцию

                        if (results.size() == 0) {//если в бд нет такой станции
                            station = realm.createObject(Station.class);//создаём станцию
                            station.setCode(schedules.get(iCopy).getStation().getCode());
                        } else //иначе - получаем станцию из базы
                            station = results.first();

                        station.getSchedules().add(schedules.get(iCopy));//сохраняем для этой станции расписание
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title;
        TextView number;
        TextView company;
        TextView vehicle;
        TextView days;
        TextView departure;
        ImageView transport;
        AppCompatImageButton save;

        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            title = (TextView) itemView.findViewById(R.id.titleTV);
            number = (TextView) itemView.findViewById(R.id.numberTV);
            company = (TextView) itemView.findViewById(R.id.companyTV);
            vehicle = (TextView) itemView.findViewById(R.id.vehicleTV);
            days = (TextView) itemView.findViewById(R.id.daysTV);
            departure = (TextView) itemView.findViewById(R.id.departureTV);
            transport = itemView.findViewById(R.id.img_transport);
            save = itemView.findViewById(R.id.btn_save);
        }
    }
}
