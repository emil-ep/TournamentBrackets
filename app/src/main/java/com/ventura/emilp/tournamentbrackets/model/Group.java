package com.ventura.emilp.tournamentbrackets.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Group {
    @SerializedName("name")
    private String name;

    @SerializedName("teams")
    private List<GroupTeamEntry> teams;

    public String getName() {
        return name;
    }

    public List<GroupTeamEntry> getTeams() {
        return teams;
    }
}
