package com.exteragram.messenger.icons.ui;

import android.content.Context;
import android.view.View;

import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalRecyclerView;

import java.util.ArrayList;
import xyz.nextalone.nagram.NaConfig;

public class IconPacksActivity extends BaseFragment {

    @Override
    public View createView(Context context) {
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setTitle(LocaleController.getString(R.string.IconReplacements));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });

        this.fragmentView = new UniversalRecyclerView(this, new Utilities.Callback2<ArrayList<UItem>, UniversalAdapter>() {
            @Override
            public void run(ArrayList<UItem> items, UniversalAdapter adapter) {
                int currentType = NaConfig.INSTANCE.getIconReplacements().Int();

                items.add(UItem.asRadio(1, LocaleController.getString(R.string.Default))
                        .setChecked(currentType == 0));
                items.add(UItem.asRadio(2, "Solar Icons di @Design480")
                        .setChecked(currentType == 1));
                items.add(UItem.asRadio(3, "Remix Icons di @Design480")
                        .setChecked(currentType == 2));
            }
        }, new Utilities.Callback5<UItem, View, Integer, Float, Float>() {
            @Override
            public void run(UItem item, View view, Integer position, Float x, Float y) {
                int newType = 0;
                if (item.id == 1) {
                    newType = 0;
                } else if (item.id == 2) {
                    newType = 1;
                } else if (item.id == 3) {
                    newType = 2;
                }
                NaConfig.INSTANCE.getIconReplacements().setConfigInt(newType);
                getNotificationCenter().postNotificationName(NotificationCenter.reloadInterface);
                finishFragment();
            }
        }, null);

        return this.fragmentView;
    }
}
