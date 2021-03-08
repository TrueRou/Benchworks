package trou.benchworks.work;

import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class WorkBreeding implements IBenchWork {

    @Override
    public boolean work(World world, BlockPos blockPos, DefaultedList<ItemStack> itemStacks) {
        Optional<ItemStack> stack = itemStacks.stream().findFirst();
        List<AnimalEntity> entity = world.getEntitiesByClass(AnimalEntity.class, new Box(blockPos).expand(2), livingEntity -> true);
        if (stack.isPresent()) {
            for (AnimalEntity animalEntity : entity) {
                if (animalEntity.isBreedingItem(stack.get()) && animalEntity.canEat() && animalEntity.getBreedingAge() == 0) {
                    animalEntity.lovePlayer(null);
                    stack.get().decrement(1);
                    return true;
                }
            }
        }
        return false;
    }
}
