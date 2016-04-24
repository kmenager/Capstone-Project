package io.github.kmenager.getmesomefeed.injection.component;


import dagger.Component;
import io.github.kmenager.getmesomefeed.injection.PerFragment;
import io.github.kmenager.getmesomefeed.injection.module.FragmentModule;
import io.github.kmenager.getmesomefeed.ui.menu.MenuFragment;

@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    void inject(MenuFragment menuFragment);
}
