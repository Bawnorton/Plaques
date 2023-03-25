package com.bawnorton.plaques.networking;

import com.bawnorton.plaques.Plaques;
import com.bawnorton.plaques.block.entity.PlaqueBlockEntity;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class Networking {
    public static Identifier OPEN_PLAQUE_SCREEN = Plaques.id("open_plaque_screen");

    public static void sendOpenPlaqueScreen(ServerPlayerEntity player, PlaqueBlockEntity plaque) {
        plaque.setEditor(player.getUuid());
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBlockPos(plaque.getPos());

        player.networkHandler.sendPacket(new BlockUpdateS2CPacket(player.world, plaque.getPos()));
        NetworkManager.sendToPlayer(player, OPEN_PLAQUE_SCREEN, buf);
    }

    public static void init() {
        Plaques.LOGGER.debug("Networking initialized");
    }
}
