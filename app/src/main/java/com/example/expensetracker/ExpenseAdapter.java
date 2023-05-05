package com.example.expensetracker;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.MyViewHolder> {
    private Context context;
    private OnItemsClick onItemsClick;
    private List<ExpenseModel> expenseModelList;

    public ExpenseAdapter(Context context,OnItemsClick onItemsClick){
        this.context=context;
        expenseModelList = new ArrayList<>();
        this.onItemsClick=onItemsClick;
    }
    public void add(ExpenseModel expenseModel){
        expenseModelList.add(expenseModel);
        notifyDataSetChanged();
    }
    public void clear(){
        expenseModelList.clear();
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ExpenseModel expenseModel=expenseModelList.get(position);
        //for time
        holder.date.setText(String.valueOf(getTimeDate(expenseModel.getTime())));

        holder.note.setText(expenseModel.getNote());
        holder.category.setText(expenseModel.getCategory());
        holder.amount.setText(String.valueOf(expenseModel.getAmount()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemsClick.onClick(expenseModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenseModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView note,category,amount,date;

        public MyViewHolder(@NotNull View itemView){
            super(itemView);
            date=itemView.findViewById(R.id.date);
            note=itemView.findViewById(R.id.note);
            category=itemView.findViewById(R.id.category);
            amount=itemView.findViewById(R.id.amount);

        }
    }

    //for time
    public static String getTimeDate(long timestamp){
        try{
            Date date = (new Date(timestamp));
            //previous time format
           // SimpleDateFormat sdf= new SimpleDateFormat("EEE, MMM d, ''yy", Locale.getDefault());

            //new trial
            SimpleDateFormat sdf= new SimpleDateFormat("EEE, MMM d, yyyy HH:mm", Locale.getDefault());
            return sdf.format(date);
        }catch(Exception e){
            return "date";
        }
    }
}
