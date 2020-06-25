package com.example.banat_travel_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class County_selector extends AppCompatActivity // clasa in care se desfasoara activitatea de selectare a judetului
{

    private String[] counties={"Caraș-Severin County", "Mehedinți County", "Timiș County", "Vojvodina County"};
    private int[] images={R.drawable.cs_coat_of_arms, R.drawable.mh, R.drawable.tm, R.drawable.v_coat_of_arms};
    private  String[] description={"Population: 274 277 people\nArea: 8 514 sq km\nImportant cities: Reșița, Caransebeș\nTourism: Cheile Nerei–Beușnita National Park, Domogled–Valea Cernei National Park, Danube Iron Gate National Park, Semenic resort, Băile-Herculane resort", "Population: 254 570 people\nArea: 4 933 sq km\nImportant cities: Orșova\nTourism: the ruins of Trajan's first bridge over the Danube, the city of Orșova, the Danube's Iron Gates", "Population: 758 380 people\nImportant cities: Timișoara, Lugoj\nTourism: Theresia Bastion, Huniade Castle, Timișoara Orthodox Cathedral, Timișoara Dome, Timișoara Millennium Church, Buziaș resort", "Population: 1 931 809 people\nArea: 21 614 sq km\nImportant cities: Zrenjanin, Pančevo, Kikinda, Vršac\nTourism: Deliblato Sands, Carska Bara bog, the city of Zrenjanin, the city of Vršac"};
    private ListView list;
    private ImageView shop, logout;
    private FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.county_selector);
        setVariables();
        setOnClickListeners();
        CustomAdaptor adaptor=new CustomAdaptor(); // cream un adaptor customizat
        list.setAdapter(adaptor); // setam adaptorul listview-ului, pentru a putea afisa cardview-urile in lista
    }

    private void setVariables()
    {
        list=findViewById(R.id.county_selector_listview);
        shop=findViewById(R.id.county_selector_shopping_bag);
        logout=findViewById(R.id.county_selector_logout);
        fbAuth=FirebaseAuth.getInstance();
    }

    private void setOnClickListeners()
    {
        logout.setOnClickListener(new View.OnClickListener() // daca dam click pe log out
        {
            @Override
            public void onClick(View v)
            {
                fbAuth.signOut(); // metoda FireBase de sign out
                Intent intent=new Intent(County_selector.this, Login.class);
                finishAffinity(); // incheiem toate activitatile prezente in stiva
                startActivity(intent); // ne intoarcem la log in
            }
        });

        shop.setOnClickListener(new View.OnClickListener() // daca dam click pe cosul de cumparaturi, mergem la activitatea respectiva
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(County_selector.this, Shopping_cart.class);
                startActivity(intent);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() // daca dam click pe oricare element al listei
        {

            String county_name;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent=new Intent(County_selector.this, Area_selector.class);
                county_name=counties[position]; // trimitem numele judetului selectat activitatii de selectare a zonelor specifice din fiecare judet, pentru a putea genera urmatoarea activitate
                intent.putExtra("county_name", county_name); // trimitem numele judetului selectat
                startActivity(intent);
            }
        });
    }

    class CustomAdaptor extends BaseAdapter // adaptor customizat al listei, luat de pe youtube si particularizat pe cazul meu
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
        public View getView(int position, View convertView, ViewGroup parent) // cream fiecare element al listei sub forma unui cardview ce are o imagine, un titlu si o descriere
        {
            @SuppressLint({"ViewHolder", "InflateParams"}) View view=getLayoutInflater().inflate(R.layout.county_selector_card,null);
            ImageView image=view.findViewById(R.id.county_selector_cardImage);
            TextView title=view.findViewById(R.id.county_selector_cardTitle);
            TextView desc=view.findViewById(R.id.county_selector_cardDescription);
            image.setImageResource(images[position]);
            title.setText(counties[position]);
            desc.setText(description[position]);
            return view;
        }
    }
}