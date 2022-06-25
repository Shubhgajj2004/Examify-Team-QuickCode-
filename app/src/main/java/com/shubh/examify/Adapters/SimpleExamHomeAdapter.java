package com.shubh.examify.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shubh.examify.DetailSimpleActivity;
import com.shubh.examify.Model.Model_Exam;
import com.shubh.examify.R;

import java.util.ArrayList;
import java.util.Locale;

public class SimpleExamHomeAdapter extends RecyclerView.Adapter<SimpleExamHomeAdapter.MyHolder> {


    ArrayList<Model_Exam> list;
    ArrayList<Model_Exam> listKey;
    Context context;

    public SimpleExamHomeAdapter(ArrayList<Model_Exam> list, ArrayList<Model_Exam> listKey, Context context) {
        this.list = list;
        this.listKey = listKey;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_home,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        Model_Exam adp= list.get(position);
        Model_Exam adp2= listKey.get(position);

        holder.subject.setText(adp.getSubject());
        holder.title.setText(adp.getTitle());
//        holder.time.setText(adp.getInHour()+":"+adp.getInMin()+" - "+adp.getFinHour()+":"+adp.getFinMin());
        holder.time.setText( String.format(Locale.getDefault() , "%02d:%02d - %02d:%02d" , adp.getInHour() , adp.getInMin() ,adp.getFinHour() , adp.getFinMin() ));

        holder.date.setText(adp.getDate().toString());
        Glide.with(context).load(adp.getImg()).into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context , DetailSimpleActivity.class);
                intent.putExtra("subject" , adp.getSubject());
                intent.putExtra("date" , adp.getDate());
                intent.putExtra("img" , adp.getImg());
                intent.putExtra("instructions" , adp.getInstructions());
                intent.putExtra("title" , adp.getTitle());
                intent.putExtra("name" , adp.getName());
                intent.putExtra("uri" , adp.getPDFurl());
                intent.putExtra("chapter" , adp.getChapter());
                intent.putExtra("key" , adp2.getKey());
                intent.putExtra("inHour" , adp.getInHour());
                intent.putExtra("inMinute" , adp.getInMin());
                intent.putExtra("finHour" , adp.getFinHour());
                intent.putExtra("finMinute" , adp.getInMin());
                intent.putExtra("examinarID" , adp.getExaminarID());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView subject , time , date , title;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_Res);
            subject = itemView.findViewById(R.id.subject_Res);
            time = itemView.findViewById(R.id.time_Res);
            date = itemView.findViewById(R.id.date_Res);
            title = itemView.findViewById(R.id.title_Res);


        }
    }


}
