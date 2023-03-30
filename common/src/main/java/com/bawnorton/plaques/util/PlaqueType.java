package com.bawnorton.plaques.util;

import com.bawnorton.plaques.Plaques;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;

public enum PlaqueType {
    ANDESITE(Blocks.ANDESITE),
    BLACKSTONE(Blocks.BLACKSTONE),
    BRICKS(Blocks.BRICKS),
    COBBLESTONE(Blocks.COBBLESTONE),
    CRACKED_STONE_BRICKS(Blocks.CRACKED_STONE_BRICKS),
    DIORITE(Blocks.DIORITE),
    END_STONE_BRICKS(Blocks.END_STONE_BRICKS),
    GRANITE(Blocks.GRANITE),
    MOSSY_COBBLESTONE(Blocks.MOSSY_COBBLESTONE),
    MOSSY_STONE_BRICKS(Blocks.MOSSY_STONE_BRICKS),
    NETHER_BRICKS(Blocks.NETHER_BRICKS),
    POLISHED_ANDESITE(Blocks.POLISHED_ANDESITE),
    POLISHED_DIORITE(Blocks.POLISHED_DIORITE),
    POLISHED_GRANITE(Blocks.POLISHED_GRANITE),
    PRISMARINE_BRICKS(Blocks.PRISMARINE_BRICKS),
    PURPUR_BLOCK(Blocks.PURPUR_BLOCK),
    QUARTZ_BLOCK(Blocks.QUARTZ_BLOCK),
    RED_SANDSTONE(Blocks.RED_SANDSTONE),
    SANDSTONE(Blocks.SANDSTONE),
    STONE(Blocks.STONE),
    STONE_BRICKS(Blocks.STONE_BRICKS);

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

    public Identifier getTexture() {
        return Plaques.id("textures/block/" + getName() + ".png");
    }
}
