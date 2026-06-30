package com.ventura.emilp.tournamentbrackets.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ventura.emilp.tournamentbrackets.adapter.GroupStandingsAdapter;
import com.ventura.emilp.tournamentbrackets.adapter.GroupStandingsAdapter.GroupData;
import com.ventura.emilp.tournamentbrackets.databinding.ActivityGroupStandingsBinding;
import com.ventura.emilp.tournamentbrackets.model.Group;
import com.ventura.emilp.tournamentbrackets.model.GroupTeamDisplayItem;
import com.ventura.emilp.tournamentbrackets.model.GroupTeamEntry;
import com.ventura.emilp.tournamentbrackets.model.GroupsResponse;
import com.ventura.emilp.tournamentbrackets.model.Team;
import com.ventura.emilp.tournamentbrackets.model.TeamsResponse;
import com.ventura.emilp.tournamentbrackets.network.RetrofitClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupStandingsActivity extends AppCompatActivity {

    private static final String TAG = "GroupStandingsActivity";

    private ActivityGroupStandingsBinding binding;
    private GroupStandingsAdapter adapter;

    // Results from parallel API calls (accessed only on main thread)
    private List<Group> groups;
    private Map<String, Team> teamsMap;
    private int pendingCalls;
    private String errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupStandingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Group Standings");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        adapter = new GroupStandingsAdapter();
        binding.recyclerGroups.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerGroups.setAdapter(adapter);

        binding.swipeRefresh.setOnRefreshListener(this::loadData);

        binding.pBar.setVisibility(android.view.View.VISIBLE);
        binding.recyclerGroups.setVisibility(android.view.View.GONE);

        loadData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void loadData() {
        groups = null;
        teamsMap = null;
        errorMessage = null;
        pendingCalls = 2;
        fetchGroups();
        fetchTeams();
    }

    private void fetchGroups() {
        RetrofitClient.getApi().getGroups().enqueue(new Callback<GroupsResponse>() {
            @Override
            public void onResponse(Call<GroupsResponse> call, Response<GroupsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    groups = response.body().getGroups();
                    Log.d(TAG, "Groups fetched: " + groups.size());
                } else {
                    errorMessage = "Failed to load groups (HTTP " + response.code() + ")";
                    Log.e(TAG, errorMessage);
                }
                onCallComplete();
            }

            @Override
            public void onFailure(Call<GroupsResponse> call, Throwable t) {
                errorMessage = "Network error loading groups: " + t.getMessage();
                Log.e(TAG, errorMessage, t);
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
                    Log.d(TAG, "Teams fetched: " + teamsMap.size());
                } else {
                    errorMessage = "Failed to load teams (HTTP " + response.code() + ")";
                    Log.e(TAG, errorMessage);
                }
                onCallComplete();
            }

            @Override
            public void onFailure(Call<TeamsResponse> call, Throwable t) {
                errorMessage = "Network error loading teams: " + t.getMessage();
                Log.e(TAG, errorMessage, t);
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
        binding.swipeRefresh.setRefreshing(false);

        if (groups == null || teamsMap == null) {
            String msg = errorMessage != null ? errorMessage : "Failed to load standings";
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            return;
        }

        // Build display list, sorting groups alphabetically then teams by points desc
        List<GroupData> displayData = new ArrayList<>();
        Collections.sort(groups, (a, b) -> a.getName().compareTo(b.getName()));

        for (Group group : groups) {
            List<GroupTeamEntry> entries = new ArrayList<>(group.getTeams());
            // Sort by points descending, then goal difference descending
            Collections.sort(entries, (a, b) -> {
                int ptsDiff = parseInt(b.getPts()) - parseInt(a.getPts());
                if (ptsDiff != 0) return ptsDiff;
                return parseInt(b.getGd()) - parseInt(a.getGd());
            });

            List<GroupTeamDisplayItem> rowItems = new ArrayList<>();
            for (GroupTeamEntry entry : entries) {
                Team team = teamsMap.get(entry.getTeamId());
                if (team != null) {
                    rowItems.add(new GroupTeamDisplayItem(team, entry));
                }
            }
            displayData.add(new GroupData(group.getName(), rowItems));
        }

        binding.recyclerGroups.setVisibility(android.view.View.VISIBLE);
        adapter.setData(displayData);
    }

    private static int parseInt(String s) {
        if (s == null) return 0;
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return 0; }
    }
}
