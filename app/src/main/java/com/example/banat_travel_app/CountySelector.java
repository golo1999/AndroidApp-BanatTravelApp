package com.example.banat_travel_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.banat_travel_app.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CountySelector extends AppCompatActivity // clasa in care se desfasoara activitatea de selectare a judetului
{
    private SharedPreferences preferences;
    private final String[] counties = {"Caraș-Severin County", "Mehedinți County", "Timiș County", "Vojvodina County"};
    private final int[] images = {R.drawable.cs_coat_of_arms, R.drawable.mh, R.drawable.tm, R.drawable.v_coat_of_arms};
    private final String[] description = {"Population: 274 277 people\nArea: 8 514 sq km\nImportant cities: Reșița, Caransebeș\nTourism: Cheile Nerei–Beușnita National Park, Domogled–Valea Cernei National Park, Danube Iron Gate National Park, Semenic resort, Băile-Herculane resort", "Population: 254 570 people\nArea: 4 933 sq km\nImportant cities: Orșova\nTourism: the ruins of Trajan's first bridge over the Danube, the city of Orșova, the Danube's Iron Gates", "Population: 758 380 people\nImportant cities: Timișoara, Lugoj\nTourism: Theresia Bastion, Huniade Castle, Timișoara Orthodox Cathedral, Timișoara Dome, Timișoara Millennium Church, Buziaș resort", "Population: 1 931 809 people\nArea: 21 614 sq km\nImportant cities: Zrenjanin, Pančevo, Kikinda, Vršac\nTourism: Deliblato Sands, Carska Bara bog, the city of Zrenjanin, the city of Vršac"};
    private ListView list;
    private ImageView shop, logout;
    private FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.county_selector);
        setVariables();
        setOnClickListeners();
        CustomAdaptor adaptor = new CustomAdaptor(); // cream un adaptor customizat
        list.setAdapter(adaptor); // setam adaptorul listview-ului, pentru a putea afisa cardview-urile in lista

        FirebaseDatabase.getInstance().getReference().child("UsersList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("snapshot", snapshot.toString());
                if (snapshot.exists() && snapshot.hasChildren())
                    for (DataSnapshot snapshot1 : snapshot.getChildren())
                        Toast.makeText(CountySelector.this, snapshot1.getKey() + " " + snapshot1.getValue(), Toast.LENGTH_SHORT).show();

                if (!snapshot.exists())
                    Toast.makeText(CountySelector.this, "no", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setVariables() {
        preferences = getSharedPreferences("BANAT_TRAVEL", MODE_PRIVATE);
        list = findViewById(R.id.county_selector_listview);
        shop = findViewById(R.id.county_selector_shopping_bag);
        logout = findViewById(R.id.county_selector_logout);
        fbAuth = FirebaseAuth.getInstance();
    }

    private void setOnClickListeners() {
        // daca dam click pe log out
        logout.setOnClickListener(v -> {
            final User authenticatedUser = MyCustomMethods.retrieveUserFromSharedPreferences(preferences,
                    "authenticatedUser");

            if (authenticatedUser != null && MyCustomMethods.sharedPreferencesContainsKey(preferences,
                    "authenticatedUser")) {
                MyCustomMethods.removeUserFromSharedPreferences(preferences, "authenticatedUser");
            }

            MyCustomMethods.logOut(fbAuth, this);

//            fbAuth.signOut(); // metoda FireBase de sign out
//            Intent intent = new Intent(CountySelector.this, LoginActivity.class);
//            finishAffinity(); // incheiem toate activitatile prezente in stiva
//            startActivity(intent); // ne intoarcem la log in
        });

        // daca dam click pe cosul de cumparaturi, mergem la activitatea respectiva
        shop.setOnClickListener(v -> {
            Intent intent = new Intent(CountySelector.this, ShoppingCart.class);
            startActivity(intent);
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() // daca dam click pe oricare element al listei
        {

            String county_name;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CountySelector.this, AreaSelector.class);
                county_name = counties[position]; // trimitem numele judetului selectat activitatii de selectare a zonelor specifice din fiecare judet, pentru a putea genera urmatoarea activitate
                intent.putExtra("county_name", county_name); // trimitem numele judetului selectat
                startActivity(intent);
            }
        });
    }

    class CustomAdaptor extends BaseAdapter // adaptor customizat al listei, luat de pe youtube si particularizat pe cazul meu
    {
        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) // cream fiecare element al listei sub forma unui cardview ce are o imagine, un titlu si o descriere
        {
            @SuppressLint({"ViewHolder", "InflateParams"}) View view = getLayoutInflater().inflate(R.layout.county_selector_card, null);
            ImageView image = view.findViewById(R.id.county_selector_cardImage);
            TextView title = view.findViewById(R.id.county_selector_cardTitle);
            TextView desc = view.findViewById(R.id.county_selector_cardDescription);
            image.setImageResource(images[position]);
            title.setText(counties[position]);
            desc.setText(description[position]);
            return view;
        }
    }
}