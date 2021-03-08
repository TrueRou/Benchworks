package trou.benchworks.work;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Random;

public class WorkShearing implements IBenchWork {
    @Override
    public boolean work(World world, BlockPos blockPos, DefaultedList<ItemStack> itemStacks) {
        Optional<ItemStack> stack = itemStacks.stream().filter((itemStack -> itemStack.getItem() instanceof ShearsItem)).findFirst();
        if (stack.isPresent()) {
            Optional<SheepEntity> sheepEntity = world.getEntitiesByType(EntityType.SHEEP, new Box(blockPos).expand(1), SheepEntity::isShearable).stream().findFirst();
            if (sheepEntity.isPresent()) {
                sheepEntity.get().sheared(SoundCategory.BLOCKS);
                stack.get().damage(1, new Random(), null);
            }
            return true;
        }
        return false;
    }
}
