package com.ventura.emilp.tournamentbrackets.model;

import com.google.gson.annotations.SerializedName;

public class Team {
    @SerializedName("id")
    private String id;

    @SerializedName("name_en")
    private String nameEn;

    @SerializedName("flag")
    private String flagUrl;

    @SerializedName("fifa_code")
    private String fifaCode;

    public String getId() { return id; }
    public String getNameEn() { return nameEn; }
    public String getFlagUrl() { return flagUrl; }
    public String getFifaCode() { return fifaCode; }
}
