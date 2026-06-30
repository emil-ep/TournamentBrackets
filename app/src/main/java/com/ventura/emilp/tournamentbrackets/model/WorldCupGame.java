package com.ventura.emilp.tournamentbrackets.model;

import com.google.gson.annotations.SerializedName;

public class WorldCupGame {
    @SerializedName("id")
    private String id;

    @SerializedName("home_team_name_en")
    private String homeTeam;

    @SerializedName("away_team_name_en")
    private String awayTeam;

    @SerializedName("home_score")
    private String homeScore;

    @SerializedName("away_score")
    private String awayScore;

    @SerializedName("type")
    private String type;

    @SerializedName("home_team_label")
    private String homeTeamLabel;

    @SerializedName("away_team_label")
    private String awayTeamLabel;

    @SerializedName("finished")
    private String finished;

    @SerializedName("home_team_id")
    private String homeTeamId;

    @SerializedName("away_team_id")
    private String awayTeamId;

    public String getId() {
        return id;
    }

    public String getHomeTeam() {
        return homeTeam != null ? homeTeam : homeTeamLabel;
    }

    public String getAwayTeam() {
        return awayTeam != null ? awayTeam : awayTeamLabel;
    }

    public boolean isFinished() {
        return "TRUE".equalsIgnoreCase(finished);
    }

    public String getHomeScore() {
        return isFinished() ? homeScore : null;
    }

    public String getAwayScore() {
        return isFinished() ? awayScore : null;
    }

    public String getType() {
        return type;
    }

    public String getHomeTeamId() {
        return homeTeamId;
    }

    public String getAwayTeamId() {
        return awayTeamId;
    }
}
