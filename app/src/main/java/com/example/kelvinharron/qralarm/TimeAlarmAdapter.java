package com.example.kelvinharron.qralarm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * This class acts as an adapter between alarm objects data and card view objects.
 * When alarm objects are created we can add them to the alarm view as card layouts through this process.
 * TODO: add database wrapper so we can update database with card information
 * Created by kelvinharron on 10/04/2016.
 */
public class TimeAlarmAdapter extends RecyclerView.Adapter<TimeAlarmAdapter.AlarmViewHolder>{

    private LayoutInflater inflater;
    private List<Alarm> alarmData = Collections.emptyList();

    /**
     * Constructor with arguments
     *
     * @param context
     * @param alarmData
     */
    public TimeAlarmAdapter(Context context, List<Alarm> alarmData) {
        inflater = LayoutInflater.from(context);
        this.alarmData = alarmData;
    }

    /**
     * Creates an alarm object card view.
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View alarmView = inflater.inflate(R.layout.time_alarm_card, parent, false);
        AlarmViewHolder holder = new AlarmViewHolder(alarmView);
        return holder;
    }

    /**
     * Method binds database alarm object data to the view objects for text fields.
     * Allows us to set the name of the alarm, memos etc that are stored each time an alarm is created.
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(AlarmViewHolder holder, final int position) {
        Alarm alarm = alarmData.get(position);

        holder.alarmName.setText(alarm.getName());
        holder.alarmMemo.setText(alarm.getMemo());
        holder.alarmTimeHour.setText(String.valueOf(alarm.getHour()));
        holder.alarmTimeMin.setText(String.valueOf(alarm.getMin()));
        holder.alarmDays.setText(String.valueOf(alarm.getDays()));
        holder.alarmIsOn.setChecked(alarm.isOn());

    }

    /**
     * Gets the count of alarm objects in the database
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return alarmData.size();
    }


    /**
     * Inner class that links the view objects from the card layout object and object properties together.
     */
    public static class AlarmViewHolder extends RecyclerView.ViewHolder {

        protected TextView alarmName;
        protected TextView alarmMemo;
        protected TextView alarmTimeHour;
        protected TextView alarmTimeMin;
        protected TextView alarmDays;
        protected Switch alarmIsOn;

        public AlarmViewHolder(View itemView) {
            super(itemView);
            alarmName = (TextView) itemView.findViewById(R.id.alarm_name);
            alarmMemo = (TextView) itemView.findViewById(R.id.alarm_memo);
            alarmTimeHour = (TextView) itemView.findViewById(R.id.alarm_time_hour);
            alarmTimeMin = (TextView) itemView.findViewById(R.id.alarm_time_min);
            alarmDays = (TextView) itemView.findViewById(R.id.alarm_days);
            alarmIsOn = (Switch) itemView.findViewById(R.id.toggleAlarmSwitch);
        }
    }
}