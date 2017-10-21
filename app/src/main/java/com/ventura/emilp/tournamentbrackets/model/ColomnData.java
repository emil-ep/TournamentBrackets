package com.ventura.emilp.tournamentbrackets.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Emil on 21/10/17.
 */

public class ColomnData implements Serializable{

    public ColomnData(ArrayList<MatchData> matches) {
        this.matches = matches;
    }

    private ArrayList<MatchData> matches;

    public void setMatches(ArrayList<MatchData> matches) {
        this.matches = matches;
    }

    public ArrayList<MatchData> getMatches() {
        return matches;
    }
}
