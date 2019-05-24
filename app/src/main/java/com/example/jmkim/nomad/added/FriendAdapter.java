package com.example.jmkim.nomad.added;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.jmkim.nomad.DB.UserModel;
import com.example.jmkim.nomad.R;
import com.example.jmkim.nomad.prev.WriterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private Context context;
    private List<UserModel> userModels;
    private RequestManager mGlide;

    public FriendAdapter(Context context, List<UserModel> userModels){
        this.context = context;
        this.userModels = userModels;
    }

    @NonNull
    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_row, parent, false);
        return new FriendAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final UserModel userModel = userModels.get(position);
        mGlide = Glide.with(context);

        holder.btn_follow.setVisibility(View.VISIBLE);
        holder.userName.setText(userModel.userName);
        mGlide.load(userModel.profileImageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.image_profile);

        isFollowing(userModel.uid, holder.btn_follow);

        if(userModel.uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            holder.btn_follow.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,WriterActivity.class);
                intent.putExtra("publisher",userModel.uid);
                context.startActivity(intent);
            }
        });

        holder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.btn_follow.getText().toString().equals("친구추가")){
                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("Follow")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("following")
                            .child(userModel.uid)
                            .setValue(true);

                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("Follow")
                            .child(userModel.uid)
                            .child("follower")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(true);
                }else if(holder.btn_follow.getText().toString().equals("친구삭제")){
                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("Follow")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("following")
                            .child(userModel.uid)
                            .removeValue();

                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("Follow")
                            .child(userModel.uid)
                            .child("follower")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .removeValue();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView userName;
        public ImageView image_profile;
        public Button btn_follow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = (TextView)itemView.findViewById(R.id.friend_row_userName);
            image_profile = (ImageView)itemView.findViewById(R.id.friend_row_profile);
            btn_follow = (Button)itemView.findViewById(R.id.friend_row_follow);
        }
    }

    private void isFollowing(final String uid, final Button button){
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Follow")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("following")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(uid).exists()){
                            button.setText("친구삭제");
                        }else{
                            button.setText("친구추가");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}