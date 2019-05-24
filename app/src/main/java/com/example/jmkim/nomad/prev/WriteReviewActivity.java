package com.example.jmkim.nomad.prev;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.jmkim.nomad.DB.Plan;
import com.example.jmkim.nomad.DB.Review;
import com.example.jmkim.nomad.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WriteReviewActivity extends AppCompatActivity {

    private RecyclerView rv_main;
    private RecyclerView.LayoutManager main_layoutManager;

    private Uri imageUri;

    private FirebaseUser user;
    private String uid;
    private int PICK_FROM_ALBUM = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        rv_main = findViewById(R.id.review_rv_main);
        rv_main.setHasFixedSize(true);
        main_layoutManager = new LinearLayoutManager(this);
        rv_main.setLayoutManager(main_layoutManager);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        String key = getIntent().getStringExtra("key");
        Log.e("KEY",key);

        ArrayList<ReviewMainInfo> reviewMainInfos = new ArrayList<>();

        List<Plan> plans = new ArrayList<>();

        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Plan")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        plans.clear();

                        plans.add(dataSnapshot.getValue(Plan.class));

                        for(int i=0;i<plans.get(0).hashtag.size();i++){
                            Log.e("SIZE", String.valueOf(plans.get(0).hashtag.size()));

                            Set set = plans.get(0).hashtag.keySet();
                            Iterator iterator = set.iterator();

                            reviewMainInfos.clear();
                            while (iterator.hasNext()){
                                String key = (String)iterator.next();
                                reviewMainInfos.add(new ReviewMainInfo(key));
                                Log.e("TAG",key);
                            }
                        }
                        ReviewAdapter reviewAdapter = new ReviewAdapter(getApplicationContext(), reviewMainInfos, WriteReviewActivity.this);
                        rv_main.setAdapter(reviewAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK) {
            imageUri = data.getData();

            final ReviewAdapter.pic picture = new ReviewAdapter.pic();
            picture.setImageUri(imageUri);
            picture.setImaOk(true);

            final ProgressDialog pd;
            pd = new ProgressDialog(WriteReviewActivity.this);
            pd.setMessage("잠시만 기다려주세요");
            pd.show();

            FirebaseStorage
                    .getInstance()
                    .getReference()
                    .child("review")
                    .child(uid)
                    .child(String.valueOf(picture.getPosition()))
                    .child(String.valueOf(picture.getIndex()))
                    .putFile(imageUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                        final StorageReference ref = FirebaseStorage
                                .getInstance()
                                .getReference()
                                .child("review")
                                .child(uid)
                                .child(String.valueOf(picture.getPosition()))
                                .child(String.valueOf(picture.getIndex()));

                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Task<Uri> uriTask = ref.getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            Uri downloadUri = uriTask.getResult();
                            String image = String.valueOf(downloadUri);

                            Review review = new Review();
                            review.uid = uid;
                            review.index = String.valueOf(picture.getIndex());
                            review.position = String.valueOf(picture.getPosition());
                            review.imageUri = image;

                            FirebaseDatabase
                                    .getInstance()
                                    .getReference()
                                    .child("Review")
                                    .child(uid)
                                    .child(String.valueOf(picture.getPosition()))
                                    .child(String.valueOf(picture.getIndex()))
                                    .setValue(review)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(WriteReviewActivity.this, "완료", Toast.LENGTH_SHORT).show();
                                            pd.dismiss();
                                        }
                                    });
                        }
                    });

        }
    }
}
