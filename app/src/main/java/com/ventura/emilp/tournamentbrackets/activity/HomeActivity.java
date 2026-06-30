package com.ventura.emilp.tournamentbrackets.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ventura.emilp.tournamentbrackets.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.cardGroupStandings.setOnClickListener(v ->
                startActivity(new Intent(this, GroupStandingsActivity.class)));

        binding.cardBrackets.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));
    }
}
