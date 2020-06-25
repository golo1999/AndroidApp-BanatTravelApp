package com.example.banat_travel_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Shopping_cart_details extends AppCompatActivity //
{

    private TextView location, price, duration, total_price, from, to;
    private Bundle extras;
    private DatabaseReference myRef;
    private FirebaseAuth fbAuth;
    private EditText persons;
    private Button save, remove, date_select_button;
    private Activity_details details;
    private final Integer request_code=1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart_details);
        setVariables();
        setLayoutXML_onClickListeners(); // metoda ce implementeaza evenimentele onClick si seteaza UI-ul
    }

    private void setVariables()
    {
        location=findViewById(R.id.shopping_cart_details_location);
        price=findViewById(R.id.shopping_cart_details_price);
        extras=getIntent().getExtras();
        fbAuth=FirebaseAuth.getInstance();
        myRef= FirebaseDatabase.getInstance().getReference().child(fbAuth.getUid());
        persons=findViewById(R.id.shopping_cart_details_persons);
        duration=findViewById(R.id.shopping_cart_details_duration);
        total_price=findViewById(R.id.shopping_cart_details_total_price);
        save=findViewById(R.id.shopping_cart_details_save);
        remove=findViewById(R.id.shopping_cart_details_remove);
        date_select_button=findViewById(R.id.shopping_cart_details_select_from_date);
        from=findViewById(R.id.shopping_cart_details_from);
        to=findViewById(R.id.shopping_cart_details_to);
    }

    private void setLayoutXML_onClickListeners()
    {
        String shopping_cart_name=extras.getString("shopping_cart_name"); // salvam numele rezervarii pe care dorim sa o editam (provine din activitatea precedenta, 'Shopping_cart')
        location.setText("Reservation for "+shopping_cart_name);
        final String activity_location= String.valueOf(location.getText());
        myRef.child(activity_location).addValueEventListener(new ValueEventListener() // citim datele din baza de date Firebase
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    Intent intent=getIntent();
                    details=intent.getParcelableExtra("reservation_object"); // cream un obiect cu datele obiectului primit din activitatea precedenta
                    price.setText(details.getPrice()+" lei"); // setam pe ecran pretul per persoana al rezervarii
                    persons.setHint(details.getPersons().toString()); // utilizatorul are posibilitatea sa editeze numarul de persoane si este afisat un hint cu numarul persoanelor deja existente in baza de date
                    if(details.getDuration()!=1)
                        duration.setHint(details.getDuration().toString()+" days");
                    else duration.setHint(details.getDuration().toString()+" day");
                    total_price.setText(Integer.parseInt(details.getPrice())*details.getPersons()+" lei"); // setam pe ecran pretul total al rezervarii
                    details.setTotalPrice();
                    from.setText(details.getFrom()); // afisam pe ecran data de inceput si de sfarsit a rezervarii sub forma de string, initial gol
                    to.setText(details.getTo());
                }

                save.setOnClickListener(new View.OnClickListener() // daca dam click pe butonul de salvare a modificarilor
                {
                    @Override
                    public void onClick(View v)
                    {
                        String numberOfPersons=persons.getText().toString();
                        Intent intent=new Intent();
                        if(numberOfPersons!=null && !numberOfPersons.isEmpty() && Integer.parseInt(numberOfPersons)>0) // daca am modificat numarul de persoane deja existent, vor fi actualizate si in baza de date Firebase
                        {
                            myRef.child(activity_location).child("persons").setValue(Integer.parseInt(numberOfPersons));
                            myRef.child(activity_location).child("totalPrice").setValue(Integer.parseInt(numberOfPersons)*Integer.parseInt(details.getPrice())); // afiseaza mereu pretul de dinainte (nu este actualizat)
                        }
                        Toast.makeText(Shopping_cart_details.this, "Reservation details have been saved", Toast.LENGTH_SHORT).show(); // afisam mesajul ca salvarea s-a realizat cu succes
                        intent.putExtra("from_date", from.getText()); // trimitem activitatii 'Shopping_cart' cele doua date: de inceput si de final si oprim activitatea aceasta, revenind la cosul de cumparaturi
                        intent.putExtra("to_date", to.getText());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });

                remove.setOnClickListener(new View.OnClickListener() // daca dam click pe butonul de stergere a rezervarii
                {
                    @Override
                    public void onClick(View v)
                    {
                        myRef.child(activity_location).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() // stergem efectiv rezervarea din baza de date si revenim la activitatea 'Shopping_cart', primind un Toast ca rezervarea a fost stearsa cu succes
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(Shopping_cart_details.this, "Reservation removed", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        });
                    }
                });

                date_select_button.setOnClickListener(new View.OnClickListener() // daca dam click pe butonul de modificare a datelor de inceput si de sfarsit
                {
                    @Override
                    public void onClick(View v) // mergem in activitatea 'Shopping_cart_details_from_date', de la care primim cele doua date (in cazul in care le-am setat)
                    {
                        Intent intent=new Intent(Shopping_cart_details.this, Shopping_cart_details_from_date.class);
                        intent.putExtra("reservation_object_2", details); // trimitem obiectul rezervarii, numele rezervarii, precum si durata acesteia
                        intent.putExtra("activity_location", activity_location);
                        intent.putExtra("activity_duration", details.getDuration().toString());
                        startActivityForResult(intent, request_code);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    @Override
    public void onBackPressed() // daca apasam butonul de inapoi
    { // trimitem cele doua date sub forma de string activitatii precedente, 'Shopping_cart' si inchidem activitatea curenta
        super.onBackPressed();
        Intent intent=new Intent();
        intent.putExtra("from_date", from.getText());
        intent.putExtra("to_date", to.getText());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) // aici verificam daca am primit cele doua date inapoi de la activitatea 'Shopping_cart_details_from_date'
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==request_code)
        {
            if(resultCode==RESULT_OK) // daca am primit datele cu succes (adica daca am apasat pe butonul de salvare in activitatea 'Shopping_cart_details_from_date'), le afisam pe ecranul aplicatiei
            {
                from.setText(data.getStringExtra("fromDate"));
                to.setText(data.getStringExtra("toDate"));
            }
            else if(resultCode==RESULT_CANCELED) // daca nu am apasat pe salvare in activitatea 'Shopping_cart_details_from_date' si am apasat pe butonul de inapoi
            {
                Toast.makeText(Shopping_cart_details.this, "The changes have not been saved", Toast.LENGTH_SHORT).show();
            }
        }
    }
}