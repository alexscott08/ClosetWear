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

public class BrandFragment extends Fragment {

    /** Define the listener of the interface type listener will the activity instance containing
     * fragment
     **/
    private BrandFragment.OnItemTypedListener listener;

    // Interface to define the events that the fragment will use to communicate
    public interface OnItemTypedListener {
        // This can be any number of events to be sent to the activity
        public void onBrandItemTyped(String link);
    }

    private String brand;
    private TextInputEditText brandEdit;

    public BrandFragment() {
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
        return inflater.inflate(R.layout.fragment_brand, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        brandEdit = view.findViewById(R.id.brandEdit);
        brandEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                brand = brandEdit.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                listener.onBrandItemTyped(brand);
            }
        });
    }

    // Store the listener (activity) that will have events fired once the fragment is attached
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BrandFragment.OnItemTypedListener) {
            listener = (BrandFragment.OnItemTypedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement BrandFragment.OnItemTypedListener");
        }
    }
}