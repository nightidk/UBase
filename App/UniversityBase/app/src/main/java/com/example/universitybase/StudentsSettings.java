package com.example.universitybase;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.universitybase.databinding.FragmentStudentsSettingsBinding;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.concurrent.ExecutionException;


public class StudentsSettings extends Fragment {
    private boolean showHideAddDepartmentsStatus;
    private FragmentStudentsSettingsBinding binding;

    private void changeAddDepartmentsLayoutStatus(int height, boolean status, String text) {
        binding.addStudentLayout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        height
                )
        );
        binding.add.setText(text);
        showHideAddDepartmentsStatus = status;
    }

    private void removeText() {
        binding.facultyNameAdd.setText("");
        binding.departmentNameAdd.setText("");
        binding.studentNameAdd.setText("");
        binding.studentBirthday.setText("");
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        MainActivity.setFragmentNow("StudentsSettings");
        binding = FragmentStudentsSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = requireContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private void confirmDelete(TableLayout ll, TableRow row, String param, String text) {
        binding.contentLayout.setVisibility(View.INVISIBLE);
        binding.deleteConfirmation.setVisibility(View.VISIBLE);
        binding.deleteConfirmationMessage.setText("Удалить студента \"" + text + "\"?");

        binding.confirmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row.setVisibility(View.INVISIBLE);
                ll.removeView(row);
                binding.deleteConfirmation.setVisibility(View.INVISIBLE);
                binding.contentLayout.setVisibility(View.VISIBLE);
                new ServerRequests().execute("student/delete", param);
            }
        });
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TableLayout ll = view.findViewById(R.id.linear_fragment);
        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        try {
            String result = new ServerRequests().execute("students/getAll").get();
            JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
            JsonArray students = jsonObject.get("message").getAsJsonArray();
            for (int i = 0; i < students.size(); i++) {
                TableRow row = new TableRow(getActivity());
                TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                        TableRow.LayoutParams. MATCH_PARENT ,
                        TableRow.LayoutParams. MATCH_PARENT );
                params.setMargins(0, dpToPx(10), 0, 0);
                row.setLayoutParams(params);
                row.setPadding(10, 10, 10, 10);
                row.setTag("@+id/row-" + (i + 1));
                TableRow.LayoutParams paramsText = new TableRow.LayoutParams(
                        TableRow.LayoutParams. MATCH_PARENT ,
                        TableRow.LayoutParams.WRAP_CONTENT );
                paramsText.setMargins(dpToPx(3), dpToPx(3), 0, dpToPx(3));
                EditText text = new EditText(requireContext());
                text.setLayoutParams(paramsText);
                text.setText(students.get(i).getAsJsonObject().get("name").getAsString());
                text.setTextSize(18);
                text.setEnabled(false);
                text.setTextColor(0xFFFFFFFF);
                text.setBackgroundColor(0xFF0e0e10);
                text.setTag("@+id/button-" + (i + 1));
                text.setId(students.get(i).getAsJsonObject().get("id").getAsInt());
                EditText date = new EditText(requireContext());
                date.setLayoutParams(paramsText);
                date.setText(students.get(i).getAsJsonObject().get("date").getAsString());
                date.setTextSize(18);
                date.setEnabled(false);
                date.setTextColor(0xFFFFFFFF);
                date.setBackgroundColor(0xFF0e0e10);
                date.setTag("@+id/date-" + (i + 1));
                //date.setId(faculties.get(i).getAsJsonObject().get("id").getAsInt());
                LinearLayout llb = new LinearLayout(requireContext());
                llb.setOrientation(LinearLayout.HORIZONTAL);
                ImageButton accept = new ImageButton(requireContext());
                ImageButton edit = new ImageButton(requireContext());
                ImageButton delete = new ImageButton(requireContext());

                TableLayout.LayoutParams icons = new TableLayout.LayoutParams(
                        TableRow.LayoutParams. MATCH_PARENT ,
                        TableRow.LayoutParams. WRAP_CONTENT ) ;
                icons.setLayoutDirection(TableRow.HORIZONTAL);
                icons.height = dpToPx(30);
                icons.weight = 1;
                icons.width = dpToPx(30);
                icons.topMargin = dpToPx(10);
                //icons.setMargins(0, dpToPx(10), 0, 0);

                accept.setLayoutParams(icons);
                accept.setImageResource(R.drawable.ic_icons8_done);
                accept.setBackgroundColor(0xFF0e0e10);
                accept.setVisibility(View.INVISIBLE);
                accept.setPadding(0, dpToPx(10), 0, 0);
                edit.setLayoutParams(icons);
                edit.setImageResource(R.drawable.ic_icons8_edit);
                edit.setBackgroundColor(0xFF0e0e10);
                edit.setPadding(0, 10, 0, 0);
                delete.setLayoutParams(icons);
                delete.setImageResource(R.drawable.ic_icons8_delete);
                delete.setBackgroundColor(0xFF0e0e10);
                edit.setPadding(0, 10, 0, 0);

                accept.setOnClickListener(v -> {

                    text.clearFocus();
                    text.setEnabled(false);
                    date.setEnabled(false);
                    accept.setVisibility(View.INVISIBLE);
                    EditText textedited = view.findViewWithTag(text.getTag());
                    EditText dateedited = view.findViewWithTag(date.getTag());

                    new ServerRequests().execute("student/edit", String.valueOf(textedited.getId()), textedited.getText().toString(), dateedited.getText().toString());
                });

                int finalI = i;
                delete.setOnClickListener(v -> {

                    text.clearFocus();
                    text.setEnabled(false);
                    date.setEnabled(false);
                    accept.setVisibility(View.INVISIBLE);

                    confirmDelete(ll, row, String.valueOf(students.get(finalI).getAsJsonObject().get("id").getAsInt()), text.getText().toString());
                });

                edit.setOnClickListener(v -> {
                    text.setEnabled(true);
                    text.requestFocus();
                    date.setEnabled(true);
                    accept.setVisibility(View.VISIBLE);
                });

                TableLayout.LayoutParams paramsLayout = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                llb.addView(accept);
                llb.addView(edit);
                llb.addView(delete);
                row.addView(text);
                row.addView(date);
                row.addView(llb);
                //row.addView(llb);
                //row.addView(accept);
                //row.addView(edit);
                //row.addView(delete);
                ll.addView(row, paramsLayout);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        showHideAddDepartmentsStatus = false;
        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showHideAddDepartmentsStatus) {
                    changeAddDepartmentsLayoutStatus(0, false, "Добавить");
                    removeText();
                }
                else
                    changeAddDepartmentsLayoutStatus(LinearLayout.LayoutParams.MATCH_PARENT, true, "Отменить");

            }
        });

        binding.cancelDelete.setOnClickListener(v_inner -> {
            binding.deleteConfirmation.setVisibility(View.INVISIBLE);
            binding.contentLayout.setVisibility(View.VISIBLE);
        });

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText texteditfaculty = binding.facultyNameAdd;
                String nameFaculty = texteditfaculty.getText().toString();
                EditText texteditdepartament = binding.departmentNameAdd;
                String nameDepartament = texteditdepartament.getText().toString();
                EditText texteditName = binding.studentNameAdd;
                String name = texteditName.getText().toString();
                EditText texteditDate = binding.studentBirthday;
                String dateN = texteditDate.getText().toString();

                if (!name.equals("") && !nameFaculty.equals("") && !nameDepartament.equals("") && !dateN.equals("")) {
                    try {
                        String result = new ServerRequests().execute("student/add", name, nameFaculty, nameDepartament, dateN).get();

                        JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
                        Integer i = jsonObject.get("message").getAsJsonObject().get("insertId").getAsInt() - 1;
                        TableRow row = new TableRow(getActivity());
                        TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                                TableRow.LayoutParams. MATCH_PARENT ,
                                TableRow.LayoutParams. MATCH_PARENT );
                        params.setMargins(0, dpToPx(10), 0, 0);
                        row.setLayoutParams(params);
                        row.setPadding(10, 10, 10, 10);
                        row.setTag("@+id/row-" + (i + 1));
                        TableRow.LayoutParams paramsText = new TableRow.LayoutParams(
                                TableRow.LayoutParams. MATCH_PARENT ,
                                TableRow.LayoutParams.WRAP_CONTENT );
                        paramsText.setMargins(dpToPx(3), dpToPx(3), 0, dpToPx(3));
                        EditText text = new EditText(requireContext());
                        text.setLayoutParams(paramsText);
                        text.setText(name);
                        text.setTextSize(18);
                        text.setEnabled(false);
                        text.setTextColor(0xFFFFFFFF);
                        text.setBackgroundColor(0xFF0e0e10);
                        text.setTag("@+id/button-" + (i + 1));
                        text.setId(i+1);
                        EditText date = new EditText(requireContext());
                        date.setLayoutParams(paramsText);
                        date.setText(dateN);
                        date.setTextSize(18);
                        date.setEnabled(false);
                        date.setTextColor(0xFFFFFFFF);
                        date.setBackgroundColor(0xFF0e0e10);
                        date.setTag("@+id/date-" + (i + 1));
                        LinearLayout llb = new LinearLayout(requireContext());
                        llb.setOrientation(LinearLayout.HORIZONTAL);
                        ImageButton accept = new ImageButton(requireContext());
                        ImageButton edit = new ImageButton(requireContext());
                        ImageButton delete = new ImageButton(requireContext());

                        TableLayout.LayoutParams icons = new TableLayout.LayoutParams(
                                TableRow.LayoutParams. MATCH_PARENT ,
                                TableRow.LayoutParams. WRAP_CONTENT ) ;
                        icons.setLayoutDirection(TableRow.HORIZONTAL);
                        icons.height = dpToPx(30);
                        icons.weight = 1;
                        icons.width = dpToPx(30);
                        icons.topMargin = dpToPx(10);

                        accept.setLayoutParams(icons);
                        accept.setImageResource(R.drawable.ic_icons8_done);
                        accept.setBackgroundColor(0xFF0e0e10);
                        accept.setVisibility(View.INVISIBLE);
                        accept.setPadding(0, dpToPx(10), 0, 0);
                        edit.setLayoutParams(icons);
                        edit.setImageResource(R.drawable.ic_icons8_edit);
                        edit.setBackgroundColor(0xFF0e0e10);
                        edit.setPadding(0, 10, 0, 0);
                        delete.setLayoutParams(icons);
                        delete.setImageResource(R.drawable.ic_icons8_delete);
                        delete.setBackgroundColor(0xFF0e0e10);
                        edit.setPadding(0, 10, 0, 0);

                        accept.setOnClickListener(v -> {

                            text.clearFocus();
                            text.setEnabled(false);
                            date.setEnabled(false);
                            accept.setVisibility(View.INVISIBLE);
                            EditText textedited = view.findViewWithTag(text.getTag());
                            EditText dateedited = view.findViewWithTag(date.getTag());

                            new ServerRequests().execute("student/edit", String.valueOf(textedited.getId()), textedited.getText().toString(), dateedited.getText().toString());
                        });

                        int finalI = i;
                        delete.setOnClickListener(v -> {

                            text.clearFocus();
                            text.setEnabled(false);
                            date.setEnabled(false);
                            accept.setVisibility(View.INVISIBLE);

                            confirmDelete(ll, row, String.valueOf(finalI+1), text.getText().toString());
                        });

                        edit.setOnClickListener(v -> {
                            text.setEnabled(true);
                            text.requestFocus();
                            date.setEnabled(true);
                            accept.setVisibility(View.VISIBLE);
                        });

                        TableLayout.LayoutParams paramsLayout = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                        llb.addView(accept);
                        llb.addView(edit);
                        llb.addView(delete);
                        row.addView(text);
                        row.addView(date);
                        row.addView(llb);
                        ll.addView(row, paramsLayout);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                changeAddDepartmentsLayoutStatus(0, false, "Добавить");
                removeText();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}