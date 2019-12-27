package io.github.t3r1jj.pbmap.main;

import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.View;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.github.clans.fab.FloatingActionMenu;

import io.github.t3r1jj.pbmap.R;

public class Tutorial extends ContextWrapper {
    static final String INTRODUCTION_FINISHED = "io.github.t3r1jj.pbmap.main.INTRODUCTION_FINISHED";
    private final MapActivity map;

    private final TapTargetSequence tapTargetSequence;

    public Tutorial(MapActivity activity) {
        super(activity);
        this.map = activity;

        tapTargetSequence = new TapTargetSequence(map);
        tapTargetSequence.continueOnCancel(true);
        tapTargetSequence.considerOuterCircleCanceled(true);
    }

    void start() {
        tapTargetSequence.targets(
                defaultWrap(TapTarget.forView(map.findViewById(R.id.action_search),
                        getString(R.string.action_search), getString(R.string.action_search_description))),
                defaultWrap(TapTarget.forToolbarNavigationIcon(map.toolbar,
                        getString(R.string.menu), getString(R.string.menu_description))),
                defaultWrap(TapTarget.forView(getMenuFab(map.levelMenu),
                        getString(R.string.floor), getString(R.string.floor_description)))
                        .transparentTarget(true)
                ,
                defaultWrap(TapTarget.forView(getMenuFab(map.moreOptions),
                        getString(R.string.more_features), getString(R.string.more_features_description)))
                        .transparentTarget(true)
                ,
                defaultWrap(TapTarget.forBounds(calculateScreenRect(),
                        getString(R.string.map), getString(R.string.maps_description)))
                        .transparentTarget(true)
                        .targetRadius(map.getResources().getDimensionPixelSize(R.dimen.target_map_radius))
                ,
                defaultWrap(TapTarget.forView(getBackButtonView(),
                        getString(R.string.action_back), getString(R.string.action_back_description)))

        );
        tapTargetSequence.listener(new TutorialSequenceListener());
        tapTargetSequence.start();
    }

    private View getBackButtonView() {
        return map.findViewById(R.id.action_back) != null ? map.findViewById(R.id.action_back) : map.findViewById(R.id.action_search);
    }

    private View getMenuFab(FloatingActionMenu moreOptions) {
        if (moreOptions.isOpened()) {
            return moreOptions.getChildAt(moreOptions.getChildCount() - 2);
        } else {
            return moreOptions.getChildAt(moreOptions.getChildCount() - 1);
        }
    }

    private Rect calculateScreenRect() {
        Display display = map.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        int[] attrs = new int[]{R.attr.actionBarSize};
        TypedArray ta = map.obtainStyledAttributes(attrs);
        int toolBarHeight = ta.getDimensionPixelSize(0, -1);
        ta.recycle();
        return new Rect(0, toolBarHeight, width, height);
    }

    private TapTarget defaultWrap(TapTarget tapTarget) {
        tapTarget.targetCircleColor(R.color.colorAccent)
                .outerCircleColor(R.color.colorAccentSecondary)
                .textColor(R.color.colorSecondaryText)
                .titleTextColor(R.color.colorSecondaryText)
                .descriptionTextColor(R.color.colorSecondaryText)
                .tintTarget(false)
                .drawShadow(true)
                .outerCircleAlpha(1)
                .transparentTarget(false);
        return tapTarget;
    }

    private class TutorialSequenceListener implements TapTargetSequence.Listener {
        private final int levelMenuVisibility;
        private final boolean backItemVisible;

        private TutorialSequenceListener() {
            levelMenuVisibility = map.levelMenu.getVisibility();
            if (levelMenuVisibility != View.VISIBLE) {
                map.levelMenu.setVisibility(View.VISIBLE);
            }
            backItemVisible = map.backButton.isVisible();
            if (!backItemVisible) {
                map.backButton.setVisible(true);
            }
        }

        @Override
        public void onSequenceFinish() {
            map.levelMenu.setVisibility(levelMenuVisibility);
            map.backButton.setVisible(backItemVisible);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(map);
            preferences.edit().putBoolean(INTRODUCTION_FINISHED, true).apply();
        }

        @Override
        public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
            // on sequence step we don't do anything special
        }

        @Override
        public void onSequenceCanceled(TapTarget lastTarget) {
            // we have set continueOnCancel to true thus this is not called
        }
    }
}
