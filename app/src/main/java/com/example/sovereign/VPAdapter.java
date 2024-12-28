package com.example.sovereign;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class VPAdapter extends FragmentStateAdapter {

    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

    // Constructor for FragmentStateAdapter (ViewPager2)
    public VPAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentArrayList.get(position); // Return the fragment at the given position
    }

    @Override
    public int getItemCount() {
        return fragmentArrayList.size(); // Return the total number of fragments
    }

    // Method to add fragments
    public void addFragment(Fragment fragment) {
        fragmentArrayList.add(fragment);

    }


}
