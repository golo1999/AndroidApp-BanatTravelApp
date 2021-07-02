package com.example.banat_travel_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.banat_travel_app.StartingPart.Activity.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Activities extends AppCompatActivity // clasa in care se desfasoara activitatea de selectare a activitatilor specifice fiecarei zone
{
    private TextView name;
    private ImageView logout, shop;
    private ListView list;
    private FirebaseAuth fbAuth;
    private String[] activities={};
    private int[] images={};
    private Button book_now;
    private Integer days;
    private DatabaseReference myRef;
    private String price;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities);
        setVariables();
        setLayoutXML();
        setOnClickListeners();
    }

    private void setVariables()
    {
        name=findViewById(R.id.activities_name);
        list=findViewById(R.id.activities_listview);
        logout=findViewById(R.id.activities_logout);
        shop=findViewById(R.id.activities_shopping_bag);
        fbAuth=FirebaseAuth.getInstance();
        book_now=findViewById(R.id.activities_book_now);
        myRef= FirebaseDatabase.getInstance().getReference();
    }

    private void setLayoutXML()
    {
        Bundle extras=getIntent().getExtras();
        if(extras!=null)
        {
            String loc=extras.getString("location"); // salvam zona primita de la activitatea precedenta
            if(loc!=null)
            {
                switch(loc) // setam numele activitatilor disponibile, imaginile lor, precum si pretul acestora, totul in functie de zona selectata in activitatea precedenta
                {
                    case "Băile-Herculane area":
                        activities=new String[]{"The casino", "The cast iron bridge", "The cave of outlaws", "Decebal Hotel", "Diana thermal bath", "Domogled-Valea Cernei National Park", "Goddess Diana's statue", "Hercules square", "Mihai Eminescu's bust", "Roman Hotel", "The stone bridge", "The train station"};
                        images=new int[]{R.drawable.herculane_1, R.drawable.herculane_2, R.drawable.herculane_3, R.drawable.herculane_4, R.drawable.herculane_5, R.drawable.herculane_6, R.drawable.herculane_7, R.drawable.herculane_8, R.drawable.herculane_9, R.drawable.herculane_10, R.drawable.herculane_11, R.drawable.herculane_12};
                        price="1000";
                        days=3;
                        break;
                    case "Buziaș and Recaș":
                        activities=new String[]{"Buziaș Colonnade", "The Buziaș Park", "The Cramele Recaș winery"};
                        images=new int[]{R.drawable.buzias_1, R.drawable.buzias_2, R.drawable.recas_1};
                        price="400";
                        days=2;
                        break;
                    case "Danube area":
                        activities=new String[]{"The Sokolovac canyon", "The Golubac fortress", "The Ram fortress", "The Smederevo fortress", "The Deliblato Sands", "Tabula Traiana"};
                        images=new int[]{R.drawable.danube_1, R.drawable.danube_2, R.drawable.danube_3, R.drawable.danube_4, R.drawable.danube_5, R.drawable.danube_6};
                        price="800";
                        days=3;
                        break;
                    case "Danube Defile":
                        activities=new String[]{"The Banat sphinx", "Berzasca commune", "Bigăr waterfall", "Coronini commune", "The devil's lake", "Eftimie Murgu water mills", "Moldova Nouă town", "Ochiul Beiului lake"};
                        images=new int[]{R.drawable.defile_1, R.drawable.defile_2, R.drawable.defile_3, R.drawable.defile_4, R.drawable.defile_5, R.drawable.defile_6, R.drawable.defile_7, R.drawable.defile_8};
                        price="1000";
                        days=3;
                        break;
                    case "Dubova":
                        activities=new String[]{"Danube's boilers gorge", "Mraconia monastery", "Ponicova cave", "The rock sculpture of Decebalus", "Tricule fortress", "Veterani cave"};
                        images=new int[]{R.drawable.dubova_1, R.drawable.dubova_2, R.drawable.dubova_3, R.drawable.dubova_4, R.drawable.dubova_5, R.drawable.dubova_6};
                        price="700";
                        days=2;
                        break;
                    case "Kikinda":
                        activities=new String[]{"The mill", "The sculptures", "Generala Drapsina street"};
                        images=new int[]{R.drawable.kikinda_1, R.drawable.kikinda_2, R.drawable.kikinda_3};
                        price="250";
                        days=1;
                        break;
                    case "Lugoj":
                        activities=new String[]{"The Baroque Orthodox Cathedral", "The city of Lugoj", "The Surduc lake", "The I.C.Drăgan square"};
                        images=new int[]{R.drawable.lugoj_1, R.drawable.lugoj_2, R.drawable.lugoj_3, R.drawable.lugoj_4};
                        price="400";
                        days=2;
                        break;
                    case "Orșova":
                        activities=new String[]{"Cruise on Danube", "Clisura Dunării", "The Iron Gates I", "Saint Anne monastery", "Vodița Monastery"};
                        images=new int[]{R.drawable.orsova_1, R.drawable.orsova_2, R.drawable.orsova_3, R.drawable.orsova_4, R.drawable.orsova_5};
                        price="300";
                        days=1;
                        break;
                    case "Pančevo":
                        activities=new String[]{"The Pančevo city", "The National Museum"};
                        images=new int[]{R.drawable.pancevo_1, R.drawable.pancevo_2};
                        price="250";
                        days=1;
                        break;
                    case "Reșița area":
                        activities=new String[]{"Comarnic cave", "Oravița-Anina historical railway", "Reșița's emblem", "Secu lake", "Steam locomotives museum"};
                        images=new int[]{R.drawable.resita_1, R.drawable.resita_2, R.drawable.resita_3, R.drawable.resita_4, R.drawable.resita_5};
                        price="700";
                        days=2;
                        break;
                    case "Timișoara":
                        activities=new String[]{"Theresia Bastion", "Timișoreana Beer Factory", "The Bega Canal", "Millennium Roman Catholic Church", "Huniade Castle", "The Metropolitan Cathedral", "The Dome", "The Botanical park", "The Roses park", "The Liberty square", "The Unirii square", "The Victory square", "The Capitoline Wolf Statue"};
                        images=new int[]{R.drawable.timisoara_1, R.drawable.timisoara_2, R.drawable.timisoara_3, R.drawable.timisoara_4, R.drawable.timisoara_5, R.drawable.timisoara_6, R.drawable.timisoara_7, R.drawable.timisoara_8, R.drawable.timisoara_9, R.drawable.timisoara_10, R.drawable.timisoara_11, R.drawable.timisoara_12, R.drawable.timisoara_13};
                        price="1000";
                        days=3;
                        break;
                    case "Semenic area":
                        activities=new String[]{"The Caraș quays", "Semenic Mountain", "Trei Ape area", "Văliug commune"};
                        images=new int[]{R.drawable.semenic_1, R.drawable.semenic_2, R.drawable.semenic_3, R.drawable.semenic_4};
                        price="900";
                        days=3;
                        break;
                    case "Vršac":
                        activities=new String[]{"St. Gerhard Bishop and Martyr Catholic Church", "The castle", "Vršac city center", "Romanian Orthodox Cathedral"};
                        images=new int[]{R.drawable.vrsac_1, R.drawable.vrsac_2, R.drawable.vrsac_3, R.drawable.vrsac_4};
                        price="250";
                        days=1;
                        break;
                    case "Zrenjanin":
                        activities=new String[]{"Cathedral of St. John of Nepomuk", "City center", "The Court House", "The lake", "Bukovac Palace", "The Finance Palace", "The Theatre"};
                        images=new int[]{R.drawable.zrenjanin_1, R.drawable.zrenjanin_2, R.drawable.zrenjanin_3, R.drawable.zrenjanin_4, R.drawable.zrenjanin_6, R.drawable.zrenjanin_7, R.drawable.zrenjanin_8};
                        price="600";
                        days=2;
                        break;
                }
            }
            name.setText(loc); // setam textul din 'header' drept zona selectata in activitatea precedenta, pentru a-i face viata mai usoara utilizatorului
            CustomAdaptor adaptor=new CustomAdaptor();
            list.setAdapter(adaptor);
        }
    }

    private void setOnClickListeners()
    {
        book_now.setOnClickListener(new View.OnClickListener() // daca apasam pe butonul de 'Book now', se va folosi metoda de adaugare in cos (definita mai jos)
        {
            @Override
            public void onClick(View v)
            {
                addToShoppingCart();
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() // daca apasam pe butonul de log out
        {
            @Override
            public void onClick(View v)
            {
                fbAuth.signOut();
                Intent intent=new Intent(Activities.this, LoginActivity.class);
                finishAffinity();
                startActivity(intent);
            }
        });

        shop.setOnClickListener(new View.OnClickListener() // daca apasam pe butonul de cos de cumparaturi
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(Activities.this, ShoppingCart.class);
                startActivity(intent);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() // daca dam click pe oricare element al listei (adica pe una dintre activitate disponibile in zona respectiva), vom merge in activitatea ce prezinta 'activitatea' (de exemplu, prin 'activitate' intelegem Piata Victoriei din Timisoara)
        {

            String selected_location, location_details;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) // trimitem activitatii 'Activity_presentation' numele locatiei selectate
            {
                Bundle extras=getIntent().getExtras();
                Intent intent=new Intent(Activities.this, ActivityPresentation.class);
                if(extras!=null)
                {
                    selected_location=extras.getString("location");
                    if(selected_location!=null)
                    {
                        switch (selected_location)
                        {
                            case "Băile-Herculane area":
                                switch (position)
                                {
                                    case 0:
                                        location_details="The casino";
                                        break;
                                    case 1:
                                        location_details="The cast iron bridge";
                                        break;
                                    case 2:
                                        location_details="The cave of outlaws";
                                        break;
                                    case 3:
                                        location_details="Decebal Hotel";
                                        break;
                                    case 4:
                                        location_details="Diana thermal bath";
                                        break;
                                    case 5:
                                        location_details="Domogled-Valea Cernei National Park";
                                        break;
                                    case 6:
                                        location_details="Goddess Diana's statue";
                                        break;
                                    case 7:
                                        location_details="Hercules square";
                                        break;
                                    case 8:
                                        location_details="Mihai Eminescu's bust";
                                        break;
                                    case 9:
                                        location_details="Roman Hotel";
                                        break;
                                    case 10:
                                        location_details="The stone bridge";
                                        break;
                                    case 11:
                                        location_details="The train station";
                                }
                                break;
                            case "Buziaș and Recaș":
                                switch (position)
                                {
                                    case 0:
                                        location_details="Buziaș Colonnade";
                                        break;
                                    case 1:
                                        location_details="The Buziaș Park";
                                        break;
                                    case 2:
                                        location_details="The Cramele Recaș winery";
                                        break;
                                }
                                break;
                            case "Danube area":
                                switch (position)
                                {
                                    case 0:
                                        location_details="The Sokolovac canyon";
                                        break;
                                    case 1:
                                        location_details="The Golubac fortress";
                                        break;
                                    case 2:
                                        location_details="The Ram fortress";
                                        break;
                                    case 3:
                                        location_details="The Smederevo fortress";
                                        break;
                                    case 4:
                                        location_details="The Deliblato Sands";
                                        break;
                                    case 5:
                                        location_details="Tabula Traiana";
                                        break;
                                }
                                break;
                            case "Danube Defile":
                                switch (position)
                                {
                                    case 0:
                                        location_details="The Banat sphinx";
                                        break;
                                    case 1:
                                        location_details="Berzasca commune";
                                        break;
                                    case 2:
                                        location_details="Bigăr waterfall";
                                        break;
                                    case 3:
                                        location_details="Coronini commune";
                                        break;
                                    case 4:
                                        location_details="The devil's lake";
                                        break;
                                    case 5:
                                        location_details="Eftimie Murgu water mills";
                                        break;
                                    case 6:
                                        location_details="Moldova Nouă town";
                                        break;
                                    case 7:
                                        location_details="Ochiul Beiului lake";
                                        break;
                                }
                                break;
                            case "Dubova":
                                switch (position)
                                {
                                    case 0:
                                        location_details="Danube's boilers gorge";
                                        break;
                                    case 1:
                                        location_details="Mraconia monastery";
                                        break;
                                    case 2:
                                        location_details="Ponicova cave";
                                        break;
                                    case 3:
                                        location_details="The rock sculpture of Decebalus";
                                        break;
                                    case 4:
                                        location_details="Tricule fortress";
                                        break;
                                    case 5:
                                        location_details="Veterani cave";
                                        break;
                                }
                                break;
                            case "Kikinda":
                                switch (position)
                                {
                                    case 0:
                                        location_details="The mill";
                                        break;
                                    case 1:
                                        location_details="The sculptures";
                                        break;
                                    case 2:
                                        location_details="Generala Drapsina street";
                                        break;
                                }
                                break;
                            case "Lugoj":
                                switch (position)
                                {
                                    case 0:
                                        location_details="The Baroque Orthodox Cathedral";
                                        break;
                                    case 1:
                                        location_details="The city of Lugoj";
                                        break;
                                    case 2:
                                        location_details="The Surduc lake";
                                        break;
                                    case 3:
                                        location_details="The I.C.Drăgan square";
                                        break;
                                }
                                break;
                            case "Orșova":
                                switch (position)
                                {
                                    case 0:
                                        location_details="Cruise on Danube";
                                        break;
                                    case 1:
                                        location_details="Clisura Dunarii";
                                        break;
                                    case 2:
                                        location_details="Iron Gates I";
                                        break;
                                    case 3:
                                        location_details="Saint Anne Monastery";
                                        break;
                                    case 4:
                                        location_details="Vodița Monastery";
                                        break;
                                }
                                break;
                            case "Pančevo":
                                switch (position)
                                {
                                    case 0:
                                        location_details="The Pančevo city";
                                        break;
                                    case 1:
                                        location_details="Pančevo National Museum";
                                        break;
                                }
                                break;
                            case "Reșița area":
                                switch (position)
                                {
                                    case 0:
                                        location_details="Comarnic cave";
                                        break;
                                    case 1:
                                        location_details="Oravița-Anina historical railway";
                                        break;
                                    case 2:
                                        location_details="Resita's emblem";
                                        break;
                                    case 3:
                                        location_details="Secu lake";
                                        break;
                                    case 4:
                                        location_details="Steam locomotives museum";
                                        break;
                                }
                                break;
                            case "Semenic area":
                                switch (position)
                                {
                                    case 0:
                                        location_details="The Caraș quays";
                                        break;
                                    case 1:
                                        location_details="Semenic mountain";
                                        break;
                                    case 2:
                                        location_details="Trei Ape area";
                                        break;
                                    case 3:
                                        location_details="Văliug commune";
                                        break;
                                }
                                break;
                            case "Timișoara":
                                switch (position)
                                {
                                    case 0:
                                        location_details="Theresia Bastion";
                                        break;
                                    case 1:
                                        location_details="Timișoreana beer factory";
                                        break;
                                    case 2:
                                        location_details="The Bega Canal";
                                        break;
                                    case 3:
                                        location_details="Millennium Roman Catholic Church";
                                        break;
                                    case 4:
                                        location_details="Huniade Castle";
                                        break;
                                    case 5:
                                        location_details="The Metropolitan Cathedral";
                                        break;
                                    case 6:
                                        location_details="The Dome";
                                        break;
                                    case 7:
                                        location_details="The Botanical park";
                                        break;
                                    case 8:
                                        location_details="The Roses park";
                                        break;
                                    case 9:
                                        location_details="The Liberty square";
                                        break;
                                    case 10:
                                        location_details="The Unirii square";
                                        break;
                                    case 11:
                                        location_details="The Victory square";
                                    case 12:
                                        location_details="The Capitoline Wolf Statue";
                                        break;
                                }
                                break;
                            case "Vršac":
                                switch (position)
                                {
                                    case 0:
                                        location_details="St. Gerhard Bishop and Martyr Catholic Church";
                                        break;
                                    case 1:
                                        location_details="The Vršac castle";
                                        break;
                                    case 2:
                                        location_details="Vršac city center";
                                        break;
                                    case 3:
                                        location_details="Romanian Orthodox Cathedral";
                                        break;
                                }
                                break;
                            case "Zrenjanin":
                                switch (position)
                                {
                                    case 0:
                                        location_details="The Cathedral of St. John of Nepomuk";
                                        break;
                                    case 1:
                                        location_details="Zrenjanin city center";
                                        break;
                                    case 2:
                                        location_details="The Court House";
                                        break;
                                    case 3:
                                        location_details="The Zrenjanin lake";
                                        break;
                                    case 4:
                                        location_details="The Bukovac Palace";
                                        break;
                                    case 5:
                                        location_details="The Finance Palace";
                                        break;
                                    case 6:
                                        location_details="The theatre";
                                        break;
                                }
                                break;
                        }
                    }
                    intent.putExtra("selected_location",location_details);
                }
                startActivity(intent);
            }
        });
    }

    private void addToShoppingCart() // metoda de adaugare in cosul de cumparaturi (folosind Firebase)
    {
        Bundle extras=getIntent().getExtras();
        ActivityDetails details; // definim un obiect al clasei definite de mine, 'Activity_details', obiect ce va fi adaugat in baza de date cu actualizare in timp real a Firebase
        if(extras!=null)
        {
            details=new ActivityDetails(extras.getString("location"), "", "", price, days, 1); // instantierea obiectului ce va fi adaugat in baza de date
            String reservation="Reservation for "+details.getLocation(); // numele fiecarei 'activitati' va fi trecut in baza de date drept 'Reservation for...' (de exemplu, Reservation for Semenic area)
            if(fbAuth.getUid()!=null) // daca utilizatorul are un ID unic creat in partea de autentificare din cadrul Firebase
            {
                myRef.child(fbAuth.getUid()).child(reservation).setValue(details).addOnCompleteListener(new OnCompleteListener<Void>() // ii adaugam cheii cu numele 'Reservation for...' obiectul creat mai sus -> rezervarea va avea drept copii variabilele membre ale obiectului (durata 'activitatii', pretul acesteia, numarul de persoane etc.)
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful()) // vom fi instiintati daca rezervarea a fost sau nu adaugata cu succes in baza de date
                            Toast.makeText(Activities.this, "Successfully added to the cart", Toast.LENGTH_SHORT).show();
                        else Toast.makeText(Activities.this, "Couldn't add to the cart", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    class CustomAdaptor extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return images.length;
        }

        @Override
        public Object getItem(int position)
        {
            return null;
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            @SuppressLint({"ViewHolder", "InflateParams"}) View view=getLayoutInflater().inflate(R.layout.activities_card_design,null);
            ImageView image=view.findViewById(R.id.cardImage);
            TextView title=view.findViewById(R.id.cardTitle);
            image.setImageResource(images[position]);
            title.setText(activities[position]);
            return view;
        }
    }
}