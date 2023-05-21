package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.expensetracker.databinding.ActivityDashboardBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements OnItemsClick{
    ActivityDashboardBinding binding;
    private ExpenseAdapter expenseAdapter;
    //Intent intent ;
    private long income,expense=0;
    private long pressedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        expenseAdapter = new ExpenseAdapter(this,this);
        binding.recycler.setAdapter(expenseAdapter);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));

//        intent = new Intent(DashboardActivity.this,AddExpenseActivity.class);

        //for add button
        binding.add.setOnClickListener(new View.OnClickListener() {
            private int count =0;
            @Override
            public void onClick(View view) {
                count++;
                if(count%2==0){
                    findViewById(R.id.add_expense).setVisibility(View.GONE);
                    findViewById(R.id.add_income).setVisibility(View.GONE);
                }else {
                    findViewById(R.id.add_expense).setVisibility(view.VISIBLE);
                    findViewById(R.id.add_income).setVisibility(View.VISIBLE);

                }
            }
        });

        //when Add income is pressed
        binding.addIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this,AddExpenseActivity.class);
                intent.putExtra("type","Income");
                startActivity(intent);
            }
        });

        //when Add Expenses is pressed
        binding.addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this,AddExpenseActivity.class);
                intent.putExtra("type","Expense");
                startActivity(intent);
            }
        });
    }

    //for menu options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // menu.add("User");
        menu.add("Logout");
        return super.onCreateOptionsMenu(menu);
    }

    //for clicking on menu options
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals("Logout"));
            getLogout();
        return super.onOptionsItemSelected(item);
    }

    private void getLogout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(DashboardActivity.this,MainActivity.class);
        startActivity(intent);
    }

    //function for double press to exit
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onResume() {
        super.onResume();
        income=expense=0;
        getData();
    }

    private void getData() {
        FirebaseFirestore
                .getInstance()
                .collection("expenses")
                .whereEqualTo("uid",FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        expenseAdapter.clear();
                        List<DocumentSnapshot> dsList  =queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot ds:dsList){
                            ExpenseModel expenseModel=ds.toObject(ExpenseModel.class);
                            if(expenseModel.getType().equals("Income")){
                                income+=expenseModel.getAmount();
                            }else{
                                expense+=expenseModel.getAmount();
                            }
                            expenseAdapter.add(expenseModel);
                        }
                        setUpGraph();
                    }
                });
    }

    private void setUpGraph() {
        List<PieEntry> pieEntryList = new ArrayList<>();
        List<Integer> colorsList = new ArrayList<>();
        if(income!=0){
            pieEntryList.add(new PieEntry(income,"Income"));
            colorsList.add(getResources().getColor(R.color.teal_700));
        }
        if(expense!=0){
            pieEntryList.add(new PieEntry(expense,"Expense"));
            colorsList.add(getResources().getColor(R.color.red));
        }
        String graph_details;
        if(income>expense){
            graph_details= "| Balance: " +(income-expense);
        }
        else{
            graph_details="| Expense Exceeded: "+(income-expense);
        }
        //old one
       // PieDataSet pieDataSet = new PieDataSet(pieEntryList,String.valueOf(income-expense));

        //new trial
        PieDataSet pieDataSet = new PieDataSet(pieEntryList,graph_details);
        pieDataSet.setColors(colorsList);
        pieDataSet.setValueTextColor(getResources().getColor(R.color.white));
        pieDataSet.setValueTextSize(17);

        PieData pieData=new PieData(pieDataSet);
        binding.pieChart.setData(pieData);
        binding.pieChart.invalidate();
        binding.pieChart.getDescription().setEnabled(false);


    }

    @Override
    public void onClick(ExpenseModel expenseModel) {
        Intent intent = new Intent(DashboardActivity.this,AddExpenseActivity.class);
        intent.putExtra("model",expenseModel);
        startActivity(intent);
    }
}

