package com.faltenreich.diaguard.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.database.DatabaseDataSource;
import com.faltenreich.diaguard.database.DatabaseHelper;
import com.faltenreich.diaguard.database.Entry;
import com.faltenreich.diaguard.database.Model;
import com.faltenreich.diaguard.database.measurements.Measurement;
import com.faltenreich.diaguard.helpers.Helper;
import com.faltenreich.diaguard.helpers.PreferenceHelper;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment implements View.OnClickListener, OnChartValueSelectedListener, OnChartGestureListener {

    private static final int TEXT_SIZE = 4;
    private static final int LINE_WIDTH = 1;
    private static final int SCATTER_SHAPE_SIZE = 5;
    private static final int FACTOR_MOVING_SPEED = 2;

    private PreferenceHelper preferenceHelper;
    private DatabaseDataSource dataSource;

    private ProgressBar progressBar;
    private LineChart viewport;
    private ScatterChart chart;
    private Button buttonDate;
    private View buttonPrevious;
    private View buttonNext;

    private DateTime currentDateTime;

    private boolean isCurrentlyScrolling;

    public ChartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chart, container, false);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    private void initialize() {
        preferenceHelper = new PreferenceHelper(getActivity());
        dataSource = new DatabaseDataSource(getActivity());

        currentDateTime = DateTime.now().withHourOfDay(0).withMinuteOfHour(0);

        getComponents();
        initializeGUI();
        initializeChart();
    }

    private void getComponents() {
        progressBar = (ProgressBar) getView().findViewById(R.id.progressbar);
        viewport = (LineChart) getView().findViewById(R.id.viewport);
        chart = (ScatterChart) getView().findViewById(R.id.chart);
        buttonDate = (Button) getView().findViewById(R.id.button_date);
        buttonPrevious = getView().findViewById(R.id.button_previous);
        buttonNext = getView().findViewById(R.id.button_next);
    }

    private void initializeGUI() {
        buttonDate.setOnClickListener(this);
        buttonPrevious.setOnClickListener(this);
        buttonNext.setOnClickListener(this);

        setChartDefaultStyle(viewport);
        viewport.getAxisLeft().setEnabled(false);
        viewport.getXAxis().setDrawGridLines(false);

        setChartDefaultStyle(chart);
        chart.setOnChartValueSelectedListener(this);
        chart.setOnChartGestureListener(this);
    }

    private void setChartDefaultStyle(BarLineChartBase chartBase) {
        // General
        chartBase.setDrawGridBackground(false);
        chartBase.setHighlightEnabled(false);
        chartBase.setDoubleTapToZoomEnabled(false);
        chartBase.setScaleEnabled(false);

        // Text
        chartBase.getLegend().setEnabled(false);
        chartBase.setDescription(null);
        chartBase.setNoDataText("");
        chartBase.getXAxis().setTextSize(Helper.getDPI(getActivity(), TEXT_SIZE));
        chartBase.getAxisLeft().setTextSize(Helper.getDPI(getActivity(), TEXT_SIZE));

        // Axes
        chartBase.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chartBase.getXAxis().setDrawAxisLine(false);
        chartBase.getAxisLeft().setDrawAxisLine(false);
        chartBase.getAxisLeft().setAxisLineColor(getResources().getColor(android.R.color.darker_gray));
        chartBase.getAxisLeft().setGridColor(getResources().getColor(android.R.color.darker_gray));
        chartBase.getAxisRight().setEnabled(false);
    }

    // Add a few hours to show target day in UI not until user has scrolled halfway to it
    private DateTime getCurrentDateTimeForUI() {
        return this.currentDateTime.plusHours(14);
    }

    private void updateDateTime() {
        final int xMinimumVisibleValue = chart.getLowestVisibleXIndex();
        final int dayOfMonth = xMinimumVisibleValue / DateTimeConstants.MINUTES_PER_DAY;
        final int hourOfDay = (xMinimumVisibleValue - (dayOfMonth * DateTimeConstants.MINUTES_PER_DAY)) / DateTimeConstants.MINUTES_PER_HOUR;
        final int minuteOfHour = xMinimumVisibleValue - (dayOfMonth * DateTimeConstants.MINUTES_PER_DAY) - (hourOfDay * DateTimeConstants.MINUTES_PER_HOUR);
        currentDateTime = currentDateTime
                .withDayOfMonth(dayOfMonth + 1)
                .withHourOfDay(hourOfDay)
                .withMinuteOfHour(minuteOfHour);
        updateLabels();
    }

    private void previousDay() {
        currentDateTime = currentDateTime.minusDays(1);
        updateLabels();
        moveViewportToCurrentDateTime();
    }

    private void nextDay() {
        currentDateTime = currentDateTime.plusDays(1);
        updateLabels();
        moveViewportToCurrentDateTime();
    }

    private void showDatePicker() {
        DialogFragment fragment = new DatePickerFragment() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                currentDateTime = currentDateTime.withYear(year).withMonthOfYear(month+1).withDayOfMonth(day);
                updateLabels();
                moveViewportToCurrentDateTime();
            }
        };
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(DatePickerFragment.DATE, currentDateTime);
        fragment.setArguments(bundle);
        fragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
    }

    //region Chart

    private void initializeChart() {
        if (preferenceHelper.limitsAreHighlighted()) {
            YAxis leftAxis = chart.getAxisLeft();

            LimitLine hyperglycemia = new LimitLine(preferenceHelper.getLimitHyperglycemia(), getString(R.string.hyper));
            hyperglycemia.setLineColor(getResources().getColor(R.color.red));
            hyperglycemia.setLabel(null);
            leftAxis.addLimitLine(hyperglycemia);

            LimitLine hypoglycemia = new LimitLine(preferenceHelper.getLimitHypoglycemia(), getString(R.string.hypo));
            hypoglycemia.setLineColor(getResources().getColor(R.color.blue));
            hypoglycemia.setLabel(null);
            leftAxis.addLimitLine(hypoglycemia);
        }
        updateChartData();
        updateLabels();
    }

    private void updateLabels() {
        if(isAdded()) {
            DateTimeFormatter format = preferenceHelper.getDateFormat();
            String weekDay = getResources().getStringArray(R.array.weekdays)[getCurrentDateTimeForUI().dayOfWeek().get() - 1];
            String dateButtonText = weekDay.substring(0, 2) + "., " + format.print(getCurrentDateTimeForUI());
            buttonDate.setText(dateButtonText);
        }
    }

    private void moveViewportToCurrentDateTime(){
        // TODO: Stop current scrolling
        if(isCurrentlyScrolling) {
            return;
        }
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            public void run() {
                isCurrentlyScrolling = true;

                int lowestVisibleXIndex = chart.getLowestVisibleXIndex();
                // Target is start of day
                final int xPositionTarget = ((getCurrentDateTimeForUI().getDayOfMonth() - 1) * DateTimeConstants.MINUTES_PER_DAY);
                final int distanceInMinutes = xPositionTarget - lowestVisibleXIndex;
                final int movingSpeed = (distanceInMinutes / DateTimeConstants.MINUTES_PER_HOUR) * FACTOR_MOVING_SPEED;

                // Do until target is reached
                int currentXPosition = lowestVisibleXIndex;
                while(distanceInMinutes > 0 ?
                        currentXPosition < xPositionTarget :
                        currentXPosition > xPositionTarget) {

                    currentXPosition += movingSpeed;

                    // Wait some time
                    try {
                        Thread.sleep(10);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Move viewport
                    final int currentFinalXPosition = currentXPosition;
                    handler.post(new Runnable() {
                        public void run() {
                            if(isAdded()) {
                                chart.moveViewToX(currentFinalXPosition);
                                chart.invalidate();

                                isCurrentlyScrolling = false;
                            }
                        }
                    });
                }
            }
        };
        new Thread(runnable).start();
    }

    private void updateChartData() {
        new FetchDataTask().execute();
    }

    //endregion

    //region Viewport

    // TODO: Viewport for large devices
    private void updateViewport() {
        ArrayList<String> xLabels = new ArrayList<>();
        ArrayList<com.github.mikephil.charting.data.Entry> entries = new ArrayList<>();

        // Iterate through every day of the current selected month
        for(DateTime day = currentDateTime.dayOfMonth().withMinimumValue();
            day.isBefore(currentDateTime.dayOfMonth().withMaximumValue());
            day = day.plusDays(1)) {

            float averageOfDay = dataSource.getBloodSugarAverageOfDay(day);
            if(averageOfDay > 0) {
                averageOfDay = preferenceHelper.formatDefaultToCustomUnit(Measurement.Category.BloodSugar, averageOfDay);
                entries.add(new com.github.mikephil.charting.data.Entry(averageOfDay, day.getDayOfMonth()));
            }

            xLabels.add(day.getDayOfMonth() + ".");
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "Viewport");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setColor(getResources().getColor(android.R.color.darker_gray));
        lineDataSet.setLineWidth(Helper.getDPI(getActivity(), LINE_WIDTH));
        lineDataSet.setDrawCubic(true);
        lineDataSet.setDrawValues(false);

        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        viewport.setData(new LineData(xLabels, dataSets));
        viewport.invalidate();
    }

    //endregion

    //region Listeners

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button_date:
                showDatePicker();
                break;
            case R.id.button_previous:
                previousDay();
                break;
            case R.id.button_next:
                nextDay();
                break;
            default:
                break;
        }
    }

    // OnChartGestureListener

    @Override
    public void onChartLongPressed(MotionEvent me) {
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        updateDateTime();
    }

    // OnChartValueSelectedListener

    @Override
    public void onValueSelected(com.github.mikephil.charting.data.Entry e, int dataSetIndex, Highlight h) {
        MarkerView markerView = new ChartMarkerView (getActivity());
        chart.setMarkerView(markerView);
    }

    @Override
    public void onNothingSelected() {
        // TODO: Dismiss MarkerView
    }

    //endregion

    private class FetchDataTask extends AsyncTask<Void, Void, ScatterData> {

        @Override
        protected void onPreExecute(){
            chart.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        protected ScatterData doInBackground(Void... params) {

            // Set x-axis labels minute-precisely from first to last day of month
            ArrayList<String> xLabels = new ArrayList<>();
            String[] weekDays = getResources().getStringArray(R.array.weekdays);

            DateTime dateTime = currentDateTime.dayOfMonth()
                    .withMinimumValue()
                    .withTime(0, 0, 0, 0);
            DateTime end = currentDateTime.dayOfMonth()
                    .withMaximumValue()
                    .withTime(DateTimeConstants.HOURS_PER_DAY - 1,
                            DateTimeConstants.MINUTES_PER_HOUR - 1,
                            DateTimeConstants.SECONDS_PER_MINUTE - 1,
                            DateTimeConstants.MILLIS_PER_SECOND - 1);

            while(dateTime.isBefore(end)) {
                // Full hour
                if(dateTime.getMinuteOfHour() == 0) {
                    if(dateTime.getHourOfDay() == 0) {
                        String weekDay = weekDays[dateTime.dayOfWeek().get()-1].substring(0,2);
                        xLabels.add(/* TODO:
                         dateTime.getHourOfDay() + "\n" + */
                                weekDay + ", " + dateTime.getDayOfMonth() + "." + dateTime.getMonthOfYear());
                    }
                    else {
                        xLabels.add(Integer.toString(dateTime.getHourOfDay()));
                    }
                }
                else {
                    xLabels.add("TEST");
                }

                dateTime = dateTime.plusMinutes(1);
            }

            ArrayList<com.github.mikephil.charting.data.Entry> entries = new ArrayList<>();

            dataSource.open();

            for(DateTime day = currentDateTime.dayOfMonth().withMinimumValue();
                day.isBefore(currentDateTime.dayOfMonth().withMaximumValue().plusDays(1));
                day = day.plusDays(1)) {

                List<Entry> entriesOfDay = dataSource.getEntriesOfDay(day);
                for(Entry entry : entriesOfDay) {
                    // TODO: Generic method call
                    List<Model> models = dataSource.get(DatabaseHelper.BLOODSUGAR, null,
                            DatabaseHelper.ENTRY_ID + "=?",
                            new String[]{ Long.toString(entry.getId()) },
                            null, null, null, null);
                    for(Model model : models) {
                        Measurement measurement = (Measurement) model;
                        float value = preferenceHelper.formatDefaultToCustomUnit(Measurement.Category.BloodSugar, measurement.getValue());
                        int minutesOfMonth = (entry.getDate().getDayOfMonth() - 1) * DateTimeConstants.MINUTES_PER_DAY;
                        int minutesOfHour = entry.getDate().getHourOfDay() * DateTimeConstants.MINUTES_PER_HOUR;
                        int minutes = entry.getDate().getMinuteOfHour();
                        entries.add(new com.github.mikephil.charting.data.Entry(value, minutesOfMonth + minutesOfHour + minutes));
                    }
                }
            }

            dataSource.close();

            ScatterDataSet scatterDataSet = new ScatterDataSet(entries, DatabaseHelper.BLOODSUGAR);
            scatterDataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
            scatterDataSet.setScatterShapeSize(Helper.getDPI(getActivity(), SCATTER_SHAPE_SIZE));
            scatterDataSet.setColor(getResources().getColor(R.color.green));
            scatterDataSet.setDrawValues(false);

            ArrayList<ScatterDataSet> dataSets = new ArrayList<>();
            dataSets.add(scatterDataSet);

            return new ScatterData(xLabels, dataSets);
        }

        protected void onProgressUpdate(Void... progress) {
        }

        protected void onPostExecute(ScatterData data) {
            if(isAdded()) {
                chart.setData(data);

                chart.setVisibleXRange(DateTimeConstants.MINUTES_PER_DAY);
                chart.setVisibleYRange(
                        preferenceHelper.formatDefaultToCustomUnit(Measurement.Category.BloodSugar, 300),
                        YAxis.AxisDependency.LEFT);
                chart.getXAxis().setLabelsToSkip((DateTimeConstants.MINUTES_PER_HOUR * 4) - 1);
                chart.moveViewToX(DateTimeConstants.MINUTES_PER_DAY * (currentDateTime.getDayOfMonth() - 1));

                chart.invalidate();

                progressBar.setVisibility(View.GONE);
                chart.setVisibility(View.VISIBLE);
            }
        }
    }
}
