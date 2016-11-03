package com.berggrentech.myeconomics;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Simon Berggren for assignment 1 in the course Development of Mobile Devices.
 */
public class LoginFragment extends Fragment {

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        final EditText etEmail = (EditText) view.findViewById(R.id.login_email);
        final EditText etPassword = (EditText) view.findViewById(R.id.login_password);
        final Button btnLogin = (Button) view.findViewById(R.id.btnLogin);
        final Button btnRegister = (Button) view.findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment regFrag = new RegisterFragment();
                regFrag.show(getActivity().getFragmentManager(), "registerFragment");
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().toLowerCase().trim();
                String password = etPassword.getText().toString();

                if(email.equals("") && password.equals("")) {
                    etEmail.setText(etEmail.getHint());
                    etPassword.setText(etPassword.getHint());

                    email = etEmail.getText().toString().toLowerCase().trim();
                    password = etPassword.getText().toString();
                }

                User user;

                if(Utils.isValidEmail(email))
                    user = MainActivity.DBM.getUser(email);
                else {
                    Toast.makeText(getActivity(), "Invalid email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(user == null) {
                    // wrong or invalid email
                    Toast.makeText(getActivity(), "Wrong email!", Toast.LENGTH_SHORT).show();
                } else if(!user.getPassword().contentEquals(password)) {
                    // wrong password
                    Toast.makeText(getActivity(), "Invalid password!", Toast.LENGTH_SHORT).show();

                } else {
                    // success
                    Toast.makeText(getActivity(), "Success! Welcome, " + user.getFirstName() + ".", Toast.LENGTH_SHORT).show();

                    Utils.saveUserPref(user);
                    MainActivity.ID = user.getID();
                    MainActivity.USER = user;

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        return view;
    }
}
