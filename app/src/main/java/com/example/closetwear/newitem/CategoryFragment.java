package com.example.closetwear.newitem;

import android.content.Context;
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

    /** Define the listener of the interface type listener will the activity instance containing
     * fragment
     **/
    private OnItemSelectedListener listener;

    // Interface to define the events that the fragment will use to communicate
    public interface OnItemSelectedListener {
        // This can be any number of events to be sent to the activity
        public void onCategoryItemSelected(String link);
    }

    private MaterialButtonToggleGroup toggleButton;
    private String category;

    // Constructor where listener events are ignored
    public CategoryFragment() {

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
        onButtonClick(view);
    }

    // Store the listener (activity) that will have events fired once the fragment is attached
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement CategoryFragment.OnItemSelectedListener");
        }
    }

    // Now we can fire the event when the user selects something in the fragment
    public void onButtonClick(View v) {
        toggleButton.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    if (group.getChildAt(i).getId() == group.getCheckedButtonId()) {
                        getButtonDescription(i);
                        listener.onCategoryItemSelected(category);
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
}
