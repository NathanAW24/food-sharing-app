package com.example.just_hungry;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.just_hungry.models.PostModel;

import java.util.ArrayList;

public class AddOrderFragment extends Fragment {
    public ArrayList<PostModel> yourOrders = new ArrayList<>();

    Button newOrderButton;

    public AddOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_new_order, container, false);
        newOrderButton = rootView.findViewById(R.id.buttonAddOrder);

        newOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("HELLO");
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new NewOrderFormFragment()).commit();
            }
        });

        return rootView;
    }
}