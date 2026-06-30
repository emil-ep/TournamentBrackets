package com.ventura.emilp.tournamentbrackets.model;

/** Merged display model combining GroupTeamEntry stats with Team name/flag. */
public class GroupTeamDisplayItem {
    private final String name;
    private final String flagUrl;
    private final String mp;
    private final String w;
    private final String d;
    private final String l;
    private final String gf;
    private final String ga;
    private final String gd;
    private final String pts;

    public GroupTeamDisplayItem(Team team, GroupTeamEntry entry) {
        this.name = team.getNameEn();
        this.flagUrl = team.getFlagUrl();
        this.mp = entry.getMp();
        this.w = entry.getW();
        this.d = entry.getD();
        this.l = entry.getL();
        this.gf = entry.getGf();
        this.ga = entry.getGa();
        this.gd = entry.getGd();
        this.pts = entry.getPts();
    }

    public String getName() { return name; }
    public String getFlagUrl() { return flagUrl; }
    public String getMp() { return mp; }
    public String getW() { return w; }
    public String getD() { return d; }
    public String getL() { return l; }
    public String getGf() { return gf; }
    public String getGa() { return ga; }
    public String getGd() { return gd; }
    public String getPts() { return pts; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupTeamDisplayItem)) return false;
        GroupTeamDisplayItem other = (GroupTeamDisplayItem) o;
        return eq(name, other.name) && eq(pts, other.pts) && eq(mp, other.mp)
                && eq(w, other.w) && eq(d, other.d) && eq(l, other.l)
                && eq(gf, other.gf) && eq(ga, other.ga) && eq(gd, other.gd);
    }

    private static boolean eq(String a, String b) {
        return a != null ? a.equals(b) : b == null;
    }
}
