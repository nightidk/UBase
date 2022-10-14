package com.example.universitybase;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.universitybase.databinding.FragmentDepartmentsSettingsBinding;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.concurrent.ExecutionException;


public class DepartmentsSettings extends Fragment {

    private boolean showHideAddDepartmentsStatus;
    private FragmentDepartmentsSettingsBinding binding;

    private void changeAddDepartmentsLayoutStatus(int height, boolean status, String text) {
        binding.addDepartmentLayout.setLayoutParams(
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
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        MainActivity.setFragmentNow("DepartmentsSettings");
        binding = FragmentDepartmentsSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private void confirmDelete(TableLayout ll, TableRow row, String param, String text) {
        binding.contentLayout.setVisibility(View.INVISIBLE);
        binding.deleteConfirmation.setVisibility(View.VISIBLE);
        binding.deleteConfirmationMessage.setText("Удалить кафедру \"" + text + "\"?");

        binding.confirmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row.setVisibility(View.INVISIBLE);
                ll.removeView(row);
                binding.deleteConfirmation.setVisibility(View.INVISIBLE);
                binding.contentLayout.setVisibility(View.VISIBLE);
                new ServerRequests().execute("departament/delete", param);
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TableLayout ll = view.findViewById(R.id.table_layout);
        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        try {
            String result = new ServerRequests().execute("departaments/get").get();
            JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
            JsonArray departments = jsonObject.get("message").getAsJsonArray();
            for (int i = 0; i < departments.size(); i++) {
                TableRow row = new TableRow(getActivity());
                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        TableRow.LayoutParams. MATCH_PARENT ,
                        dpToPx(100) );
                row.setLayoutParams(params);
                row.setTag("@+id/row-" + String.valueOf(i + 1));
                EditText text = new EditText(requireContext());
                text.setText(departments.get(i).getAsJsonObject().get("name").getAsString());
                text.setTextSize(18);
                text.setEnabled(false);
                text.setTextColor(0xFFFFFFFF);
                text.setBackgroundColor(0xFF0e0e10);
                text.setTag("@+id/button-" + String.valueOf(i + 1));
                text.setId(departments.get(i).getAsJsonObject().get("id").getAsInt());
                LinearLayout llb = new LinearLayout(requireContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams. MATCH_PARENT ,
                        LinearLayout.LayoutParams. WRAP_CONTENT ) ;
                llb.setOrientation(LinearLayout.HORIZONTAL);
                llb.addView(text);
                ImageButton accept = new ImageButton(requireContext());
                ImageButton edit = new ImageButton(requireContext());
                ImageButton delete = new ImageButton(requireContext());

                TableRow.LayoutParams icons = new TableRow.LayoutParams(
                        TableRow.LayoutParams. MATCH_PARENT ,
                        TableRow.LayoutParams. WRAP_CONTENT ) ;
                icons.height = dpToPx(30);
                icons.weight = 1;
                icons.width = dpToPx(30);
                icons.setMargins(dpToPx(3), dpToPx(3), dpToPx(3), dpToPx(3));

                accept.setLayoutParams(icons);
                accept.setImageResource(R.drawable.ic_icons8_done);
                accept.setBackgroundColor(0xFF0e0e10);
                accept.setVisibility(View.INVISIBLE);
                edit.setLayoutParams(icons);
                edit.setImageResource(R.drawable.ic_icons8_edit);
                edit.setBackgroundColor(0xFF0e0e10);
                delete.setLayoutParams(icons);
                delete.setImageResource(R.drawable.ic_icons8_delete);
                delete.setBackgroundColor(0xFF0e0e10);

                accept.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {

                          text.clearFocus();
                          text.setEnabled(false);
                          accept.setVisibility(view.INVISIBLE);
                          EditText textedited = view.findViewWithTag(text.getTag());

                          new ServerRequests().execute("departament/edit", String.valueOf(textedited.getId()), textedited.getText().toString());
                      }
                });

                int finalI = i;
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text.clearFocus();
                        text.setEnabled(false);
                        accept.setVisibility(view.INVISIBLE);

                        confirmDelete(ll, row, String.valueOf(departments.get(finalI).getAsJsonObject().get("id").getAsInt()), text.getText().toString());
                    }
                });

                edit.setOnClickListener(v -> {
                    text.setEnabled(true);
                    text.requestFocus();
                    accept.setVisibility(view.VISIBLE);
                });

                TableLayout.LayoutParams paramsLayout = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);

                row.addView(llb);
                row.addView(accept);
                row.addView(edit);
                row.addView(delete);
                ll.addView(row, paramsLayout);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        showHideAddDepartmentsStatus = false;

        ////////////////////////// Обработчик события нажания кнопки для добавления кафедры /////////////////////////////////
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
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        binding.cancelDelete.setOnClickListener(v_inner -> {
            binding.deleteConfirmation.setVisibility(View.INVISIBLE);
            binding.contentLayout.setVisibility(View.VISIBLE);
        });

        ///////////////////// Обработчик события нажатия кнопки сохранения внесенных изменений в названии кафедры ///////////////////////////////
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText FacultyEdit = binding.facultyNameAdd;
                EditText DepartamentEdit = binding.departmentNameAdd;
                String FacultyName = FacultyEdit.getText().toString();
                String DepartamentName = DepartamentEdit.getText().toString();
                if (!FacultyName.equals("") && !DepartamentName.equals("")) {
                    try {
                        String result = new ServerRequests().execute("departament/add", FacultyName, DepartamentName).get();

                        JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
                        int i = jsonObject.get("message").getAsJsonObject().get("insertId").getAsInt() - 1;
                        TableRow row = new TableRow(getActivity());
                        TableRow.LayoutParams params = new TableRow.LayoutParams(
                                TableRow.LayoutParams. MATCH_PARENT ,
                                dpToPx(100) );
                        row.setLayoutParams(params);
                        row.setTag("@+id/row-" + (i + 1));
                        EditText text = new EditText(requireContext());
                        text.setText(DepartamentName);
                        text.setTextSize(18);
                        text.setEnabled(false);
                        text.setTextColor(0xFFFFFFFF);
                        text.setBackgroundColor(0xFF0e0e10);
                        text.setTag("@+id/button-" + (i + 1));
                        text.setId(i + 1);
                        LinearLayout llb = new LinearLayout(requireContext());
                        llb.setOrientation(LinearLayout.HORIZONTAL);
                        llb.addView(text);
                        ImageButton accept = new ImageButton(requireContext());
                        ImageButton edit = new ImageButton(requireContext());
                        ImageButton delete = new ImageButton(requireContext());

                        TableRow.LayoutParams icons = new TableRow.LayoutParams(
                                TableRow.LayoutParams. MATCH_PARENT ,
                                TableRow.LayoutParams. WRAP_CONTENT ) ;
                        icons.height = dpToPx(30);
                        icons.weight = 1;
                        icons.width = dpToPx(30);
                        icons.setMargins(dpToPx(3), dpToPx(3), dpToPx(3), dpToPx(3));

                        accept.setLayoutParams(icons);
                        accept.setImageResource(R.drawable.ic_icons8_done);
                        accept.setBackgroundColor(0xFF0e0e10);
                        accept.setVisibility(View.INVISIBLE);
                        edit.setLayoutParams(icons);
                        edit.setImageResource(R.drawable.ic_icons8_edit);
                        edit.setBackgroundColor(0xFF0e0e10);
                        delete.setLayoutParams(icons);
                        delete.setImageResource(R.drawable.ic_icons8_delete);
                        delete.setBackgroundColor(0xFF0e0e10);

                        accept.setOnClickListener(v -> {

                            text.clearFocus();
                            text.setEnabled(false);
                            accept.setVisibility(View.INVISIBLE);
                            EditText textedited = view.findViewWithTag(text.getTag());

                            new ServerRequests().execute("departament/edit", String.valueOf(textedited.getId()), textedited.getText().toString());
                        });

                        int finalI = i;
                        delete.setOnClickListener(v -> {

                            text.clearFocus();
                            text.setEnabled(false);
                            accept.setVisibility(View.INVISIBLE);

                            confirmDelete(ll, row, String.valueOf(finalI + 1), text.getText().toString());
                        });

                        edit.setOnClickListener(v -> {
                            text.setEnabled(true);
                            text.requestFocus();
                            accept.setVisibility(View.VISIBLE);
                        });

                        TableLayout.LayoutParams paramsLayout = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);

                        row.addView(llb);
                        row.addView(accept);
                        row.addView(edit);
                        row.addView(delete);
                        ll.addView(row, paramsLayout);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                changeAddDepartmentsLayoutStatus(0, false, "Добавить");
                removeText();
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}