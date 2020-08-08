package com.example.schoolattack.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.schoolattack.R;
import com.example.schoolattack.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistroActivity extends AppCompatActivity {

    private EditText etName,etEmail,etPassword;
    private Button btRegistro;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    String name,email,password;
    ProgressBar pbRegistro;
    ScrollView formRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etName = findViewById(R.id.editTextName);
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);

        btRegistro = findViewById(R.id.buttonRegistro);
        pbRegistro = findViewById(R.id.progressBarRegistro);
        formRegistro = findViewById(R.id.formRegistro);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        changeRegistroFormVisibilty(true);
        eventos();
    }

    private void eventos() {
        btRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etName.getText().toString();
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();

                if(name.isEmpty()){
                    etName.setError("El nombre es obligatorio");
                } else if(email.isEmpty()){
                    etEmail.setError("El email es obligatorio");
                } else if(password.isEmpty()){
                    etPassword.setError("El password es obligatorio");
                } else{
                    //TODO: Realizar autenticacion en firebase
                    createUser();
                }

            }
        });

    }

    public void createUser(){
        changeRegistroFormVisibilty(false);
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        }else{
                            Toast.makeText(RegistroActivity.this,"Error en el Registro",Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if(user!=null){
            //TODO: Almacenar la informacion en el firestore

            User nuevoUsuario = new User(name,0,0);
            db.collection("users")
                    .document(user.getUid())
                    .set(nuevoUsuario)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            finish();
                            //Intent i = new Intent(RegistroActivity.this, LoginActivity.class);
                            Intent i = new Intent(RegistroActivity.this, MenuPrincipalActivity.class);
                            startActivity(i);
                        }
                    });

        }else{
            changeRegistroFormVisibilty(true);
            etPassword.setError("Nombre, email y/o contraseña incorrectos");
            etPassword.requestFocus();
        }
    }

    private void changeRegistroFormVisibilty(boolean showForm){
        pbRegistro.setVisibility(showForm ? View.GONE : View.VISIBLE);
        formRegistro.setVisibility(showForm ? View.VISIBLE : View.GONE);
    }

}
