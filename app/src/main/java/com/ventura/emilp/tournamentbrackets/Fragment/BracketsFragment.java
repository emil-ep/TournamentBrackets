package com.ventura.emilp.tournamentbrackets.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ventura.emilp.tournamentbrackets.databinding.FragmentBracktsBinding;
import com.ventura.emilp.tournamentbrackets.adapter.BracketsSectionAdapter;
import com.ventura.emilp.tournamentbrackets.model.ColomnData;
import com.ventura.emilp.tournamentbrackets.model.CompetitorData;
import com.ventura.emilp.tournamentbrackets.model.MatchData;
import com.ventura.emilp.tournamentbrackets.utility.BracketsUtility;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Emil on 21/10/17.
 */

public class BracketsFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private FragmentBracktsBinding binding;
    private BracketsSectionAdapter sectionAdapter;
    private ArrayList<ColomnData> sectionList;
    private int mNextSelectedScreen;
    private int mCurrentPagerState;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBracktsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setData();
        intialiseViewPagerAdapter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setData() {
        sectionList = new ArrayList<>();
        ArrayList<MatchData> colomn1matchesList = new ArrayList<>();
        ArrayList<MatchData> colomn2MatchesList = new ArrayList<>();
        ArrayList<MatchData> colomn3MatchesList = new ArrayList<>();
        
        CompetitorData competitorOne = new CompetitorData("Manchester United Fc", "2");
        CompetitorData competitorTwo = new CompetitorData("Arsenal", "1");
        CompetitorData competitorThree = new CompetitorData("Chelsea", "2");
        CompetitorData competitorFour = new CompetitorData("Tottenham", "1");
        CompetitorData competitorFive = new CompetitorData("Manchester FC", "2");
        CompetitorData competitorSix = new CompetitorData("Liverpool", "4");
        CompetitorData competitorSeven = new CompetitorData("West ham ", "2");
        CompetitorData competitorEight = new CompetitorData("Bayern munich", "1");
        
        MatchData matchData1 = new MatchData(competitorOne, competitorTwo);
        MatchData matchData2 = new MatchData(competitorThree, competitorFour);
        MatchData matchData3 = new MatchData(competitorFive, competitorSix);
        MatchData matchData4 = new MatchData(competitorSeven, competitorEight);
        
        colomn1matchesList.add(matchData1);
        colomn1matchesList.add(matchData2);
        colomn1matchesList.add(matchData3);
        colomn1matchesList.add(matchData4);
        
        ColomnData colomnData1 = new ColomnData(colomn1matchesList);
        sectionList.add(colomnData1);
        
        CompetitorData competitorNine = new CompetitorData("Manchester United Fc", "2");
        CompetitorData competitorTen = new CompetitorData("Chelsea", "4");
        CompetitorData competitorEleven = new CompetitorData("Liverpool", "2");
        CompetitorData competitorTwelve = new CompetitorData("westham", "1");
        
        MatchData matchData5 = new MatchData(competitorNine, competitorTen);
        MatchData matchData6 = new MatchData(competitorEleven, competitorTwelve);
        
        colomn2MatchesList.add(matchData5);
        colomn2MatchesList.add(matchData6);
        
        ColomnData colomnData2 = new ColomnData(colomn2MatchesList);
        sectionList.add(colomnData2);
        
        CompetitorData competitorThirteen = new CompetitorData("Chelsea", "2");
        CompetitorData competitorForteen = new CompetitorData("Liverpool", "1");
        
        MatchData matchData7 = new MatchData(competitorThirteen, competitorForteen);
        
        colomn3MatchesList.add(matchData7);
        
        ColomnData colomnData3 = new ColomnData(colomn3MatchesList);
        sectionList.add(colomnData3);

    }

    private void intialiseViewPagerAdapter() {
        sectionAdapter = new BracketsSectionAdapter(getChildFragmentManager(), this.sectionList);
        binding.container.setOffscreenPageLimit(10);
        binding.container.setAdapter(sectionAdapter);
        binding.container.setCurrentItem(0);
        binding.container.setPageMargin(-200);
        binding.container.setHorizontalFadingEdgeEnabled(true);
        binding.container.setFadingEdgeLength(50);

        binding.container.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if (mCurrentPagerState != ViewPager.SCROLL_STATE_SETTLING) {
            BracketsColomnFragment currentFragment = getBracketsFragment(position);
            BracketsColomnFragment nextFragment = getBracketsFragment(position + 1);

            // We are moving to next screen on right side
            if (positionOffset > 0.5) {
                // Closer to next screen than to current
                if (position + 1 != mNextSelectedScreen) {
                    mNextSelectedScreen = position + 1;
                    //update view here
                    if (currentFragment != null && currentFragment.getColomnList() != null && !currentFragment.getColomnList().isEmpty()) {
                        if (currentFragment.getColomnList().get(0).getHeight() != BracketsUtility.dpToPx(131)) {
                            currentFragment.shrinkView(BracketsUtility.dpToPx(131));
                        }
                    }
                    if (nextFragment != null && nextFragment.getColomnList() != null && !nextFragment.getColomnList().isEmpty()) {
                        if (nextFragment.getColomnList().get(0).getHeight() != BracketsUtility.dpToPx(131)) {
                            nextFragment.shrinkView(BracketsUtility.dpToPx(131));
                        }
                    }
                }
            } else {
                // Closer to current screen than to next
                if (position != mNextSelectedScreen) {
                    mNextSelectedScreen = position;
                    //updateViewhere

                    if (nextFragment != null && currentFragment != null) {
                        if (nextFragment.getCurrentBracketSize() == nextFragment.getPreviousBracketSize()) {
                            nextFragment.shrinkView(BracketsUtility.dpToPx(131));
                            currentFragment.shrinkView(BracketsUtility.dpToPx(131));
                        } else {
                            int currentFragmentSize = nextFragment.getCurrentBracketSize();
                            int previousFragmentSize = nextFragment.getPreviousBracketSize();
                            if (currentFragmentSize != previousFragmentSize) {
                                nextFragment.expandHeight(BracketsUtility.dpToPx(262));
                                currentFragment.shrinkView(BracketsUtility.dpToPx(131));
                            }
                        }
                    }
                }
            }
        } else {
            // We are moving to next screen left side
            if (positionOffset > 0.5) {
                // Closer to current screen than to next
                if (position + 1 != mNextSelectedScreen) {
                    mNextSelectedScreen = position + 1;
                    //update view for screen

                }
            } else {
                // Closer to next screen than to current
                if (position != mNextSelectedScreen) {
                    mNextSelectedScreen = position;
                    //updateviewfor screem
                }
            }
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mCurrentPagerState = state;
    }

    public BracketsColomnFragment getBracketsFragment(int position) {
        BracketsColomnFragment bracktsFrgmnt = null;
        if (getChildFragmentManager() != null) {
            List<Fragment> fragments = getChildFragmentManager().getFragments();
            if (fragments != null) {
                for (Fragment fragment : fragments) {
                    if (fragment instanceof BracketsColomnFragment) {
                        bracktsFrgmnt = (BracketsColomnFragment) fragment;
                        if (bracktsFrgmnt.getSectionNumber() == position)
                            break;
                    }
                }
            }
        }
        return bracktsFrgmnt;
    }
}
