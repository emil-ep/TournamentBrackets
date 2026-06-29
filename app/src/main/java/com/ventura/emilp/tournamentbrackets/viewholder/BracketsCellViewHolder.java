package com.ventura.emilp.tournamentbrackets.viewholder;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ventura.emilp.tournamentbrackets.animation.SlideAnimation;
import com.ventura.emilp.tournamentbrackets.databinding.LayoutCellBracketsBinding;

/**
 * Created by Emil on 21/10/17.
 */

public class BracketsCellViewHolder extends RecyclerView.ViewHolder {

    private final LayoutCellBracketsBinding binding;
    private Animation animation;

    public BracketsCellViewHolder(@NonNull LayoutCellBracketsBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void setAnimation(int height) {
        animation = new SlideAnimation(binding.layoutRoot, binding.layoutRoot.getHeight(), height);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(200);
        binding.layoutRoot.setAnimation(animation);
        binding.layoutRoot.startAnimation(animation);
    }

    public TextView getTeamTwoName() {
        return binding.teamTwoName;
    }

    public TextView getTeamOneScore() {
        return binding.teamOneScore;
    }

    public TextView getTeamTwoScore() {
        return binding.teamTwoScore;
    }

    public TextView getTeamOneName() {
        return binding.teamOneName;
    }
}
