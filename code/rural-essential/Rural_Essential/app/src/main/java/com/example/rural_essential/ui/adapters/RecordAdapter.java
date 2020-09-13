package com.example.rural_essential.ui.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rural_essential.ui.model.LocationPoint;
import com.example.rural_essential.R;
import com.example.rural_essential.ui.model.Record;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.apache.lucene.util.SloppyMath;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordHolder> {

    private List<Record> records = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false);
        return new RecordHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordHolder holder, int position) {
        Record currentRecord = records.get(position);
        //holder.textViewTitle.setText("Record" + currentRecord.getRecordid());
        String startTime = "Start time:  " + DateFormat(currentRecord.getTimeStart());
        String endTime = "End time:  " + DateFormat(currentRecord.getTimeEnd());
        String startDay = DayFormat(currentRecord.getTimeStart());
        String performanceRate = RatePerformance(currentRecord.getCurrentSpeed(), currentRecord.getSpeedLimit());
        String totalDisstance = setTotalDistance(currentRecord.getLocationPoint());
        PieData pieData = setPieChart(performanceRate);
        holder.textViewPerformance.setText("Performance:  " + performanceRate);
        holder.textViewStartTime.setText(startTime);
        holder.textViewEndTime.setText(endTime);
        holder.textViewStartDay.setText(startDay);
        holder.textViewTotalDistance.setText(totalDisstance);
        holder.pieChart.setData(pieData);
        holder.pieChart.getLegend().setEnabled(false);
        holder.pieChart.getDescription().setEnabled(false);
        holder.pieChart.setDrawEntryLabels(false);
        holder.pieChart.setTransparentCircleRadius(58f);
        //holder.pieChart.setCenterText("performance");
        holder.pieChart.animate();
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public void setRecords(List<Record> records) {
        this.records = records;
        notifyDataSetChanged();
    }

    public Record getRecordAt(int position){
        return records.get(position);
    }

    class RecordHolder extends RecyclerView.ViewHolder {
        //private TextView textViewTitle;
        private TextView textViewStartTime;
        private TextView textViewEndTime;
        private TextView textViewPerformance;
        private TextView textViewStartDay;
        private TextView textViewTotalDistance;
        private PieChart pieChart;

        public RecordHolder(@NonNull View itemView) {
            super(itemView);
            //textViewTitle = itemView.findViewById(R.id.text_view_recordTitle);
            textViewStartTime = itemView.findViewById(R.id.text_view_startTime);
            textViewEndTime = itemView.findViewById(R.id.text_view_endTime);
            textViewPerformance = itemView.findViewById(R.id.text_view_performance);
            textViewStartDay = itemView.findViewById(R.id.text_view_startDay);
            textViewTotalDistance = itemView.findViewById(R.id.text_view_totalDistance);
            pieChart = itemView.findViewById(R.id.pie_Chart_Performance);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(records.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Record record);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private PieData setPieChart(String rate) {
        Float rateInLimit = Float.valueOf(rate);

        ArrayList<PieEntry> performance = new ArrayList<>();
        if(rateInLimit == 100.0){
            performance.add(new PieEntry(rateInLimit,"INLIMIT"));
        }
        else {
            performance.add(new PieEntry(rateInLimit,"INLIMIT"));
            performance.add(new PieEntry( 100 - rateInLimit,"overspeed"));
        }

        PieDataSet pieDataSet = new PieDataSet(performance, "performance");
        pieDataSet.setColors(Color.parseColor("#CEDA38"),Color.parseColor("#CB4747"));
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(10f);

        PieData pieData = new PieData(pieDataSet);
        return pieData;
    }

    // Set date value to preferred day format
    public static String DateFormat(Date oldDate) {
        String newDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        try {
            //Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(oldDate);
            newDate = dateFormat.format(oldDate);
        } catch (Exception e) {
            e.printStackTrace();
            newDate = "";
        }

        return newDate;
    }

    // Set date value to preferred day format
    public static String DayFormat(Date oldDate) {
        String newDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");
        try {
            //Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(oldDate);
            newDate = dateFormat.format(oldDate);
        } catch (Exception e) {
            e.printStackTrace();
            newDate = "";
        }

        return newDate;
    }

    //count performance base on the persentage user is overspeed or not
    private String RatePerformance(List<Double> currentSpeed, List<Integer> speedLimit) {
        double length = currentSpeed.size();
        double count = 0.0;
        for (int i = 0; i < length; i++) {
            if (currentSpeed.get(i) > speedLimit.get(i).doubleValue()) {
                count += 1.0;
            }
        }
        double rate = ((length - count) / length) * 100;
        DecimalFormat df = new DecimalFormat("#");
        String str = df.format(rate);
        return str;
    }

    //Calculate the distance travelled by user
    private String setTotalDistance(List<LocationPoint> locations) {
        double totalDistance = 0.0;
        String str = "";
        if(locations.size() <= 1){
            return "0";
        }
        try {
            for (int i = 0; i < locations.size()-1; i++) {
                double lat1 = locations.get(i).getLantitude();
                double lon1 = locations.get(i).getLongitude();
                double lat2 = locations.get(i+1).getLantitude();
                double lon2 = locations.get(i+1).getLongitude();
                double distance = calculateDistanceInMeters(lat1,lon1,lat2,lon2);
                totalDistance += distance;
            }
            DecimalFormat df = new DecimalFormat("#.00");
            if(totalDistance == 0.0){
                str = "0";
            }
            else{ str = df.format(totalDistance); }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return ("Total distance: " + str + "m");
    }

    //  calculation based on two geo points
    public double calculateDistanceInMeters(double lat1, double long1, double lat2,
                                            double long2) {

        double dist = SloppyMath.haversinMeters(lat1, long1, lat2, long2);
        return dist;
    }
}
