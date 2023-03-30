package com.bawnorton.plaques.block;

import com.bawnorton.plaques.block.entity.PlaqueBlockEntity;
import com.bawnorton.plaques.util.PlaqueType;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SignType;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;

public class PlaqueBlock extends WallSignBlock {
    private static final List<Item> ACCENTS = List.of(
            Items.IRON_INGOT,
            Items.GOLD_INGOT,
            Items.NETHERITE_INGOT,
            Items.COPPER_INGOT,
            Items.DIAMOND,
            Items.EMERALD,
            Items.LAPIS_LAZULI,
            Items.REDSTONE,
            Items.QUARTZ
    );

    private static final VoxelShape SHAPE_N = Block.createCuboidShape(0, 2, 14, 16, 14, 16);
    private static final VoxelShape SHAPE_E = Block.createCuboidShape(0, 2, 0, 2, 14, 16);
    private static final VoxelShape SHAPE_W = Block.createCuboidShape(14, 2, 0, 16, 14, 16);
    private static final VoxelShape SHAPE_S = Block.createCuboidShape(0, 2, 0, 16, 14, 2);

    private final PlaqueType plaqueType;

    public PlaqueBlock(Settings settings, PlaqueType plaqueType) {
        super(settings, SignType.OAK);
        this.plaqueType = plaqueType;
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PlaqueBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();
        boolean isGlowstone = item == Items.GLOWSTONE_DUST;
        boolean isCoal = item == Items.COAL;
        boolean canInteract = (isGlowstone || isCoal || ACCENTS.contains(item)) && player.getAbilities().allowModifyWorld;

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if(!(blockEntity instanceof PlaqueBlockEntity plaqueBlockEntity)) return ActionResult.PASS;
        if(world.isClient) return canInteract ? ActionResult.SUCCESS : ActionResult.CONSUME;

        boolean isGlowing = plaqueBlockEntity.isGlowingText();
        if(isGlowing && isGlowstone || !isGlowing && isCoal) return ActionResult.PASS;

        if(canInteract) {
            boolean usedItem;
            if(isGlowstone) {
                world.playSound(null, pos, SoundEvents.BLOCK_GLASS_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                usedItem = plaqueBlockEntity.setGlowingText(true);
                if (player instanceof ServerPlayerEntity serverPlayer) {
                    Criteria.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, itemStack);
                }
            } else if(isCoal) {
                world.playSound(null, pos, SoundEvents.BLOCK_GLASS_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                usedItem = plaqueBlockEntity.setGlowingText(false);
            } else {
                world.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                usedItem = plaqueBlockEntity.setAccent(item);
            }

            if(usedItem) {
                if(!player.isCreative()) {
                    itemStack.decrement(1);
                }
                player.incrementStat(Stats.USED.getOrCreateStat(item));
            }
        }

        return plaqueBlockEntity.onActivate((ServerPlayerEntity) player) ? ActionResult.SUCCESS : ActionResult.PASS;
    }

    public PlaqueType getPlaqueType() {
        return plaqueType;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case SOUTH -> SHAPE_S;
            case WEST -> SHAPE_W;
            case EAST -> SHAPE_E;
            default -> SHAPE_N;
        };
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public String getTranslationKey() {
        return "block.plaques." + plaqueType.getName() + "_plaque";
    }
}
