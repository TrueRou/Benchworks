package trou.benchworks;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import trou.benchworks.blockentity.WorkbenchBlockEntity;

public class Benchworks implements ModInitializer {
    public static BlockEntityType<WorkbenchBlockEntity> WORKBENCH_BLOCKENTITY;

    @Override
    public void onInitialize() {
        WORKBENCH_BLOCKENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "benchworks:workbench", BlockEntityType.Builder.create(WorkbenchBlockEntity::new, Blocks.CRAFTING_TABLE).build(null));
    }
}
