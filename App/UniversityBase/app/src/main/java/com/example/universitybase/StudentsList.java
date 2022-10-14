package com.example.universitybase;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.universitybase.databinding.FragmentStudentsListBinding;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class StudentsList extends Fragment {

    private FragmentStudentsListBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        MainActivity.setFragmentNow("StudentsList");
        binding = FragmentStudentsListBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String departament = new Session(requireContext()).getAction("departament");
        binding.calendarView.setMaxDate(System.currentTimeMillis());
        LoadingDialog loadingDialog = new LoadingDialog(requireActivity());

        binding.calendarView.setOnDateChangeListener((viewcal, year, month, dayOfMonth) -> {
            @SuppressLint("SimpleDateFormat") DateFormat simple = new SimpleDateFormat("dd.MM.yyyy");
            String fixMonth;
            if (String.valueOf(month + 1).length() == 1) {
                fixMonth = "0" + (month + 1);
            } else {
                fixMonth = "" + (month + 1);
            }
            String dateSession = new Session(requireContext()).getAction("date");
            if (!Objects.equals(dateSession, dayOfMonth + "." + fixMonth + "." + year)) {
                dateSession = dayOfMonth + "." + fixMonth + "." + year;
                TableLayout ll = view.findViewById(R.id.table_layout);
                ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                ll.removeAllViews();
                new Session(requireContext()).setDate("date");
                //Log.d("test", dayOfMonth + "." + fixMonth + "." + year);
                try {

                    String result = new ServerRequests().execute("students/get", departament).get();
                    JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
                    JsonArray students = jsonObject.get("message").getAsJsonArray();
                    for (int i = 0; i < students.size(); i++) {
                        int finalI = i;
                        TableRow.LayoutParams params = new TableRow.LayoutParams(
                                TableRow.LayoutParams. MATCH_PARENT ,
                                dpToPx(100) );
                        params.setMargins(dpToPx(100), dpToPx(100), dpToPx(100), dpToPx(100));
                        TableRow row = new TableRow(requireContext());
                        row.setLayoutParams(params);
                        row.setPadding(dpToPx(15), dpToPx(15), dpToPx(15), dpToPx(15));
                        row.setBackgroundResource(R.drawable.faculities_widget);
                        TableLayout.LayoutParams paramsRow = new TableLayout.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT ,
                                TableRow.LayoutParams.WRAP_CONTENT );
                        paramsRow.setMargins(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
                        TextView name = new TextView(requireContext());
                        String studentName = students.get(finalI).getAsJsonObject().get("name").getAsString();
                        name.setText(studentName.split(" ")[0] + " " + studentName.split(" ")[1]);
                        name.setTextColor(0xFFFFFFFF);
                        name.setClickable(true);

                        TextView N = new TextView(requireContext());

                        String resultcheck = new ServerRequests().execute("student/checkDate", students.get(finalI).getAsJsonObject().get("id").getAsString(), dateSession).get();
                        JsonObject jsonObjectcheck = JsonParser.parseString(resultcheck).getAsJsonObject();
                        String check = jsonObjectcheck.get("message").getAsString();

                        if (Objects.equals(check, "false")) {
                            N.setText("Н               ");
                            N.setTextColor(0xFF808080);
                            N.setId(students.get(finalI).getAsJsonObject().get("id").getAsInt());
                        } else {
                            N.setText("Н               ");
                            N.setTextColor(0xFFFFFFFF);
                            N.setId(students.get(finalI).getAsJsonObject().get("id").getAsInt());
                        }

                        row.addView(name);
                        row.addView(N);

                        String finalDateSession = dateSession;
                        N.setOnClickListener(v -> {
                            int color=N.getCurrentTextColor();
                            if (color == -8355712) {
                                try {
                                    new ServerRequests().execute("student/editDates", "add", students.get(finalI).getAsJsonObject().get("id").getAsString(), finalDateSession).get();
                                } catch (ExecutionException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                                N.setTextColor(0xFFFFFFFF);
                            } else {
                                try {
                                    new ServerRequests().execute("student/editDates", "delete", students.get(finalI).getAsJsonObject().get("id").getAsString(), finalDateSession).get();
                                } catch (ExecutionException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                                N.setTextColor(0xFF808080);
                            }
                        });

                        row.setOnClickListener(view1 -> {
                            new Session(requireContext()).setActionStudent(students.get(finalI).getAsJsonObject().get("name").getAsString());
                            NavHostFragment.findNavController(StudentsList.this)
                                    .navigate(R.id.action_FragmentStudentsList_to_FragmentStudentInfo);
                        });

                        name.setOnClickListener(view1 -> {
                            new Session(requireContext()).setActionStudent(students.get(finalI).getAsJsonObject().get("name").getAsString());
                            NavHostFragment.findNavController(StudentsList.this)
                                    .navigate(R.id.action_FragmentStudentsList_to_FragmentStudentInfo);
                        });

                        ll.addView(row, paramsRow);

                    }

                    if (students.size() == 0) {
                        TableRow.LayoutParams params = new TableRow.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                dpToPx(100));
                        params.setMargins(dpToPx(100), dpToPx(100), dpToPx(100), dpToPx(100));
                        TableRow row = new TableRow(requireContext());
                        row.setLayoutParams(params);
                        row.setPadding(dpToPx(15), dpToPx(15), dpToPx(15), dpToPx(15));
                        row.setBackgroundResource(R.drawable.faculities_widget);
                        TableLayout.LayoutParams paramsRow = new TableLayout.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT);
                        paramsRow.setMargins(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
                        TextView name = new TextView(requireContext());
                        name.setText("Студенты не найдены");
                        name.setTextColor(0xFFFFFFFF);

                        row.addView(name);

                        ll.addView(row, paramsRow);
                    }

                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        TableLayout ll = view.findViewById(R.id.table_layout);
        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        try {
            String result = new ServerRequests().execute("students/get", departament).get();
            JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
            JsonArray students = jsonObject.get("message").getAsJsonArray();
            @SuppressLint("SimpleDateFormat") DateFormat simple = new SimpleDateFormat("dd.MM.yyyy");
            Date daters = new Date(binding.calendarView.getDate());
            String date = simple.format(daters);
            //Log.d("test", date);
            //Log.d("test", String.valueOf(binding.calendarView.getDate()));
            new Session(requireContext()).setDate(date);
            for (int i = 0; i < students.size(); i++) {
                int finalI = i;
                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        TableRow.LayoutParams. MATCH_PARENT ,
                        dpToPx(100) );
                params.setMargins(dpToPx(100), dpToPx(100), dpToPx(100), dpToPx(100));
                TableRow row = new TableRow(requireContext());
                row.setLayoutParams(params);
                row.setPadding(dpToPx(15), dpToPx(15), dpToPx(15), dpToPx(15));
                row.setBackgroundResource(R.drawable.faculities_widget);
                TableLayout.LayoutParams paramsRow = new TableLayout.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT ,
                        TableRow.LayoutParams.WRAP_CONTENT );
                paramsRow.setMargins(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
                TextView name = new TextView(requireContext());
                String studentName = students.get(finalI).getAsJsonObject().get("name").getAsString();
                name.setText(studentName.split(" ")[0] + " " + studentName.split(" ")[1]);
                name.setTextColor(0xFFFFFFFF);
                name.setClickable(true);

                TextView N = new TextView(requireContext());

                String resultcheck = new ServerRequests().execute("student/checkDate", students.get(finalI).getAsJsonObject().get("id").getAsString(), date).get();
                JsonObject jsonObjectcheck = JsonParser.parseString(resultcheck).getAsJsonObject();
                String check = jsonObjectcheck.get("message").getAsString();

                if (Objects.equals(check, "false")) {
                    N.setText("Н               ");
                    N.setTextColor(0xFF808080);
                    N.setId(students.get(finalI).getAsJsonObject().get("id").getAsInt());
                } else {
                    N.setText("Н               ");
                    N.setTextColor(0xFFFFFFFF);
                    N.setId(students.get(finalI).getAsJsonObject().get("id").getAsInt());
                }

                row.addView(name);
                row.addView(N);

                N.setOnClickListener(v -> {
                    int color=N.getCurrentTextColor();
                    if (color == -8355712) {
                        try {
                            new ServerRequests().execute("student/editDates", "add", students.get(finalI).getAsJsonObject().get("id").getAsString(), date).get();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        N.setTextColor(0xFFFFFFFF);
                    } else {
                        try {
                            new ServerRequests().execute("student/editDates", "delete", students.get(finalI).getAsJsonObject().get("id").getAsString(), date).get();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        N.setTextColor(0xFF808080);
                    }
                });

                row.setOnClickListener(view1 -> {
                    new Session(requireContext()).setActionStudent(students.get(finalI).getAsJsonObject().get("name").getAsString());
                    NavHostFragment.findNavController(StudentsList.this)
                            .navigate(R.id.action_FragmentStudentsList_to_FragmentStudentInfo);
                });

                name.setOnClickListener(view1 -> {
                    new Session(requireContext()).setActionStudent(students.get(finalI).getAsJsonObject().get("name").getAsString());
                    NavHostFragment.findNavController(StudentsList.this)
                            .navigate(R.id.action_FragmentStudentsList_to_FragmentStudentInfo);
                });

                ll.addView(row, paramsRow);

            }
            if (students.size() == 0) {
                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        TableRow.LayoutParams. MATCH_PARENT ,
                        dpToPx(100) );
                params.setMargins(dpToPx(100), dpToPx(100), dpToPx(100), dpToPx(100));
                TableRow row = new TableRow(requireContext());
                row.setLayoutParams(params);
                row.setPadding(dpToPx(15), dpToPx(15), dpToPx(15), dpToPx(15));
                row.setBackgroundResource(R.drawable.faculities_widget);
                TableLayout.LayoutParams paramsRow = new TableLayout.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT ,
                        TableRow.LayoutParams.WRAP_CONTENT );
                paramsRow.setMargins(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
                TextView name = new TextView(requireContext());
                name.setText("Студенты не найдены");
                name.setTextColor(0xFFFFFFFF);

                row.addView(name);

                ll.addView(row, paramsRow);
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