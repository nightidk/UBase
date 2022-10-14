package com.example.universitybase;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.universitybase.databinding.FragmentStudentInfoBinding;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.concurrent.ExecutionException;

public class StudentInfo extends Fragment {

    private FragmentStudentInfoBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        MainActivity.setFragmentNow("StudentInfo");
        binding = FragmentStudentInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String studentName = new Session(requireContext()).getAction("student");
        try {
            String studentInfoString = new ServerRequests().execute("student/get", studentName).get();
            JsonObject jsonObject = JsonParser.parseString(studentInfoString).getAsJsonObject();
            JsonArray studentInfos = jsonObject.get("message").getAsJsonArray();
            JsonObject studentInfo = studentInfos.get(0).getAsJsonObject();
            binding.studentFullName.setText(studentInfo.get("name").getAsString());
            binding.facultyName.setText(studentInfo.get("faculty").getAsString());
            binding.departmentName.setText(studentInfo.get("departament").getAsString());
            binding.birthdayValue.setText(studentInfo.get("date").getAsString());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}