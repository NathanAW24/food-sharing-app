package com.example.just_hungry;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.just_hungry.models.AssetModel;
import com.example.just_hungry.models.LocationModel;
import com.example.just_hungry.models.ParticipantModel;
import com.example.just_hungry.models.PostModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class NewOrderFormFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    public ArrayList<PostModel> yourOrders = new ArrayList<>();
    Spinner spinnerCuisine;

    // form data
    private PostModel orderFormData = new PostModel();
    private String posterId;
    private String dateCreated;
    private String timing;
    private ArrayList<ParticipantModel> participants;
    private ArrayList<AssetModel> assets;
    private LocationModel location;
    private String storeName;
    private Integer maxParticipants;
    SimpleDateFormat ISO_8601_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'");

    // new form data to be added to the PostModel
    private String cuisine;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_order_form, container, false);

        // Cuisine spinner code - is there a function to add to all spinners?
        spinnerCuisine = (Spinner) rootView.findViewById(R.id.spinnerCuisine);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_cuisine, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCuisine.setAdapter(adapter);



        //

        // Inflate the layout for this fragment
        return rootView;


    }


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewOrderFormFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddOrderFragment newInstance(String param1, String param2) {
        AddOrderFragment fragment = new AddOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        cuisine= (String) adapterView.getItemAtPosition(i);
        System.out.println(cuisine);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}