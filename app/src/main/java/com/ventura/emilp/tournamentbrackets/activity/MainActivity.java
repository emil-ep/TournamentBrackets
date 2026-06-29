package com.ventura.emilp.tournamentbrackets.activity;

import android.graphics.Insets;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowInsets;
import android.view.WindowMetrics;

import androidx.appcompat.app.AppCompatActivity;

import com.ventura.bracketslib.application.BracketsConfig;
import com.ventura.bracketslib.model.ColomnData;
import com.ventura.bracketslib.model.CompetitorData;
import com.ventura.bracketslib.model.MatchData;
import com.ventura.emilp.tournamentbrackets.databinding.ActivityMainBinding;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupBrackets();
    }

    private void setupBrackets() {
        CompetitorData brazilSemiFinal = new CompetitorData("Brazil", "3");
        CompetitorData englandSemiFinal = new CompetitorData("England", "1");
        CompetitorData argentinaSemiFinal = new CompetitorData("Argentina", "3");
        CompetitorData russiaSemiFinal = new CompetitorData("Russia", "2");
        CompetitorData brazilFinal = new CompetitorData("Brazil", "4");
        CompetitorData argentinaFinal = new CompetitorData("Argentina", "2");

        MatchData match1SemiFinal = new MatchData(brazilSemiFinal, englandSemiFinal);
        MatchData match2SemiFinal = new MatchData(argentinaSemiFinal, russiaSemiFinal);
        MatchData match3Final = new MatchData(brazilFinal, argentinaFinal);

        ColomnData semiFinalColomn = new ColomnData(Arrays.asList(match1SemiFinal, match2SemiFinal));
        ColomnData finalColomn = new ColomnData(Arrays.asList(match3Final));

        binding.bracketView.setBracketsData(Arrays.asList(semiFinalColomn, finalColomn));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setScreenSize();
    }

    private void setScreenSize() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets()
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            int height = windowMetrics.getBounds().height() - insets.top - insets.bottom;
            BracketsConfig.getInstance().setScreenHeight(height);
        } else {
            // Support for devices below API 30 (minimum SDK is 24)
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            BracketsConfig.getInstance().setScreenHeight(height);
        }
    }
}
