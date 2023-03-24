package com.bawnorton.plaques.item;

import com.bawnorton.plaques.Plaques;
import com.bawnorton.plaques.block.PlaqueBlocks;
import com.bawnorton.plaques.platform.RegistryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public class PlaqueItems {
    public static final Item STONE_PLAQUE = register("stone_plaque", new PlaqueItem((new Item.Settings()).maxCount(16).group(ItemGroup.DECORATIONS), PlaqueBlocks.STONE_PLAQUE));
    public static final Item BLACKSTONE_PLAQUE = register("blackstone_plaque", new PlaqueItem((new Item.Settings()).maxCount(16).group(ItemGroup.DECORATIONS), PlaqueBlocks.BLACKSTONE_PLAQUE));
    public static final Item Nether_BRICK_PLAQUE = register("nether_brick_plaque", new PlaqueItem((new Item.Settings()).maxCount(16).group(ItemGroup.DECORATIONS), PlaqueBlocks.NETHER_BRICK_PLAQUE));
    public static final Item PRISMARINE_PLAQUE = register("prismarine_plaque", new PlaqueItem((new Item.Settings()).maxCount(16).group(ItemGroup.DECORATIONS), PlaqueBlocks.PRISMARINE_PLAQUE));
    public static final Item QUARTZ_PLAQUE = register("quartz_plaque", new PlaqueItem((new Item.Settings()).maxCount(16).group(ItemGroup.DECORATIONS), PlaqueBlocks.QUARTZ_PLAQUE));
    public static final Item SANDSTONE_PLAQUE = register("sandstone_plaque", new PlaqueItem((new Item.Settings()).maxCount(16).group(ItemGroup.DECORATIONS), PlaqueBlocks.SANDSTONE_PLAQUE));
    public static final Item RED_SANDSTONE_PLAQUE = register("red_sandstone_plaque", new PlaqueItem((new Item.Settings()).maxCount(16).group(ItemGroup.DECORATIONS), PlaqueBlocks.RED_SANDSTONE_PLAQUE));
    public static final Item BRICK_PLAQUE = register("brick_plaque", new PlaqueItem((new Item.Settings()).maxCount(16).group(ItemGroup.DECORATIONS), PlaqueBlocks.BRICK_PLAQUE));
    public static final Item STONE_BRICK_PLAQUE = register("stone_brick_plaque", new PlaqueItem((new Item.Settings()).maxCount(16).group(ItemGroup.DECORATIONS), PlaqueBlocks.STONE_BRICK_PLAQUE));
    public static final Item MOSSY_STONE_BRICK_PLAQUE = register("mossy_stone_brick_plaque", new PlaqueItem((new Item.Settings()).maxCount(16).group(ItemGroup.DECORATIONS), PlaqueBlocks.MOSSY_STONE_BRICK_PLAQUE));
    public static final Item CRACKED_STONE_BRICK_PLAQUE = register("cracked_stone_brick_plaque", new PlaqueItem((new Item.Settings()).maxCount(16).group(ItemGroup.DECORATIONS), PlaqueBlocks.CRACKED_STONE_BRICK_PLAQUE));
    public static final Item ANDESITE_PLAQUE = register("andesite_plaque", new PlaqueItem((new Item.Settings()).maxCount(16).group(ItemGroup.DECORATIONS), PlaqueBlocks.ANDESITE_PLAQUE));
    public static final Item DIORITE_PLAQUE = register("diorite_plaque", new PlaqueItem((new Item.Settings()).maxCount(16).group(ItemGroup.DECORATIONS), PlaqueBlocks.DIORITE_PLAQUE));
    public static final Item GRANITE_PLAQUE = register("granite_plaque", new PlaqueItem((new Item.Settings()).maxCount(16).group(ItemGroup.DECORATIONS), PlaqueBlocks.GRANITE_PLAQUE));
    public static final Item POLISHED_ANDESITE_PLAQUE = register("polished_andesite_plaque", new PlaqueItem((new Item.Settings()).maxCount(16).group(ItemGroup.DECORATIONS), PlaqueBlocks.POLISHED_ANDESITE_PLAQUE));
    public static final Item POLISHED_DIORITE_PLAQUE = register("polished_diorite_plaque", new PlaqueItem((new Item.Settings()).maxCount(16).group(ItemGroup.DECORATIONS), PlaqueBlocks.POLISHED_DIORITE_PLAQUE));
    public static final Item POLISHED_GRANITE_PLAQUE = register("polished_granite_plaque", new PlaqueItem((new Item.Settings()).maxCount(16).group(ItemGroup.DECORATIONS), PlaqueBlocks.POLISHED_GRANITE_PLAQUE));
    public static final Item COBBLESTONE_PLAQUE = register("cobblestone_plaque", new PlaqueItem((new Item.Settings()).maxCount(16).group(ItemGroup.DECORATIONS), PlaqueBlocks.COBBLESTONE_PLAQUE));
    public static final Item MOSSY_COBBLESTONE_PLAQUE = register("mossy_cobblestone_plaque", new PlaqueItem((new Item.Settings()).maxCount(16).group(ItemGroup.DECORATIONS), PlaqueBlocks.MOSSY_COBBLESTONE_PLAQUE));
    public static final Item END_STONE_BRICK_PLAQUE = register("end_stone_brick_plaque", new PlaqueItem((new Item.Settings()).maxCount(16).group(ItemGroup.DECORATIONS), PlaqueBlocks.END_STONE_BRICK_PLAQUE));
    public static final Item PURPUR_PLAQUE = register("purpur_plaque", new PlaqueItem((new Item.Settings()).maxCount(16).group(ItemGroup.DECORATIONS), PlaqueBlocks.PURPUR_PLAQUE));

    public static void register() {
        Plaques.LOGGER.info("Registering items");
    }

    private static Item register(String id, Item item) {
        return RegistryHelper.registerItem(id, () -> item).get();
    }
}
