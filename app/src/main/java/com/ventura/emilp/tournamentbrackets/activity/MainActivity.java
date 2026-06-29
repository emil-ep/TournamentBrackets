package com.ventura.emilp.tournamentbrackets.activity;

import android.graphics.Insets;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowInsets;
import android.view.WindowMetrics;

import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ventura.bracketslib.application.BracketsConfig;
import com.ventura.bracketslib.model.ColomnData;
import com.ventura.bracketslib.model.CompetitorData;
import com.ventura.bracketslib.model.MatchData;
import com.ventura.emilp.tournamentbrackets.databinding.ActivityMainBinding;
import com.ventura.emilp.tournamentbrackets.model.WorldCupGame;
import com.ventura.emilp.tournamentbrackets.model.WorldCupResponse;
import com.ventura.emilp.tournamentbrackets.network.RetrofitClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setScreenSize();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.pBar.setVisibility(android.view.View.VISIBLE);
        binding.bracketView.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchGames();
            }
        }, 1000);
    }

    private void fetchGames() {
        RetrofitClient.getApi().getGames().enqueue(new Callback<WorldCupResponse>() {
            @Override
            public void onResponse(Call<WorldCupResponse> call, Response<WorldCupResponse> response) {
                binding.pBar.setVisibility(android.view.View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<WorldCupGame> games = response.body().getGames();
                    Log.d(TAG, "Fetched " + (games != null ? games.size() : 0) + " games");
                    processGames(games);
                } else {
                    Log.e(TAG, "Failed to fetch games: " + response.code());
                    Toast.makeText(MainActivity.this, "Failed to fetch games", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WorldCupResponse> call, Throwable t) {
                binding.pBar.setVisibility(android.view.View.GONE);
                Log.e(TAG, "Error fetching games", t);
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processGames(List<WorldCupGame> games) {
        if (games == null) return;
        Toast.makeText(this, "Processing " + games.size() + " games", Toast.LENGTH_SHORT).show();
        List<ColomnData> colomnDataList = new ArrayList<>();

        colomnDataList.add(getColomnDataForType(games, "r32", Arrays.asList("74", "77", "73", "75", "83", "84", "81", "82", "76", "78", "79", "80", "86", "88", "85", "87")));
        colomnDataList.add(getColomnDataForType(games, "r16", Arrays.asList("89", "90", "93", "94", "91", "92", "95", "96")));
        colomnDataList.add(getColomnDataForType(games, "qf", Arrays.asList("97", "98", "99", "100")));
        colomnDataList.add(getColomnDataForType(games, "sf", Arrays.asList("101", "102")));
        colomnDataList.add(getColomnDataForType(games, "final", Arrays.asList("104")));

        Log.d(TAG, "Setting brackets data with " + colomnDataList.size() + " columns");
        for (int j = 0; j < colomnDataList.size(); j++) {
            Log.d(TAG, "Column " + j + " matches: " + colomnDataList.get(j).getMatches().size());
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.bracketView.setBracketsData(colomnDataList);
            }
        });
    }

    private ColomnData getColomnDataForType(List<WorldCupGame> games, String type, List<String> orderedIds) {
        List<MatchData> matches = new ArrayList<>();

        for (String id : orderedIds) {
            for (WorldCupGame game : games) {
                if (id.equals(game.getId())) {
                    CompetitorData home = new CompetitorData(game.getHomeTeam(), game.getHomeScore());
                    CompetitorData away = new CompetitorData(game.getAwayTeam(), game.getAwayScore());
                    matches.add(new MatchData(home, away));
                    break;
                }
            }
        }

        // Fallback if some games are missing or we want to show all games of that type
        if (matches.isEmpty()) {
            for (WorldCupGame game : games) {
                if (type.equals(game.getType())) {
                    CompetitorData home = new CompetitorData(game.getHomeTeam(), game.getHomeScore());
                    CompetitorData away = new CompetitorData(game.getAwayTeam(), game.getAwayScore());
                    matches.add(new MatchData(home, away));
                }
            }
        }

        return new ColomnData(matches);
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
