package com.example.bookkeeper;

import android.app.DatePickerDialog;
import android.content.Intent;
import java.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.bookkeeper.DateToString.getDate;

public class ExpensesActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener{

    private static final int UNIX_CONVERT = 86400000;

    String[] categoriesArr = {"Супермаркеты", "Рестораны", "Счета", "Развлечения", "Транспорт", "Здоровье", "Одежда", "Бытовые", "Личное", "Другое"};
    Button okButton;
    Spinner spinner;
    TextView textDate;
    EditText amount;
    Calendar date = Calendar.getInstance();
    DatePickerDialog datePickerDialog;
    DBHelper dbHelper;
    Intent intent;

    int id = -1;
    String crossAmount, crossDate, crossCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        setContentPane();

        intent = getIntent();
        String idString = intent.getStringExtra("id");
        if (idString!=null) { // восстановление данных для редактирования
            id = Integer.parseInt(idString);
            crossAmount = intent.getStringExtra("amount");
            crossDate = intent.getStringExtra("date");
            crossCategory = intent.getStringExtra("category");
            amount.setText(crossAmount);
            spinner.setSelection(Integer.parseInt(crossCategory));
            date.setTimeInMillis((long)Integer.parseInt(crossDate)*UNIX_CONVERT);
            textDate.setText(getDate(date));
            datePickerDialog = new DatePickerDialog(this,this,date.get(Calendar.YEAR),date.get(Calendar.MONTH),date
                    .get(Calendar.DAY_OF_MONTH));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.okButton:
                String amountCheck = amount.getText().toString();
                try {
                    int amountValue = Integer.parseInt(amountCheck);
                    int category = (int) spinner.getSelectedItemId();

                    dbHelper = new DBHelper(this);
                    dbHelper.saveEntry(id, amountValue, category, date);

                    Intent intent;
                    if (id!=-1) {
                        intent = new Intent(this, HistoryActivity.class); // переброс на старую страницу
                    } else {
                        intent = new Intent(this, MainActivity.class);
                    }
                    startActivity(intent);
                    break;
                }
                catch (NumberFormatException e) {
                    Toast.makeText(this, "Введите корректную сумму", Toast.LENGTH_SHORT).show();
                    break;
                }

            case R.id.date:
                datePickerDialog.show();
                break;
        }
    }

    public void setContentPane() {

        okButton = (Button) findViewById(R.id.okButton);
        okButton.setOnClickListener(this);

        setSpinner();

        datePickerDialog = new DatePickerDialog(this,this,date.get(Calendar.YEAR),date.get(Calendar.MONTH),date
                .get(Calendar.DAY_OF_MONTH));
        textDate = (TextView) findViewById(R.id.date);
        textDate.setText(getDate(date));
        textDate.setOnClickListener(this);

        amount = (EditText) findViewById(R.id.amount);
    }

    public void setSpinner() {

        // адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = (Spinner) findViewById(R.id.categorySpinner);
        spinner.setAdapter(adapter);
        // заголовок
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
        date.set(year, month, day);
        textDate.setText(getDate(date));
    }


}
