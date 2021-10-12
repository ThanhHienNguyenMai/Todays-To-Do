package com.example.goodhabits;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.datepicker.MaterialCalendar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;

import android.provider.MediaStore;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.w3c.dom.Text;

import petrov.kristiyan.colorpicker.ColorPicker;
import sun.bob.mcalendarview.MCalendarView;

import static java.security.AccessController.getContext;

public class IndividualPage extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, AdapterView.OnItemSelectedListener {

    private ProgressBar mProgressBar;
    private TextView generalTextView;
    private EditText generalEditText;
    private TextView percentageText;
    private TextView streakDaysText;
    private TextView goalNameText;
    private Toolbar toolbar;
    private ImageButton image;
    public static int hour;
    public static int minute;
    public static String amPmM;
    private String amPm;


    static String color = "#CE93D8";
    private int[] monthData = {15, 19, 24, 27, 14, 18, 20, 18, 27, 31, 30, 31};
    //private int[] monthData = {1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1};
    private ArrayList<int[]> yearData = new ArrayList<int[]>();
    private ArrayList<int[]> oldDailyData = new ArrayList<int[]>();
    private int[] dailyData = new int[366];
    private int streakStartDate;
    private int daysBetweenStreak = 6;
    private int timePeriodCounter = 7;
    private int completeStreakCounter = 3;
    private int bestStreak;
    private int previousYearStreak;



