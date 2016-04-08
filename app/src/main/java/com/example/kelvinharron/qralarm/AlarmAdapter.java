package com.example.kelvinharron.qralarm;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * This class acts as an adapter between alarm objects data and card view objects.
 * When alarm objects are created we can add them to the alarm view as card layouts through this process.
 * TODO: STUB CLASS GOTTA REWORK IT FROM TUTORIAL. DEPENDED ON LOCAL ALARM OBJECT
 * Created by kelvinharron on 07/04/2016.
 */
public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    /**
     * AlarmList is a left over variable from a local tutorial I completed before importing into the project.
     * It needs to be linked to our database of alarm objects.
     * OBJECT IS PLACEHOLDER AS getItemCount() method will not compile without an object type in the ara
     */
    private List<Object> alarmList;
    /**
     * Default constructor
     */
    //  public AlarmAdapter(List<> alarmList) {
    //      this.alarmList = alarmList;
    //   }

    /**
     * Creates an alarm object card view.
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public AlarmAdapter.AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_card, parent, false);
        return new AlarmViewHolder(itemView);
    }

    /**
     * Method binds database alarm object data to the view objects for text fields.
     * Allows us to set the name of the alarm, memos etc that are stored each time an alarm is created.
     *
     * @param alarmViewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(AlarmAdapter.AlarmViewHolder alarmViewHolder, int position) {
        //AlarmInfo alarms = alarmList.get(position);
        //alarmViewHolder.vTitle.setText(alarms.title);
        //alarmViewHolder.vMemo.setText(alarms.memo);
    }

    /**
     * Inner class, left over from the tutorial and needs to be reworked.
     */
    public static class AlarmViewHolder extends RecyclerView.ViewHolder {
        protected TextView vTitle;
        protected TextView vMemo;

        public AlarmViewHolder(View itemView) {
            super(itemView);
            vTitle = (TextView) itemView.findViewById(R.id.alarm_name);
            vMemo = (TextView) itemView.findViewById(R.id.alarm_memo);
        }
    }

    /**
     * Gets the count of alarm objects in the database
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return alarmList.size();
    }
}