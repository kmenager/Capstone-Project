package io.github.kmenager.getmesomefeed.ui.base;

import android.support.v4.app.Fragment;

import io.github.kmenager.getmesomefeed.FeedApplication;
import io.github.kmenager.getmesomefeed.injection.component.DaggerFragmentComponent;
import io.github.kmenager.getmesomefeed.injection.component.FragmentComponent;
import io.github.kmenager.getmesomefeed.injection.module.FragmentModule;


public class BaseFragment extends Fragment {

    private FragmentComponent mFragmentComponent;

    public FragmentComponent getFragmentComponent() {
        if (mFragmentComponent == null) {
            mFragmentComponent = DaggerFragmentComponent.builder()
                    .applicationComponent(FeedApplication.get(getActivity()).getComponent())
                    .fragmentModule(new FragmentModule(getActivity()))
                    .build();
        }
        return mFragmentComponent;
    }
}