    public static String goalName = "Goal Name Placeholder Text";
    public int currentStreak = 87;
    public int streakEndDate;
    private int totalDays = 100;
    private int percent = (int) (currentStreak * 100.0f) / totalDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_page);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        //Set goal name
        goalNameText = (TextView) findViewById(R.id.textView);
        goalNameText.setText(goalName);

        //Set reminder time
        if(amPmM !=null) {
            if(hour == 12) {
                generalTextView = (TextView) findViewById(R.id.textView4);
                generalTextView.setText(String.format("%02d:%02d", hour, minute) + " " + amPmM);
            } else if (hour >12) {
                generalTextView = (TextView) findViewById(R.id.textView4);
                generalTextView.setText(String.format("%02d:%02d", hour-12, minute) + " " + amPmM);
            } else {
                generalTextView = (TextView) findViewById(R.id.textView4);
                generalTextView.setText(String.format("%02d:%02d", hour, minute) + " " + amPmM);
            }
        }



        /*
        //Set progress bar percent
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setProgress(currentStreak);

        //Set percentage text and streak day text
        percentageText = (TextView) findViewById(R.id.textView3);
        streakDaysText = (TextView) findViewById(R.id.textView2);
        streakDaysText.setText(String.valueOf(currentStreak) + " Day Streak!");
        percentageText.setText(String.valueOf(percent) +"%");
        */
        streakDaysText = (TextView) findViewById(R.id.textView2);
        streakDaysText.setText(String.valueOf(currentStreak) + " Day Streak!");

        //Back arrow
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Remove toolbar title
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Set up image button for Reminders
        image = (ImageButton) findViewById(R.id.imageButton2);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time();

            }
        });

        //Set up text "button" for Text of Reminder (the text that says time)
        TextView text = (TextView) findViewById(R.id.textView4);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               time();
            }
        });

        //Bar chart
        BarChart barChart = findViewById(R.id.bar_chart);
        ArrayList<BarEntry> barEntryArray = new ArrayList<>();
        barEntryArray.add(new BarEntry(0, 15));
        barEntryArray.add(new BarEntry(1, 19));
        barEntryArray.add(new BarEntry(2, 24));

        /*
        barEntryArray.add(new BarEntry(3, 3));
        barEntryArray.add(new BarEntry(4, 4));
        barEntryArray.add(new BarEntry(5, 5));
        barEntryArray.add(new BarEntry(6, 6));
        barEntryArray.add(new BarEntry(7, 7));
        barEntryArray.add(new BarEntry(8, 8));
        barEntryArray.add(new BarEntry(9, 9));
        barEntryArray.add(new BarEntry(10, 10));
        barEntryArray.add(new BarEntry(11, 11));
        */


        BarDataSet barDataSet = new BarDataSet(barEntryArray, "barEntryArray");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);


        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);

        barChart.setDrawGridBackground(false);

        //barChart.getAxisRight().setDrawGridLines(false);
        //arChart.getAxisLeft().setDrawGridLines(false);

        //Removes Vertical graph lines
        //barChart.getXAxis().setEnabled(false);

        //Removes Zoom in feature
        barChart.setScaleEnabled(false);

        //Remove clicking on data bars
        barChart.setTouchEnabled(false);

        //Remove legend
        barChart.getLegend().setEnabled(false);

        //Turn on chart border
        barChart.setDrawBorders(true);

        //Turn off description
        barChart.getDescription().setEnabled(false);

        //Format data set from float to int, uses IntegerValueFormatter class
        barData.setValueFormatter(new IntegerValueFormatter());
        barChart.setData(barData);

        //Sets Year Data for "Year" Filtered Graph
        setYearData(2021, 295);
        setYearData(2022, 236);
        setYearData(2023, 300);
        setYearData(2024, 359);
        setYearData(2025, 320);

        //Initialize dailyData array
        initalizeDailyData();


        XAxis xAxis = barChart.getXAxis();
        final String[] months = {"Jan.", "Feb.", "Mar.", "Apr.", "May", "June", "July", "Aug.", "Sept.", "Oct.", "Nov.", "Dec."};
        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
        barChart.getXAxis().setDrawLabels(true);
        //barChart.getXAxis().setLabelCount(12);
        barChart.getXAxis().setLabelCount(3);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(-1);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getAxisLeft().setAxisMaximum(35);


        Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numbers, R.layout.spinner_item);
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numbers, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mySpinner.setAdapter(adapter);
        mySpinner.setOnItemSelectedListener(this);


        //xAxis.setAxisMaximum(2);

        //BarData data2 = barChart.getData();


        //MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();

        //builder.setTitleText("THIS IS A DATE");
        //MaterialDatePicker materialDatePicker = builder.build();

        //materialDatePicker.show(getSupportFragmentManager(),  "DATE_PICKER");

        MCalendarView calendarView = ((MCalendarView) findViewById(R.id.calendarView2));
        //calendarView.markDate(2021, 1, 7);

        //For marking 20 days in calendar
        int foo = 20;

        for (int i =0; i<foo; i++) {
            calendarView.markDate(2021, 1, i);

        }

        //For getting month and year values in ints
        java.util.Date date= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int year = Calendar.getInstance().get(Calendar.YEAR);

        String[] monthsArray = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

        //Showing Month in Text form
        generalTextView = (TextView) findViewById(R.id.textView6);
        generalTextView.setText(String.format(monthsArray[month]) + " " + String.valueOf(year));



        //Setting up Edit Reminders
        image = (ImageButton) findViewById(R.id.imageButton3);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRadio();


            }
        });

        changeColor(color);

        //Complete Button
        Button button = findViewById(R.id.completeButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(dailyData[266]);
                setDailyData();
                calendarView.markDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                //setDailyStreak();
                streakDaysText.setText(String.valueOf(currentStreak) + " Day Streak!");
                //streakDaysText.invalidate();
                System.out.println(dailyData[66]);
          }
        });

    }

    public void setColor(String color) {
        this.color = color;

    }

    public void changeColor(String color) {
        //Change toolbar colour -- to link with Jasmine's part
        Toolbar nToolbar = (Toolbar) findViewById(R.id.toolbar2);
        nToolbar.setBackgroundColor(Color.parseColor(color));

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar3);
        mToolbar.setBackgroundColor(Color.parseColor(color));

        //Change Image Button Colours
        image = (ImageButton) findViewById(R.id.imageButton3);
        image.setBackgroundColor(Color.parseColor(color));

        image = (ImageButton) findViewById(R.id.imageButton2);
        image.setBackgroundColor(Color.parseColor(color));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }


    public void callRadio() {


        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Edit How Often Goal Repeats");




        final LayoutInflater factory = getLayoutInflater();
        final View textEntryView = factory.inflate(R.layout.radiobutton_layout, null);

        EditText text = (EditText) textEntryView.findViewById(R.id.editTextTextPersonName2);
        text.setVisibility(View.INVISIBLE);
        alert.setView(R.layout.radiobutton_layout);


        Activity parentActivity = this.getParent();
        if (parentActivity != null)
        {
            View landmarkEditNameView = (EditText) parentActivity.findViewById(R.id.editTextTextPersonName2);
            landmarkEditNameView.setVisibility(View.INVISIBLE);
        }

        RadioGroup rg = (RadioGroup) textEntryView.findViewById(R.id.radioGroup);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(RadioGroup group, int checkedId) {

                  findRadioButton(checkedId);

                  RadioButton rbLanguage = findViewById(checkedId);


                  System.out.println("PRINT BUTTON NUM MAYBE:     " + checkedId);




              }
        });


        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.show();
    }


    public int getDailyStreak() {
        return currentStreak;
    }


    private void findRadioButton(int checkedId) {
        switch (checkedId) {
            case R.id.radioButton:
                System.out.println("CASE 1 SDSADSADSADD");
                break;
            case R.id.radioButton2:
                System.out.println("CASE 2 SDSADSADSADD");
                break;
            case R.id.radioButton15:
                System.out.println("CASE 15 SDSADSADSADD");
                break;
            case R.id.radioButton4:
                System.out.println("CASE 4 SDSADSADSADD");
                break;
            case R.id.radioButton5:
                System.out.println("CASE 5 SDSADSADSADD");
                break;


        }
    }
    //Method for changing reminder time
    public void time() {
        TimePickerDialog timePickerDialog;
        Calendar calendar;
        int currentHour;
        int currentMinute;

        generalTextView = (TextView) findViewById(R.id.textView4);
        calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(IndividualPage.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                if (hourOfDay == 12) {
                    amPm = "PM";
                    generalTextView.setText(String.format("%02d:%02d", hourOfDay, minutes) + " " + amPm);
                    amPmM = amPm;
                } else if (hourOfDay > 12) {
                    amPm = "PM";
                    generalTextView.setText(String.format("%02d:%02d", hourOfDay-12, minutes) + " " + amPm);
                    amPmM = amPm;

                } else {
                    amPm = "AM";
                    generalTextView.setText(String.format("%02d:%02d", hourOfDay, minutes) + " " + amPm);
                    amPmM = amPm;

                }
                hour = hourOfDay;
                minute = minutes;
            }
        }, currentHour, currentMinute, false);

        timePickerDialog.show();
    }

    public void showEditPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.editpopup_menu);
        popup.show();
    }



    //Clock function for clock popup and time setting


    //Menu for The 3 dot button
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        switch (item.getItemId()) {
            case R.id.editGoal:
                alert.setTitle("Edit Goal Name");
                alert.setMessage("Enter New Goal Name");

                //Sets up edit text to take user input
                generalEditText = new EditText(this);
                alert.setView(generalEditText);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goalName = generalEditText.getText().toString();
                        goalNameText = (TextView) findViewById(R.id.textView);
                        goalNameText.setText(goalName);

                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alert.show();



                return true;

            case R.id.deleteGoal:
                Toast.makeText(this, "Delete Goal clicked", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.goalColor:
                openColourPicker();
                return true;

            default:
                return false;
            }

    }


    public void openColourPicker(){

        final ColorPicker colourPicker = new ColorPicker(this);
        ArrayList<String> colours = new ArrayList<>();
        colours.add("#fc5a4c");//red
        colours.add("#fca14c");//orange
        colours.add("#fce74c");//yellow
        colours.add("#a7fc4c");//light green
        colours.add("#06bf34");//green
        colours.add("#4cd3fc");//light blue
        colours.add("#1644db");//blue
        colours.add("#7e4cfc");//purple

        colourPicker.setColors(colours)
                .setColumns(4)
                .setRoundColorButton(true)
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int colour) {
                        //Sets the color variable to the new color
                        setColor(colours.get(position));

                        //Change the colors on the page
                        changeColor(colours.get(position));


                    }

                    @Override
                    public void onCancel() {
                        //does nothing
                    }
                }).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        makeGraph(position);


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    //Initalizes dailyData array, sets all 366 spots to -1, and index 0 to year
    public void initalizeDailyData() {
        Arrays.fill(dailyData, -1);
        dailyData[0] = Calendar.getInstance().get(Calendar.YEAR);

    }

    public void setStreakDay(String text) {
        currentStreak = Integer.parseInt(text);

    }

    public void setDailyStreak() {
        //dailyData[] = 1 ;
        currentStreak +=1;
    }

    public void setBestStreak() {
        if (currentStreak > bestStreak) {
            bestStreak = currentStreak;
        }
    }


    public void setIncompleteDays() {

        //For every day up to 1 day before today
        for (int i = 0; i < Calendar.getInstance().get(Calendar.DAY_OF_YEAR); i++) {

            //If dailyData[i] == -1, change to 0 because it is incomplete day
            if(dailyData[i] == -1) {
                dailyData[i] = 0;
            }
        }

    }

    public void calculateDailyStreak() {
        if(currentStreak == 0) {
            streakStartDate = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        }
        //#Number of days to complete all habits for streak
        int counter2 = 7;
        //# of times to complete
        int counter3 = 3;

        int streak = 0;

        int streakCounter = 0;
        //Increment day data
        int i = 0;

        while(true) {
            //If dailyData[i] == -1, that is future date so break
            if(dailyData[streakStartDate + i] == -1) {
                if(streak + previousYearStreak > currentStreak) {
                    currentStreak = streak + previousYearStreak;
                }
                break;
            }

            //If dailyData[i] == 1, habit is done
            if(dailyData[streakStartDate + i] == 1 && counter3 > 0) {
                streak++;
                counter2--;
                counter3--;
            } else if(dailyData[streakStartDate+i] == 1 && counter3 == 0) {
                counter2--;
            } else {
                counter2--;
            }

            //When counter2 == 0, counter3 must = 0 or else streak is broken
            if(counter2 == 0) {
                if (counter3 == 0) {
                    //Counter3 == 0, successful completion so reset counters
                    counter2 = 7;
                    counter3 = 3;
                } else if (counter3 > 0) {
                    //Counter > 0 means streak broken
                    streakEndDate = streakStartDate + i;
                    previousYearStreak = 0;
                    currentStreak = 0;
                    break;
                }
            }

            //Increment day by 1
            i++;

        }


    }

    public void setDailyData() {
        dailyData[Calendar.getInstance().get(Calendar.DAY_OF_YEAR)] = 1;
        calculateDailyStreak();
    }

    public void setNewDailyDataYear() {
        if(Calendar.getInstance().get(Calendar.DAY_OF_YEAR) == 1) {
            oldDailyData.add(dailyData);
            initalizeDailyData();
            if(currentStreak > 0) {
                streakStartDate = 1;
                previousYearStreak = currentStreak;
            }
        }

    }

    public void setYearData(int year, int streakData) {
        if(yearData.size() == 0) {
            int[] newYearData = {Calendar.getInstance().get(Calendar.YEAR),0};
            yearData.add(newYearData);
        }

        int[] list;
        for(int i = 0; i < yearData.size(); i++) {
            list = yearData.get(i);
            if(list[0] == year) {
                list[1] = list[1] + streakData;
                yearData.set(i, list);
                break;
            } else if(i+1 == yearData.size()) {
                int[] newYearData = {year, streakData};
                yearData.add(newYearData);
                break;
            } else {

            }

        }
    }

    public void makeGraph(int type) {
        if (type == 0) {
            BarChart barChart = findViewById(R.id.bar_chart);
            ArrayList<BarEntry> barEntryArray = new ArrayList<>();

            int monthValue = monthData[Calendar.getInstance().get(Calendar.MONTH)];

            barEntryArray.add(new BarEntry(0, monthValue));

            BarDataSet barDataSet = new BarDataSet(barEntryArray, "barEntryArray");
            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            barDataSet.setValueTextColor(Color.BLACK);
            barDataSet.setValueTextSize(16f);


            BarData barData = new BarData(barDataSet);



            //Format data set from float to int, uses IntegerValueFormatter class
            barData.setValueFormatter(new IntegerValueFormatter());
            barChart.setData(barData);


            barChart.getXAxis().setLabelCount(1);
            barChart.getAxisLeft().setAxisMaximum(monthValue + 3);
            final String[] months = {"Jan.", "Feb.", "Mar.", "Apr.", "May", "June", "July", "Aug.", "Sept.", "Oct.", "Nov.", "Dec."};
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));
            //Refreshes graph
            barChart.invalidate();


        } else if(type == 1) {
            //Month

            BarChart barChart = findViewById(R.id.bar_chart);
            ArrayList<BarEntry> barEntryArray = new ArrayList<>();



            //For setting X Axis Label Count
            int activeMonthCount = 0;

            //For setting Y Axis Limit
            int maxYAxis = 0;

            for(int i = 0; i < monthData.length;i++) {
                if (monthData[i] == -1) {
                } else {
                    barEntryArray.add(new BarEntry(i, monthData[i]));
                    activeMonthCount++;
                    if (maxYAxis < monthData[i]) {
                        maxYAxis = monthData[i];
                    }
                }

            }

            BarDataSet barDataSet = new BarDataSet(barEntryArray, "barEntryArray");
            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            barDataSet.setValueTextColor(Color.BLACK);
            barDataSet.setValueTextSize(16f);


            BarData barData = new BarData(barDataSet);



            //Format data set from float to int, uses IntegerValueFormatter class
            barData.setValueFormatter(new IntegerValueFormatter());
            barChart.setData(barData);


            barChart.getXAxis().setLabelCount(activeMonthCount);
            barChart.getAxisLeft().setAxisMaximum(maxYAxis + 5);
            final String[] months = {"Jan.", "Feb.", "Mar.", "Apr.", "May", "June", "July", "Aug.", "Sept.", "Oct.", "Nov.", "Dec."};
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));
            //Refreshes graph
            barChart.invalidate();
        } else if(type == 2) {
            BarChart barChart = findViewById(R.id.bar_chart);
            ArrayList<BarEntry> barEntryArray = new ArrayList<>();


            int quarterOne = -1;
            int quarterTwo = -1;
            int quarterThree = -1;
            int quarterFour = -1;
            int quarterCount = 0;
            int maxYAxis = 0;

            for(int i = 0; i <monthData.length; i++) {
                if (monthData[i] != -1 && i < 3 ) {
                    quarterOne += monthData[i];
                } else if(monthData[i] != -1 && i < 6) {
                    quarterTwo += monthData[i];
                } else if(monthData[i] != -1 && i < 9) {
                    quarterThree += monthData[i];
                } else if(monthData[i] != -1 && i < 12) {
                    quarterFour += monthData[i];
                } else {

                }
            }

            if(quarterOne != -1) {
                barEntryArray.add(new BarEntry(0, quarterOne));
                quarterCount ++;
                if(maxYAxis < quarterOne) {
                    maxYAxis = quarterOne;
                }
            }

            if(quarterTwo != -1) {
                barEntryArray.add(new BarEntry(1, quarterTwo));
                quarterCount ++;
                if(maxYAxis < quarterTwo) {
                    maxYAxis = quarterTwo;
                }
            }

            if(quarterThree != -1) {
                barEntryArray.add(new BarEntry(2, quarterThree));
                quarterCount ++;
                if(maxYAxis < quarterThree) {
                    maxYAxis = quarterThree;
                }
            }

            if(quarterFour != -1) {
                barEntryArray.add(new BarEntry(3, quarterFour));
                quarterCount ++;
                if(maxYAxis < quarterFour) {
                    maxYAxis = quarterFour;
                }
            }



            BarDataSet barDataSet = new BarDataSet(barEntryArray, "barEntryArray");
            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            barDataSet.setValueTextColor(Color.BLACK);
            barDataSet.setValueTextSize(16f);


            BarData barData = new BarData(barDataSet);



            //Format data set from float to int, uses IntegerValueFormatter class
            barData.setValueFormatter(new IntegerValueFormatter());
            barChart.setData(barData);


            barChart.getXAxis().setLabelCount(quarterCount);
            barChart.getAxisLeft().setAxisMaximum(maxYAxis + 10);
            final String[] months = {"Jan. - Mar.", "Apr. - June", "July - Sept.", "Oct. - Dec."};
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));
            //Refreshes graph
            barChart.invalidate();
        } else if(type ==3) {

            BarChart barChart = findViewById(R.id.bar_chart);
            ArrayList<BarEntry> barEntryArray = new ArrayList<>();

            int[] list;
            int max = -1;


            String[] xLabels = new String[yearData.size()];

            for(int i = 0; i < yearData.size();i++) {
                list = yearData.get(i);
                xLabels[i] = String.valueOf(list[0]);
                barEntryArray.add(new BarEntry(i, list[1]));

                if(list[1] > max) {
                    max = list[1];
                }
            }


            BarDataSet barDataSet = new BarDataSet(barEntryArray, "barEntryArray");
            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            barDataSet.setValueTextColor(Color.BLACK);
            barDataSet.setValueTextSize(16f);

            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));

            BarData barData = new BarData(barDataSet);



            //Format data set from float to int, uses IntegerValueFormatter class
            barData.setValueFormatter(new IntegerValueFormatter());
            barChart.setData(barData);

            barChart.getXAxis().setLabelCount(yearData.size());

            barChart.getAxisLeft().setAxisMaximum(max+40);

            //Refreshes graph
            barChart.invalidate();
            //Year


        } else {
            //Other? Nothing?

        }

    }

}