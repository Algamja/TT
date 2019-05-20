package com.example.jmkim.nomad.prev;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.example.jmkim.nomad.DB.Review;
import com.example.jmkim.nomad.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

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

        ArrayList<ReviewMainInfo> reviewMainInfos = new ArrayList<>();
        reviewMainInfos.add(new ReviewMainInfo("해시태그1"));
        reviewMainInfos.add(new ReviewMainInfo("해시태그2"));
        reviewMainInfos.add(new ReviewMainInfo("해시태그3"));

        ReviewAdapter reviewAdapter = new ReviewAdapter(getApplicationContext(), reviewMainInfos, WriteReviewActivity.this);
        rv_main.setAdapter(reviewAdapter);
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
