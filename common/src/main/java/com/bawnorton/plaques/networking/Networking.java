package com.bawnorton.plaques.networking;

import com.bawnorton.plaques.Plaques;
import com.bawnorton.plaques.block.entity.PlaqueBlockEntity;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.filter.FilteredMessage;
import net.minecraft.server.filter.TextStream;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public class Networking {
    public static final Identifier UPDATE_PLAQUE = Plaques.id("update_plaque");
    public static final Identifier OPEN_PLAQUE_SCREEN = Plaques.id("open_plaque_screen");
    public static final Object SERVER_LOCK = new Object();
    public static MinecraftServer server;

    public static void sendOpenPlaqueScreen(ServerPlayerEntity player, PlaqueBlockEntity plaque) {
        plaque.setEditor(player.getUuid());
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBlockPos(plaque.getPos());

        player.networkHandler.sendPacket(new BlockUpdateS2CPacket(player.world, plaque.getPos()));
        NetworkManager.sendToPlayer(player, OPEN_PLAQUE_SCREEN, buf);
    }

    public static void init() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, UPDATE_PLAQUE, (packetByteBuf, packetContext) -> {
            List<String> text = new ArrayList<>();
            BlockPos pos = packetByteBuf.readBlockPos();
            int size = packetByteBuf.readInt();
            for(int i = 0; i < size; ++i) {
                text.add(packetByteBuf.readString());
            }

            ServerPlayerEntity player = (ServerPlayerEntity) packetContext.getPlayer();
            MinecraftServer server = player.getServer();

            filterTexts(text, player).thenAcceptAsync((texts) -> {
                player.updateLastActionTime();
                ServerWorld world = player.getWorld();
                if(world.isChunkLoaded(pos)) {
                    BlockState state = world.getBlockState(pos);
                    BlockEntity blockEntity = world.getBlockEntity(pos);
                    if(!(blockEntity instanceof PlaqueBlockEntity plaque)) {
                        return;
                    }

                    if(!plaque.isEditable() || !player.getUuid().equals(plaque.getEditor())) {
                        Plaques.LOGGER.warn("Player {} just tried to change non-editable plaque", player.getName().getString());
                        return;
                    }

                    for(int i = 0; i < text.size(); ++i) {
                        FilteredMessage filteredMessage = texts.get(i);
                        if(player.shouldFilterText()) {
                            plaque.setTextOnRow(i, Text.literal(filteredMessage.getString()));
                        } else {
                            plaque.setTextOnRow(i, Text.literal(filteredMessage.raw()), Text.literal(filteredMessage.getString()));
                        }
                    }

                    plaque.markDirty();
                    world.updateListeners(pos, state, state, 3);
                }
            }, server);
        });

        Plaques.LOGGER.debug("Networking initialized");
    }

    private static CompletableFuture<List<FilteredMessage>> filterTexts(List<String> texts, ServerPlayerEntity player) {
        return filterText(texts, player, TextStream::filterTexts);
    }

    private static <T, R> CompletableFuture<R> filterText(T text, ServerPlayerEntity player, BiFunction<TextStream, T, CompletableFuture<R>> filterer) {
        return filterer.apply(player.getTextStream(), text).thenApply((filtered) -> filtered);
    }

    public static void onServerInit(ServerRunnable serverAccess) {
        synchronized (SERVER_LOCK) {
            serverAccess.run(server);
        }
    }

    @FunctionalInterface
    public interface ServerRunnable {
        void run(MinecraftServer server);
    }
}
