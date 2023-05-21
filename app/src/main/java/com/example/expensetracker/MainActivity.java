package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.expensetracker.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth firebaseAuth;
//    ProgressBar pb =  findViewById(R.id.progress_bar);
    private long pressedTime;

      @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());

        //for disabling dark mode


        //for progressbar



          //

        setContentView(binding.getRoot());
        firebaseAuth= FirebaseAuth.getInstance();
        //for hiding actionbar
          if (getSupportActionBar() != null) {
              getSupportActionBar().hide();
          }



          //to go to sign-up page
        binding.gotosignupscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                try{
                    startActivity(intent);
                }catch(Exception e){

                }
            }
        });

            //for login button
          binding.loginButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  String email=binding.emailLogin.getText().toString().trim();
                  String password =binding.passwordLogin.getText().toString().trim();
                  if(email.isEmpty()||password.isEmpty()){
                      Toast.makeText(MainActivity.this,"Error: This field/s cannot be empty",Toast.LENGTH_SHORT).show();
                      return;
                  }
                  firebaseAuth.signInWithEmailAndPassword(email,password)
                          .addOnSuccessListener(new OnSuccessListener<AuthResult>() {

                              @Override
                              public void onSuccess(AuthResult authResult) {
                                  try{
                                      Toast.makeText(MainActivity.this,"Success!",Toast.LENGTH_SHORT).show();
                                      startActivity(new Intent(MainActivity.this,DashboardActivity.class));

                                  }catch(Exception e){

                                  }
                              }
                          })
                          .addOnFailureListener(new OnFailureListener() {
                              @Override
                              public void onFailure(@NonNull Exception e) {
                                  Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                              }
                          });
              }
          });

          //if already logged directly to dashboard

    }
    //Double press to exit
    public void onBackPressed(){
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }
}