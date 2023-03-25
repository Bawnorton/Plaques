package com.bawnorton.plaques.item;

import com.bawnorton.plaques.block.entity.PlaqueBlockEntity;
import com.bawnorton.plaques.networking.Networking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class PlaqueItem extends BlockItem {
    public PlaqueItem(Settings settings, Block block) {
        super(block, settings);
    }

    @Nullable
    protected BlockState getPlacementState(ItemPlacementContext context) {
        BlockState blockState = this.getBlock().getPlacementState(context);
        BlockState blockState2 = null;
        WorldView worldView = context.getWorld();
        BlockPos blockPos = context.getBlockPos();

        for (Direction direction : context.getPlacementDirections()) {
            if (direction != Direction.UP) {
                BlockState blockState3 = direction == Direction.DOWN ? this.getBlock().getPlacementState(context) : blockState;
                if (blockState3 != null && blockState3.canPlaceAt(worldView, blockPos)) {
                    blockState2 = blockState3;
                    break;
                }
            }
        }

        return blockState2 != null && worldView.canPlace(blockState2, blockPos, ShapeContext.absent()) ? blockState2 : null;
    }

    protected boolean postPlacement(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
        boolean bl = super.postPlacement(pos, world, player, stack, state);
        if (!world.isClient && !bl && player != null) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof PlaqueBlockEntity plaqueBlockEntity) {
                Networking.sendOpenPlaqueScreen((ServerPlayerEntity) player, plaqueBlockEntity);
            }
        }

        return bl;
    }
}
