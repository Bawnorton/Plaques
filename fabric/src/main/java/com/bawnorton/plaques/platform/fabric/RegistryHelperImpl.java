package com.bawnorton.plaques.platform.fabric;

import com.bawnorton.plaques.Plaques;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class RegistryHelperImpl {

    public static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        T registry = Registry.register(Registry.BLOCK, Plaques.id(name), block.get());
        return () -> registry;
    }

    public static <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
        T registry = Registry.register(Registry.ITEM, Plaques.id(name), item.get());
        return () -> registry;
    }

    public static <T extends BlockEntityType<?>> Supplier<T> registerBlockEntityType(String name, Supplier<T> blockEntity) {
        T registry = Registry.register(Registry.BLOCK_ENTITY_TYPE, Plaques.id(name), blockEntity.get());
        return () -> registry;
    }
}
