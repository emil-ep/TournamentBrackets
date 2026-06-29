package com.ventura.emilp.tournamentbrackets.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TeamsResponse {
    @SerializedName("teams")
    private List<Team> teams;

    public List<Team> getTeams() {
        return teams;
    }
}
