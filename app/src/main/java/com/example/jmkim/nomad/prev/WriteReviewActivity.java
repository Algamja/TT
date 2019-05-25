package com.example.jmkim.nomad.prev;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.jmkim.nomad.DB.Plan;
import com.example.jmkim.nomad.DB.Review;
import com.example.jmkim.nomad.R;
import com.example.jmkim.nomad.added.WriteReview;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WriteReviewActivity extends AppCompatActivity {

    private RecyclerView rv_main;
    private RecyclerView.LayoutManager main_layoutManager;

    private Button submit;

    private Uri imageUri;
    private FirebaseUser user;
    private String uid;
    private int PICK_FROM_ALBUM = 10;

    ArrayList<ReviewMainInfo> reviewMainInfos = new ArrayList<>();
    Review review = new Review();
    Map<String,Review.Review_Tag> review_tagMap = new HashMap<>();

    WriteReview parent = (WriteReview) WriteReview.activity_review;

    @Override
    public void onBackPressed() {
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Imsi")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .removeValue();

        finish();
        parent.finish(); 
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        rv_main = findViewById(R.id.review_rv_main);
        rv_main.setHasFixedSize(true);
        main_layoutManager = new LinearLayoutManager(this);
        rv_main.setLayoutManager(main_layoutManager);

        submit = findViewById(R.id.review_submit);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("Imsi")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                review_tagMap.clear();

                                for(DataSnapshot item : dataSnapshot.getChildren()) {
                                    review_tagMap.put(item.getKey(),item.getValue(Review.Review_Tag.class));
                                }
                                review.hashtag.putAll(review_tagMap);

                                FirebaseDatabase
                                        .getInstance()
                                        .getReference()
                                        .child("WriteReview")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .push()
                                        .setValue(review)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                finish();
                                                parent.finish();
                                                Toast.makeText(WriteReviewActivity.this, "리뷰작성이 완료되었습니다.", Toast.LENGTH_SHORT).show();

                                                FirebaseDatabase
                                                        .getInstance()
                                                        .getReference()
                                                        .child("Imsi")
                                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .removeValue();
                                            }
                                        });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        });

        String key = getIntent().getStringExtra("key");
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
                            Set set = plans.get(0).hashtag.keySet();
                            Iterator iterator = set.iterator();

                            reviewMainInfos.clear();
                            while (iterator.hasNext()){
                                String key = (String)iterator.next();
                                reviewMainInfos.add(new ReviewMainInfo(key));
                                review.period = plans.get(0).period;
                                review.publisher = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                review.city = plans.get(0).country;
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
                    .child(reviewMainInfos.get(picture.getPosition()).hashtag)
                    .putFile(imageUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                        final StorageReference ref = FirebaseStorage
                                .getInstance()
                                .getReference()
                                .child("review")
                                .child(uid)
                                .child(reviewMainInfos.get(picture.getPosition()).hashtag);

                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String image = String.valueOf(uri);

                                    Review.Review_Tag review_tag = new Review.Review_Tag();
                                    review_tag.uid = uid;
                                    review_tag.index = String.valueOf(picture.getIndex());
                                    review_tag.position = String.valueOf(picture.getPosition());
                                    review_tag.imageUri = image;

                                    FirebaseDatabase
                                            .getInstance()
                                            .getReference()
                                            .child("Imsi")
                                            .child(uid)
                                            .child(reviewMainInfos.get(picture.getPosition()).hashtag)
                                            .setValue(review_tag)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    pd.dismiss();
                                                }
                                            });
                                }
                            });
                        }
                    });
        }
    }
}
