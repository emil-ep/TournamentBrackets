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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";

    // Results from parallel API calls (accessed only on main thread)
    private List<WorldCupGame> games;
    private Map<String, Team> teamsMap;
    private int pendingCalls = 2;

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
                    games = response.body().getGames();
                    Log.d(TAG, "Fetched " + games.size() + " games");
                } else {
                    Log.e(TAG, "Failed to fetch games: " + response.code());
                }
                onCallComplete();
            }

            @Override
            public void onFailure(Call<WorldCupResponse> call, Throwable t) {
                Log.e(TAG, "Error fetching games", t);
                onCallComplete();
            }
        });
    }

    private void fetchTeams() {
        RetrofitClient.getApi().getTeams().enqueue(new Callback<TeamsResponse>() {
            @Override
            public void onResponse(Call<TeamsResponse> call, Response<TeamsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    teamsMap = new HashMap<>();
                    for (Team t : response.body().getTeams()) {
                        teamsMap.put(t.getId(), t);
                    }
                    Log.d(TAG, "Fetched " + teamsMap.size() + " teams");
                } else {
                    Log.e(TAG, "Failed to fetch teams: " + response.code());
                }
                onCallComplete();
            }

            @Override
            public void onFailure(Call<TeamsResponse> call, Throwable t) {
                Log.e(TAG, "Error fetching teams", t);
                onCallComplete();
            }
        });
    }

    /**
     * Called on the main thread after each Retrofit callback completes.
     * Retrofit enqueue delivers callbacks on the main thread, so no
     * synchronization is needed.
     */
    private void onCallComplete() {
        pendingCalls--;
        if (pendingCalls != 0) return;

        binding.pBar.setVisibility(android.view.View.GONE);
        if (games == null) {
            Toast.makeText(this, "Failed to load games", Toast.LENGTH_SHORT).show();
            return;
        }
        binding.bracketView.setVisibility(android.view.View.VISIBLE);
        processGames(games, teamsMap);
    }

    private static final String[] KNOCKOUT_ROUNDS = {"r32", "r16", "qf", "sf", "final"};

    private void processGames(List<WorldCupGame> games, Map<String, Team> teamsMap) {
        // Group games by type
        Map<String, List<WorldCupGame>> gamesByType = new HashMap<>();
        for (WorldCupGame game : games) {
            String type = game.getType();
            if (type == null) continue;
            List<WorldCupGame> list = gamesByType.get(type);
            if (list == null) {
                list = new ArrayList<>();
                gamesByType.put(type, list);
            }
            list.add(game);
        }

        // Sort each round's games by ID ascending
        for (List<WorldCupGame> list : gamesByType.values()) {
            Collections.sort(list, (a, b) -> Integer.compare(parseId(a.getId()), parseId(b.getId())));
        }

        // Build bracket columns in round order
        List<ColomnData> colomnDataList = new ArrayList<>();
        for (String round : KNOCKOUT_ROUNDS) {
            List<WorldCupGame> roundGames = gamesByType.get(round);
            if (roundGames != null && !roundGames.isEmpty()) {
                List<MatchData> matches = new ArrayList<>();
                for (WorldCupGame game : roundGames) {
                    matches.add(createMatchData(game, teamsMap));
                }
                colomnDataList.add(new ColomnData(matches));
            }
        }

        Log.d(TAG, "Setting brackets data with " + colomnDataList.size() + " columns");
        binding.bracketView.setBracketsData(colomnDataList);
    }

    private static int parseId(String id) {
        if (id == null) return 0;
        try { return Integer.parseInt(id); } catch (NumberFormatException e) { return 0; }
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
