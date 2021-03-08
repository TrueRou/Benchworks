package trou.benchworks.work;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Random;

public class WorkAttacking implements IBenchWork {

    @Override
    public boolean work(World world, BlockPos blockPos, DefaultedList<ItemStack> itemStacks) {
        Optional<ItemStack> stack = itemStacks.stream().filter((itemStack -> itemStack.getItem() instanceof SwordItem)).findFirst();
        if (stack.isPresent()) {
            Optional<LivingEntity> entity = world.getEntitiesByClass(LivingEntity.class, new Box(blockPos).expand(1), livingEntity -> true).stream().findFirst();
            if (entity.isPresent() && !(entity.get() instanceof PlayerEntity)) {
                entity.get().damage(DamageSource.MAGIC, ((SwordItem) stack.get().getItem()).getAttackDamage());
                stack.get().damage(1, new Random(), null);
            }
        }
        return false;
    }
}
