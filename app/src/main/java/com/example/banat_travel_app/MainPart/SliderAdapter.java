package com.example.banat_travel_app.MainPart;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banat_travel_app.R;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderAdapterViewHolder> {
    private ArrayList<String> imagesList;

    public SliderAdapter(ArrayList<String> imagesList) {
        this.imagesList = imagesList;
    }

    @NonNull
    @Override
    public SliderAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, parent, false);

        return new SliderAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapterViewHolder holder, int position) {
//        holder.view.setBackground(imagesList.get(position));
//        Picasso.get().load(imagesList.get(position)).into(holder.view);
//        try {
//            URL url = new URL(imagesList.get(position));
//            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            holder.view.setImageBitmap(bmp);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        holder.view.setbackground

//        holder.view.setImageURI(Uri.parse(imagesList.get(position)));

        Picasso.get().load(imagesList.get(position)).into(holder.view);

    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public static class SliderAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView view;

        public SliderAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView.findViewById(R.id.slider_item_image_view);
        }
    }
}