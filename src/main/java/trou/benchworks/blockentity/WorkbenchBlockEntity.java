package trou.benchworks.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;
import trou.benchworks.Benchworks;

import java.util.Iterator;

public class WorkbenchBlockEntity extends BlockEntity implements Inventory {
    public DefaultedList<ItemStack> innerItemStacks = DefaultedList.ofSize(9, ItemStack.EMPTY);

    public WorkbenchBlockEntity() {
        super(Benchworks.WORKBENCH_BLOCKENTITY);
    }

    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        Inventories.toTag(tag, innerItemStacks);
        return tag;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        if (innerItemStacks != null && !innerItemStacks.isEmpty()) {
            Inventories.fromTag(tag, innerItemStacks);
        }
    }


    //Code is from CraftingInventory, in order to support hoppers and other pipes to transfer items
    @Override
    public int size() {
        return innerItemStacks.size();
    }

    @Override
    public boolean isEmpty() {
        Iterator var1 = this.innerItemStacks.iterator();

        ItemStack itemStack;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            itemStack = (ItemStack) var1.next();
        } while (itemStack.isEmpty());

        return false;
    }

    @Override
    public ItemStack getStack(int slot) {
        return slot >= this.size() ? ItemStack.EMPTY : this.innerItemStacks.get(slot);
    }

    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.innerItemStacks, slot);
    }

    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(this.innerItemStacks, slot, amount);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.innerItemStacks.set(slot, stack);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        this.innerItemStacks.clear();
    }
}
