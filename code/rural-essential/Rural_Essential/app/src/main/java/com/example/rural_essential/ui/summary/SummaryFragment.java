package com.example.rural_essential.ui.summary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rural_essential.ui.model.LocationPoint;
import com.example.rural_essential.R;
import com.example.rural_essential.RecordDetailActivity;
import com.example.rural_essential.RecordViewModel;
import com.example.rural_essential.ui.adapters.RecordAdapter;
import com.example.rural_essential.ui.model.Record;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.snackbar.Snackbar;

import org.apache.lucene.util.SloppyMath;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class SummaryFragment extends Fragment {

    private RecordAdapter adapter;
    private RecordViewModel recordViewModel;
    private TextView textViewNoRecord;
    private PieChart pieChart;
    private CardView overallCard;
    private TextView textViewOverallDistance;
    private TextView textViewOverallTime;
    private TextView textViewOverallPerformance;
    private RecyclerView recyclerView;
    private List<Record> allrecords = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_summary, container, false);

        recyclerView = root.findViewById(R.id.recyclerView_summary);
        textViewNoRecord = root.findViewById(R.id.text_view_noRecord);
        overallCard = root.findViewById(R.id.card_overall);
        pieChart = root.findViewById(R.id.overall_performance_pieChart);
        textViewOverallDistance = root.findViewById(R.id.overall_distance);
        textViewOverallTime = root.findViewById(R.id.overall_time);
        textViewOverallPerformance = root.findViewById(R.id.overall_performance);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        RecordAdapter adapter = new RecordAdapter();
        recyclerView.setAdapter(adapter);


        recordViewModel = new ViewModelProvider(this).get(RecordViewModel.class);
        recordViewModel.getAllRecords().observe(getViewLifecycleOwner(), new Observer<List<Record>>() {
            @Override
            public void onChanged(@Nullable List<Record> records) {
                //update RecyclerView
                //Toast.makeText(getActivity(), "onChanged", Toast.LENGTH_SHORT).show();
                if(records.size() == 0){
                    textViewNoRecord.setVisibility(View.VISIBLE);
                    overallCard.setVisibility(View.INVISIBLE);
                }
                if(records.size() > 0){
                    textViewNoRecord.setVisibility(View.INVISIBLE);
                    overallCard.setVisibility(View.VISIBLE);
                }
                adapter.setRecords(records);
                allrecords = records;
                if(allrecords.size() > 0){
                    setOverallCard();
                }

            }
        });

        //setOverallCard();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {


            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Record deletedRecord = adapter.getRecordAt(viewHolder.getAdapterPosition());
                recordViewModel.delete(deletedRecord);
                adapter.notifyDataSetChanged();
                setOverallCard();
                //Toast.makeText(getContext(),"Record deleted",Toast.LENGTH_SHORT).setGravity(Gravity.BOTTOM,0,0).show();

                Snackbar.make(recyclerView, "Record " + deletedRecord.getRecordid() + " is deleted",4000)
                        .setBackgroundTint(Color.parseColor("#40a9ff"))
                        .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recordViewModel.insert(deletedRecord);
                        adapter.notifyDataSetChanged();
                        setOverallCard();
                    }
                        }).show();

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorDelete))
                        .addSwipeLeftActionIcon(R.drawable.ic_delete_black_24dp)
                        .addSwipeLeftLabel("Delete")
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        }).attachToRecyclerView(recyclerView);



        // Set the behavior of onitemclicklistener to start the record detail activity
        adapter.setOnItemClickListener(new RecordAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Record record) {
                // Pass list to intent
                Intent intent = new Intent(getActivity(), RecordDetailActivity.class);
                intent.putExtra("locations", (Serializable) record.getLocationPoint());
                intent.putExtra("speedLimit", (Serializable) record.getSpeedLimit());
                intent.putExtra("currentSpeed",(Serializable) record.getCurrentSpeed());
                intent.putExtra("startTime",(Serializable)record.getTimeStart());
                startActivity(intent);
            }
        });

        return root;
    }

    // set information for overall card displayed in the top of summary screen
    private void setOverallCard() {
        setOverallPerformance();
        setOverallDistance();
        setOverallTime();
    }

    // set overall distance
    private void setOverallDistance() {
        Double overallDistance = 0.0;
        String str = "";
        for (int i = 0; i < allrecords.size(); i++) {
            List<LocationPoint> locations = allrecords.get(i).getLocationPoint();
            for (int j = 0; j < locations.size()-1; j++) {
                double lat1 = locations.get(j).getLantitude();
                double lon1 = locations.get(j).getLongitude();
                double lat2 = locations.get(j+1).getLantitude();
                double lon2 = locations.get(j+1).getLongitude();
                double distance = calculateDistanceInMeters(lat1,lon1,lat2,lon2);
                overallDistance += distance;
                System.out.println(overallDistance);
            }
        }
        DecimalFormat df = new DecimalFormat("#.00");
        if(overallDistance == 0.0){
            str = "0";
        }
        else {str = df.format(overallDistance);}
        textViewOverallDistance.setText("Total distance: "+ "\n" + str + "m");
    }

    //Set overall time text
    @SuppressLint("SetTextI18n")
    private void setOverallTime() {
        long overallDuration = 0;
        //String str = "";
        for (int i = 0; i < allrecords.size(); i++) {
           Date startDate = allrecords.get(i).getTimeStart();
           Date endDate = allrecords.get(i).getTimeEnd();
           long duration  = endDate.getTime() - startDate.getTime();
           overallDuration += duration;

        }
        long durationInSeconds = TimeUnit.MILLISECONDS.toSeconds(overallDuration);
        long SECONDS_IN_A_MINUTE = 60;
        long MINUTES_IN_AN_HOUR = 60;
        long HOURS_IN_A_DAY = 24;
        long DAYS_IN_A_MONTH = 30;
        long MONTHS_IN_A_YEAR = 12;

        long sec = (durationInSeconds >= SECONDS_IN_A_MINUTE) ? durationInSeconds % SECONDS_IN_A_MINUTE : durationInSeconds;
        long min = (durationInSeconds /= SECONDS_IN_A_MINUTE) >= MINUTES_IN_AN_HOUR ? durationInSeconds%MINUTES_IN_AN_HOUR : durationInSeconds;
        long hrs = (durationInSeconds /= MINUTES_IN_AN_HOUR) >= HOURS_IN_A_DAY ? durationInSeconds % HOURS_IN_A_DAY : durationInSeconds;
        long days = (durationInSeconds /= HOURS_IN_A_DAY) >= DAYS_IN_A_MONTH ? durationInSeconds % DAYS_IN_A_MONTH : durationInSeconds;
        long months = (durationInSeconds /=DAYS_IN_A_MONTH) >= MONTHS_IN_A_YEAR ? durationInSeconds % MONTHS_IN_A_YEAR : durationInSeconds;
        long years = (durationInSeconds /= MONTHS_IN_A_YEAR);

        String duration = getDuration(sec,min,hrs,days,months,years);
        textViewOverallTime.setText("Total time:" + "\n" + duration );

    }

    private static String getDuration(long secs, long mins, long hrs, long days, long months, long years) {
        StringBuffer sb = new StringBuffer();
        String EMPTY_STRING = "";
        sb.append(years > 0 ? years + (years > 1 ? " years " : " year "): EMPTY_STRING);
        sb.append(months > 0 ? months + (months > 1 ? " months " : " month "): EMPTY_STRING);
        sb.append(days > 0 ? days + (days > 1 ? " days " : " day "): EMPTY_STRING);
        sb.append(hrs > 0 ? hrs + (hrs > 1 ? " hours " : " hour "): EMPTY_STRING);
        sb.append(mins > 0 ? mins + (mins > 1 ? " mins " : " min "): EMPTY_STRING);
        sb.append(secs > 0 ? secs + (secs > 1 ? " secs " : " secs "): EMPTY_STRING);
        return sb.toString();
    }

    // Set the performance rate of pie chart and the text
    private void setOverallPerformance() {
        double totalCount = 0;
        double overSpeedCount = 0;
        for (int i = 0; i < allrecords.size() ; i++) {
            Record record = allrecords.get(i);
            List<Integer> speedLimit = record.getSpeedLimit();
            List<Double> currenSpeed = record.getCurrentSpeed();
            for (int j = 0; j < speedLimit.size(); j++) {
                if(speedLimit.get(j) < currenSpeed.get(j)){
                    totalCount += 1;
                    overSpeedCount += 1;
                }
                else {
                    totalCount += 1;
                }
            }
        }

        double rates = ((totalCount - overSpeedCount) / totalCount) * 100;
        DecimalFormat df = new DecimalFormat("#");
        String str = df.format(rates);
        textViewOverallPerformance.setText("Total performance: " + "\n" + str);
        Float rate = Float.valueOf(str);

        ArrayList<PieEntry> performance = new ArrayList<>();
        if(rate == 100.0){
            performance.add(new PieEntry(rate,"INLIMIT"));
        }
        else {
            performance.add(new PieEntry(rate,"INLIMIT"));
            performance.add(new PieEntry( 100 - rate,"overspeed"));
        }

        PieDataSet pieDataSet = new PieDataSet(performance, "performance");
        pieDataSet.setColors(Color.parseColor("#CEDA38"),Color.parseColor("#CB4747"));
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(10f);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.setMinOffset(10f);
        pieChart.getLegend().setEnabled(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setTransparentCircleRadius(58f);
        //holder.pieChart.setCenterText("performance");
        pieChart.invalidate();
        pieChart.animate();
    }

    //  calculation based on two geo points
    public double calculateDistanceInMeters(double lat1, double long1, double lat2,
                                            double long2) {

        double dist = SloppyMath.haversinMeters(lat1, long1, lat2, long2);
        return dist;
    }

}
