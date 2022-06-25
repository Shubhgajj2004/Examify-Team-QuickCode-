package com.shubh.examify.DashBoard;

import android.os.Bundle;
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
import com.shubh.examify.Adapters.SimpleExamHomeAdapter;
import com.shubh.examify.FirebaseVar.FirebaseVarClass;
import com.shubh.examify.Model.Model_Exam;
import com.shubh.examify.databinding.FragmentHomeBinding;

import java.util.ArrayList;

//This fragment Mainly show all upcoming and ongoing exams to the student
public class Home_Fragment extends Fragment {

    public Home_Fragment() {
        // Required empty public constructor
    }

    FragmentHomeBinding binding;
    ArrayList<Model_Exam> list = new ArrayList<>();
    ArrayList<Model_Exam> listKey = new ArrayList<>();
    FirebaseDatabase database;
    SimpleExamHomeAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        database = FirebaseDatabase.getInstance();

        //Set the Recyclerview with adapter
        adapter = new SimpleExamHomeAdapter(list, listKey, getContext());
        binding.SimpleRes.setAdapter(adapter);
        binding.SimpleRes.setLayoutManager(new LinearLayoutManager(getContext()));

        //Fetch the data from database to populate the recycler view via model
        database.getReference().child(FirebaseVarClass.ALLEXAM).child(FirebaseVarClass.EXAM).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Model_Exam adp = snapshot1.getValue(Model_Exam.class);
                    list.add(adp);
                }

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


        return binding.getRoot();
    }
}