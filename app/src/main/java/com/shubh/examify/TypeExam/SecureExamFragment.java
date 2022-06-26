package com.shubh.examify.TypeExam;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shubh.examify.FirebaseVar.FirebaseVarClass;
import com.shubh.examify.R;

import java.util.ArrayList;


public class SecureExamFragment extends Fragment {

    public SecureExamFragment() {
        // Required empty public constructor
    }

    //var declaration
    Button add, send ;

    EditText size ,examID , title;
    LinearLayout list;
    ArrayList<String> text = new ArrayList<>();
    Context context;
    FirebaseDatabase mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_secure_exam, container, false);
        add = view.findViewById(R.id.addQuestionBtn);
        size = view.findViewById(R.id.size);
        send = view.findViewById(R.id.send);
        list = view.findViewById(R.id.lLayout);
        examID = view.findViewById(R.id.EID2);
        title = view.findViewById(R.id.secureExamName2);
        context = getContext();

        //instances
        mDatabase = FirebaseDatabase.getInstance();

        //Local Storage where teacher's id is stored
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String teacherID = sharedPreferences.getString("ID", "false");



        //This button find the id of all edittext and write Questions on database
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = 1;
                String nodeID = "false";
                for (int i = 0; i < list.getChildCount(); i++) {
                    if (list.getChildAt(i) instanceof LinearLayoutCompat) {
                        LinearLayoutCompat ll = (LinearLayoutCompat) list.getChildAt(i);
                        for (int j = 0; j < ll.getChildCount(); j++) {

                            if (ll.getChildAt(j) instanceof EditText) {
                                EditText et = (EditText) ll.getChildAt(j);
                                if (et.getId() == R.id.size) {

                                    if (count == 1) {
                                        DatabaseReference reference = mDatabase.getReference().child(FirebaseVarClass.ALLEXAM).child(FirebaseVarClass.SECUREEXAM).child(examID.getText().toString());
                                        nodeID = reference.getKey();
                                        reference.child("Q" + String.valueOf(count)).setValue(et.getText().toString());
                                        reference.child(FirebaseVarClass.TEACHERID).setValue(teacherID);
                                        reference.child("TotalQ").setValue(String.valueOf(list.getChildCount()));
                                        reference.child("Title").setValue(title.getText().toString());

                                        //upload copy on teacher node in firebase
                                        DatabaseReference ref = mDatabase.getReference().child(FirebaseVarClass.TEACHERS).child(teacherID).child(FirebaseVarClass.SECUREEXAM).child(nodeID);
                                        ref.child("Q" + String.valueOf(count)).setValue(et.getText().toString());

//                                        //Exam hidden ID upload to the firebase database
//                                        DatabaseReference reference3 = mDatabase.getReference().child(FirebaseVarClass.ALLEXAM).child(FirebaseVarClass.SECUREEXAM).child(nodeID);
//                                        reference3.child(FirebaseVarClass.EXAMID).setValue(examID.getText().toString());
//
//                                        DatabaseReference ref3 = mDatabase.getReference().child(FirebaseVarClass.TEACHERS).child(teacherID).child(FirebaseVarClass.SECUREEXAM).child(nodeID);
//                                        ref3.child(FirebaseVarClass.EXAMID).setValue(examID.getText().toString());

                                    } else {
                                        DatabaseReference reference2 = mDatabase.getReference().child(FirebaseVarClass.ALLEXAM).child(FirebaseVarClass.SECUREEXAM).child(nodeID);
                                        reference2.child("Q" + String.valueOf(count)).setValue(et.getText().toString());

                                        //upload copy on teacher node in firebase
                                        DatabaseReference ref2 = mDatabase.getReference().child(FirebaseVarClass.TEACHERS).child(teacherID).child(FirebaseVarClass.SECUREEXAM).child(nodeID);
                                        ref2.child("Q" + String.valueOf(count)).setValue(et.getText().toString());
                                        ref2.child("TotalQ").setValue(String.valueOf(list.getChildCount()));
                                        ref2.child("Title").setValue(title.getText().toString());
                                    }

                                    count++;
                                }
                            }
                        }
                    }
                }
                Toast.makeText(getContext(), "Submitted", Toast.LENGTH_SHORT).show();

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addView();
            }
        });


        return view;
    }


    //Add edittext as per teacher's need to make question
    private void addView() {
        final View aa = getLayoutInflater().inflate(R.layout.add_edittext, null, false);
        ImageView cross = (ImageView) aa.findViewById(R.id.cross2);
        cross.setVisibility(View.VISIBLE);

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeView(aa);
            }
        });
        list.addView(aa);
    }

    //remove edittext if teacher click on cross button on edittext

    private void removeView(View view) {
        list.removeView(view);
    }
}