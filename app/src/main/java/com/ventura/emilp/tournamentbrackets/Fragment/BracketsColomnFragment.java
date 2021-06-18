package com.ventura.emilp.tournamentbrackets.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ventura.emilp.tournamentbrackets.R;
import com.ventura.emilp.tournamentbrackets.adapter.BracketsCellAdapter;
import com.ventura.emilp.tournamentbrackets.model.ColomnData;
import com.ventura.emilp.tournamentbrackets.model.MatchData;
import com.ventura.emilp.tournamentbrackets.utility.BracketsUtility;

import java.util.ArrayList;

/**
 * Created by Emil on 21/10/17.
 */

public class BracketsColomnFragment extends Fragment {

    private ColomnData colomnData;
    private int sectionNumber = 0;
    private int previousBracketSize;
    private ArrayList<MatchData> list;
    private RecyclerView bracketsRV;

    private BracketsCellAdapter  adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_brackets_colomn, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        getExtras();
        initAdapter();
    }

    private void initViews() {

        bracketsRV = (RecyclerView) getView().findViewById(R.id.rv_score_board);
    }

    public ArrayList<MatchData> getColomnList() {
        return list;
    }

    private void getExtras() {
        if (getArguments() != null) {
            list = new ArrayList<>();
            colomnData = (ColomnData) getArguments().getSerializable("colomn_data");
            sectionNumber = getArguments().getInt("section_number");
            previousBracketSize = getArguments().getInt("previous_section_size");
            list.addAll(colomnData.getMatches());
            setInitialHeightForList();
        }
    }
    public int getSectionNumber() {
        return sectionNumber;
    }

    private void setInitialHeightForList() {
        for (MatchData data : list){
            if (sectionNumber == 0){
                data.setHeight(BracketsUtility.dpToPx(131));
            }else if (sectionNumber == 1 && previousBracketSize != list.size()){
                data.setHeight(BracketsUtility.dpToPx(262));
            }else if (sectionNumber == 1 && previousBracketSize == list.size()) {
                data.setHeight(BracketsUtility.dpToPx(131));
            } else if (previousBracketSize > list.size()) {
                data.setHeight(BracketsUtility.dpToPx(262));
            }else if (previousBracketSize == list.size()) {
                data.setHeight(BracketsUtility.dpToPx(131));
            }
        }

    }

    public void expandHeight(int height) {

        for (MatchData data : list) {
            data.setHeight(height);
        }
        adapter.setList(list);
    }

    public void shrinkView(int height) {
        for (MatchData data : list) {
            data.setHeight(height);
        }
        adapter.setList(list);
    }
    private void initAdapter() {

//        pBar.setVisibility(View.GONE);
         adapter = new BracketsCellAdapter(this, getContext(), list);
        if (bracketsRV != null) {
            bracketsRV.setHasFixedSize(true);
            bracketsRV.setNestedScrollingEnabled(false);
            bracketsRV.setAdapter(adapter);
            bracketsRV.smoothScrollToPosition(0);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            bracketsRV.setLayoutManager(layoutManager);
            bracketsRV.setItemAnimator(new DefaultItemAnimator());
        }
    }

    public int getCurrentBracketSize() {
        return list.size();
    }
    public int getPreviousBracketSize() {
        return previousBracketSize;
    }
}
