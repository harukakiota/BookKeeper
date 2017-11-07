package com.example.bookkeeper;

import android.app.DatePickerDialog;
import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static android.view.Gravity.CENTER;
import static android.view.Gravity.LEFT;
import static android.view.Gravity.RIGHT;
import static com.example.bookkeeper.DateToString.convertFromUnix;
import static com.example.bookkeeper.DateToString.convertToUnix;
import static com.example.bookkeeper.DateToString.getDate;

public class HistoryActivity extends MainActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private static final int EDIT_ID = 1;
    private static final int DELETE_ID = 2;
    int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;
    int matchParent = LinearLayout.LayoutParams.MATCH_PARENT;

    String[] categoriesArr = {"Все", "Продукты", "Счета", "Развлечения", "Транспорт", "Здоровье", "Одежда", "Бытовые", "Другое"};
    String[] shownNumber = {"50", "100", "200", "Все"};
    Spinner categorySpinner, entriesSpinner;
    TableLayout tableLayout;
    TextView from, to;
    Button searchButton;
    Calendar calendarFrom = Calendar.getInstance();
    Calendar calendarTo = Calendar.getInstance();
    DatePickerDialog fromDatePickerDialog, toDatePickerDialog;
    DBHelper dbHelper;
    String [][] searchResults;
    Intent intent;

    int id = -1;
    boolean onCreateHistory = true;
    String crossAmount, crossDate, crossCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setContentPane();

        searchButton.performClick();
        onCreateHistory = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fromText:
                fromDatePickerDialog.show();
                break;
            case R.id.toText:
                toDatePickerDialog.show();
                break;
            case R.id.searchButton:
                dbHelper = new DBHelper(this);
                int category = (int) categorySpinner.getSelectedItemId();
                String numberOfEntries = shownNumber[(int) entriesSpinner.getSelectedItemId()];
                int toUnixValue = convertToUnix(calendarTo);
                int fromUnixValue = convertToUnix(calendarFrom);

                if (fromUnixValue <= toUnixValue) {
                    searchResults = dbHelper.searchEntries(fromUnixValue, toUnixValue, category, numberOfEntries, onCreateHistory);
                    displayResults(searchResults.length, searchResults);
                    break;
                } else {
                    Toast.makeText(this, "Введите корректные границы дат", Toast.LENGTH_SHORT).show();
                    break;
                }
        }
    }

    public void setContentPane() {

        setSpinners();

        fromDatePickerDialog = new DatePickerDialog(this, this, calendarFrom
                .get(Calendar.YEAR), calendarFrom.get(Calendar.MONTH),
                calendarFrom.get(Calendar.DAY_OF_MONTH));
        calendarFrom.set(calendarFrom.get(Calendar.YEAR) - 1, calendarFrom.get(Calendar.MONTH),
                calendarFrom.get(Calendar.DAY_OF_MONTH));
        from = (TextView) findViewById(R.id.fromText);
        from.setText(getDate(calendarFrom));
        from.setOnClickListener(this);

        toDatePickerDialog = new DatePickerDialog(this, this, calendarTo
                .get(Calendar.YEAR), calendarTo.get(Calendar.MONTH),
                calendarTo.get(Calendar.DAY_OF_MONTH));

        to = (TextView) findViewById(R.id.toText);
        to.setText(getDate(calendarTo));
        to.setOnClickListener(this);

        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);
    }

    public void displayResults(int n, String[][] searchResults) {
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        tableLayout.removeAllViews();

        setTableTitle();

        TableRow [] row = new TableRow[n];

        for (int i = 0; i < n; i++) {
            row[i] = new TableRow(this);
            tableLayout.addView(row[i]);
            row[i].setId(Integer.parseInt(searchResults[i][0]));
            registerForContextMenu(row[i]);

            TextView amount = new TextView(this);
            TextView date = new TextView(this);
            TextView category = new TextView(this);

            row[i].addView(amount, new TableRow.LayoutParams(wrapContent, wrapContent, 1f));
            row[i].addView(date, new TableRow.LayoutParams(wrapContent, wrapContent, 1f));
            row[i].addView(category, new TableRow.LayoutParams(wrapContent, wrapContent, 1f));

            amount.setText(searchResults[i][1]);
            category.setText(categoriesArr[Integer.parseInt(searchResults[i][2])+1]);
            date.setText(convertFromUnix(Integer.parseInt(searchResults[i][3])));

            row[i].setLayoutParams(new TableRow.LayoutParams(matchParent, wrapContent));
        }
    }

    public void setTableTitle() {
        TableRow title = new TableRow(this);
        tableLayout.addView(title);
        TextView amountTitle = new TextView(this);
        TextView dateTitle = new TextView(this);
        TextView categoryTitle = new TextView(this);

        title.addView(amountTitle, new TableRow.LayoutParams(wrapContent, wrapContent, 1));
        title.addView(dateTitle, new TableRow.LayoutParams(wrapContent, wrapContent, 1));
        title.addView(categoryTitle, new TableRow.LayoutParams(wrapContent, wrapContent, 1));

        amountTitle.setText("Сумма"); amountTitle.setBackgroundColor(0xff9ACD32); amountTitle.setTextSize(16);
        dateTitle.setText("Дата"); dateTitle.setBackgroundColor(0xff9ACD32); dateTitle.setTextSize(16);
        categoryTitle.setText("Категория"); categoryTitle.setBackgroundColor(0xff9ACD32); categoryTitle.setTextSize(16);

        title.setLayoutParams(new TableRow.LayoutParams(matchParent, wrapContent));
    }

    public void setSpinners() {
        // адаптер
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesArr);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner = (Spinner) findViewById(R.id.scrollCategorySpinner);
        categorySpinner.setAdapter(categoryAdapter);
        // устанавливаем обработчик нажатия
        setSpinnerListener(categorySpinner);

        ArrayAdapter<String> entriesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, shownNumber);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        entriesSpinner = (Spinner) findViewById(R.id.shownNumberSpinner);
        entriesSpinner.setAdapter(entriesAdapter);
        // устанавливаем обработчик нажатия
        setSpinnerListener(entriesSpinner);

    }

    public void setSpinnerListener(Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
            if (view.equals(fromDatePickerDialog.getDatePicker())) {
                calendarFrom.set(year, month, day);
                from.setText(getDate(calendarFrom));
            }
            if (view.equals(toDatePickerDialog.getDatePicker())) {
                calendarTo.set(year, month, day);
                to.setText(getDate(calendarTo));
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, EDIT_ID, 0, "Редактировать");
        menu.add(0, DELETE_ID, 0, "Удалить");
        id = v.getId();
        for (int i = 0; i < searchResults.length; i++) {
            if (searchResults[i][0].equals(String.valueOf(id))) {
                crossAmount = searchResults[i][1];
                crossDate = searchResults[i][3];
                crossCategory = searchResults[i][2];
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case EDIT_ID:
                intent = new Intent(this, ExpensesActivity.class);
                intent.putExtra("id", String.valueOf(id));
                intent.putExtra("amount", crossAmount);
                intent.putExtra("date", crossDate);
                intent.putExtra("category", crossCategory);
                startActivity(intent);
                id = -1;
                break;
            case DELETE_ID:
                dbHelper.deleteEntry(id);
                tableLayout.removeView(findViewById(id));
                Toast.makeText(this, "Запись удалена", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

}

