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
import com.ventura.emilp.tournamentbrackets.model.Team;
import com.ventura.emilp.tournamentbrackets.model.TeamsResponse;
import com.ventura.emilp.tournamentbrackets.model.WorldCupGame;
import com.ventura.emilp.tournamentbrackets.model.WorldCupResponse;
import com.ventura.emilp.tournamentbrackets.network.RetrofitClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";

    private final AtomicReference<List<WorldCupGame>> gamesRef = new AtomicReference<>();
    private final AtomicReference<Map<String, Team>> teamsMapRef = new AtomicReference<>();
    private final AtomicInteger pendingCalls = new AtomicInteger(2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setScreenSize();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Knockout Brackets");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.pBar.setVisibility(android.view.View.VISIBLE);
        binding.bracketView.setVisibility(android.view.View.GONE);
        fetchGames();
        fetchTeams();
    }

    private void fetchGames() {
        RetrofitClient.getApi().getGames().enqueue(new Callback<WorldCupResponse>() {
            @Override
            public void onResponse(Call<WorldCupResponse> call, Response<WorldCupResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    gamesRef.set(response.body().getGames());
                    Log.d(TAG, "Fetched " + response.body().getGames().size() + " games");
                } else {
                    Log.e(TAG, "Failed to fetch games: " + response.code());
                }
                checkBothReady();
            }

            @Override
            public void onFailure(Call<WorldCupResponse> call, Throwable t) {
                Log.e(TAG, "Error fetching games", t);
                checkBothReady();
            }
        });
    }

    private void fetchTeams() {
        RetrofitClient.getApi().getTeams().enqueue(new Callback<TeamsResponse>() {
            @Override
            public void onResponse(Call<TeamsResponse> call, Response<TeamsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Team> map = new HashMap<>();
                    for (Team t : response.body().getTeams()) {
                        map.put(t.getId(), t);
                    }
                    teamsMapRef.set(map);
                    Log.d(TAG, "Fetched " + map.size() + " teams");
                } else {
                    Log.e(TAG, "Failed to fetch teams: " + response.code());
                }
                checkBothReady();
            }

            @Override
            public void onFailure(Call<TeamsResponse> call, Throwable t) {
                Log.e(TAG, "Error fetching teams", t);
                checkBothReady();
            }
        });
    }

    private void checkBothReady() {
        if (pendingCalls.decrementAndGet() != 0) return;

        List<WorldCupGame> games = gamesRef.get();
        Map<String, Team> teamsMap = teamsMapRef.get();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.pBar.setVisibility(android.view.View.GONE);
                if (games == null) {
                    Toast.makeText(MainActivity.this, "Failed to load games", Toast.LENGTH_SHORT).show();
                    return;
                }
                binding.bracketView.setVisibility(android.view.View.VISIBLE);
                processGames(games, teamsMap);
            }
        });
    }

    private void processGames(List<WorldCupGame> games, Map<String, Team> teamsMap) {
        List<ColomnData> colomnDataList = new ArrayList<>();

        colomnDataList.add(getColomnDataForType(games, teamsMap, "r32", Arrays.asList("74", "77", "73", "75", "83", "84", "81", "82", "76", "78", "79", "80", "86", "88", "85", "87")));
        colomnDataList.add(getColomnDataForType(games, teamsMap, "r16", Arrays.asList("89", "90", "93", "94", "91", "92", "95", "96")));
        colomnDataList.add(getColomnDataForType(games, teamsMap, "qf", Arrays.asList("97", "98", "99", "100")));
        colomnDataList.add(getColomnDataForType(games, teamsMap, "sf", Arrays.asList("101", "102")));
        colomnDataList.add(getColomnDataForType(games, teamsMap, "final", Arrays.asList("104")));

        Log.d(TAG, "Setting brackets data with " + colomnDataList.size() + " columns");
        binding.bracketView.setBracketsData(colomnDataList);
    }

    private ColomnData getColomnDataForType(List<WorldCupGame> games, Map<String, Team> teamsMap, String type, List<String> orderedIds) {
        List<MatchData> matches = new ArrayList<>();

        for (String id : orderedIds) {
            for (WorldCupGame game : games) {
                if (id.equals(game.getId())) {
                    matches.add(createMatchData(game, teamsMap));
                    break;
                }
            }
        }

        // Fallback if some games are missing
        if (matches.isEmpty()) {
            for (WorldCupGame game : games) {
                if (type.equals(game.getType())) {
                    matches.add(createMatchData(game, teamsMap));
                }
            }
        }

        return new ColomnData(matches);
    }

    private MatchData createMatchData(WorldCupGame game, Map<String, Team> teamsMap) {
        CompetitorData home = new CompetitorData(game.getHomeTeam(), game.getHomeScore());
        CompetitorData away = new CompetitorData(game.getAwayTeam(), game.getAwayScore());

        if (teamsMap != null) {
            Team homeTeam = teamsMap.get(game.getHomeTeamId());
            Team awayTeam = teamsMap.get(game.getAwayTeamId());
            if (homeTeam != null) {
                home.setImageUrl(homeTeam.getFlagUrl());
            }
            if (awayTeam != null) {
                away.setImageUrl(awayTeam.getFlagUrl());
            }
        }

        MatchData match = new MatchData(home, away);
        match.setMatchName(getMatchName(game));
        return match;
    }

    private String getMatchName(WorldCupGame game) {
        switch (game.getType()) {
            case "r32": return "R32 - Match " + game.getId();
            case "r16": return "R16 - Match " + game.getId();
            case "qf": return "QF - Match " + game.getId();
            case "sf": return "SF - Match " + game.getId();
            case "final": return "Final";
            default: return "Match " + game.getId();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
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
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            BracketsConfig.getInstance().setScreenHeight(height);
        }
    }
}
