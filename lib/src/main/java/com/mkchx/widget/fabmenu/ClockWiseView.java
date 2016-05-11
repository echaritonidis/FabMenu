package com.mkchx.widget.fabmenu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.mkchx.widget.fabmenu.interfaces.IViewClick;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Efthimis Charitonidis
 */
public class ClockWiseView extends RelativeLayout implements View.OnClickListener {

    private FrameLayout mUiLayout;
    private FloatingActionButton mUiFab;

    private List<View> mFabList = new ArrayList<>();

    private IViewClick iViewClickListener;

    private float fabScale, startRotation;
    private int defMiniSize, defMargin, defElevation;

    public ClockWiseView(Context context) {
        this(context, null);
    }

    public ClockWiseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockWiseView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        defMiniSize = (int) getResources().getDimension(R.dimen.fab_size_mini);
        defMargin = (int) getResources().getDimension(R.dimen.fab_margin);
        defElevation = (int) getResources().getDimension(R.dimen.fab_elevation);

        fabScale = 1f;

        LayoutInflater.from(context).inflate(R.layout.clockwise_layout, this);
        mUiLayout = (FrameLayout) findViewById(R.id.custom_content);
        mUiLayout.setVisibility(GONE);

        mUiFab = (FloatingActionButton) findViewById(R.id.main_fab);
        mUiFab.setOnClickListener(this);

        setViewElevation(mUiFab);
    }

    /**
     * Set the main Fab icon
     *
     * @param drawable The icon of the fab
     */
    public void setMainFabIcon(Drawable drawable) {
        mUiFab.setImageDrawable(drawable);
    }

    /**
     * Set the main Fab start rotation degrees
     *
     * @param value The degrees of rotation
     */
    public void setMainFabRotation(float value) {
        startRotation = value;
        ViewCompat.setRotation(mUiFab, value);
    }

    /**
     * Register a callback to be invoked when this view is clicked.
     *
     * @param listener The callback that will run
     */
    public void setOnItemClickListener(IViewClick listener) {
        this.iViewClickListener = listener;
    }

    /**
     * Add a new fab view
     *
     * @param drawable The icon of the fab
     */
    public void addChildView(Drawable drawable) {

        FloatingActionButton child = createFab(drawable);
        child.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (iViewClickListener != null) {
                    int position = ((ViewGroup) v.getParent()).indexOfChild(v);
                    iViewClickListener.onItemClick(v, position);
                }
            }
        });

        mUiLayout.addView(child);
    }

    /**
     * Positions the fab's respectfully,
     * call this when you finished adding views
     */
    public void create() {

        float i = 0.5f;
        double radius = (getResources().getDimension(R.dimen.container) / 2) + (defMiniSize / 2) + (defMargin * 2);
        float angle = 90f / mFabList.size() - 1;

        for (View v : mFabList) {
            float angDegrees = angle * i;

            float x = (float) -(radius * Math.cos(Math.toRadians(angDegrees)));
            float y = (float) -(radius * Math.sin(Math.toRadians(angDegrees)));

            ViewCompat.setTranslationX(v, x);
            ViewCompat.setTranslationY(v, y);

            i++;
        }
    }

    private FloatingActionButton createFab(Drawable drawable) {

        FloatingActionButton fabView = (FloatingActionButton) LayoutInflater.from(getContext()).inflate(R.layout.fab_mini, null);

        FrameLayout.LayoutParams fabParams = new FrameLayout.LayoutParams(defMiniSize, defMiniSize);
        fabParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        fabView.setLayoutParams(fabParams);

        fabView.setImageDrawable(drawable);
        setScaleXY(fabView, 0f);

        mFabList.add(fabView);

        return fabView;
    }

    private void setViewElevation(View view) {
        ViewCompat.setElevation(view, defElevation);
    }

    private void setScaleXY(View view, float val) {
        ViewCompat.setScaleX(view, val);
        ViewCompat.setScaleY(view, val);
    }


    /**
     * Anim functions
     */
    private void rotateMain(View view, float degrees) {

        if (Build.VERSION.SDK_INT >= 11) {
            ViewCompat.animate(view)
                    .rotation(degrees)
                    .setInterpolator(new FastOutSlowInInterpolator())
                    .start();
        } else {
            AnimatorSet mSet = new AnimatorSet();

            mSet.playTogether(
                    ObjectAnimator.ofFloat(view, "rotation", degrees)
            );
            mSet.setInterpolator(new FastOutSlowInInterpolator());
            mSet.start();
        }

    }

    private void toggleFab(View view, int delay, float scale, final boolean last) {

        if (Build.VERSION.SDK_INT >= 11) {

            ViewCompat.animate(view)
                    .setStartDelay(delay)
                    .scaleX(scale)
                    .scaleY(scale)
                    .setInterpolator(new FastOutSlowInInterpolator())
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            if (last) {
                                mUiLayout.setVisibility(View.GONE);
                            }
                        }
                    })
                    .start();
        } else {

            AnimatorSet mSet = new AnimatorSet();

            mSet.playTogether(
                    ObjectAnimator.ofFloat(view, "scaleX", scale),
                    ObjectAnimator.ofFloat(view, "scaleY", scale)
            );
            mSet.setStartDelay(delay);
            mSet.setInterpolator(new FastOutSlowInInterpolator());
            mSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (last) {
                        mUiLayout.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            mSet.start();
        }

    }

    @Override
    public void onClick(View v) {

        int interpolator = 0;

        if (mUiLayout.getVisibility() != VISIBLE) {

            mUiLayout.setVisibility(View.VISIBLE);

            rotateMain(mUiFab, 270);

            for (int j = 0; j < mFabList.size(); j++) {

                toggleFab(mFabList.get(j), interpolator * 50, fabScale, false);

                interpolator++;
            }

        } else {

            rotateMain(mUiFab, startRotation);

            for (int j = mFabList.size() - 1; j >= 0; j--) {

                toggleFab(mFabList.get(j), interpolator * 50, 0, (j == 0));

                interpolator++;
            }
        }
    }
}
