package com.example.closetwear.profile;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ProfileTabAdapter extends FragmentStateAdapter {
    public ProfileTabAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                ClosetFragment tab1 = new ClosetFragment();
                return tab1;
            case 1:
                OutfitsFragment tab2 = new OutfitsFragment();
                return tab2;
            case 2:
                FavoritesFragment tab3 = new FavoritesFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

