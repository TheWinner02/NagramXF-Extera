package tw.nekomimi.nekogram.config.cell;

import static org.telegram.messenger.LocaleController.getString;

import androidx.recyclerview.widget.RecyclerView;

import tw.nekomimi.nekogram.config.CellGroup;
import tw.nekomimi.nekogram.config.ConfigItem;
import tw.nekomimi.nekogram.settings.BaseNekoXSettingsActivity;
import tw.nekomimi.nekogram.ui.cells.M3ConnectedButtonGroupCell;

public class ConfigCellConnectedButtonGroup extends AbstractConfigCell implements WithBindConfig, WithKey {
    private final ConfigItem bindConfig;
    private final String[] selectList;
    private final String title;
    private final String key;

    public ConfigCellConnectedButtonGroup(String key, ConfigItem bindConfig, String[] selectList) {
        this.bindConfig = bindConfig;
        this.key = key != null ? key : bindConfig.getKey();
        this.selectList = selectList;
        this.title = getString(this.key);
    }

    @Override
    public int getType() {
        return CellGroup.ITEM_TYPE_CONNECTED_BUTTON_GROUP;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder) {
        if (!(holder.itemView instanceof M3ConnectedButtonGroupCell cell)) {
            return;
        }
        cell.setTextAndItems(title, selectList, bindConfig.Int(), cellGroup.thisFragment.getResourceProvider(), index -> {
            bindConfig.setConfigInt(index);
            if (cellGroup.listAdapter != null) {
                int rowIndex = cellGroup.rows.indexOf(ConfigCellConnectedButtonGroup.this);
                if (rowIndex >= 0) {
                    int adapterIndex = rowIndex;
                    if (xyz.nextalone.nagram.ui.UIStyleEngine.isMaterial3Expressive()
                            && !cellGroup.rows.isEmpty()
                            && cellGroup.rows.get(0) instanceof ConfigCellHeader) {
                        adapterIndex--;
                    }
                    if (adapterIndex >= 0) {
                        cellGroup.listAdapter.notifyItemChanged(adapterIndex);
                    }
                }
            }
            if (cellGroup.thisFragment instanceof BaseNekoXSettingsActivity activity && activity.getParentLayout() != null) {
                activity.getParentLayout().rebuildAllFragmentViews(false, false);
            }
            cellGroup.runCallback(bindConfig.getKey(), index);
        });
    }

    @Override
    public ConfigItem getBindConfig() {
        return bindConfig;
    }

    @Override
    public String getKey() {
        return key;
    }
}
