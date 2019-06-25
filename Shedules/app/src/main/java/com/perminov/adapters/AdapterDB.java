package com.perminov.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.perminov.R;
import com.perminov.Schedule;

import java.util.List;


public class AdapterDB extends RecyclerView.Adapter<AdapterDB.ViewHolder> {

    private List<Schedule> schedules;

    public AdapterDB(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    @NonNull
    @Override
    public AdapterDB.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_view_db, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDB.ViewHolder viewHolder, int i) {
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

        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cvDB);
            title = (TextView) itemView.findViewById(R.id.titleTVDB);
            number = (TextView) itemView.findViewById(R.id.numberTVDB);
            company = (TextView) itemView.findViewById(R.id.companyTVDB);
            vehicle = (TextView) itemView.findViewById(R.id.vehicleTVDB);
            days = (TextView) itemView.findViewById(R.id.daysTVDB);
            departure = (TextView) itemView.findViewById(R.id.departureTVDB);
            transport = itemView.findViewById(R.id.img_transportDB);
        }
    }
}
