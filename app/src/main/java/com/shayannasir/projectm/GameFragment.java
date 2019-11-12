package com.shayannasir.projectm;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment {

    private View gameFragment;
    private ImageButton imageButton;


    public GameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        gameFragment =  inflater.inflate(R.layout.fragment_game, container, false);
        imageButton = (ImageButton)gameFragment.findViewById(R.id.imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), startGame.class);
                startActivity(intent);
            }
        });

        return gameFragment;

    }

}
