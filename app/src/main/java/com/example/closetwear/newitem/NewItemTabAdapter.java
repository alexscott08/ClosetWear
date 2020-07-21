package com.example.closetwear.newitem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class NewItemTabAdapter extends FragmentStateAdapter {

    public NewItemTabAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                CategoryFragment tab1 = new CategoryFragment();
                return tab1;
            case 1:
                SubcategoryFragment tab2 = new SubcategoryFragment();
                return tab2;
            case 2:
                BrandFragment tab3 = new BrandFragment();
                return tab3;
            case 3:
                NameFragment tab4 = new NameFragment();
                return tab4;
            case 4:
                ColorFragment tab5 = new ColorFragment();
                return tab5;
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
