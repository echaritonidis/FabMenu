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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mkchx.widget.fabmenu.interfaces.IViewClick;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Efthimis Charitonidis
 */
public class RollUpView extends RelativeLayout implements View.OnClickListener {

    private LinearLayout mUiLinearLayout;
    private FloatingActionButton mUiFab;

    private List<View> mFabList = new ArrayList<>();
    private List<View> mTextList = new ArrayList<>();

    private IViewClick iViewClickListener;

    private float fabScale, translationY, startRotation;
    private int defSize, defElevation, fabItemMarginBT, fabItemMarginLR, lrPadding, tbPadding;

    public RollUpView(Context context) {
        this(context, null);
    }

    public RollUpView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RollUpView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        defSize = (int) getResources().getDimension(R.dimen.fab_size_mini);
        defElevation = (int) getResources().getDimension(R.dimen.fab_elevation);

        fabItemMarginBT = (int) getResources().getDimension(R.dimen.fab_items_margin_bt);
        fabItemMarginLR = (int) getResources().getDimension(R.dimen.fab_items_margin_lr);

        lrPadding = (int) getResources().getDimension(R.dimen.text_lr_padding);
        tbPadding = (int) getResources().getDimension(R.dimen.text_tb_padding);

        translationY = getResources().getDimension(R.dimen.container_y);

        fabScale = 1f;

        LayoutInflater.from(context).inflate(R.layout.rollup_layout, this);
        mUiLinearLayout = (LinearLayout) findViewById(R.id.custom_content);
        mUiLinearLayout.setVisibility(GONE);
        ViewCompat.setTranslationY(mUiLinearLayout, translationY);

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
     * @param label    The text associated with the fab
     */
    public void addChildView(Drawable drawable, String label) {

        LinearLayout childWrapper = new LinearLayout(getContext());
        childWrapper.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        childParams.gravity = Gravity.RIGHT;

        childWrapper.setLayoutParams(childParams);
        childWrapper.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (iViewClickListener != null) {
                    int position = ((ViewGroup) v.getParent()).indexOfChild(v);
                    iViewClickListener.onItemClick(v, position);
                }
            }
        });

        childWrapper.addView(createTextView(label));
        childWrapper.addView(createFab(drawable));

        mUiLinearLayout.addView(childWrapper);
    }

    private TextView createTextView(String label) {

        TextView textView = new TextView(getContext());

        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.CENTER_VERTICAL;
        textView.setLayoutParams(textParams);

        textView.setText(label);
        textView.setPadding(lrPadding, tbPadding, lrPadding, tbPadding);

        setViewElevation(textView);
        setViewAlpha(textView, 0f);

        mTextList.add(textView);

        return textView;
    }

    private FloatingActionButton createFab(Drawable drawable) {

        FloatingActionButton fabView = (FloatingActionButton) LayoutInflater.from(getContext()).inflate(R.layout.fab_mini, null);

        LinearLayout.LayoutParams fabParams = new LinearLayout.LayoutParams(defSize, defSize);
        fabParams.gravity = Gravity.CENTER_VERTICAL;
        fabParams.setMargins(fabItemMarginLR, fabItemMarginBT, fabItemMarginLR, fabItemMarginBT);
        fabView.setLayoutParams(fabParams);

        fabView.setImageDrawable(drawable);
        setScaleXY(fabView, 0f);

        mFabList.add(fabView);

        return fabView;
    }

    private void setViewElevation(View view) {
        ViewCompat.setElevation(view, defElevation);
    }

    private void setViewAlpha(View view, float val) {
        ViewCompat.setAlpha(view, val);
    }

    private void setScaleXY(View view, float val) {
        ViewCompat.setScaleX(view, val);
        ViewCompat.setScaleY(view, val);
    }

    /**
     * Anim functions
     */
    private void toggleText(View view, int delay, int alpha) {

        if (Build.VERSION.SDK_INT >= 11) {
            ViewCompat.animate(view)
                    .setStartDelay(delay)
                    .setInterpolator(new FastOutSlowInInterpolator())
                    .alpha(alpha)
                    .start();
        } else {
            AnimatorSet mSet = new AnimatorSet();

            mSet.playTogether(
                    ObjectAnimator.ofFloat(view, "alpha", alpha)
            );
            mSet.setStartDelay(delay);
            mSet.setInterpolator(new FastOutSlowInInterpolator());
            mSet.start();
        }

    }

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

    private void animY(View view, float translationY) {

        if (Build.VERSION.SDK_INT >= 11) {
            ViewCompat.animate(view)
                    .setInterpolator(new FastOutSlowInInterpolator())
                    .translationY(translationY)
                    .setDuration(500)
                    .start();
        } else {
            AnimatorSet mSet = new AnimatorSet();

            mSet.playTogether(
                    ObjectAnimator.ofFloat(view, "translationY", translationY)
            );
            mSet.setInterpolator(new FastOutSlowInInterpolator());
            mSet.setDuration(500);
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
                                mUiLinearLayout.setVisibility(View.GONE);
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
                        mUiLinearLayout.setVisibility(View.GONE);
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

        if (mUiLinearLayout.getVisibility() != VISIBLE) {

            mUiLinearLayout.setVisibility(View.VISIBLE);

            animY(mUiLinearLayout, 0);
            rotateMain(mUiFab, 270);

            for (int j = 0; j < mFabList.size(); j++) {

                toggleText(mTextList.get(j), interpolator * 50, 1);
                toggleFab(mFabList.get(j), interpolator * 50, fabScale, false);

                interpolator++;
            }

        } else {

            animY(mUiLinearLayout, translationY);
            rotateMain(mUiFab, startRotation);

            for (int j = mFabList.size() - 1; j >= 0; j--) {

                toggleText(mTextList.get(j), interpolator * 50, 0);
                toggleFab(mFabList.get(j), interpolator * 50, 0, (j == 0));

                interpolator++;
            }
        }
    }
}
