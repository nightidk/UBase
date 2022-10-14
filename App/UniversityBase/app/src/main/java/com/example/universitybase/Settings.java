package com.example.universitybase;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.universitybase.databinding.FragmentSettingsBinding;


public class Settings extends Fragment {
    private FragmentSettingsBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        MainActivity.setFragmentNow("Settings");
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.facultiesSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Settings.this)
                        .navigate(R.id.action_FragmentSettings_to_FacultiesSettings);
            }
        });

        binding.departmentsSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Settings.this)
                        .navigate(R.id.action_FragmentSettings_to_DepartmentsSettings);
            }
        });

        binding.studentsSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Settings.this)
                        .navigate(R.id.action_FragmentSettings_to_StudentsSettings);
            }
        });

        binding.logout.setOnClickListener(v -> {
            new Session(requireContext()).logout();
            requireActivity().finish();
            startActivity(new Intent(requireActivity(), Auth.class));
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}