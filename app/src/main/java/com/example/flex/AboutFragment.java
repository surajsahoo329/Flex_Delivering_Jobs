package com.example.flex;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {


    public AboutFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View parentHolder=inflater.inflate(R.layout.fragment_about, container,
                false);

        TextView textView=parentHolder.findViewById(R.id.textViewAbout);
        Typeface myFont1=Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(), "fonts/coolvetica_i.ttf");
        textView.setTypeface(myFont1);

        return parentHolder;

    }

}
