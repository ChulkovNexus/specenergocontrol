package com.specenergocontrol.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.rey.material.widget.ProgressView;
import com.specenergocontrol.comands.AsyncTaskExecutor;

/**
 * Created by Комп on 30.06.2015.
 */
public class AsyncFragment extends Fragment {


    private static final long PROGRESS_ANIMATION_DELAY = 200;
    protected AsyncTaskExecutor asyncTaskExecutor;
    private boolean interruptByUnauthorized = false;
    protected View content;
    protected ProgressView progressBar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        asyncTaskExecutor = new AsyncTaskExecutor();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        asyncTaskExecutor.attach(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (interruptByUnauthorized) {
            interruptByUnauthorized = false;
            showProgress(true, null, false);
            asyncTaskExecutor.executeLastRequest();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        asyncTaskExecutor.detach();
    }

    public void setInterruptByUnuathorize() {
        asyncTaskExecutor.setSessionDied(true);
        interruptByUnauthorized = true;
    }

    protected void showProgress(boolean progress) {
        if (content==null || progressBar == null)
            return;

        if (progress) {
            content.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.start();
        } else {
            content.setVisibility(View.VISIBLE);
            progressBar.stop();
            progressBar.setVisibility(View.GONE);
        }
    }

    protected void showProgress(boolean progress, final Runnable afterProgressCallback, final boolean showContent) {
        if (content==null || progressBar == null)
            return;

        if (progress) {
            content.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.start();
        } else {
            progressBar.stop();
            progressBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (showContent){
                        AlphaAnimation anim = new AlphaAnimation(0f, 1f);
                        anim.setDuration(PROGRESS_ANIMATION_DELAY);
                        content.startAnimation(anim);
                        content.setVisibility(View.VISIBLE);
                    }
                    if (afterProgressCallback!=null)
                        afterProgressCallback.run();

                    progressBar.setVisibility(View.GONE);
                }
            }, PROGRESS_ANIMATION_DELAY);
        }
    }

    public void reloadFromBase () {

    }
}
