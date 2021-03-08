package trou.benchworks.work;

import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IBenchWork {
    boolean work(World world, BlockPos blockPos, DefaultedList<ItemStack> itemStacks);
}
