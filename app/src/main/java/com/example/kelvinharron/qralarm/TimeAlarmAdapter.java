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
public class TimeAlarmAdapter extends RecyclerView.Adapter<TimeAlarmAdapter.AlarmViewHolder> {

    private LayoutInflater inflater;
    private List<Alarm> timeAlarmData = Collections.emptyList();

    /**
     * Constructor with arguments
     *
     * @param context
     * @param timeAlarmData
     */
    public TimeAlarmAdapter(Context context, List<Alarm> timeAlarmData) {
        inflater = LayoutInflater.from(context);
        this.timeAlarmData = timeAlarmData;
    }

    public void deleteAlarm(int position) {
        Alarm alarm = timeAlarmData.get(position);
        new AlarmScheduler().cancelAlarm(inflater.getContext(), alarm);
        AlarmSQLiteHelper db = new AlarmSQLiteHelper(inflater.getContext());
        db.deleteAlarm(alarm);
        timeAlarmData.remove(position);
        notifyItemRemoved(position);

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
        Alarm alarm = timeAlarmData.get(position);

        // TOOD: looking at the create alarm void method instead???
        holder.alarmName.setText(alarm.getName());
        holder.alarmMemo.setText(alarm.getMemo());
        holder.alarmTimeHour.setText(String.valueOf(alarm.getHour()));
        holder.alarmTimeMin.setText(String.valueOf(alarm.getMin()));

        String dayText = "";
        StringBuilder strBuilder = new StringBuilder();
        if (alarm.getDays() != null) {
            for (Integer day : alarm.getDays()) {
                switch (day) {
                    //case 0:
                     //   dayText = "Not Recurring ";
                      //  break;
                    case 1:
                        dayText = "Sun ";
                        break;
                    case 2:
                        dayText = "Mon ";
                        break;
                    case 3:
                        dayText = "Tue ";
                        break;
                    case 4:
                        dayText = "Wed ";
                        break;
                    case 5:
                        dayText = "Thu ";
                        break;
                    case 6:
                        dayText = "Fri ";
                        break;
                    case 7:
                        dayText = "Sat ";
                        break;
                    default: dayText = "Not Recurring ";
                        break;
                }
                strBuilder.append(dayText);
            }
        }

        holder.alarmDays.setText(strBuilder.toString());
        holder.alarmIsOn.setChecked(alarm.isOn());

    }

    /**
     * Gets the count of alarm objects in the database
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return timeAlarmData.size();
    }


    /**
     * Inner class that links the view objects from the card layout object and object properties together.
     */
    class AlarmViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        protected TextView alarmName;
        protected TextView alarmMemo;
        protected TextView alarmTimeHour;
        protected TextView alarmTimeMin;
        protected TextView alarmDays;
        protected Switch alarmIsOn;

        public AlarmViewHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            alarmName = (TextView) itemView.findViewById(R.id.location_alarm_name);
            alarmMemo = (TextView) itemView.findViewById(R.id.alarm_memo);
            alarmTimeHour = (TextView) itemView.findViewById(R.id.alarm_time_hour);
            alarmTimeMin = (TextView) itemView.findViewById(R.id.alarm_time_min);
            alarmDays = (TextView) itemView.findViewById(R.id.alarm_days);
            alarmIsOn = (Switch) itemView.findViewById(R.id.toggleAlarmSwitch);
        }

        @Override
        public boolean onLongClick(View v) {
            deleteAlarm(getLayoutPosition());
            return false;
        }
    }
}