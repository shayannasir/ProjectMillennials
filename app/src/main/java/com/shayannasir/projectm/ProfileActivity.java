package com.shayannasir.projectm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {

    private String receiverUserID, current_state, senderUserID;

    private TextView userProfileName, userProfileStatus;
    private Button SendMessageRequestButton;

    private DatabaseReference UserRef, ChatReqRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        InitializeFields();

        RetrieveUserInfo();
    }

    private void RetrieveUserInfo() {
        UserRef.child(receiverUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String userName = dataSnapshot.child("name").getValue().toString();
                    String userStatus = dataSnapshot.child("status").getValue().toString();

                    userProfileName.setText(userName);
                    userProfileStatus.setText(userStatus);

                    ManageChatRequest();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ManageChatRequest() {

        ChatReqRef.child(senderUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(receiverUserID)){
                    String request_type = dataSnapshot.child(receiverUserID).child("request_type").getValue().toString();

                    if(request_type.equals("sent")){
                        current_state = "request_sent";
                        SendMessageRequestButton.setText("Cancel Chat Request");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(!senderUserID.equals(receiverUserID)){
            SendMessageRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SendMessageRequestButton.setEnabled(false);
                    if(current_state.equals("new")){
                        SendChatRequest();
                    }
                    if(current_state.equals("request_sent")){
                        CancelChatRequest();
                    }
                }
            });
        }else{
            SendMessageRequestButton.setVisibility(View.INVISIBLE);
        }
    }


    private void SendChatRequest() {
        ChatReqRef.child(senderUserID).child(receiverUserID).child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            ChatReqRef.child(receiverUserID).child(senderUserID).child("request_type").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                SendMessageRequestButton.setEnabled(true);
                                                current_state = "request_sent";
                                                SendMessageRequestButton.setText("Cancel Chat Request");
                                            }
                                        }
                                    });
                        }
                    }
                });

    }


    private void CancelChatRequest() {
        ChatReqRef.child(senderUserID).child(receiverUserID).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            ChatReqRef.child(receiverUserID).child(senderUserID).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                SendMessageRequestButton.setEnabled(true);
                                                current_state = "new";
                                                SendMessageRequestButton.setText("Send Message");
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void InitializeFields() {
        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ChatReqRef = FirebaseDatabase.getInstance().getReference().child("ChatRequests");

        receiverUserID = getIntent().getExtras().get("visit_user_id").toString();
        senderUserID = mAuth.getCurrentUser().getUid();
        current_state = "new";

        userProfileName = (TextView)findViewById(R.id.visit_user_name);
        userProfileStatus = (TextView)findViewById(R.id.visit_user_status);
        SendMessageRequestButton = (Button)findViewById(R.id.send_message_request_button);

    }
}
