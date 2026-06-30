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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupStandingsActivity extends AppCompatActivity {

    private static final String TAG = "GroupStandingsActivity";

    private ActivityGroupStandingsBinding binding;
    private GroupStandingsAdapter adapter;

    // Holds results from the two parallel API calls
    private final AtomicReference<List<Group>> groupsRef = new AtomicReference<>();
    private final AtomicReference<Map<String, Team>> teamsMapRef = new AtomicReference<>();
    private final AtomicInteger pendingCalls = new AtomicInteger(2);

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

        binding.pBar.setVisibility(android.view.View.VISIBLE);
        binding.recyclerGroups.setVisibility(android.view.View.GONE);

        fetchGroups();
        fetchTeams();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void fetchGroups() {
        RetrofitClient.getApi().getGroups().enqueue(new Callback<GroupsResponse>() {
            @Override
            public void onResponse(Call<GroupsResponse> call, Response<GroupsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    groupsRef.set(response.body().getGroups());
                    Log.d(TAG, "Groups fetched: " + response.body().getGroups().size());
                } else {
                    Log.e(TAG, "Groups call failed: " + response.code());
                }
                checkBothReady();
            }

            @Override
            public void onFailure(Call<GroupsResponse> call, Throwable t) {
                Log.e(TAG, "Groups fetch error", t);
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
                    Log.d(TAG, "Teams fetched: " + map.size());
                } else {
                    Log.e(TAG, "Teams call failed: " + response.code());
                }
                checkBothReady();
            }

            @Override
            public void onFailure(Call<TeamsResponse> call, Throwable t) {
                Log.e(TAG, "Teams fetch error", t);
                checkBothReady();
            }
        });
    }

    /** Called after each completed API call; builds the UI when both are done. */
    private void checkBothReady() {
        if (pendingCalls.decrementAndGet() != 0) return;

        List<Group> groups = groupsRef.get();
        Map<String, Team> teamsMap = teamsMapRef.get();

        if (groups == null || teamsMap == null) {
            runOnUiThread(() -> {
                binding.pBar.setVisibility(android.view.View.GONE);
                Toast.makeText(this, "Failed to load standings", Toast.LENGTH_SHORT).show();
            });
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

        runOnUiThread(() -> {
            binding.pBar.setVisibility(android.view.View.GONE);
            binding.recyclerGroups.setVisibility(android.view.View.VISIBLE);
            adapter.setData(displayData);
        });
    }

    private static int parseInt(String s) {
        if (s == null) return 0;
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return 0; }
    }
}
