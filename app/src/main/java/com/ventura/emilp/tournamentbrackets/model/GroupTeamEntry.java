package com.ventura.emilp.tournamentbrackets.model;

import com.google.gson.annotations.SerializedName;

public class GroupTeamEntry {
    @SerializedName("team_id")
    private String teamId;

    @SerializedName("mp")
    private String mp;

    @SerializedName("w")
    private String w;

    @SerializedName("d")
    private String d;

    @SerializedName("l")
    private String l;

    @SerializedName("gf")
    private String gf;

    @SerializedName("ga")
    private String ga;

    @SerializedName("gd")
    private String gd;

    @SerializedName("pts")
    private String pts;

    public String getTeamId() { return teamId; }
    public String getMp() { return mp; }
    public String getW() { return w; }
    public String getD() { return d; }
    public String getL() { return l; }
    public String getGf() { return gf; }
    public String getGa() { return ga; }
    public String getGd() { return gd; }
    public String getPts() { return pts; }
}
