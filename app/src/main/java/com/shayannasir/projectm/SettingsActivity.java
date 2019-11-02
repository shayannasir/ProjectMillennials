package com.shayannasir.projectm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    private Button UpdateAccountSettings;
    private EditText userName, userStatus;

    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    private Toolbar settingsToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        InitializeFields();

        UpdateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateSettings();
            }
        });
        
        RetrieveUserInfo();
    }

    private void InitializeFields() {
        UpdateAccountSettings = (Button)findViewById(R.id.update_settings_button);
        userName = (EditText) findViewById(R.id.set_user_name);
        userStatus = (EditText) findViewById(R.id.set_profile_status);
        settingsToolbar = (Toolbar)findViewById(R.id.settings_toolbar);
        setSupportActionBar(settingsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Account Settings");
    }


    private void UpdateSettings() {
        String setUserName = userName.getText().toString();
        String setUserStatus = userStatus.getText().toString();

        if(TextUtils.isEmpty(setUserName))
            Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
        if(TextUtils.isEmpty(setUserStatus))
            Toast.makeText(this, "Please enter a short description/status", Toast.LENGTH_SHORT).show();
        else{
            HashMap<String, Object> profileMap = new HashMap<>();
            profileMap.put("uid", currentUserID);
            profileMap.put("name", setUserName);
            profileMap.put("status", setUserStatus);
            RootRef.child("Users").child(currentUserID).updateChildren(profileMap).
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SettingsActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                        SendUserToMainActivity();
                    }
                    else{
                        String message = task.getException().toString();
                        Toast.makeText(SettingsActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void RetrieveUserInfo() {
        RootRef.child("Users").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            if(dataSnapshot.hasChild("name") && dataSnapshot.hasChild("status")){
                                String retrieveName = dataSnapshot.child("name").getValue().toString();
                                String retrieveStatus = dataSnapshot.child("status").getValue().toString();

                                userName.setText(retrieveName);
                                userStatus.setText(retrieveStatus);

                            }
                            if(dataSnapshot.hasChild("name")){
                                String retrieveName = dataSnapshot.child("name").getValue().toString();

                                userName.setText(retrieveName);

                            }
                        } else {
                            Toast.makeText(SettingsActivity.this, "Please update your profile information", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(SettingsActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
