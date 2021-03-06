package com.jamieswhiteshirt.clotheslinefabric.mixin;

import com.jamieswhiteshirt.clotheslinefabric.common.event.ChunkWatchEvent;
import net.fabricmc.fabric.util.HandlerArray;
import net.minecraft.class_3898;
import net.minecraft.network.Packet;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_3898.class)
public class class_3898Mixin {
    @Shadow @Final private World field_17214;

    @Inject(
        at = @At("RETURN"),
        method = "method_17241(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/world/chunk/ChunkPos;[Lnet/minecraft/network/Packet;ZZ)V"
    )
    private void method_17241(ServerPlayerEntity player, ChunkPos pos, Packet<?>[] packets, boolean previouslyWatching, boolean currentlyWatching, CallbackInfo ci) {
        if (player.world == field_17214) {
            if (currentlyWatching && !previouslyWatching) {
                for (ChunkWatchEvent.ChunkWatchEventConsumer handler : ((HandlerArray<ChunkWatchEvent.ChunkWatchEventConsumer>) ChunkWatchEvent.WATCH).getBackingArray()) {
                    handler.accept(field_17214, pos, player);
                }
            }
            if (!currentlyWatching && previouslyWatching) {
                for (ChunkWatchEvent.ChunkWatchEventConsumer handler : ((HandlerArray<ChunkWatchEvent.ChunkWatchEventConsumer>) ChunkWatchEvent.UNWATCH).getBackingArray()) {
                    handler.accept(field_17214, pos, player);
                }
            }
        }
    }
}
