package com.shubh.examify.DashBoard;

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
import com.shubh.examify.Adapters.HistoryAdapter;
import com.shubh.examify.FirebaseVar.FirebaseVarClass;
import com.shubh.examify.Model.Model_Exam;
import com.shubh.examify.databinding.FragmentHistoryBinding;

import java.util.ArrayList;

//Using this history fragment, you can see all the exams that have been completed and the marks assigned to each student.
public class HistoryFragment extends Fragment {


    public HistoryFragment() {
        // Required empty public constructor
    }

    ArrayList<Model_Exam> list = new ArrayList<>();
    FirebaseDatabase database;
    HistoryAdapter adapter;


    FragmentHistoryBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater, container, false);


        database = FirebaseDatabase.getInstance();

        //Set the Recyclerview with adapter
        adapter = new HistoryAdapter(list, getContext());
        binding.historyRes.setAdapter(adapter);
        binding.historyRes.setLayoutManager(new LinearLayoutManager(getContext()));

        //Fetch the data from database to populate the recycler view via model
        database.getReference().child(FirebaseVarClass.ALLEXAM).child(FirebaseVarClass.EXAM).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                    String isActive = snapshot1.child(FirebaseVarClass.ISACTIVE).getValue(String.class);

                    //fetch id of student from local storage
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    String studentID = sharedPreferences.getString("studentID", "false");

                    String studentMarks = snapshot1.child(FirebaseVarClass.SUBMISSIONS).child(studentID).child(FirebaseVarClass.MARKS).getValue(String.class);

                    //check whether exam is over or not from database if over then and only then show.
                    if (isActive.equals("Over")) {
                        Model_Exam adp = snapshot1.getValue(Model_Exam.class);
                        adp.setMarks(studentMarks);
                        list.add(adp);
                    }

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