package com.shubh.examify.TeacherSection;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shubh.examify.FirebaseVar.FirebaseVarClass;
import com.shubh.examify.databinding.ActivityMarksDetailBinding;


//This is Main Activity for teacher to give marks to every individuals, to evaluate them


public class MarksDetailActivity extends AppCompatActivity {

    ActivityMarksDetailBinding binding;
    FirebaseDatabase mDatabase;
    DownloadManager manager;
    String name, id, marks, key, feedback, Ans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMarksDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        mDatabase = FirebaseDatabase.getInstance();


        //Getting the data from previous Activity
        id = getIntent().getStringExtra("Id");
        name = getIntent().getStringExtra("Name");
        marks = getIntent().getStringExtra("Marks");
        key = getIntent().getStringExtra("Key");
        Ans = getIntent().getStringExtra("Ans");


        feedback = binding.giveFeedback2.getText().toString();


        //setting up data into layout
        binding.nameMarksTime.setText(name);
        binding.idMarksTime.setText(id);
        binding.MarksTime.setText(marks);


        //To setting up the student image from database to layout
        mDatabase.getReference().child(FirebaseVarClass.STUDENTS).child(id).child(FirebaseVarClass.IMG).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String img = snapshot.getValue(String.class);
                Glide.with(getApplicationContext()).load(img).into(binding.imgMarksTime);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //fetch data from local storage
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String ID = sharedPreferences.getString("ID", "false");
        String Key = sharedPreferences.getString("KEY", "false");


        binding.sendBtnMarksTime.setOnClickListener(view -> {

            if (binding.giveMarks2.getText().toString().isEmpty()) {
                binding.giveMarks2.setError("Empty");
            } else if (binding.giveFeedback2.getText().toString().isEmpty()) {
                binding.giveFeedback2.setError("Empty");
            } else {
                //setting up the given detail by teacher to database
                DatabaseReference reference = mDatabase.getReference().child(FirebaseVarClass.TEACHERS).child(ID).child(FirebaseVarClass.EXAM).child(Key);
                reference.child(FirebaseVarClass.MAXMARKS).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String maxMarks = snapshot.getValue(String.class);
                        String marks = binding.giveMarks2.getText().toString();
                        reference.child(FirebaseVarClass.SUBMISSIONS).child(id).child(FirebaseVarClass.MARKS).setValue(marks + " / " + maxMarks);
                        reference.child(FirebaseVarClass.SUBMISSIONS).child(id).child(FirebaseVarClass.FEEDBACK).setValue(binding.giveFeedback2.getText().toString()).addOnSuccessListener(unused -> Toast.makeText(MarksDetailActivity.this, "Sent", Toast.LENGTH_SHORT).show());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }


        });


        //clicking on, download the solution of individual student
        binding.downloadView2.setOnClickListener(view -> {

            manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(Ans);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            long reference = manager.enqueue(request);

        });


    }
}