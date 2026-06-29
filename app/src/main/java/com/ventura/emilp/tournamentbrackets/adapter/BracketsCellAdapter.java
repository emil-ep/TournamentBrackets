package com.ventura.emilp.tournamentbrackets.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ventura.emilp.tournamentbrackets.Fragment.BracketsColomnFragment;
import com.ventura.emilp.tournamentbrackets.databinding.LayoutCellBracketsBinding;
import com.ventura.emilp.tournamentbrackets.model.MatchData;
import com.ventura.emilp.tournamentbrackets.viewholder.BracketsCellViewHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Emil on 21/10/17.
 */

public class BracketsCellAdapter extends RecyclerView.Adapter<BracketsCellViewHolder> {

    private final BracketsColomnFragment fragment;
    private final Context context;
    private ArrayList<MatchData> list;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public BracketsCellAdapter(BracketsColomnFragment bracketsColomnFragment, Context context, ArrayList<MatchData> list) {
        this.fragment = bracketsColomnFragment;
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BracketsCellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutCellBracketsBinding binding = LayoutCellBracketsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BracketsCellViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BracketsCellViewHolder holder, int position) {
        setFields(holder, position);
    }

    private void setFields(final BracketsCellViewHolder viewHolder, final int position) {
        final int height = list.get(position).getHeight();
        final WeakReference<BracketsCellViewHolder> weakViewHolder = new WeakReference<>(viewHolder);

        mainHandler.postDelayed(() -> {
            BracketsCellViewHolder holder = weakViewHolder.get();
            if (holder != null && holder.getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
                holder.setAnimation(height);
            }
        }, 100);

        viewHolder.getTeamOneName().setText(list.get(position).getCompetitorOne().getName());
        viewHolder.getTeamTwoName().setText(list.get(position).getCompetitorTwo().getName());
        viewHolder.getTeamOneScore().setText(list.get(position).getCompetitorOne().getScore());
        viewHolder.getTeamTwoScore().setText(list.get(position).getCompetitorTwo().getScore());
    }

    @Override
    public int getItemCount() {
        return this.list != null ? this.list.size() : 0;
    }

    public void setList(ArrayList<MatchData> colomnList) {
        this.list = colomnList;
        notifyDataSetChanged();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mainHandler.removeCallbacksAndMessages(null);
    }
}
