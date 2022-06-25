package com.shubh.examify.TypeExam;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shubh.examify.DashBoardActivity;
import com.shubh.examify.databinding.FragmentSimpleExamBinding;

import java.util.Locale;

//this fragment is to add exam detail for teacher
public class SimpleExamFragment extends Fragment {


    public SimpleExamFragment() {
        // Required empty public constructor
    }


    //Var declaration

    FragmentSimpleExamBinding binding;
    private int inMin, inHour, finMin, finHour, date = 0;
    private String Date;
    FirebaseDatabase mDatabase;
    FirebaseStorage mStorage;
    Uri imageuri = null;
    Uri pdf;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSimpleExamBinding.inflate(inflater, container, false);

        //Instances
        mDatabase = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance();


        //Exam starting time picker
        binding.inTime.setOnClickListener(view -> {
            TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {

                inHour = selectedHour;
                inMin = selectedMinute;
                binding.inTime.setText(String.format(Locale.getDefault(), "%02d:%02d", inHour, inMin));

            };
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), onTimeSetListener, inHour, inMin, false);
            timePickerDialog.setTitle("Pick stating time of exam");
            timePickerDialog.show();
        });


        //Exam ending time picker
        binding.finTime.setOnClickListener(view -> {
            TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {

                finHour = selectedHour;
                finMin = selectedMinute;
                binding.finTime.setText(String.format(Locale.getDefault(), "%02d:%02d", finHour, finMin));

            };
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), onTimeSetListener, finHour, finMin, false);
            timePickerDialog.setTitle("Pick Final time of exam");
            timePickerDialog.show();
        });


        //Date picker for exam
        binding.dateSimpleExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialoue();
            }

            private void showDatePickerDialoue() {

                MaterialDatePicker datePicker = MaterialDatePicker.Builder
                        .datePicker()
                        .setTitleText("Select Date")
                        .build();

                datePicker.addOnPositiveButtonClickListener(selection -> {
                    date = 1;
                    Date = datePicker.getHeaderText();
                    binding.dateSimpleExam.setText(datePicker.getHeaderText());
                });

                datePicker.show(getChildFragmentManager(), "Tag");

            }
        });


        //On Submit button click
        binding.SimpleSubmitBtn.setOnClickListener(view -> {

            //All if else check whether data is inputted or empty by teacher
            if (binding.subject2.getText().toString().isEmpty()) {
                binding.subject2.setError("Empty");
            } else if (binding.title2.getText().toString().isEmpty()) {
                binding.title2.setError("Empty");
            } else if (binding.chapter2.getText().toString().isEmpty()) {
                binding.chapter2.setError("Empty");
            } else if (binding.maxSimpleMarks2.getText().toString().isEmpty()) {
                binding.maxSimpleMarks2.setError("Empty");

            } else if (inHour == 0 && inMin == 0) {
                Toast.makeText(getContext(), "Pick Time and Date", Toast.LENGTH_SHORT).show();
            } else if (finHour == 0 && finMin == 0) {
                Toast.makeText(getContext(), "Pick Time and Date", Toast.LENGTH_SHORT).show();

            } else if (binding.instructionsSimpleExam2.getText().toString().isEmpty()) {
                binding.instructionsSimpleExam2.setError("Empty");
            }

            //If all data is properly inputted then
            else {
                if (imageuri != null) {
                    // Here we are initialising the progress dialog box
                    dialog = new ProgressDialog(getContext());
                    dialog.setMessage("Uploading");


                    // this will show message uploading
                    // while pdf is uploading
                    dialog.show();

                    final String timestamp = "" + System.currentTimeMillis();
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    final String messagePushID = timestamp;


                    //   Here we are uploading the pdf in firebase storage with the name of current time
                    final StorageReference filepath = storageReference.child(messagePushID + "." + "pdf");
                    Toast.makeText(getContext(), filepath.getName(), Toast.LENGTH_SHORT).show();
                    filepath.putFile(imageuri).continueWithTask((Continuation) task -> {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return filepath.getDownloadUrl();
                    }).addOnCompleteListener((OnCompleteListener<Uri>) task -> {
                        if (task.isSuccessful()) {

                            filepath.getDownloadUrl().addOnSuccessListener(uri -> {
                                pdf = uri;


                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                                String ID = sharedPreferences.getString("ID", "false");
                                String Name = sharedPreferences.getString("Name", "false");
                                String Img = sharedPreferences.getString("IMG", "false");


                                DatabaseReference databaseReference = mDatabase.getReference().child("Teachers").child(ID).child("Exams").push();
                                databaseReference.child("inHour").setValue(inHour);
                                databaseReference.child("inMin").setValue(inMin);
                                databaseReference.child("finHour").setValue(finHour);
                                databaseReference.child("finMin").setValue(finMin);
                                databaseReference.child("Name").setValue(Name);
                                databaseReference.child("PDFurl").setValue(pdf.toString());
                                databaseReference.child("Chapter").setValue(binding.chapter2.getText().toString());
                                databaseReference.child("img").setValue(Img);
                                databaseReference.child("Subject").setValue(binding.subject2.getText().toString());
                                databaseReference.child("Title").setValue(binding.title2.getText().toString());
                                databaseReference.child("Date").setValue(Date);
                                databaseReference.child("isActive").setValue("Active");
                                databaseReference.child("examinarID").setValue(ID);
                                databaseReference.child("MaxMarks").setValue(binding.maxSimpleMarks2.getText().toString());
                                databaseReference.child("Instructions").setValue(binding.instructionsSimpleExam2.getText().toString());

                                DatabaseReference databaseReference2 = mDatabase.getReference().child("AllExam").child("Exams").child(databaseReference.getKey());
                                databaseReference2.child("Name").setValue(Name);
                                databaseReference2.child("img").setValue(Img);
                                databaseReference2.child("Chapter").setValue(binding.chapter2.getText().toString());
                                databaseReference2.child("PDFurl").setValue(pdf.toString());
                                databaseReference.child("isActive").setValue("Active");
                                databaseReference2.child("inHour").setValue(inHour);
                                databaseReference2.child("inMin").setValue(inMin);
                                databaseReference2.child("finHour").setValue(finHour);
                                databaseReference2.child("finMin").setValue(finMin);
                                databaseReference2.child("Subject").setValue(binding.subject2.getText().toString());
                                databaseReference2.child("Title").setValue(binding.title2.getText().toString());
                                databaseReference2.child("Date").setValue(Date);
                                databaseReference2.child("Instructions").setValue(binding.instructionsSimpleExam2.getText().toString());
                                databaseReference2.child("MaxMarks").setValue(binding.maxSimpleMarks2.getText().toString());
                                databaseReference2.child("isActive").setValue("Active");
                                databaseReference2.child("examinarID").setValue(ID);


                            });


                        } else {
                            // After uploading is done it progress
                            // dialog box will be dismissed
                            dialog.dismiss();
                            Toast.makeText(getContext(), "UploadedFailed", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(o -> {

                        Intent intent = new Intent(getContext(), DashBoardActivity.class);
                        startActivity(intent);
                        dialog.dismiss();

                    });


                } else {
                    Toast.makeText(getContext(), "Upload Exam paper PDF", Toast.LENGTH_SHORT).show();
                }

            }


        });


        // After Clicking on this we will be
        // redirected to choose pdf
        binding.AddPdfBtn.setOnClickListener(v -> {
            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

            // We will be redirected to choose pdf
            galleryIntent.setType("application/pdf");
            startActivityForResult(galleryIntent, 1);
        });


        return binding.getRoot();

//        MaterialTimePicker picker  = new MaterialTimePicker.Builder().set();

    }


    ProgressDialog dialog;

    @SuppressWarnings("deprecation")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {


            imageuri = data.getData();
            binding.AddPdfBtn.setText("Added Succesfully");

        }
    }
}


