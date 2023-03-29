package com.bawnorton.plaques.util;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public enum PlaqueType {
    STONE(Blocks.STONE), BLACKSTONE(Blocks.BLACKSTONE), NETHER_BRICK(Blocks.NETHER_BRICKS),
    PRISMARINE(Blocks.PRISMARINE), QUARTZ(Blocks.QUARTZ_BLOCK), SANDSTONE(Blocks.SANDSTONE),
    RED_SANDSTONE(Blocks.RED_SANDSTONE), BRICK(Blocks.BRICKS), STONE_BRICK(Blocks.STONE_BRICKS),
    MOSSY_STONE_BRICK(Blocks.MOSSY_STONE_BRICKS), CRACKED_STONE_BRICK(Blocks.CRACKED_STONE_BRICKS),
    ANDESITE(Blocks.ANDESITE), DIORITE(Blocks.DIORITE), GRANITE(Blocks.GRANITE),
    POLISHED_ANDESITE(Blocks.POLISHED_ANDESITE), POLISHED_DIORITE(Blocks.POLISHED_DIORITE),
    POLISHED_GRANITE(Blocks.POLISHED_GRANITE), COBBLESTONE(Blocks.COBBLESTONE),
    MOSSY_COBBLESTONE(Blocks.MOSSY_COBBLESTONE), END_STONE_BRICK(Blocks.END_STONE_BRICKS),
    PURPUR(Blocks.PURPUR_BLOCK);

    private final Block block;
    
    PlaqueType(Block block) {
        this.block = block;
    }
    
    public String getName() {
        return name().toLowerCase();
    }

    public int getColourMap() {
        return block.getDefaultMapColor().color;
    }
}
