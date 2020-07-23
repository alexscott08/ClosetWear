package com.example.closetwear.newitem;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.closetwear.R;
import com.google.android.material.textfield.TextInputEditText;

public class NameFragment extends Fragment {

    /** Define the listener of the interface type listener will the activity instance containing
     * fragment
     **/
    private NameFragment.OnItemTypedListener listener;

    // Interface to define the events that the fragment will use to communicate
    public interface OnItemTypedListener {
        // This can be any number of events to be sent to the activity
        public void onNameItemTyped(String link);
    }

    private TextInputEditText nameEdit;
    private String name;

    public NameFragment() {
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
        return inflater.inflate(R.layout.fragment_name, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameEdit = view.findViewById(R.id.nameEdit);
        nameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                name = nameEdit.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                listener.onNameItemTyped(name);
            }
        });
    }

    // Store the listener (activity) that will have events fired once the fragment is attached
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NameFragment.OnItemTypedListener) {
            listener = (NameFragment.OnItemTypedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement NameFragment.OnItemTypedListener");
        }
    }
}