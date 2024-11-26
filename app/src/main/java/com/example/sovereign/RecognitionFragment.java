package com.example.sovereign;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.sovereign.databinding.FragmentRecognitionBinding;

public class RecognitionFragment extends Fragment {
    private FragmentRecognitionBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecognitionBinding.inflate(inflater, container,false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changeFragment(new RankingFragment());
        binding.recognitionTopMenu.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.ranking){
                changeFragment(new RankingFragment());
            }else if(id == R.id.HOF){
                changeFragment(new HOFFragment());
            }
            return true;
        });
    }

    private void changeFragment (Fragment fragment){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.recognition_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}