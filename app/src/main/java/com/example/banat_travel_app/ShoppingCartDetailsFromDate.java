package com.example.banat_travel_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.timessquare.CalendarPickerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ShoppingCartDetailsFromDate extends AppCompatActivity // clasa ce seteaza data de inceput si data de sfarsit a rezervarii
{

    private FirebaseAuth fbAuth;
    private DatabaseReference myRef;
    private ActivityDetails details;
    private String activity_location, from_date, to_date, activity_duration;
    private Button save;
    private Bundle extras;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart_details_from_date);
        setVariables();
        calendarInit(); // initializam si afisam calendarul pe ecran
    }

    private void setVariables()
    {
        extras=getIntent().getExtras();
        Intent intent=getIntent();
        fbAuth=FirebaseAuth.getInstance();
        myRef= FirebaseDatabase.getInstance().getReference(fbAuth.getUid());
        details=intent.getParcelableExtra("reservation_object_2");
        activity_location=extras.getString("activity_location");
        activity_duration=extras.getString("activity_duration");
        save=findViewById(R.id.shopping_cart_details_from_date_save);
    }

    private void calendarInit() // putem sa alegem data de inceput a rezervarii, dar nu mai devreme de ziua curenta si nu mai tarziu decat aceeasi zi din anul urmator
    {
        Date today = new Date(); // initializam un obiect al clasei Date
        Calendar nextYear = Calendar.getInstance(); // cream un obiect al clasei Calendar ce are setata data curenta
        nextYear.add(Calendar.YEAR, 1); // ii adaugam un an obiectului 'nextYear'
        CalendarPickerView datePicker = findViewById(R.id.shopping_cart_details_from_date_calendar);
        datePicker.init(today, nextYear.getTime()).withSelectedDate(today); // initializam calendarul
        datePicker.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() // daca selectam o data de inceput valida
        {
            @Override
            public void onDateSelected(Date date) // salvam data respectiva si setam data de final a rezervarii in functie de durata prestabilita (ex: Semenic are 3 zile, la fel Timisoara, Vrsac are o zi etc.)
            {
                from_date=DateFormat.getDateInstance(DateFormat.LONG).format(date);
                SimpleDateFormat sdf=new SimpleDateFormat("MMMM dd, yyyy");
                Calendar c=Calendar.getInstance();
                try
                {
                    c.setTime(sdf.parse(from_date));
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }
                c.add(Calendar.DATE, Integer.parseInt(activity_duration)-1);
                SimpleDateFormat sdf2=new SimpleDateFormat("MMMM dd, yyyy");
                to_date=sdf2.format(c.getTime());
            }

            @Override
            public void onDateUnselected(Date date)
            {

            }
        });

        save.setOnClickListener(new View.OnClickListener() // daca apasam pe butonul de salvare
        {
            @Override
            public void onClick(View v)
            {
                myRef.child(activity_location).child("from").setValue(from_date); // actualizam cele doua date in baza de date
                myRef.child(activity_location).child("to").setValue(to_date);
                intent=new Intent();
                intent.putExtra("fromDate", from_date);
                intent.putExtra("toDate", to_date);
                setResult(RESULT_OK, intent); // ne intoarcem la activitatea precedenta si o inchidem pe aceasta
                finish();
            }
        });
    }
}