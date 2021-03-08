package trou.benchworks.work;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Optional;

public class WorkPlacing implements IBenchWork {
    @Override
    public boolean work(World world, BlockPos blockPos, DefaultedList<ItemStack> itemStacks) {
        Optional<ItemStack> stack = itemStacks.stream().filter((itemStack -> itemStack.getItem() instanceof BlockItem)).findFirst();
        if (stack.isPresent()) {
            BlockState blockState = ((BlockItem) stack.get().getItem()).getBlock().getDefaultState();
            blockPos = blockPos.offset(Direction.UP, 1);
            if (blockState.canPlaceAt(world, blockPos) && world.getBlockState(blockPos).getBlock() != blockState.getBlock() && world.setBlockState(blockPos, blockState))
                stack.get().decrement(1);
            return true;
        }
        return false;
    }
}
