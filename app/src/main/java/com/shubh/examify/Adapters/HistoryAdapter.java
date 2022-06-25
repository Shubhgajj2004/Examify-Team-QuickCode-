package com.shubh.examify.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shubh.examify.Model.Model_Exam;
import com.shubh.examify.R;

import java.util.ArrayList;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyHolder> {

    ArrayList<Model_Exam> list;
    Context context;

    public HistoryAdapter(ArrayList<Model_Exam> list, Context context) {
        this.list = list;
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

        holder.subject.setText(adp.getSubject());
        holder.title.setText(adp.getTitle());
        holder.time.setText( String.format(Locale.getDefault() , "%02d:%02d - %02d:%02d" , adp.getInHour() , adp.getInMin() ,adp.getFinHour() , adp.getFinMin() ));
        holder.date.setText(adp.getDate().toString());
        holder.marks.setText(adp.getMarks());
        Glide.with(context).load(adp.getImg()).into(holder.img);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView subject , time , date , title , marks;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_Res);
            subject = itemView.findViewById(R.id.subject_Res);
            time = itemView.findViewById(R.id.time_Res);
            date = itemView.findViewById(R.id.date_Res);
            title = itemView.findViewById(R.id.title_Res);
            marks = itemView.findViewById(R.id.marksRes);


        }
    }
}
