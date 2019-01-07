package com.example.bookkeeper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERENCES_COUNTING = "counting";

    private SharedPreferences settings;

    int [] categoriesArr = new int[8];
    String[] categoriesArrString = {"Супермаркеты", "Рестораны", "Счета", "Развлечения", "Транспорт", "Здоровье", "Одежда", "Бытовые", "Личное", "Другое"};
    int startingDay = 0;

    Button addButton;
    TextView totalAmount;
    PieChart pieChart;
    Intent intent;
    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(this);

        settings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE); // получаем настройки
        if (settings.contains(APP_PREFERENCES_COUNTING)) {
            startingDay = settings.getInt(APP_PREFERENCES_COUNTING, 0);
        }

        dbHelper = new DBHelper(this);
        totalAmount = (TextView) findViewById(R.id.totalAmount);
        categoriesArr = dbHelper.countMonthlyByCategories(startingDay);
        totalAmount.setText(String.valueOf(dbHelper.sumTotal(categoriesArr)));

        createPieChart();
    }

    @Override
    public void onClick(View v) {
        intent = new Intent(this, ExpensesActivity.class);
        startActivity(intent);
    }

    public void createPieChart() {
        
        pieChart = (PieChart) findViewById(R.id.piechart);
        pieChart.setUsePercentValues(false);
        
        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < categoriesArr.length; i++) {
            if (categoriesArr[i] != 0) {
                yvalues.add(new Entry(categoriesArr[i], i));
                xVals.add(categoriesArrString[i]);
            }
        };

        PieDataSet dataSet = new PieDataSet(yvalues, "Траты по категориям");
        PieData data = new PieData(xVals, dataSet);

        data.setValueFormatter(new LargeValueFormatter());
        pieChart.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.setDescription(" ");
        pieChart.getLegend().setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_main:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_history:
                intent = new Intent(this, HistoryActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
