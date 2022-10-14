package com.example.universitybase;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.universitybase.databinding.LoginBinding;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class Login extends Fragment {

    private final String LOGIN_AND_PASSWORD_EMPTY = "Введите логин и пароль";
    private final String WRONG_LOGIN_OR_PASSWORD = "Неверный логин или пароль";

    private LoginBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = LoginBinding.inflate(inflater, container, false); // Создание объекта класса LoginBinding
        return binding.getRoot();                                                // Получение корневого Layout

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ////////////// Обработчик события нажатия на кнопку "Войти" /////////////////////////////
        binding.loginButton.setOnClickListener(view12 -> {
            String login = binding.loginField.getText().toString();
            String password = binding.passwordField.getText().toString();
            if (login.length() > 0 && password.length() > 0) {
                try {
                    String result = new ServerRequests().execute("login", login, password).get(); // Отправка данных логина и пароля для попытки авторизоваться
                    JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject(); // Преобразование ответа сервера(String) в Json объект

                    /////////////////////// Проверка статуса ответа /////////////////////////
                    if (Objects.equals(jsonObject.get("status").getAsString(), "error")) {
                        binding.loginError.setText(WRONG_LOGIN_OR_PASSWORD); // сообщение о неверном логине или пароле
                    } else {
                        new Session(getContext()).setAuth(login, password); // Создание сессии для авторизированного пользователя
                        requireActivity().finish();                         // Завершение работы activity
                        startActivity(new Intent(requireActivity(), MainActivity.class)); // Запуск главного activity приложения
                    }
                    /////////////////////////////////////////////////////////////////////////
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else
                binding.loginError.setText(LOGIN_AND_PASSWORD_EMPTY);
        });
        /////////////////////////////////////////////////////////////////////////////////////////

        /////////////////////////////// Обработчик события нажатия на кнопку "Регистрация" /////////////////////////////
        binding.registrationReference.setOnClickListener(view1 -> NavHostFragment.findNavController(Login.this)
                .navigate(R.id.to_registration));
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}