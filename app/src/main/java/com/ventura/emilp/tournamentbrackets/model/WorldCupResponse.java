package com.ventura.emilp.tournamentbrackets.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WorldCupResponse {
    @SerializedName("games")
    private List<WorldCupGame> games;

    public List<WorldCupGame> getGames() {
        return games;
    }
}
