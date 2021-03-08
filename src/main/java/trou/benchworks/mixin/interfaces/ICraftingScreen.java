package trou.benchworks.mixin.interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public interface ICraftingScreen {
    void setItems(DefaultedList<ItemStack> itemStacks);

    DefaultedList<ItemStack> getItems();
}
