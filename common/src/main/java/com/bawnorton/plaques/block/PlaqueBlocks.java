package com.bawnorton.plaques.block;

import com.bawnorton.plaques.Plaques;
import com.bawnorton.plaques.platform.RegistryHelper;
import com.bawnorton.plaques.util.PlaqueType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

import java.util.ArrayList;
import java.util.List;

public class PlaqueBlocks {
    private static final List<PlaqueBlock> ALL = new ArrayList<>();

    public static final Block STONE_PLAQUE = register("stone_plaque", new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.STONE.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.STONE));
    public static final Block BLACKSTONE_PLAQUE = register("blackstone_plaque", new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.BLACKSTONE.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.BLACKSTONE));
    public static final Block NETHER_BRICK_PLAQUE = register("nether_brick_plaque", new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.NETHER_BRICKS.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.NETHER_BRICK));
    public static final Block PRISMARINE_PLAQUE = register("prismarine_plaque", new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.PRISMARINE.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.PRISMARINE));
    public static final Block QUARTZ_PLAQUE = register("quartz_plaque", new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.QUARTZ_BLOCK.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.QUARTZ));
    public static final Block SANDSTONE_PLAQUE = register("sandstone_plaque", new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.SANDSTONE.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.SANDSTONE));
    public static final Block RED_SANDSTONE_PLAQUE = register("red_sandstone_plaque", new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.RED_SANDSTONE.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.RED_SANDSTONE));
    public static final Block BRICK_PLAQUE = register("brick_plaque", new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.BRICKS.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.BRICK));
    public static final Block STONE_BRICK_PLAQUE = register("stone_brick_plaque", new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.STONE_BRICKS.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.STONE_BRICK));
    public static final Block MOSSY_STONE_BRICK_PLAQUE = register("mossy_stone_brick_plaque", new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.MOSSY_STONE_BRICKS.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.MOSSY_STONE_BRICK));
    public static final Block CRACKED_STONE_BRICK_PLAQUE = register("cracked_stone_brick_plaque", new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.CRACKED_STONE_BRICKS.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.CRACKED_STONE_BRICK));
    public static final Block ANDESITE_PLAQUE = register("andesite_plaque", new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.ANDESITE.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.ANDESITE));
    public static final Block DIORITE_PLAQUE = register("diorite_plaque", new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.DIORITE.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.DIORITE));
    public static final Block GRANITE_PLAQUE = register("granite_plaque", new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.GRANITE.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.GRANITE));
    public static final Block POLISHED_ANDESITE_PLAQUE = register("polished_andesite_plaque", new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.POLISHED_ANDESITE.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.POLISHED_ANDESITE));
    public static final Block POLISHED_DIORITE_PLAQUE = register("polished_diorite_plaque", new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.POLISHED_DIORITE.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.POLISHED_DIORITE));
    public static final Block POLISHED_GRANITE_PLAQUE = register("polished_granite_plaque", new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.POLISHED_GRANITE.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.POLISHED_GRANITE));
    public static final Block COBBLESTONE_PLAQUE = register("cobblestone_plaque", new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.COBBLESTONE.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.COBBLESTONE));
    public static final Block MOSSY_COBBLESTONE_PLAQUE = register("mossy_cobblestone_plaque", new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.MOSSY_COBBLESTONE.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.MOSSY_COBBLESTONE));
    public static final Block END_STONE_BRICK_PLAQUE = register("end_stone_brick_plaque", new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.END_STONE_BRICKS.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.END_STONE_BRICK));
    public static final Block PURPUR_PLAQUE = register("purpur_plaque", new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.PURPUR_BLOCK.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.PURPUR));


    public static void register() {
        Plaques.LOGGER.debug("Registering Plaque Blocks");
    }

    private static Block register(String id, PlaqueBlock block) {
        ALL.add(block);
        return RegistryHelper.registerBlock(id, () -> block).get();
    }

    public static PlaqueBlock[] getAll() {
        return ALL.toArray(new PlaqueBlock[0]);
    }
}
