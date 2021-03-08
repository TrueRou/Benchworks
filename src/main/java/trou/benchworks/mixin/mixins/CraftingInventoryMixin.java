package trou.benchworks.mixin.mixins;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import trou.benchworks.mixin.interfaces.ICraftingInventory;

@Mixin(CraftingInventory.class)
public class CraftingInventoryMixin implements ICraftingInventory {
    @Shadow
    @Final
    DefaultedList<ItemStack> stacks;

    @Override
    public DefaultedList<ItemStack> getItems() {
        return stacks;
    }
}
