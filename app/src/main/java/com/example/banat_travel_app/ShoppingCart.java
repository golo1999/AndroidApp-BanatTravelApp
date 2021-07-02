package com.example.banat_travel_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.banat_travel_app.StartingPart.Activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShoppingCart extends AppCompatActivity // clasa in care se descrie functionalitatea cosului de cumparaturi
{

    private RecyclerView rView;
    private DatabaseReference myRef;
    private FirebaseAuth fbAuth;
    private Button checkout;
    private ImageView logout;
    private final Integer request_code = 1;
    private String resultFrom, resultTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart);
        setVariables();
        setLayoutXML();
        setOnClickListeners();

//        FirebaseRecyclerAdapter<Activity_details, Activity_details_viewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<Activity_details, Activity_details_viewHolder>(Activity_details.class, R.layout.shopping_cart_card_design, Activity_details_viewHolder.class, myRef) { // viewholder custom luat de pe youtube
//            @Override
//            protected void populateViewHolder(final Activity_details_viewHolder activity_details_viewHolder, final Activity_details details, int i) {
//                activity_details_viewHolder.setTitle(details.getLocation()); // setam numele si pretul fiecarei rezervari pe cardview
//                activity_details_viewHolder.setPrice(details.getPrice(), details.getPersons().toString());
//                activity_details_viewHolder.view.setOnClickListener(new View.OnClickListener() // daca dam click pe oricare rezervare, suntem trimisi la activitatea 'Shopping_cart_details', unde putem sa editam rezervarea)
//                { // o noutate fata de activitatile trecute o reprezinta 'startActivityForResult', cu ajutorul careia primim datele de inceput si de final ale rezervarii (le folosim atunci cand apasam butonul de checkout) -> daca din activitatea 'Shopping_cart_details' nu primim inapoi datele de inceput si de sfarsit ale rezervarii, nu putem face checkout
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(Shopping_cart.this, Shopping_cart_details.class);
//                        intent.putExtra("shopping_cart_name", details.getLocation()); // ii trimitem activitatii 'Shopping_cart_details' numele zonei in care avem rezervarea
//                        intent.putExtra("reservation_object", details); // ii trimitem si obiectul cu rezervarea efectiva
//                        startActivityForResult(intent, request_code);
//                    }
//                });
//            }
//        };
//        rView.setAdapter(recyclerAdapter); // folosim un layout de tip recyclerview si ii setam adaptor
    }

    private void setVariables() {
        rView = findViewById(R.id.shopping_cart_recyclerView);
        fbAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference().child(fbAuth.getUid());
        checkout = findViewById(R.id.shopping_cart_checkout);
        logout = findViewById(R.id.shopping_cart_logout);
    }

    private void setLayoutXML() {
        // facem ca modificarile operate in baza de date Firebase sa fie in timp real
        myRef.keepSynced(true);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setOnClickListeners() {
        // daca dam click pe checkout
        checkout.setOnClickListener(v -> {
            // putem face checkout doar daca rezervarea are datele de inceput si de final setate
            if (resultFrom != null && !resultFrom.isEmpty() && resultTo != null && !resultTo.isEmpty()) {
                // stergem toate rezervarile din cos si afisam un mesaj daca checkout-ul are succes
                myRef.removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        Toast.makeText(ShoppingCart.this, "We wish you a wonderful holiday!", Toast.LENGTH_SHORT).show();
                });
            } else
                Toast.makeText(ShoppingCart.this,
                        "Please make sure that there are any reservations or if the 'from' and 'to' dates are set!",
                        Toast.LENGTH_LONG).show(); // daca nu sunt setate datele, nu putem face checkout
        });

        // daca dam click pe log out
        logout.setOnClickListener(v -> {
            fbAuth.signOut();
            Intent intent = new Intent(ShoppingCart.this, LoginActivity.class);
            finishAffinity();
            startActivity(intent);
        });
    }

    // aici verificam daca am primit cele doua date din activitatea 'Shopping_cart_details',
    // pentru a putea trece la checkout
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == request_code) {
            if (resultCode == RESULT_OK) {
                resultFrom = data.getStringExtra("from_date");
                resultTo = data.getStringExtra("to_date");
            }
        }
    }

    // clasa customizata pentru viewholder (luata de pe youtube)
    // -> fiecare rezervare din baza de date este afisata sub forma unui cardview ce contine
    // un titlu (numele rezervarii) si pretul, precum si numarul de persoane
    public static class Activity_details_viewHolder extends RecyclerView.ViewHolder {
        View view;

        public Activity_details_viewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setTitle(String title) {
            TextView cardTitle = view.findViewById(R.id.shopping_cart_cardTitle);
            cardTitle.setText(title);
        }

        public void setPrice(String price, String persons) {
            TextView cardPrice = view.findViewById(R.id.shopping_cart_cardPrice);
            cardPrice.setText(price + " lei " + "x " + persons);
        }
    }
}