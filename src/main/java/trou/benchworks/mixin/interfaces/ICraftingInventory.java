package trou.benchworks.mixin.interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public interface ICraftingInventory {
    DefaultedList<ItemStack> getItems();
}
