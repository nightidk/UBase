package com.example.universitybase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.universitybase.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    // Поля для идентификации текущего фрагмента //
    private final String FACULTIES = "Faculties";
    private final String DEPARTMENTS = "Departments";
    private final String STUDENT_INFO = "StudentInfo";
    private final String STUDENTS_LIST = "StudentsList";
    private final String FACULTIES_SETTINGS = "FacultiesSettings";
    private final String DEPARTMENTS_SETTINGS = "DepartmentsSettings";
    private final String STUDENTS_SETTINGS = "StudentsSettings";
    ///////////////////////////////////////////////////////////////////

    private static String login;

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private static String fragmentNow;

    public static void setFragmentNow(String fragmentName) {
        fragmentNow = fragmentName;
    }
    public static void setLogin(String login_value) {
        login = login_value;
    }
    public static void deleteLogin() {
        login = null;
    }
    public static String getLogin() {
        return login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        ////////////////////////////////////////// Установка ToolBar ///////////////////////////////////////////////
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /////////////////// Событие нажатия "Настройки" /////////////////////////////
        binding.settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int action = 0;

                switch (fragmentNow) {
                    case FACULTIES:
                        action = R.id.action_FragmentFaculties_to_FragmentSettings;
                        break;
                    case DEPARTMENTS:
                        action = R.id.action_FragmentDepartments_to_FragmentSettings;
                        break;
                    case STUDENT_INFO:
                        action = R.id.action_FragmentStudentInfo_to_FragmentSettings;
                        break;
                    case STUDENTS_LIST:
                        action = R.id.action_FragmentStudentsList_to_FragmentSettings;
                        break;
                    case FACULTIES_SETTINGS:
                        action = R.id.action_FacultiesSettings_to_FragmentSettings;
                        break;
                    case DEPARTMENTS_SETTINGS:
                        action = R.id.action_DepartmentsSettings_to_FragmentSettings;
                        break;
                    case STUDENTS_SETTINGS:
                        action = R.id.action_StudentsSettings_to_FragmentSettings;
                        break;
                    default:
                        break;
                }
                if (action > 0)
                    navController.navigate(action);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////
    }

    ///////////////////////////////// Обеспечение работы кнопки "Назад" в toolbar //////////////////////////////////
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
}