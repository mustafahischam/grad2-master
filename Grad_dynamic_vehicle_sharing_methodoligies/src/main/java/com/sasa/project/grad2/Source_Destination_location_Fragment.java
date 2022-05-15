package com.sasa.project.grad2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sasa.project.grad2.Firebase_DataObjects.SDL_DataObjects;

import java.util.ArrayList;



public class Source_Destination_location_Fragment extends Fragment {
    public static final String Sdl = "Source_Destination_location_Fragment";

    private RecyclerView recyclerView;
    private ViewHolder_SDL SdL_adapter;
    private LinearLayoutManager linearLayoutManager;

    private ArrayList<SDL_DataObjects> SDL_array;

    public Source_Destination_location_Fragment() {


    }
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // put into array to display list
        SDL_array = getArguments().getParcelableArrayList(Source_Destination_location_Fragment.Sdl );
        View view;
        view = inflater.inflate(R.layout.sdl_fragment, container, false);
        recyclerView = view.findViewById(R.id.sdl_list);
        recyclerView.setHasFixedSize(true);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // condition if array not = null
        if (SDL_array.size() > 0) {
            linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(linearLayoutManager);
            Post_list();
        }

    }
    private void Post_list() {
        // make new list for every post
        SdL_adapter = new ViewHolder_SDL(SDL_array);
        recyclerView.setAdapter(SdL_adapter);

    }

}
