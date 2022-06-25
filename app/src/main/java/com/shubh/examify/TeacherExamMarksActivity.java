package com.shubh.examify;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shubh.examify.Adapters.TeacherDetailExamAdapter;
import com.shubh.examify.FirebaseVar.FirebaseVarClass;
import com.shubh.examify.Model.Model_Teacher_Exam;
import com.shubh.examify.databinding.ActivityTeacherExamMarksBinding;

import java.util.ArrayList;


//Activity for record of the students for every exam
public class TeacherExamMarksActivity extends AppCompatActivity {

    //Global
    ActivityTeacherExamMarksBinding binding;
    ArrayList<Model_Teacher_Exam> list = new ArrayList<>();
    FirebaseDatabase database;
    TeacherDetailExamAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeacherExamMarksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //instances
        database = FirebaseDatabase.getInstance();


        adapter = new TeacherDetailExamAdapter(list, getApplicationContext());
        binding.teacherExamRes.setAdapter(adapter);
        binding.teacherExamRes.setLayoutManager(new GridLayoutManager(TeacherExamMarksActivity.this, 3));

        //fetch teacher ID from local storage
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String ID = sharedPreferences.getString("ID", "false");


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("KEY", getIntent().getStringExtra("Key"));
        editor.apply();
        editor.commit();


        //fetch data from database and feed to the recyclerview
        database.getReference().child(FirebaseVarClass.TEACHERS).child(ID).child(FirebaseVarClass.EXAM).child(getIntent().getStringExtra("Key")).child(FirebaseVarClass.SUBMISSIONS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Model_Teacher_Exam adp = snapshot1.getValue(Model_Teacher_Exam.class);
                    list.add(adp);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}