package com.cryptodekho.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cryptodekho.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity {

    private Button register;
    private FirebaseAuth auth;
    FirebaseFirestore store;
    private EditText user, pwd, coins;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        register = (Button) findViewById(R.id.register);
        auth = FirebaseAuth.getInstance();
        store = FirebaseFirestore.getInstance();
        user = (EditText) findViewById(R.id.email);
        pwd = (EditText) findViewById(R.id.pwd);
        coins = (EditText) findViewById(R.id.coins);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = user.getText().toString();
                String passwd = pwd.getText().toString();
                String coin = coins.getText().toString();
                String[] coinArray = coin.split(",");
                List<String> coins = Arrays.asList(coinArray);
                registerUser(email, passwd, coins);
            }
        });
    }
    public void registerUser(String email, String password, List<String> coins){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            HashMap<String, List<String>> coinlist = new HashMap<>();
                            coinlist.put("Coins", coins);
                            store.collection("Users").document(user.getEmail()).set(coinlist).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(RegistrationActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegistrationActivity.this, SignInActivity.class);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}