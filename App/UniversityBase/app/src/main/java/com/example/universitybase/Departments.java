package com.example.universitybase;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.universitybase.databinding.FragmentDepartmentsBinding;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.xmlpull.v1.XmlPullParser;

import java.util.concurrent.ExecutionException;


public class Departments extends Fragment {

    private FragmentDepartmentsBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        MainActivity.setFragmentNow("Departments");
        binding = FragmentDepartmentsBinding.inflate(inflater, container, false);

        return binding.getRoot();

    }

    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @SuppressLint("ResourceType")
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String faculty = new Session(requireContext()).getAction("faculty");
        LinearLayout ll = view.findViewById(R.id.linear_fragment);
        try {
            String result = new ServerRequests().execute("departament/get", faculty).get();
            JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
            JsonArray departamens = jsonObject.get("message").getAsJsonArray();
            for (int i = 0; i < departamens.size(); i++) {
                Button myButton = new Button(getActivity());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams. MATCH_PARENT ,
                        LinearLayout.LayoutParams. WRAP_CONTENT ) ;
                layoutParams.setMargins(dpToPx(15), dpToPx(15), dpToPx(15), dpToPx(15));
                myButton.setLayoutParams(layoutParams);
                myButton.setId(i + 1);
                myButton.setBackgroundResource(R.drawable.faculities_widget);
                myButton.setHeight(dpToPx(100));
                myButton.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                myButton.setPadding(dpToPx(15), dpToPx(15), dpToPx(15), dpToPx(15));
                myButton.setTextSize(17);
                myButton.setText(departamens.get(i).getAsJsonObject().get("name").getAsString());
                final int id_ = myButton.getId();
                ll.addView(myButton);
                final int finalI = i;
                myButton.setOnClickListener(view1 -> {
                    new Session(requireContext()).setActionDepartament(departamens.get(finalI).getAsJsonObject().get("name").getAsString());
                    NavHostFragment.findNavController(Departments.this)
                            .navigate(R.id.action_FragmentDepartments_to_FragmentStudentsList);
                });
            }
            if (departamens.size() == 0) {
                Button myButton = new Button(getActivity());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams. MATCH_PARENT ,
                        LinearLayout.LayoutParams. WRAP_CONTENT ) ;
                layoutParams.setMargins(dpToPx(15), dpToPx(15), dpToPx(15), dpToPx(15));
                myButton.setLayoutParams(layoutParams);
                myButton.setId(1);
                myButton.setBackgroundResource(R.drawable.faculities_widget);
                myButton.setHeight(dpToPx(100));
                myButton.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                myButton.setPadding(dpToPx(15), dpToPx(15), dpToPx(15), dpToPx(15));
                myButton.setTextSize(17);
                myButton.setText("Кафедры не найдены");
                myButton.setClickable(false);
                final int id_ = myButton.getId();
                ll.addView(myButton);
            }
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