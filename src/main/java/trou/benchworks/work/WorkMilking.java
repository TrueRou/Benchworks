package trou.benchworks.work;

import net.minecraft.entity.passive.CowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class WorkMilking implements IBenchWork {
    @Override
    public boolean work(World world, BlockPos blockPos, DefaultedList<ItemStack> itemStacks) {
        for (int i = 0; i < itemStacks.size(); i++) {
            if (itemStacks.get(i).getItem() == Items.BUCKET) {
                if (world.getEntitiesByClass(CowEntity.class, new Box(blockPos).expand(2), cowEntity -> !cowEntity.isBaby()).stream().findFirst().isPresent()) {
                    for (int j = 0; j < itemStacks.size(); j++) {
                        if (itemStacks.get(j).isEmpty()) {
                            itemStacks.get(i).decrement(1);
                            itemStacks.set(j, new ItemStack(Items.MILK_BUCKET));
                            return true;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }
}
