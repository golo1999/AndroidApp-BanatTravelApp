package com.example.banat_travel_app.MainPart;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.banat_travel_app.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private LinearLayout carouselDotsLayout;
    private SliderAdapter adapter;
    private ViewPager2 pager2;
    private ArrayList<String> imagesList = new ArrayList<>();
    private TextView[] carouselDots;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        setVariables(view);

        return view;
    }

    private void setVariables(View v) {
        carouselDotsLayout = v.findViewById(R.id.home_fragment_carousel_dots);
        pager2 = v.findViewById(R.id.home_fragment_carousel);

        imagesList.add("https://revistaprogresiv.ro/sites/default/files/article/images/gusturi_romanesti_4.jpg");
        imagesList.add("https://www.bioshopromania.com/images/thumbnails/770/709/detailed/4/" +
                "cos_traditional_romanesc_mare_2_BioShopRomania.JPG?t=1602080293");
        imagesList.add("https://www.ziromania.ro/wp-content/uploads/" +
                "2018/12/b%C4%83uturi-tradi%C8%9Bionale-rom%C3%A2ne%C8%99ti-vazute-la-crama-1777-3.jpg");
        imagesList.add("https://www.banateanul.ro/wp-content/uploads/2019/07/bauturi-traditionale-romanesti.jpg");
        imagesList.add("https://medisf.traasgpu.com/ifis/62277a094eff337f-1024x576.jpg");

        adapter = new SliderAdapter(imagesList);
        pager2.setAdapter(adapter);

        carouselDots = new TextView[imagesList.size()];

        dotsIndicator();

        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                selectedIndicator(position);
                super.onPageSelected(position);
            }
        });
    }

    private void selectedIndicator(int position) {
        for (int i = 0; i < carouselDots.length; ++i) {
            carouselDots[i].setTextColor(Integer.parseInt(String.valueOf(i == position ? requireActivity().getResources().getColor(R.color.secondColor) : requireActivity().getResources().getColor(R.color.silver))));
        }
    }

    private void dotsIndicator() {
        for (int i = 0; i < carouselDots.length; ++i) {
            carouselDots[i] = new TextView(requireContext());
            carouselDots[i].setText(Html.fromHtml("&#9679;"));
            carouselDots[i].setTextSize(18);
            carouselDotsLayout.addView(carouselDots[i]);
        }
    }
}