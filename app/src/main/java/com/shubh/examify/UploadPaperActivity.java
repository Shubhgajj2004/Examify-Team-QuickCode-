package com.shubh.examify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shubh.examify.FirebaseVar.FirebaseVarClass;
import com.shubh.examify.databinding.ActivityUploadPaperBinding;

public class UploadPaperActivity extends AppCompatActivity {

    ActivityUploadPaperBinding binding;
    Uri fileURI = null;
    ProgressDialog dialog;
    Uri file ;
    FirebaseDatabase mDatabase;
    FirebaseStorage mStorage;
    String examinarID , Key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadPaperBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        mDatabase = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance();


        examinarID = getIntent().getStringExtra("examinarID");
        Key = getIntent().getStringExtra("Key");




        binding.uplaodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Toast.makeText(UploadPaperActivity.this, fileURI.toString(), Toast.LENGTH_SHORT).show();








                if(fileURI != null) {
                    // Here we are initialising the progress dialog box
                    dialog = new ProgressDialog(UploadPaperActivity.this);
                    dialog.setMessage("Uploading");


                    // this will show message uploading
                    // while pdf is uploading
                    dialog.show();

                    final String timestamp = "" + System.currentTimeMillis();
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    final String messagePushID = timestamp;

                    Toast.makeText(getApplicationContext(), fileURI.toString(), Toast.LENGTH_SHORT).show();


                    //   Here we are uploading the pdf in firebase storage with the name of current time
                    final StorageReference filepath = storageReference.child(messagePushID + "." + "pdf");
                    Toast.makeText(getApplicationContext(), filepath.getName(), Toast.LENGTH_SHORT).show();
                    filepath.putFile(fileURI).continueWithTask(new Continuation() {
                        @Override
                        public Object then(@NonNull Task task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return filepath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                // After uploading is done it progress
                                // dialog box will be dismissed

                                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        file = uri;

                                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                        String studentID = sharedPreferences.getString("studentID" , "false");


                                        mDatabase.getReference().child(FirebaseVarClass.STUDENTS).child(studentID).child(FirebaseVarClass.STUDENTNAME).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String studentNAME = snapshot.getValue(String.class);
                                                DatabaseReference reference =  mDatabase.getReference().child(FirebaseVarClass.TEACHERS).child(examinarID).child(FirebaseVarClass.EXAM).child(Key).child(FirebaseVarClass.SUBMISSIONS).child(studentID);
                                                reference.child(FirebaseVarClass.MARKS).setValue("Not Reviewed");
                                                reference.child(FirebaseVarClass.STUDENTID).setValue(studentID);
                                                reference.child(FirebaseVarClass.STUDENTNAME).setValue(studentNAME);
                                                reference.child(FirebaseVarClass.ANS).setValue(file.toString());

                                                DatabaseReference reference2 =  mDatabase.getReference().child(FirebaseVarClass.ALLEXAM ).child(FirebaseVarClass.EXAM).child(Key).child(FirebaseVarClass.SUBMISSIONS).child(studentID);
                                                reference2.child(FirebaseVarClass.MARKS).setValue("Not Reviewed");
                                                reference2.child(FirebaseVarClass.STUDENTID).setValue(studentID);
                                                reference2.child(FirebaseVarClass.STUDENTNAME).setValue(studentNAME);
                                                reference2.child(FirebaseVarClass.ANS).setValue(file.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        dialog.dismiss();
                                                    }
                                                });



                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });





                                    }
                                });
                            }
                        }
                    });



                }





            }
        });



        // After Clicking on this we will be
        // redirected to choose pdf
        binding.chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fileIntent = new Intent();
                fileIntent.setAction(Intent.ACTION_GET_CONTENT);

                // We will be redirected to choose file
                fileIntent.setType("*/*");
                startActivityForResult(fileIntent, 1);



                }
        });
    }





    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {


            fileURI = data.getData();
            Toast.makeText(UploadPaperActivity.this, fileURI.toString(), Toast.LENGTH_SHORT).show();

        }
    }


}