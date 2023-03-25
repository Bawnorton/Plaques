package com.bawnorton.plaques.client.networking;

import com.bawnorton.plaques.block.entity.PlaqueBlockEntity;
import com.bawnorton.plaques.client.screen.PlaqueScreen;
import com.bawnorton.plaques.networking.Networking;
import dev.architectury.networking.NetworkManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public class ClientNetworking {
    public static void init() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, Networking.OPEN_PLAQUE_SCREEN, (packetByteBuf, packetContext) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            BlockPos pos = packetByteBuf.readBlockPos();
            BlockEntity blockEntity = packetContext.getPlayer().world.getBlockEntity(pos);
            if(!(blockEntity instanceof PlaqueBlockEntity)) {
                BlockState state = packetContext.getPlayer().world.getBlockState(pos);
                blockEntity = new PlaqueBlockEntity(pos, state);
                blockEntity.setWorld(packetContext.getPlayer().world);
            }

            final PlaqueBlockEntity plaqueBlock = (PlaqueBlockEntity) blockEntity;
            client.execute(() -> client.setScreen(new PlaqueScreen(plaqueBlock, client.shouldFilterText())));
        });
    }
}
