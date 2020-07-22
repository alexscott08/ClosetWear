package com.example.closetwear.newitem;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.closetwear.R;
import com.google.android.material.button.MaterialButtonToggleGroup;

public class CategoryFragment extends Fragment {

    private MaterialButtonToggleGroup toggleButton;
    private String category = "";

    public CategoryFragment() {
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
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toggleButton = view.findViewById(R.id.toggleButton);
        toggleButton.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                Toast.makeText(getContext(), checkedId + "", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < group.getChildCount(); i++) {
                    if (group.getChildAt(i).getId() == group.getCheckedButtonId()) {
                        getButtonDescription(i);
                        break;
                    }
                }
            }
        });

    }

    public void getButtonDescription(int position) {
        switch (position) {
            case 0:
                category = "tops";
                break;
            case 1:
                category = "bottoms";
                break;
            case 2:
                category = "outerwear";
                break;
            case 3:
                category = "footwear";
                break;
            case 4:
                category = "formalwear";
                break;
            case 5:
                category = "accessories";
                break;
            default:
                category = "";
                break;
        }
    }

    public String getCategory() {
        return category;
    }
}