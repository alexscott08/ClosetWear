package com.example.closetwear.profile;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * This class handles navigation between the three tabs shown in the {@link ProfileFragment}.
 */
public class ProfileTabAdapter extends FragmentStateAdapter {

    /**
     * This constructor creates a new instance of a NewOutfitAdapter.
     *
     * @param fragmentManager handles transactions between fragments
     * @param lifecycle   the fragment's lifecycle
     */
    public ProfileTabAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    /**
     * {@link FragmentStateAdapter#createFragment(int)}
     */
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

    /**
     * {@link RecyclerView.Adapter#getItemCount()}
     */
    @Override
    public int getItemCount() {
        return 3;
    }
}

