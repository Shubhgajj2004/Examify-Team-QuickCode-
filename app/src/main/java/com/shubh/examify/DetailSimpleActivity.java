package com.shubh.examify;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import com.shubh.examify.FirebaseVar.FirebaseVarClass;
import com.shubh.examify.databinding.ActivityDetailSimpleBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


//This is Main Activity for students to see all details about Exam.


public class DetailSimpleActivity extends AppCompatActivity {

    ActivityDetailSimpleBinding binding;
    DownloadManager manager;
    FirebaseDatabase mDatabase;
    String Key, date, startTime, endTime, examinarID;
    int inHour, inMinute, finHour, finMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailSimpleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        mDatabase = FirebaseDatabase.getInstance();

        //Getting data from previous Activity
        inHour = getIntent().getIntExtra("inHour", 0);
        finHour = getIntent().getIntExtra("finHour", 0);
        inMinute = getIntent().getIntExtra("inMinute", 0);
        finMinute = getIntent().getIntExtra("finMinute", 0);

        date = getIntent().getStringExtra("date");
        Key = getIntent().getStringExtra("key");
        examinarID = getIntent().getStringExtra("examinarID");


        //converting raw data time data into proper format
        startTime = String.format(Locale.getDefault(), "%02d:%02d", inHour, inMinute);
        endTime = String.format(Locale.getDefault(), "%02d:%02d", finHour, finMinute);

        //setting up data into layout
        Glide.with(getApplicationContext()).load(getIntent().getStringExtra("img")).into(binding.imgDetail);
        binding.subjectDetail.setText(getIntent().getStringExtra("subject"));
        binding.dateDetail.setText(getIntent().getStringExtra("date"));
        binding.titleDetail.setText(getIntent().getStringExtra("title"));
        binding.timeDetail.setText(String.format(Locale.getDefault(), "%02d:%02d - %02d:%02d", inHour, inMinute, finHour, finMinute));
        binding.nameDetail.setText(getIntent().getStringExtra("name"));
        binding.chapterDetail2.setText(getIntent().getStringExtra("chapter"));
        binding.instructionsDetail2.setText(getIntent().getStringExtra("instructions"));


        //To download the Exam paper
        binding.downloadView.setOnClickListener(view -> {

            if (cheker()) {
                manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(getIntent().getStringExtra("uri"));
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                long reference = manager.enqueue(request);
            }


        });


        //Check whether the time is over or still have to start
        //accordingly will help to upload the file

        binding.uploadBtnSimpleExam.setOnClickListener(view -> {

            if (cheker()) {
                mDatabase.getReference().child(FirebaseVarClass.ALLEXAM).child(FirebaseVarClass.EXAM).child(Key).child(FirebaseVarClass.ISACTIVE).setValue("Over");
                mDatabase.getReference().child(FirebaseVarClass.TEACHERS).child(examinarID).child(FirebaseVarClass.EXAM).child(Key).child(FirebaseVarClass.ISACTIVE).setValue("Over");

                //Will redirect to file upload page .
                Intent intent = new Intent(DetailSimpleActivity.this, UploadPaperActivity.class);
                intent.putExtra("examinarID", examinarID);
                intent.putExtra("Key", Key);
                startActivity(intent);
            }


        });

    }


    //Function check whether Exam is started or not
    public boolean cheker() {


        //Used to convert like June 03, 2022
        Date date3 = new Date();
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("MMM dd, yyy", Locale.UK);
        String str = formatter.format(date3);

        //Will convert full time into hour:minute format to check conditions
        SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm", Locale.UK);
        String currentTime = formatter1.format(date3);


        if (str.equals(date)) {

            //if current time is before exam staring time
            if (checktimings(currentTime, startTime)) {
                Toast.makeText(DetailSimpleActivity.this, "Exam have to start", Toast.LENGTH_SHORT).show();
                return false;
            }

            //if current time is after current time
            else if (checktimings(endTime, currentTime)) {
                Toast.makeText(DetailSimpleActivity.this, "Exam is over", Toast.LENGTH_SHORT).show();
                return false;
            }

            //if student is in betweeen exam starting time and ending time
            else {
                return true;
            }


        } else {
            Toast.makeText(DetailSimpleActivity.this, "Either time is over or Still have to start.", Toast.LENGTH_SHORT).show();
            return false;
        }


    }


    private boolean checktimings(String time, String endtime) {

        String pattern = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.UK);

        try {
            Date date1 = sdf.parse(time);
            Date date2 = sdf.parse(endtime);

            return date1.before(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}