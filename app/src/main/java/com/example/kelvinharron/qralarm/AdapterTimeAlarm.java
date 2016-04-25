package com.example.kelvinharron.qralarm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

/**
 * AdapterTimeAlarm.class acts
 * This class acts as an adapter between alarm objects data and card view objects.
 * When alarm objects are created we can add them to the alarm view as card layouts through this process.
 * Created by kelvinharron on 10/04/2016.
 */
public class AdapterTimeAlarm extends RecyclerView.Adapter<AdapterTimeAlarm.AlarmViewHolder> {

    /**
     * Layout inflater is used in this class file as a means to get the context to enable us
     * access to the alarm scheduler, alarm database and toast mechanism.
     */
    private LayoutInflater inflater;

    /**
     * This collections object is how we get access to alarm data from the alarm object its self.
     * It allows us to handle the data an alarm requires to function.
     * Instantiated to Collections.emptyList() as it will still run even if the List is empty.
     */
    private List<Alarm> timeAlarmData = Collections.emptyList();

    /**
     * A reference to the Alarm object that is used for handling the binding of data to our recycler
     * view as well as the process of deleting alarms from the recycler view.
     */
    private Alarm alarm;

    /**
     * Constructor with arguments used as a handle to the alarm data that the RecyclerView displays.
     *
     * @param context
     * @param timeAlarmData
     */
    public AdapterTimeAlarm(Context context, List<Alarm> timeAlarmData) {
        inflater = LayoutInflater.from(context);
        this.timeAlarmData = timeAlarmData;
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
        View alarmView = inflater.inflate(R.layout.card_time_alarm, parent, false);
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
        alarm = timeAlarmData.get(position);

        holder.alarmName.setText(alarm.getName());
        holder.alarmMemo.setText(alarm.getMemo());
        holder.alarmTimeHour.setText(String.valueOf(alarm.getHour()));
        holder.alarmTimeMin.setText(String.valueOf(alarm.getMin()));

        // String builder used to append commas in day values so we can
        // correctly store and display them as acceptable strings.
        String dayText = "";
        StringBuilder strBuilder = new StringBuilder();
        if (alarm.getDays() != null) {
            for (Integer day : alarm.getDays()) {
                switch (day) {
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
                    default:
                        dayText = "Not Recurring ";
                        break;
                }
                strBuilder.append(dayText);
            }
        }
        holder.alarmDays.setText(strBuilder.toString());
        holder.alarmIsOn.setChecked(alarm.isOn());
    }

    /**
     * Gets the count of alarm objects in the database.
     * Attempted to display an empty view in here on the ActivityMain.class view if the count == 0
     * but it was not functioning. For now this is an override method required by the RecyclerView.
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return timeAlarmData.size();
    }

    /**
     * Handles the process of removing alarms from the recyclerview and database.
     * Displays a toast nofication upon successful deletion.
     *
     * @param position
     */
    public void deleteAlarm(int position) {
        alarm = timeAlarmData.get(position);
        new AlarmScheduler().cancelAlarm(inflater.getContext(), alarm);
        SQLiteHelperAlarm db = new SQLiteHelperAlarm(inflater.getContext());
        db.deleteAlarm(alarm);
        timeAlarmData.remove(position);
        notifyItemRemoved(position);
        Toast.makeText(inflater.getContext(), "Alarm Deleted", Toast.LENGTH_SHORT).show();
    }

    /**
     * Inner class that initializes the views that belong to the card_time_alarm layout file
     */
    class AlarmViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        // Below are view objects that belong to the card_time_layout layout file
        protected TextView alarmName;
        protected TextView alarmMemo;
        protected TextView alarmTimeHour;
        protected TextView alarmTimeMin;
        protected TextView alarmDays;
        protected Switch alarmIsOn;

        /**
         * Constructor with arguments for the inner class that instantiates the view objects
         * linking them each to the individual views found on the card_time_alarm layout file.
         *
         * @param itemView
         */
        public AlarmViewHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            alarmName = (TextView) itemView.findViewById(R.id.time_alarm_name);
            alarmMemo = (TextView) itemView.findViewById(R.id.time_alarm_memo);
            alarmTimeHour = (TextView) itemView.findViewById(R.id.time_alarm_hour);
            alarmTimeMin = (TextView) itemView.findViewById(R.id.time_alarm_min);
            alarmDays = (TextView) itemView.findViewById(R.id.time_alarm_days);
            alarmIsOn = (Switch) itemView.findViewById(R.id.time_alarm_switch);
        }

        /**
         * As we required a long click function to delete, this override method grants us the ability
         * to delete alarms by havng the user long press on an alarm. Method for deleting the actual
         * alarm is kept seperate.
         *
         * @param v
         * @return
         */
        @Override
        public boolean onLongClick(View v) {
            deleteAlarm(getLayoutPosition());
            return false;
        }
    }
}