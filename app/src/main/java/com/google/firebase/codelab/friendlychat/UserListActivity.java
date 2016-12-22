package com.google.firebase.codelab.friendlychat;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListActivity extends AppCompatActivity {

    RecyclerView userListRecyclerView;
    private ProgressBar progressBar;
    private LinearLayoutManager mLinearLayoutManager;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    //Firebase database
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<FriendlyMessage, UserListViewHolder> mFirebaseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        userListRecyclerView = (RecyclerView) findViewById(R.id.userListRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        //mLinearLayoutManager.setStackFromEnd(true);
        userListRecyclerView.setLayoutManager(mLinearLayoutManager);


        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();


        mFirebaseAdapter = new FirebaseRecyclerAdapter<FriendlyMessage, UserListViewHolder>(
                FriendlyMessage.class,
                R.layout.item_message,
                UserListViewHolder.class,
                mFirebaseDatabaseReference.child("users")) {

            @Override
            protected void populateViewHolder(UserListViewHolder viewHolder, FriendlyMessage friendlyMessage,
                                              int position) {

                progressBar.setVisibility(ProgressBar.INVISIBLE);
                viewHolder.messageTextView.setText(friendlyMessage.getText());
                viewHolder.messengerTextView.setText(friendlyMessage.getName());
                if (friendlyMessage.getPhotoUrl() == null) {
                    viewHolder.messengerImageView
                            .setImageDrawable(ContextCompat
                                    .getDrawable(UserListActivity.this,
                                            R.drawable.ic_account_circle_black_36dp));
                } else {
                    Glide.with(UserListActivity.this)
                            .load(friendlyMessage.getPhotoUrl())
                            .into(viewHolder.messengerImageView);
                }

                viewHolder.itemRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                     // insert into firebase database



                     // move to chat window
                        startActivity(new Intent(UserListActivity.this, MainActivity.class));

                    }
                });



            }
        };




        // observer  for database changes
        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver(){

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();

                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
               /* if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    userListRecyclerView.scrollToPosition(positionStart);
                }*/

            }
        });
        userListRecyclerView.setLayoutManager(mLinearLayoutManager);
        userListRecyclerView.setAdapter(mFirebaseAdapter);



    }



    public static class UserListViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public TextView messengerTextView;
        public CircleImageView messengerImageView;
        public LinearLayout itemRow;

        public UserListViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
            messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
            itemRow = (LinearLayout) itemView.findViewById(R.id.itemRow);

        }
    }


}
