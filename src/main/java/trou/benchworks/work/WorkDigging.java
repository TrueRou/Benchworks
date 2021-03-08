package trou.benchworks.work;

import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Random;

public class WorkDigging implements IBenchWork {
    @Override
    public boolean work(World world, BlockPos blockPos, DefaultedList<ItemStack> itemStacks) {
        Optional<ItemStack> stack = itemStacks.stream().filter((itemStack -> itemStack.getItem() instanceof PickaxeItem)).findFirst();
        if (stack.isPresent()) {
            if (world.breakBlock(blockPos.offset(Direction.UP, 1), true)) stack.get().damage(1, new Random(), null);
        }
        return false;
    }
}
