package com.bawnorton.plaques;

import com.bawnorton.plaques.block.PlaqueBlock;
import com.bawnorton.plaques.block.entity.PlaqueBlockEntity;
import com.bawnorton.plaques.item.PlaqueItem;
import com.bawnorton.plaques.networking.Networking;
import com.bawnorton.plaques.util.PlaqueType;
import com.google.common.base.Suppliers;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class Plaques {
    public static final String MOD_ID = "plaques";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Supplier<RegistrarManager> REGISTRIES = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

    public static final Registrar<Block> BLOCK_REGISTRAR = REGISTRIES.get().get(RegistryKeys.BLOCK);
    public static final RegistrySupplier<Block> ANDESITE_PLAQUE = BLOCK_REGISTRAR.register(Plaques.id("andesite_plaque"), () -> new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.ANDESITE.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.ANDESITE));
    public static final RegistrySupplier<Block> BLACKSTONE_PLAQUE = BLOCK_REGISTRAR.register(Plaques.id("blackstone_plaque"), () -> new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.BLACKSTONE.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.BLACKSTONE));
    public static final RegistrySupplier<Block> BRICKS_PLAQUE = BLOCK_REGISTRAR.register(Plaques.id("bricks_plaque"), () -> new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.BRICKS.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.BRICKS));
    public static final RegistrySupplier<Block> COBBLESTONE_PLAQUE = BLOCK_REGISTRAR.register(Plaques.id("cobblestone_plaque"), () -> new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.COBBLESTONE.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.COBBLESTONE));
    public static final RegistrySupplier<Block> CRACKED_STONE_BRICKS_PLAQUE = BLOCK_REGISTRAR.register(Plaques.id("cracked_stone_bricks_plaque"), () -> new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.CRACKED_STONE_BRICKS.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.CRACKED_STONE_BRICKS));
    public static final RegistrySupplier<Block> DIORITE_PLAQUE = BLOCK_REGISTRAR.register(Plaques.id("diorite_plaque"), () -> new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.DIORITE.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.DIORITE));
    public static final RegistrySupplier<Block> END_STONE_BRICKS_PLAQUE = BLOCK_REGISTRAR.register(Plaques.id("end_stone_bricks_plaque"), () -> new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.END_STONE_BRICKS.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.END_STONE_BRICKS));
    public static final RegistrySupplier<Block> GRANITE_PLAQUE = BLOCK_REGISTRAR.register(Plaques.id("granite_plaque"), () -> new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.GRANITE.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.GRANITE));
    public static final RegistrySupplier<Block> MOSSY_COBBLESTONE_PLAQUE = BLOCK_REGISTRAR.register(Plaques.id("mossy_cobblestone_plaque"), () -> new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.MOSSY_COBBLESTONE.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.MOSSY_COBBLESTONE));
    public static final RegistrySupplier<Block> MOSSY_STONE_BRICKS_PLAQUE = BLOCK_REGISTRAR.register(Plaques.id("mossy_stone_bricks_plaque"), () -> new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.MOSSY_STONE_BRICKS.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.MOSSY_STONE_BRICKS));
    public static final RegistrySupplier<Block> NETHER_BRICKS_PLAQUE = BLOCK_REGISTRAR.register(Plaques.id("nether_bricks_plaque"), () -> new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.NETHER_BRICKS.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.NETHER_BRICKS));
    public static final RegistrySupplier<Block> POLISHED_ANDESITE_PLAQUE = BLOCK_REGISTRAR.register(Plaques.id("polished_andesite_plaque"), () -> new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.POLISHED_ANDESITE.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.POLISHED_ANDESITE));
    public static final RegistrySupplier<Block> POLISHED_DIORITE_PLAQUE = BLOCK_REGISTRAR.register(Plaques.id("polished_diorite_plaque"), () -> new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.POLISHED_DIORITE.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.POLISHED_DIORITE));
    public static final RegistrySupplier<Block> POLISHED_GRANITE_PLAQUE = BLOCK_REGISTRAR.register(Plaques.id("polished_granite_plaque"), () -> new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.POLISHED_GRANITE.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.POLISHED_GRANITE));
    public static final RegistrySupplier<Block> PURPUR_PLAQUE = BLOCK_REGISTRAR.register(Plaques.id("purpur_plaque"), () -> new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.PURPUR_BLOCK.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.PURPUR_BLOCK));
    public static final RegistrySupplier<Block> PRISMARINE_BRICKS_PLAQUE = BLOCK_REGISTRAR.register(Plaques.id("prismarine_bricks_plaque"), () -> new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.PRISMARINE.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.PRISMARINE_BRICKS));
    public static final RegistrySupplier<Block> QUARTZ_PLAQUE = BLOCK_REGISTRAR.register(Plaques.id("quartz_plaque"), () -> new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.QUARTZ_BLOCK.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.QUARTZ_BLOCK));
    public static final RegistrySupplier<Block> RED_SANDSTONE_PLAQUE = BLOCK_REGISTRAR.register(Plaques.id("red_sandstone_plaque"), () -> new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.RED_SANDSTONE.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.RED_SANDSTONE));
    public static final RegistrySupplier<Block> SANDSTONE_PLAQUE = BLOCK_REGISTRAR.register(Plaques.id("sandstone_plaque"), () -> new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.SANDSTONE.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.SANDSTONE));
    public static final RegistrySupplier<Block> STONE_PLAQUE = BLOCK_REGISTRAR.register(Plaques.id("stone_plaque"), () -> new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.STONE.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.STONE));
    public static final RegistrySupplier<Block> STONE_BRICKS_PLAQUE = BLOCK_REGISTRAR.register(Plaques.id("stone_bricks_plaque"), () -> new PlaqueBlock(AbstractBlock.Settings.of(Material.STONE, Blocks.STONE_BRICKS.getDefaultMapColor()).requiresTool().noCollision().strength(0.5f).sounds(BlockSoundGroup.STONE), PlaqueType.STONE_BRICKS));

    public static final Registrar<Item> ITEM_REGISTRAR = REGISTRIES.get().get(RegistryKeys.ITEM);
    public static final RegistrySupplier<Item> ANDESITE_PLAQUE_ITEM = ITEM_REGISTRAR.register(Plaques.id("andesite_plaque"), () -> new PlaqueItem(new Item.Settings().arch$tab(ItemGroups.FUNCTIONAL).maxCount(16), ANDESITE_PLAQUE.get()));
    public static final RegistrySupplier<Item> BLACKSTONE_PLAQUE_ITEM = ITEM_REGISTRAR.register(Plaques.id("blackstone_plaque"), () -> new PlaqueItem(new Item.Settings().arch$tab(ItemGroups.FUNCTIONAL).maxCount(16), BLACKSTONE_PLAQUE.get()));
    public static final RegistrySupplier<Item> BRICKS_PLAQUE_ITEM = ITEM_REGISTRAR.register(Plaques.id("bricks_plaque"), () -> new PlaqueItem(new Item.Settings().arch$tab(ItemGroups.FUNCTIONAL).maxCount(16), BRICKS_PLAQUE.get()));
    public static final RegistrySupplier<Item> COBBLESTONE_PLAQUE_ITEM = ITEM_REGISTRAR.register(Plaques.id("cobblestone_plaque"), () -> new PlaqueItem(new Item.Settings().arch$tab(ItemGroups.FUNCTIONAL).maxCount(16), COBBLESTONE_PLAQUE.get()));
    public static final RegistrySupplier<Item> CRACKED_STONE_BRICKS_PLAQUE_ITEM = ITEM_REGISTRAR.register(Plaques.id("cracked_stone_bricks_plaque"), () -> new PlaqueItem(new Item.Settings().arch$tab(ItemGroups.FUNCTIONAL).maxCount(16), CRACKED_STONE_BRICKS_PLAQUE.get()));
    public static final RegistrySupplier<Item> DIORITE_PLAQUE_ITEM = ITEM_REGISTRAR.register(Plaques.id("diorite_plaque"), () -> new PlaqueItem(new Item.Settings().arch$tab(ItemGroups.FUNCTIONAL).maxCount(16), DIORITE_PLAQUE.get()));
    public static final RegistrySupplier<Item> END_STONE_BRICKS_PLAQUE_ITEM = ITEM_REGISTRAR.register(Plaques.id("end_stone_bricks_plaque"), () -> new PlaqueItem(new Item.Settings().arch$tab(ItemGroups.FUNCTIONAL).maxCount(16), END_STONE_BRICKS_PLAQUE.get()));
    public static final RegistrySupplier<Item> GRANITE_PLAQUE_ITEM = ITEM_REGISTRAR.register(Plaques.id("granite_plaque"), () -> new PlaqueItem(new Item.Settings().arch$tab(ItemGroups.FUNCTIONAL).maxCount(16), GRANITE_PLAQUE.get()));
    public static final RegistrySupplier<Item> MOSSY_COBBLESTONE_PLAQUE_ITEM = ITEM_REGISTRAR.register(Plaques.id("mossy_cobblestone_plaque"), () -> new PlaqueItem(new Item.Settings().arch$tab(ItemGroups.FUNCTIONAL).maxCount(16), MOSSY_COBBLESTONE_PLAQUE.get()));
    public static final RegistrySupplier<Item> MOSSY_STONE_BRICKS_PLAQUE_ITEM = ITEM_REGISTRAR.register(Plaques.id("mossy_stone_bricks_plaque"), () -> new PlaqueItem(new Item.Settings().arch$tab(ItemGroups.FUNCTIONAL).maxCount(16), MOSSY_STONE_BRICKS_PLAQUE.get()));
    public static final RegistrySupplier<Item> NETHER_BRICKS_PLAQUE_ITEM = ITEM_REGISTRAR.register(Plaques.id("nether_bricks_plaque"), () -> new PlaqueItem(new Item.Settings().arch$tab(ItemGroups.FUNCTIONAL).maxCount(16), NETHER_BRICKS_PLAQUE.get()));
    public static final RegistrySupplier<Item> POLISHED_ANDESITE_PLAQUE_ITEM = ITEM_REGISTRAR.register(Plaques.id("polished_andesite_plaque"), () -> new PlaqueItem(new Item.Settings().arch$tab(ItemGroups.FUNCTIONAL).maxCount(16), POLISHED_ANDESITE_PLAQUE.get()));
    public static final RegistrySupplier<Item> POLISHED_DIORITE_PLAQUE_ITEM = ITEM_REGISTRAR.register(Plaques.id("polished_diorite_plaque"), () -> new PlaqueItem(new Item.Settings().arch$tab(ItemGroups.FUNCTIONAL).maxCount(16), POLISHED_DIORITE_PLAQUE.get()));
    public static final RegistrySupplier<Item> POLISHED_GRANITE_PLAQUE_ITEM = ITEM_REGISTRAR.register(Plaques.id("polished_granite_plaque"), () -> new PlaqueItem(new Item.Settings().arch$tab(ItemGroups.FUNCTIONAL).maxCount(16), POLISHED_GRANITE_PLAQUE.get()));
    public static final RegistrySupplier<Item> PRISMARINE_BRICKS_PLAQUE_ITEM = ITEM_REGISTRAR.register(Plaques.id("prismarine_bricks_plaque"), () -> new PlaqueItem(new Item.Settings().arch$tab(ItemGroups.FUNCTIONAL).maxCount(16), PRISMARINE_BRICKS_PLAQUE.get()));
    public static final RegistrySupplier<Item> PURPUR_PLAQUE_ITEM = ITEM_REGISTRAR.register(Plaques.id("purpur_plaque"), () -> new PlaqueItem(new Item.Settings().arch$tab(ItemGroups.FUNCTIONAL).maxCount(16), PURPUR_PLAQUE.get()));
    public static final RegistrySupplier<Item> QUARTZ_PLAQUE_ITEM = ITEM_REGISTRAR.register(Plaques.id("quartz_plaque"), () -> new PlaqueItem(new Item.Settings().arch$tab(ItemGroups.FUNCTIONAL).maxCount(16), QUARTZ_PLAQUE.get()));
    public static final RegistrySupplier<Item> RED_SANDSTONE_PLAQUE_ITEM = ITEM_REGISTRAR.register(Plaques.id("red_sandstone_plaque"), () -> new PlaqueItem(new Item.Settings().arch$tab(ItemGroups.FUNCTIONAL).maxCount(16), RED_SANDSTONE_PLAQUE.get()));
    public static final RegistrySupplier<Item> SANDSTONE_PLAQUE_ITEM = ITEM_REGISTRAR.register(Plaques.id("sandstone_plaque"), () -> new PlaqueItem(new Item.Settings().arch$tab(ItemGroups.FUNCTIONAL).maxCount(16), SANDSTONE_PLAQUE.get()));
    public static final RegistrySupplier<Item> STONE_PLAQUE_ITEM = ITEM_REGISTRAR.register(Plaques.id("stone_plaque"), () -> new PlaqueItem(new Item.Settings().arch$tab(ItemGroups.FUNCTIONAL).maxCount(16), STONE_PLAQUE.get()));
    public static final RegistrySupplier<Item> STONE_BRICKS_PLAQUE_ITEM = ITEM_REGISTRAR.register(Plaques.id("stone_bricks_plaque"), () -> new PlaqueItem(new Item.Settings().arch$tab(ItemGroups.FUNCTIONAL).maxCount(16), STONE_BRICKS_PLAQUE.get()));

    public static final Registrar<BlockEntityType<?>> BLOCK_ENTITY_REGISTRAR = REGISTRIES.get().get(RegistryKeys.BLOCK_ENTITY_TYPE);
    public static final RegistrySupplier<BlockEntityType<PlaqueBlockEntity>> PLAQUE = BLOCK_ENTITY_REGISTRAR.register(Plaques.id("plaque"), () -> BlockEntityType.Builder.create(PlaqueBlockEntity::new,
            STONE_PLAQUE.get(), BLACKSTONE_PLAQUE.get(), NETHER_BRICKS_PLAQUE.get(), PRISMARINE_BRICKS_PLAQUE.get(),
            QUARTZ_PLAQUE.get(), SANDSTONE_PLAQUE.get(), RED_SANDSTONE_PLAQUE.get(), BRICKS_PLAQUE.get(),
            STONE_BRICKS_PLAQUE.get(), MOSSY_STONE_BRICKS_PLAQUE.get(), CRACKED_STONE_BRICKS_PLAQUE.get(),
            ANDESITE_PLAQUE.get(), DIORITE_PLAQUE.get(), GRANITE_PLAQUE.get(), POLISHED_ANDESITE_PLAQUE.get(),
            POLISHED_DIORITE_PLAQUE.get(), POLISHED_GRANITE_PLAQUE.get(), COBBLESTONE_PLAQUE.get(),
            MOSSY_COBBLESTONE_PLAQUE.get(), END_STONE_BRICKS_PLAQUE.get(), PURPUR_PLAQUE.get()
    ).build(Util.getChoiceType(TypeReferences.BLOCK_ENTITY, "plaque")));

    public static void init() {
        LOGGER.info("Initializing Plaques");
        Networking.init();
    }

    public static Identifier id(String id) {
        return new Identifier(MOD_ID, id);
    }
}
