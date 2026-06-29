package com.ventura.emilp.tournamentbrackets.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GroupsResponse {
    @SerializedName("groups")
    private List<Group> groups;

    public List<Group> getGroups() {
        return groups;
    }
}
