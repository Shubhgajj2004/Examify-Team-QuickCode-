package com.shubh.examify.DashBoard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.adapters.ToolbarBindingAdapter;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shubh.examify.FirebaseVar.FirebaseVarClass;
import com.shubh.examify.R;
import com.shubh.examify.Student.SecureAnsSubmissionActivity;
import com.shubh.examify.databinding.FragmentSecureBinding;
import com.shubh.examify.databinding.FragmentSimpleExamBinding;

//This fragment is for student to Enter in the online exam by ExamID
public class SecureFragment extends Fragment {

    public SecureFragment() {

    }

    FragmentSecureBinding binding;
    FirebaseDatabase mDatabase;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSecureBinding.inflate(inflater, container, false);

        //Instances
        mDatabase = FirebaseDatabase.getInstance();

        //Get Student ID from local Storage
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String studentID = sharedPreferences.getString("studentID" , "false");


        binding.studentEnterInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ExamID = binding.ExamID2.getText().toString();

                mDatabase.getReference().child(FirebaseVarClass.ALLEXAM).child(FirebaseVarClass.SECUREEXAM).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(ExamID))
                        {
//                            Toast.makeText(getContext(), String.valueOf(snapshot.child(ExamID).getChildrenCount()), Toast.LENGTH_SHORT).show();
                         if(!snapshot.child(ExamID).child(FirebaseVarClass.SUBMISSIONS).hasChild(studentID))
                         {
                             Intent intent = new Intent(getContext() , SecureAnsSubmissionActivity.class);
                             intent.putExtra("KEY" , ExamID);
                             startActivity(intent);
                         }
                         else
                         {
                             Toast.makeText(getContext(), "You have already Submitted", Toast.LENGTH_SHORT).show();
                         }

                        }
                        else {
                            Toast.makeText(getContext(), "ID is wrong", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });




        return binding.getRoot();
    }
}