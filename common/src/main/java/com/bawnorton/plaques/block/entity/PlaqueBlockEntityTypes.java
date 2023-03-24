package com.bawnorton.plaques.block.entity;

import com.bawnorton.plaques.Plaques;
import com.bawnorton.plaques.block.PlaqueBlocks;
import com.bawnorton.plaques.platform.RegistryHelper;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.util.Util;

public class PlaqueBlockEntityTypes {
    public static final BlockEntityType<PlaqueBlockEntity> PLAQUE = register(BlockEntityType.Builder.create(PlaqueBlockEntity::new, PlaqueBlocks.getAll()).build(Util.getChoiceType(TypeReferences.BLOCK_ENTITY, "plaque")));

    public static void register() {
        Plaques.LOGGER.info("Registering block entities");
    }

    private static BlockEntityType<PlaqueBlockEntity> register(BlockEntityType<PlaqueBlockEntity> blockEntity) {
        return RegistryHelper.registerBlockEntityType("plaque", () -> blockEntity).get();
    }
}
