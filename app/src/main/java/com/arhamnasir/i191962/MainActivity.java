package com.arhamnasir.i191962;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();
        mFirestore= FirebaseFirestore.getInstance();


        TextView signUpButton = findViewById(R.id.signUpButton);
        EditText email = findViewById(R.id.email);
        EditText pass = findViewById(R.id.pass);



        signUpButton.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                Log.d("myTag", "This is my message");

                mAuth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    Log.d("myTag", "Successfull");

                                    Map<String, String> data = new HashMap<>();

// Add key-value pairs to the map
                                    data.put("email", email.getText().toString());
                                    data.put("password",pass.getText().toString());

                                    CollectionReference usersCollection = mFirestore.collection("Users");

                                    usersCollection.add(data)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    // Document added successfully
                                                    String documentId = documentReference.getId();
                                                    Log.d("myTag", "Document added with ID: " + documentId);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Handle any errors that occur
                                                    Log.e("myTag", "Error adding document", e);
                                                    // You can display an error message or take appropriate action here.
                                                }
                                            });




                                    Toast.makeText(
                                            MainActivity.this,
                                            "Successfully Created User",
                                            Toast.LENGTH_LONG).show();
                                    Toast.makeText(
                                            MainActivity.this,
                                            mAuth.getCurrentUser().getUid(),
                                            Toast.LENGTH_LONG
                                    ).show();
                                }
                            }
                        })

//                        .addOnCompleteListener(new onCompleteLitener<AuthResult>)
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(
//                                        MainActivity.this,
//                                        "Failed to create user",
//                                        Toast.LENGTH_LONG
//                                ).show();
//                            }
//                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("myTag", e.toString());

                                Toast.makeText(
                                        MainActivity.this,
                                        "Failed to create user",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        });
                Intent intent = new Intent(MainActivity.this, registration.class);
                startActivity(intent);
            }
        });

        TextView forgotPasswordTextView= findViewById(R.id.forgotPasswordTextView);

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, forget_password.class);
                startActivity(intent);

            }
        });

        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,WelcomeScreen.class);
                startActivity(intent);

            }
        });








    }
//////////////////////////////////////////////Firebase///////////////////////////////////
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user= mAuth.getCurrentUser();
        if(user != null)
        {
            Toast.makeText(
                    MainActivity.this,
                    user.getUid()+"",
                    Toast.LENGTH_SHORT
            ).show();
        }


    }
}
