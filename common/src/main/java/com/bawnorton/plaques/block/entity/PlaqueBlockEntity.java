package com.bawnorton.plaques.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;

public class PlaqueBlockEntity extends SignBlockEntity {
    public PlaqueBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public boolean setAccent(Item item) {
        return false;
    }
}
