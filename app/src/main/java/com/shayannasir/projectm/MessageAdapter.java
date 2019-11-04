package com.shayannasir.projectm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Messages> userMessagesList;
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;

    public MessageAdapter (List<Messages> userMessagesList){

        this.userMessagesList = userMessagesList;
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView senderMessageText, receiverMessageText;
        public ImageView messageSenderImage, messageReceiverImage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMessageText = (TextView)itemView.findViewById(R.id.sender_message_text);
            receiverMessageText = (TextView)itemView.findViewById(R.id.receiver_message_text);
            messageSenderImage = (ImageView)itemView.findViewById(R.id.message_sender_image);
            messageReceiverImage = (ImageView)itemView.findViewById(R.id.message_receiver_image);

        }
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_messages_layout, parent, false);

        mAuth = FirebaseAuth.getInstance();

        return new MessageViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, final int position) {

        String messageSenderID = mAuth.getCurrentUser().getUid();
        Messages messages = userMessagesList.get(position);

        String fromUserID = messages.getFrom();
        String fromMessageType = messages.getType();

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserID);

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        holder.receiverMessageText.setVisibility(View.GONE);
        holder.senderMessageText.setVisibility(View.GONE);

        holder.messageSenderImage.setVisibility(View.GONE);
        holder.messageReceiverImage.setVisibility(View.GONE);

        if(fromMessageType.equals("text")){

            if(fromUserID.equals(messageSenderID)){
                holder.senderMessageText.setVisibility(View.VISIBLE);
                holder.senderMessageText.setBackgroundResource(R.drawable.sender_messages_layout);
                holder.senderMessageText.setText(messages.getMessage());    //+messages.getTime()

            } else {
                holder.receiverMessageText.setVisibility(View.VISIBLE);

                holder.receiverMessageText.setBackgroundResource(R.drawable.receiver_messages_layout);
                holder.receiverMessageText.setText(messages.getMessage());  //+messages.getTime()


            }

        } else if(fromMessageType.equals("image")){
            if(fromUserID.equals(messageSenderID)){

                holder.messageSenderImage.setVisibility(View.VISIBLE);

                Picasso.get().load(messages.getMessage()).into(holder.messageSenderImage);
            } else {

                holder.messageReceiverImage.setVisibility(View.VISIBLE);

                Picasso.get().load(messages.getMessage()).into(holder.messageReceiverImage);
            }
        } else if(fromMessageType.equals("pdf") || fromMessageType.equals("docx")){
            if(fromUserID.equals(messageSenderID)){

                holder.messageSenderImage.setVisibility(View.VISIBLE);

                Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/projectmillennial-fdf44.appspot.com/o/ImageFiles%2Ftext_document.png?alt=media&token=7b22dd49-ddc2-43a9-8f09-287f206e10cf").into(holder.messageSenderImage);


            } else {

                holder.messageReceiverImage.setVisibility(View.VISIBLE);

                Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/projectmillennial-fdf44.appspot.com/o/ImageFiles%2Ftext_document.png?alt=media&token=7b22dd49-ddc2-43a9-8f09-287f206e10cf")
                        .into(holder.messageReceiverImage);

            }
        }

        if(fromUserID.equals(messageSenderID)){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(userMessagesList.get(position).getType().equals("pdf") || userMessagesList.get(position).getType().equals("docx")){

                        CharSequence options[] = new CharSequence[]
                                {
                                        "Delete",
                                        "Download and View",
                                        "Cancel"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Select operation");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0){

                                    deleteMessagesForAll(position, holder);
                                    Intent intent = new Intent(holder.itemView.getContext(), MainActivity.class);
                                    holder.itemView.getContext().startActivity(intent);

                                } else if(which == 1){

                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                                    holder.itemView.getContext().startActivity(intent);

                                }
                            }
                        });
                        builder.show();
                    } else if (userMessagesList.get(position).getType().equals("text")){

                        CharSequence options[] = new CharSequence[]
                                {
                                        "Delete",
                                        "Cancel"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Select operation");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0){

                                    deleteMessagesForAll(position, holder);
                                    Intent intent = new Intent(holder.itemView.getContext(), MainActivity.class);
                                    holder.itemView.getContext().startActivity(intent);
                                }
                            }
                        });
                        builder.show();
                    } else if(userMessagesList.get(position).getType().equals("image")){

                        CharSequence options[] = new CharSequence[]
                                {
                                        "Delete",
                                        "View Image",
                                        "Cancel"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Select operation");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0){

                                    deleteMessagesForAll(position, holder);
                                    Intent intent = new Intent(holder.itemView.getContext(), MainActivity.class);
                                    holder.itemView.getContext().startActivity(intent);


                                } else if(which == 1) {

                                    Intent intent = new Intent(holder.itemView.getContext(), ImageViewerActivity.class);
                                    intent.putExtra("url", userMessagesList.get(position).getMessage());
                                    holder.itemView.getContext().startActivity(intent);

                                }
                            }
                        });
                        builder.show();
                    }
                }
            });
        } else {


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(userMessagesList.get(position).getType().equals("pdf") || userMessagesList.get(position).getType().equals("docx")){

                        CharSequence options[] = new CharSequence[]
                                {
                                        "Delete",
                                        "Download and View",
                                        "Cancel"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Select operation");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0){

                                    deleteMessagesForAll(position, holder);
                                    Intent intent = new Intent(holder.itemView.getContext(), MainActivity.class);
                                    holder.itemView.getContext().startActivity(intent);


                                } else if(which == 1){

                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                                    holder.itemView.getContext().startActivity(intent);

                                }
                            }
                        });
                        builder.show();
                    } else if (userMessagesList.get(position).getType().equals("text")){

                        CharSequence options[] = new CharSequence[]
                                {
                                        "Delete",
                                        "Cancel"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Select operation");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0){
                                    deleteMessagesForAll(position, holder);
                                    Intent intent = new Intent(holder.itemView.getContext(), MainActivity.class);
                                    holder.itemView.getContext().startActivity(intent);
                                }
                            }
                        });
                        builder.show();
                    } else if(userMessagesList.get(position).getType().equals("image")){

                        CharSequence options[] = new CharSequence[]
                                {
                                        "Delete",
                                        "View Image",
                                        "Cancel"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Select operation");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0){
                                    deleteMessagesForAll(position, holder);
                                    Intent intent = new Intent(holder.itemView.getContext(), MainActivity.class);
                                    holder.itemView.getContext().startActivity(intent);


                                } else if(which == 1) {
                                    Intent intent = new Intent(holder.itemView.getContext(), ImageViewerActivity.class);
                                    intent.putExtra("url", userMessagesList.get(position).getMessage());
                                    holder.itemView.getContext().startActivity(intent);
                                }
                            }
                        });
                        builder.show();
                    }
                }
            });



        }

    }

    @Override
    public int getItemCount() {

        return userMessagesList.size();
    }


    private void deleteMessagesForAll(final int position, final MessageViewHolder holder){

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("Messages")
                .child(userMessagesList.get(position).getTo())
                .child(userMessagesList.get(position).getFrom())
                .child(userMessagesList.get(position).getMessageID())
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            rootRef.child("Messages")
                                    .child(userMessagesList.get(position).getFrom())
                                    .child(userMessagesList.get(position).getTo())
                                    .child(userMessagesList.get(position).getMessageID())
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(holder.itemView.getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });

                        } else {
                            Toast.makeText(holder.itemView.getContext(), "Error while deleting", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



}
