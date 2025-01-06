package com.example.sovereign;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.sovereign.databinding.FragmentHomeBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        return binding.getRoot();
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        changeFragment(new ClanFragment());
//        binding.homeTopMenu.setOnItemSelectedListener(item -> {
//            int id = item.getItemId();
//            if (id == R.id.clan) {
//                changeFragment(new ClanFragment());
//            } else if (id == R.id.kingdom) {
//                changeFragment(new KingdomFragment());
//            }
//            return true;
//        });
//    }

//    private void changeFragment(Fragment fragment) {
//        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.home_layout, fragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//    }



    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment's layout
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find views within the inflated layout
        TabLayout tablayout = view.findViewById(R.id.tablayout);
        ViewPager2 viewpager = view.findViewById(R.id.viewpager);

        // Set up the VPAdapter with FragmentStateAdapter for ViewPager2
        VPAdapter vpAdapter = new VPAdapter(requireActivity()); // Pass the hosting activity
        vpAdapter.addFragment(new ClanFragment());
        vpAdapter.addFragment(new KingdomFragment());
        viewpager.setAdapter(vpAdapter);

        viewpager.setUserInputEnabled(true); // Ensures swiping is enabled


        // Use TabLayoutMediator to link TabLayout with ViewPager2
        new TabLayoutMediator(tablayout, viewpager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Clan");
                    break;
                case 1:
                    tab.setText("Kingdom");
                    break;
            }
        }).attach();
    }


    private void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.viewpager, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
