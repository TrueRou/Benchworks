package trou.benchworks.mixin.mixins;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import trou.benchworks.blockentity.WorkbenchBlockEntity;
import trou.benchworks.mixin.interfaces.ICraftingInventory;
import trou.benchworks.mixin.interfaces.ICraftingScreen;

@Mixin(CraftingScreenHandler.class)
public abstract class CraftingScreenMixin extends AbstractRecipeScreenHandler<CraftingInventory> implements ICraftingScreen {
    @Shadow
    @Final
    private CraftingInventory input;
    @Shadow
    @Final
    private ScreenHandlerContext context;

    public CraftingScreenMixin(ScreenHandlerType<?> screenHandlerType, int i) {
        super(screenHandlerType, i);
    }

    @Override
    public void setItems(DefaultedList<ItemStack> itemStacks) {
        if (itemStacks != null && !itemStacks.isEmpty()) {
            for (int i = 0; i < itemStacks.size(); i++) {
                ((ICraftingInventory) input).getItems().set(i, itemStacks.get(i));
            }
            onContentChanged(null);
        }
    }


    /**
     * Filled the BlockEntity with the data in the crafting grid.
     *
     * @author TuRou
     * @reason Handle Crafting Progress
     */
    @Overwrite
    public void close(PlayerEntity player) {
        super.close(player);
        context.run((world, pos) -> {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity != null) {
                ((WorkbenchBlockEntity) blockEntity).innerItemStacks = getItems();
            }
        });
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return ((ICraftingInventory) input).getItems();
    }
}
