package com.shubh.examify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shubh.examify.databinding.ActivityMainBinding;


//Sign in Activity for students
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth mAuth;
    FirebaseUser mUSer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //instances
        mAuth = FirebaseAuth.getInstance();
        mUSer = mAuth.getCurrentUser();


        //On clicking sign in button check the details with database details, if matches then let to go in  dashboard activity
        binding.studentSignInbtn.setOnClickListener(v -> mAuth.signInWithEmailAndPassword(binding.studentId2.getText().toString(), binding.studentPassword2.getText().toString()).
                addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        //student id inputted from layout is gmail account hence first take the  preceding
                        //part as studentID .
                        String email = binding.studentId2.getText().toString();
                        String find = "@gmail.com";
                        int i = email.indexOf(find);
                        if (i > 0) {
                            String studentID = email.substring(0, i);
                            Toast.makeText(MainActivity.this, studentID, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, DashBoardActivity.class);

                            //store studentID into local storage
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("studentID", studentID);
                            editor.apply();
                            editor.commit();
                            startActivity(intent);

                        } else
                            System.out.println("string not found");


                    } else {
                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));

    }


    //every time open this app if user object is not null means student have logged in then will automatically let in DashBoard Activity
    @Override
    protected void onStart() {
        super.onStart();

        if (mUSer != null) {
            Intent intent = new Intent(MainActivity.this, DashBoardActivity.class);
            startActivity(intent);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
        }
    }
}