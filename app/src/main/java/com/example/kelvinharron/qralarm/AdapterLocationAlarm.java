package com.example.kelvinharron.qralarm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

/**
 * UNUSED CLASS because we did not implement geoAlarms.
 * Seperate adapter required for this type of alarm as we would be populating different views than
 * what the time alarm has.
 * <p/>
 * This class acts as an adapter between alarm objects data and card view objects.
 * When alarm objects are created we can add them to the alarm view as card layouts through this process.
 * Created by kelvinharron on 10/04/2016.
 */
public class AdapterLocationAlarm extends RecyclerView.Adapter<AdapterLocationAlarm.AlarmViewHolder> {

    private List<GeoAlarm> GeoAlarm = Collections.emptyList();
    private LayoutInflater inflater;

    /**
     * Constructor with arguments
     *
     * @param context
     * @param alarmData
     */
    public AdapterLocationAlarm(Context context, List<GeoAlarm> alarmData) {
        inflater = LayoutInflater.from(context);
        this.GeoAlarm = alarmData;
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
        //    View alarmView = inflater.inflate(R.layout.card_location_alarm, parent, false);
        //   AlarmViewHolder holder = new AlarmViewHolder(alarmView);
        return null;
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
        GeoAlarm geoAlarm = GeoAlarm.get(position);

    }

    /**
     * Gets the count of alarm objects in the database
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return GeoAlarm.size();
    }


    /**
     * Inner class that links the view objects from the card layout object and object properties together.
     */
    static class AlarmViewHolder extends RecyclerView.ViewHolder {

        public AlarmViewHolder(View itemView) {
            super(itemView);

        }
    }
}