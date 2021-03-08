package trou.benchworks.mixin.mixins;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import trou.benchworks.blockentity.WorkbenchBlockEntity;
import trou.benchworks.work.BenchWorks;
import trou.benchworks.work.IBenchWork;
import trou.benchworks.mixin.interfaces.ICraftingScreen;

import java.util.Random;

@Mixin(CraftingTableBlock.class)
public class CraftingTableMixin extends Block implements BlockEntityProvider {

    private static final BooleanProperty TRIGGERED = BooleanProperty.of("triggered");

    public CraftingTableMixin(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(TRIGGERED);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void setDefaultState(CallbackInfo ci) {
        this.setDefaultState(this.stateManager.getDefaultState().with(TRIGGERED, false));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new WorkbenchBlockEntity();
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        super.afterBreak(world, player, pos, state, blockEntity, stack);
        if (blockEntity != null) {
            for (ItemStack innerItemStack : ((WorkbenchBlockEntity) blockEntity).innerItemStacks) {
                world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), innerItemStack));
            }
        }
    }

    //We need a redstone pulse to begin the effect.
    //These code is from the solution of Dispenser.
    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        super.neighborUpdate(state, world, pos, block, fromPos, notify);
        boolean bl = world.isReceivingRedstonePower(pos);
        boolean bl2 = state.get(TRIGGERED);
        if (bl && !bl2) {
            world.getBlockTickScheduler().schedule(pos, this, 2);
            world.setBlockState(pos, state.with(TRIGGERED, true), 4);
        } else if (!bl && bl2) {
            world.setBlockState(pos, state.with(TRIGGERED, false), 4);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (world.isReceivingRedstonePower(pos) && blockEntity != null) {
            DefaultedList<ItemStack> itemStacks = ((WorkbenchBlockEntity) blockEntity).innerItemStacks;
            for (BenchWorks value : BenchWorks.values()) {
                IBenchWork benchWork = value.benchWork;
                if (benchWork.work(world, pos, itemStacks)) return;
            }
        }
    }

    /**
     * Filled the crafting grid with the data in BlockEntity.
     *
     * @author TuRou
     * @reason Handle Crafting Progress
     */
    @Overwrite
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity == null) {
            blockEntity = new WorkbenchBlockEntity();
            world.setBlockEntity(pos, blockEntity);
        }
        DefaultedList<ItemStack> itemStacks = ((WorkbenchBlockEntity) blockEntity).innerItemStacks;
        return new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> {
            CraftingScreenHandler screenHandler = new CraftingScreenHandler(i, playerInventory, ScreenHandlerContext.create(world, pos));
            if (!itemStacks.isEmpty()) ((ICraftingScreen) screenHandler).setItems(itemStacks);
            return screenHandler;
        }, new TranslatableText("container.crafting"));
    }
}
