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

public class Area_selector extends AppCompatActivity // clasa in care se desfasoara activitatea de selectare a zonei specifice fiecarui judet
{

    private String[] areas={}, prices={};
    private int[] images={};
    private ListView list;
    private ImageView shop, logout;
    private FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.area_selector);
        setVariables();
        setLayoutXML();
        setOnClickListeners();
        CustomAdaptor adaptor=new CustomAdaptor();
        list.setAdapter(adaptor);
    }

    private void setVariables()
    {
        list=findViewById(R.id.area_selector_listview);
        logout=findViewById(R.id.area_selector_logout);
        shop=findViewById(R.id.area_selector_shopping_bag);
        fbAuth=FirebaseAuth.getInstance();
    }

    private void setLayoutXML()
    {
        String county_name;
        Bundle extras=getIntent().getExtras(); // initializam un obiect al clasei Bundle, obiect ce ne permite sa utilizam datele primite de la activitatea precedenta
        if(extras!=null) // daca am primit date de la activitatea precedenta
        {
            county_name=extras.getString("county_name"); // salvam numele judetului primit
            if(county_name!=null)
            {
                switch(county_name) // setam numele zonelor, imaginile si preturile in functie de numele judetului selectat in activitatea precedenta
                {
                    case "Caraș-Severin County":
                        areas=new String[]{"Băile-Herculane area", "Danube Defile", "Reșița area", "Semenic area"};
                        images=new int[]{R.drawable.herculane_12, R.drawable.clisura, R.drawable.resita, R.drawable.semenic_2};
                        prices=new String[]{"Price: 800 lei for 3 days", "Price: 1000 lei for 3 days", "Price: 700 lei for 2 days", "Price: 900 lei for 3 days"};
                        break;
                    case "Mehedinți County":
                        areas=new String[]{"Dubova", "Orșova"};
                        images=new int[]{R.drawable.dubova, R.drawable.orsova};
                        prices=new String[]{"Price: 700 lei for 2 days", "Price: 300 lei for 1 day"};
                        break;
                    case "Timiș County":
                        areas=new String[]{"Buziaș and Recaș", "Lugoj", "Timișoara"};
                        images=new int[]{R.drawable.buzias_1, R.drawable.lugoj, R.drawable.timisoara_7};
                        prices=new String[]{"Price: 400 lei for 2 days", "Price: 400 lei for 2 days", "Price: 1000 lei for 3 days"};
                        break;
                    case "Vojvodina County":
                        areas=new String[]{"Danube area", "Kikinda", "Pančevo", "Vršac", "Zrenjanin"};
                        images=new int[]{R.drawable.danube_4, R.drawable.kikinda, R.drawable.pancevo_1, R.drawable.vrsac_4, R.drawable.zrenjanin_2};
                        prices=new String[]{"Price: 800 lei for 3 days", "Price: 250 lei for 1 day", "Price: 250 lei for 1 day", "Price: 250 lei for 1 day", "Price: 600 lei for 2 days"};
                        break;
                }
            }
        }
    }

    private void setOnClickListeners()
    {
        shop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(Area_selector.this, Shopping_cart.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fbAuth.signOut();
                Intent intent=new Intent(Area_selector.this, Login.class);
                finishAffinity();
                startActivity(intent);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() // daca dam click pe oricare element al listei, trimitem numele zonei selectate catre activitatea de selectare a activitatilor specifice fiecarei zone din aceasta activitate
        {
            String location, county_name;
            Bundle extras=getIntent().getExtras();

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                county_name=extras.getString("county_name"); // verificam pozitia din cadrul listei in functie de judet
                Intent intent=new Intent(Area_selector.this, Activities.class);
                if(extras!=null)
                {
                    switch(county_name)
                    {
                        case "Caraș-Severin County": // in cazul in care judetul este Caras-Severin, avem 4 optiuni de zone
                            if(position==0)
                                location="Băile-Herculane area";
                            else if(position==1)
                                location="Danube Defile";
                            else if(position==2)
                                location="Reșița area";
                            else if(position==3)
                                location="Semenic area";
                            break;
                        case "Mehedinți County": // in cazul in care judetul este Mehedinti, avem 2 optiuni de zone
                            if(position==0)
                                location="Dubova";
                            else if(position==1)
                                location="Orșova";
                            break;
                        case "Timiș County": // in cazul in care judetul este Timis, avem 3 optiuni de zone
                            if(position==0)
                                location="Buziaș and Recaș";
                            else if(position==1)
                                location="Lugoj";
                            else if(position==2)
                                location="Timișoara";
                            break;
                        case "Vojvodina County": // in cazul in care judetul este Vojvodina, avem 5 optiuni de zone
                            if(position==0)
                                location="Danube area";
                            else if(position==1)
                                location="Kikinda";
                            else if(position==2)
                                location="Pančevo";
                            else if(position==3)
                                location="Vršac";
                            else if(position==4)
                                location="Zrenjanin";
                            break;
                    }
                }
                intent.putExtra("location", location); // trimitem numele zonei selectate catre activitatea urmatoare
                startActivity(intent);
            }
        });
    }

    class CustomAdaptor extends BaseAdapter // acelasi adaptor customizat, similar cu cel din activitatea precedenta, County_selector
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
        public View getView(int position, View convertView, ViewGroup parent) // fiecare element al listei este, din nou, un cardview ce are un titlu, o imagine si, de data aceasta, un pret in functie de zona
        {
            @SuppressLint({"ViewHolder", "InflateParams"}) View view=getLayoutInflater().inflate(R.layout.area_selector_card_design,null);
            ImageView image=view.findViewById(R.id.area_selector_cardImage);
            TextView title=view.findViewById(R.id.area_selector_cardTitle);
            TextView price=view.findViewById(R.id.area_selector_cardPrice);
            image.setImageResource(images[position]);
            title.setText(areas[position]);
            price.setText(prices[position]);
            return view;
        }
    }
}