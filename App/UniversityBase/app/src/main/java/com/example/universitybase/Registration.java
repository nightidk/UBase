package com.example.universitybase;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.universitybase.databinding.RegistrationBinding;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class Registration extends Fragment {

    private final String LOGIN_ALREADY_EXISTS = "Данный логин уже занят";
    private final String UNKNOWN_ERROR = "Произошла неизвестная ошибка";
    private final String EMPTY_STRINGS = "Присутствуют незаполненные поля";

    private RegistrationBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = RegistrationBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //////////////////////// Обработчик события нажатия кнопки "Регистрация" /////////////////////////////
        binding.registrationButton.setOnClickListener(view1 -> {
            String name = binding.nameField.getText().toString();
            String login = binding.logoField.getText().toString();
            String password = binding.passwordField.getText().toString();
            if (login.length() > 0 && password.length() > 0 && name.length() > 0) {
                try {
                    String result = new ServerRequests().execute("register", login, password, name).get();  // Отправка данных на сервер
                    JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();               // Преобразование String объекта в Json объект

                    //////////////////////// Проверка статуса ответа /////////////////////////////////
                    if (Objects.equals(jsonObject.get("status").getAsString(), "error")) {
                        if (Objects.equals(jsonObject.get("code").getAsInt(), 2)) {
                            binding.registrationError.setText(LOGIN_ALREADY_EXISTS);
                        } else {
                            binding.registrationError.setText(UNKNOWN_ERROR);
                        }
                    } else {
                        new Thread(() -> {
                            try {
                                binding.registrationButton.setClickable(false);
                                binding.registrationError.setTextColor(0xFF18FF2F);
                                binding.registrationError.setText("Вы успешно зарегестрировались");
                                Thread.sleep(3000);
                                requireActivity().finish();                                // Завершение работы activity
                                startActivity(new Intent(requireActivity(), Auth.class)); // Запуск Auth activity
                            }catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }).start();
                    }
                    /////////////////////////////////////////////////////////////////////////////////
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }


            }
            else {
                binding.registrationError.setText(EMPTY_STRINGS);
            }
            ////////////////////////////////////////////////////////////////////////////////////////////////
        });

        /////////////////////////////// Обработчик события нажатия на кнопку "Вход" /////////////////////////////
        binding.loginReference.setOnClickListener(view12 -> NavHostFragment.findNavController(Registration.this)
                .navigate(R.id.to_login));
        ////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}