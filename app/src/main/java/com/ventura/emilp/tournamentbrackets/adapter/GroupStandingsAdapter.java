package com.ventura.emilp.tournamentbrackets.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ventura.emilp.tournamentbrackets.R;
import com.ventura.emilp.tournamentbrackets.model.GroupTeamDisplayItem;

import java.util.ArrayList;
import java.util.List;

public class GroupStandingsAdapter extends RecyclerView.Adapter<GroupStandingsAdapter.GroupViewHolder> {

    private final List<GroupData> groupList = new ArrayList<>();

    public void setData(List<GroupData> newData) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() { return groupList.size(); }

            @Override
            public int getNewListSize() { return newData.size(); }

            @Override
            public boolean areItemsTheSame(int oldPos, int newPos) {
                return groupList.get(oldPos).groupName.equals(newData.get(newPos).groupName);
            }

            @Override
            public boolean areContentsTheSame(int oldPos, int newPos) {
                return groupList.get(oldPos).equals(newData.get(newPos));
            }
        });
        groupList.clear();
        groupList.addAll(newData);
        result.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_group_table, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        holder.bind(groupList.get(position));
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvGroupName;
        private final LinearLayout llTeamsContainer;

        GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGroupName = itemView.findViewById(R.id.tv_group_name);
            llTeamsContainer = itemView.findViewById(R.id.ll_teams_container);
        }

        void bind(GroupData groupData) {
            tvGroupName.setText("Group " + groupData.groupName);
            llTeamsContainer.removeAllViews();

            Context context = itemView.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            for (GroupTeamDisplayItem item : groupData.teams) {
                View row = inflater.inflate(R.layout.item_group_team_row, llTeamsContainer, false);

                ImageView ivFlag = row.findViewById(R.id.iv_flag);
                TextView tvName = row.findViewById(R.id.tv_name);
                TextView tvMp = row.findViewById(R.id.tv_mp);
                TextView tvW = row.findViewById(R.id.tv_w);
                TextView tvD = row.findViewById(R.id.tv_d);
                TextView tvL = row.findViewById(R.id.tv_l);
                TextView tvGf = row.findViewById(R.id.tv_gf);
                TextView tvGa = row.findViewById(R.id.tv_ga);
                TextView tvGd = row.findViewById(R.id.tv_gd);
                TextView tvPts = row.findViewById(R.id.tv_pts);

                Glide.with(context)
                        .load(item.getFlagUrl())
                        .placeholder(R.drawable.flag_placeholder)
                        .error(R.drawable.flag_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivFlag);

                tvName.setText(item.getName());
                tvMp.setText(item.getMp());
                tvW.setText(item.getW());
                tvD.setText(item.getD());
                tvL.setText(item.getL());
                tvGf.setText(item.getGf());
                tvGa.setText(item.getGa());
                tvGd.setText(item.getGd());
                tvPts.setText(item.getPts());

                llTeamsContainer.addView(row);
            }
        }
    }

    /** Simple data holder for one group's display data. */
    public static class GroupData {
        public final String groupName;
        public final List<GroupTeamDisplayItem> teams;

        public GroupData(String groupName, List<GroupTeamDisplayItem> teams) {
            this.groupName = groupName;
            this.teams = teams;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof GroupData)) return false;
            GroupData other = (GroupData) o;
            if (!groupName.equals(other.groupName)) return false;
            if (teams.size() != other.teams.size()) return false;
            for (int i = 0; i < teams.size(); i++) {
                if (!teams.get(i).equals(other.teams.get(i))) return false;
            }
            return true;
        }
    }
}
