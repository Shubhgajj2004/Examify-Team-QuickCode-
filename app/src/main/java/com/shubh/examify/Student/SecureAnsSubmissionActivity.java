package com.shubh.examify.Student;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shubh.examify.DashBoardActivity;
import com.shubh.examify.FirebaseVar.FirebaseVarClass;
import com.shubh.examify.R;

import java.util.ArrayList;

public class SecureAnsSubmissionActivity extends AppCompatActivity {

    //var declaration
    Button send;
    EditText size;
    LinearLayout list;
    ArrayList<String> text = new ArrayList<>();
    Context context;
    FirebaseDatabase mDatabase;
    String ExamID, teacherID, studentID;
    int noOfQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        setContentView(R.layout.activity_secure_ans_submission);

        //binding ID's with var
        size = findViewById(R.id.size2);
        send = findViewById(R.id.secureAnswerSubmitBtn);
        list = findViewById(R.id.answerLayout);
        context = getApplicationContext();

        //instances
        mDatabase = FirebaseDatabase.getInstance();

        //retrieving data from previous activity
        ExamID = getIntent().getStringExtra("KEY");

        //Get Student ID from local Storage
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        studentID = sharedPreferences.getString("studentID", "false");


        mDatabase.getReference().child(FirebaseVarClass.ALLEXAM).child(FirebaseVarClass.SECUREEXAM).child(ExamID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String NoOfQ = snapshot.child(FirebaseVarClass.TOTALQ).getValue(String.class);
                noOfQ = Integer.parseInt(NoOfQ);

                for (int i = 1; i <= noOfQ; i++) {
                    String Questions = snapshot.child("Q" + String.valueOf(i)).getValue(String.class);
                    teacherID = snapshot.child(FirebaseVarClass.TEACHERID).getValue(String.class);
                    addView(Questions, i);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //On send Button click
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToFirebase();
                Intent intent = new Intent(SecureAnsSubmissionActivity.this, DashBoardActivity.class);
                startActivity(intent);
            }
        });


    }


    //Add edittext as per teacher's need to make question
    private void addView(String Q, int No) {
        final View aa = getLayoutInflater().inflate(R.layout.item_secure_exam_time, null, false);

        TextView question = (TextView) aa.findViewById(R.id.secureQuestion);
        question.setText("Q" + No + ") " + Q);

        list.addView(aa);
    }


    //When student Minimise the app or press back button or
    //in-short close this page then it will automatically submit the answers
    @Override
    protected void onPause() {
        super.onPause();

        addToFirebase();
        Intent intent = new Intent(SecureAnsSubmissionActivity.this, DashBoardActivity.class);
        startActivity(intent);

    }


    //Upload Answers to Firebase
    public void addToFirebase() {

        int count = 1;
        for (int i = 0; i < list.getChildCount(); i++) {
            if (list.getChildAt(i) instanceof LinearLayoutCompat) {
                LinearLayoutCompat ll = (LinearLayoutCompat) list.getChildAt(i);
                for (int j = 0; j < ll.getChildCount(); j++) {
                    if (ll.getChildAt(j) instanceof EditText) {
                        EditText et = (EditText) ll.getChildAt(j);
                        if (et.getId() == R.id.size2) {

                            if (count == 1) {
                                //for student node
                                mDatabase.getReference().child(FirebaseVarClass.ALLEXAM)
                                        .child(FirebaseVarClass.SECUREEXAM).child(ExamID).child(FirebaseVarClass.SUBMISSIONS)
                                        .child(studentID).child("StudentDetails").setValue(et.getText().toString());

                                //For teacher node
                                mDatabase.getReference().child(FirebaseVarClass.TEACHERS).child(teacherID)
                                        .child(FirebaseVarClass.SECUREEXAM).child(ExamID).child(FirebaseVarClass.SUBMISSIONS)
                                        .child(studentID).child("StudentDetails").setValue(et.getText().toString());

                            } else if (count > 1 && count <= noOfQ + 1) {
                                //for student node
                                mDatabase.getReference().child(FirebaseVarClass.ALLEXAM)
                                        .child(FirebaseVarClass.SECUREEXAM).child(ExamID).child(FirebaseVarClass.SUBMISSIONS)
                                        .child(studentID).child("ANS" + String.valueOf(count - 1)).setValue(et.getText().toString());

                                //for teacher node
                                mDatabase.getReference().child(FirebaseVarClass.TEACHERS).child(teacherID)
                                        .child(FirebaseVarClass.SECUREEXAM).child(ExamID).child(FirebaseVarClass.SUBMISSIONS)
                                        .child(studentID).child("ANS" + String.valueOf(count - 1)).setValue(et.getText().toString());

                            }

                            count++;
                        }
                    }
                }
            }
        }
        Toast.makeText(SecureAnsSubmissionActivity.this, "Submitted", Toast.LENGTH_SHORT).show();


    }


}