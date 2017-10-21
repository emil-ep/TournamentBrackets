package com.ventura.emilp.tournamentbrackets.activity;

import android.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.ventura.emilp.tournamentbrackets.Fragment.BracketsFragment;
import com.ventura.emilp.tournamentbrackets.R;
import com.ventura.emilp.tournamentbrackets.application.BracketsApplication;



public class MainActivity extends AppCompatActivity {


    private BracketsFragment bracketFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseBracketsFragment();
    }

    private void initialiseBracketsFragment() {

        bracketFragment = new BracketsFragment();
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, bracketFragment, "brackets_home_fragment");
        transaction.commit();
        manager.executePendingTransactions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setScreenSize();

    }

    private void setScreenSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        BracketsApplication.getInstance().setScreenHeight(height);
    }
}
