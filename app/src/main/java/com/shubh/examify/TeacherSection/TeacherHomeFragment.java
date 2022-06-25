package com.shubh.examify.TeacherSection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shubh.examify.Adapters.TeacherAdapter;
import com.shubh.examify.FirebaseVar.FirebaseVarClass;
import com.shubh.examify.Model.Model_Exam;
import com.shubh.examify.databinding.FragmentTeacherHomeBinding;

import java.util.ArrayList;

//To show all previously conducted exam to the teacher
public class TeacherHomeFragment extends Fragment {

    public TeacherHomeFragment() {   //required empy constructor
    }

    //all global var, objects
    FragmentTeacherHomeBinding binding;
    ArrayList<Model_Exam> list = new ArrayList<>();
    ArrayList<Model_Exam> listKey = new ArrayList<>();
    FirebaseDatabase database;
    TeacherAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTeacherHomeBinding.inflate(inflater, container, false);


        //redirect to another activity for making
        binding.floatingAddButton.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ExamMakerActivity.class);
            startActivity(intent);
        });


        database = FirebaseDatabase.getInstance();

        //Set adapter with recyclerView
        adapter = new TeacherAdapter(list, listKey, getContext());
        binding.teacherRes.setAdapter(adapter);
        binding.teacherRes.setLayoutManager(new LinearLayoutManager(getContext()));


        //fetch id of Teacher from local storage
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String ID = sharedPreferences.getString("ID", "false");


        //Retrieve data from a database about previously held exams by particular faculty
        database.getReference().child(FirebaseVarClass.TEACHERS).child(ID).child(FirebaseVarClass.EXAM).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Model_Exam adp = snapshot1.getValue(Model_Exam.class);
                    list.add(adp);
                }

                //here key is access node for database hierarchy of particular exam
                listKey.clear();
                for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                    Model_Exam adp = new Model_Exam(snapshot2.getKey());
                    listKey.add(adp);
                }


                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}