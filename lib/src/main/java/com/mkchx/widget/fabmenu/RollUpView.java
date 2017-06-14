package com.mkchx.widget.fabmenu;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Efthimis Charitonidis
 */
public class RollUpView extends RelativeLayout implements View.OnClickListener {

    private LinearLayout uiLinearLayout;
    private FloatingActionButton uiFab;

    private List<View> mFabList = new ArrayList<>();
    private List<View> mTextList = new ArrayList<>();

    private IViewClick iViewClickListener;

    private float fabScale, startRotation;
    private int defSize, defElevation, fabItemMarginTop, fabItemMarginLeft, lrPadding, tbPadding;

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

        fabItemMarginLeft = (int) getResources().getDimension(R.dimen.fab_items_margin_left);

        lrPadding = (int) getResources().getDimension(R.dimen.text_lr_padding);
        tbPadding = (int) getResources().getDimension(R.dimen.text_tb_padding);

        fabScale = 1f;

        LayoutInflater.from(context).inflate(R.layout.rollup_layout, this);
        uiLinearLayout = findViewById(R.id.custom_content);
        uiLinearLayout.setVisibility(GONE);

        uiFab = findViewById(R.id.main_fab);
        uiFab.setOnClickListener(this);

        setViewElevation(uiFab);
    }

    /**
     * Set the main Fab icon
     *
     * @param drawable The icon of the fab
     */
    public void setMainFabIcon(Drawable drawable) {
        uiFab.setImageDrawable(drawable);
    }

    /**
     * Set the main Fab start rotation degrees
     *
     * @param value The degrees of rotation
     */
    public void setMainFabRotation(float value) {
        startRotation = value;
        uiFab.setRotation(value);
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
        childParams.gravity = Gravity.END;

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

        uiLinearLayout.addView(childWrapper);
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
        fabParams.setMargins(fabItemMarginLeft, 0, 0, 0);
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
        view.setAlpha(val);
    }

    private void setScaleXY(View view, float val) {
        view.setScaleX(val);
        view.setScaleY(val);
    }

    /**
     * Anim functions
     */
    private void toggleText(View view, int delay, int alpha) {

        ViewCompat.animate(view)
                .setStartDelay(delay)
                .setInterpolator(new FastOutSlowInInterpolator())
                .alpha(alpha)
                .start();
    }

    private void rotateMain(View view, float degrees) {

        ViewCompat.animate(view)
                .rotation(degrees)
                .setInterpolator(new FastOutSlowInInterpolator())
                .start();
    }

    private void animY(View view, float translationY) {

        ViewCompat.animate(view)
                .setInterpolator(new FastOutSlowInInterpolator())
                .translationY(translationY)
                .setDuration(500)
                .start();
    }

    private void toggleFab(View view, int delay, float scale, final boolean last) {

        ViewCompat.animate(view)
                .setStartDelay(delay)
                .scaleX(scale)
                .scaleY(scale)
                .setInterpolator(new FastOutSlowInInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        if (last) {
                            uiLinearLayout.setVisibility(View.GONE);
                        }
                    }
                })
                .start();
    }

    @Override
    public void onClick(View v) {

        int interpolator = 0;

        if (uiLinearLayout.getVisibility() != VISIBLE) {

            uiLinearLayout.setVisibility(View.VISIBLE);

            // animY(uiLinearLayout, 0);
            rotateMain(uiFab, 270);

            for (int j = 0; j < mFabList.size(); j++) {

                toggleText(mTextList.get(j), interpolator * 50, 1);
                toggleFab(mFabList.get(j), interpolator * 50, fabScale, false);

                interpolator++;
            }

        } else {

            // animY(uiLinearLayout, translationY);
            rotateMain(uiFab, startRotation);

            for (int j = mFabList.size() - 1; j >= 0; j--) {

                toggleText(mTextList.get(j), interpolator * 50, 0);
                toggleFab(mFabList.get(j), interpolator * 50, 0, (j == 0));

                interpolator++;
            }
        }
    }
}
