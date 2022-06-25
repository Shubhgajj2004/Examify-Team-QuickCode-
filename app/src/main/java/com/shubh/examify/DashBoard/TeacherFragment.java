package com.shubh.examify.DashBoard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shubh.examify.DashBoardActivity;
import com.shubh.examify.FirebaseVar.FirebaseVarClass;
import com.shubh.examify.databinding.FragmentTeacherBinding;

//This is sign in fragment for teacher
public class TeacherFragment extends Fragment {


    public TeacherFragment() {
        // Required empty public constructor
    }

    FragmentTeacherBinding binding;
    FirebaseDatabase mDatabase;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTeacherBinding.inflate(inflater, container, false);

        mDatabase = FirebaseDatabase.getInstance();

        //Clicking on SignIn button
        binding.teacherSignInbtn.setOnClickListener(view -> {

            //if input fild is empty then throw an error
            if (binding.teacherId2.getText().toString().isEmpty()) {
                binding.teacherId2.setError("Empty");
            } else if (binding.teacherPassword2.getText().toString().isEmpty()) {
                binding.teacherPassword2.setError("Empty");
            } else {

                //fetch and check the inputted information with database
                mDatabase.getReference().child(FirebaseVarClass.TEACHERS).child(binding.teacherId2.getText().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String Password = snapshot.child(FirebaseVarClass.PASSWORD).getValue(String.class);
                        String Name = snapshot.child(FirebaseVarClass.NAME).getValue(String.class);
                        String Img = snapshot.child(FirebaseVarClass.IMG).getValue(String.class);

                        //If password is null that means inputted ID is wrong
                        if (Password != null) {
                            if (Password.equals(binding.teacherPassword2.getText().toString())) {
                                Toast.makeText(getContext(), "Successfully SingedIn ", Toast.LENGTH_SHORT).show();


                                //set the information like image, name of signedIn faculty in local storage
                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("isVerified", true);
                                editor.putString("Name", Name);
                                editor.putString("IMG", Img);
                                editor.putString("ID", binding.teacherId2.getText().toString());
                                editor.apply();
                                editor.commit();

                                Intent intent = new Intent(getContext(), DashBoardActivity.class);
                                startActivity(intent);


                            } else {
                                binding.teacherPassword2.setError("Password is wrong");
                            }
                        } else {
                            binding.teacherId2.setError("ID is wrong");


                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        // Inflate the layout for this fragment
        return binding.getRoot();
    }


}
