package com.jamieswhiteshirt.clotheslinefabric.mixin.client.world;

import com.jamieswhiteshirt.clotheslinefabric.api.NetworkCollection;
import com.jamieswhiteshirt.clotheslinefabric.api.NetworkManager;
import com.jamieswhiteshirt.clotheslinefabric.api.NetworkManagerProvider;
import com.jamieswhiteshirt.clotheslinefabric.client.SoundNetworkCollectionListener;
import com.jamieswhiteshirt.clotheslinefabric.client.impl.ClientNetworkManager;
import com.jamieswhiteshirt.clotheslinefabric.common.impl.NetworkCollectionImpl;
import com.jamieswhiteshirt.clotheslinefabric.internal.WorldExtension;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Profiler;
import net.minecraft.world.Difficulty;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import net.minecraft.world.WorldSaveHandler;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.chunk.ChunkPos;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.function.BiFunction;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World implements NetworkManagerProvider, WorldExtension {
    private static final Identifier SOUND_KEY = new Identifier("clothesline-fabric", "sound");

    private final NetworkCollection networkCollection = new NetworkCollectionImpl();
    private final NetworkManager networkManager = new ClientNetworkManager((ClientWorld)(Object) this, networkCollection);

    protected ClientWorldMixin(WorldSaveHandler var1, @Nullable PersistentStateManager var2, LevelProperties var3, DimensionType var4, BiFunction<World, Dimension, ChunkManager> var5, Profiler var6, boolean var7) {
        super(var1, var2, var3, var4, var5, var6, var7);
    }

    @Inject(
        at = @At("RETURN"),
        method = "<init>(Lnet/minecraft/client/network/ClientPlayNetworkHandler;Lnet/minecraft/world/level/LevelInfo;Lnet/minecraft/world/dimension/DimensionType;Lnet/minecraft/world/Difficulty;Lnet/minecraft/util/Profiler;)V"
    )
    private void constructor(ClientPlayNetworkHandler clientPlayNetworkHandler_1, LevelInfo levelInfo_1, DimensionType dimensionType_1, Difficulty difficulty_1, Profiler profiler_1, CallbackInfo ci) {
        networkManager.getNetworks().addEventListener(SOUND_KEY, new SoundNetworkCollectionListener());
    }

    @Override
    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    @Override
    public void clotheslineTick() {
        networkManager.update();
    }

    @Override
    public void onPlayerWatchChunk(ChunkPos pos, ServerPlayerEntity player) {
    }

    @Override
    public void onPlayerUnWatchChunk(ChunkPos pos, ServerPlayerEntity player) {
    }

    @Override
    public void onChunkLoaded(ChunkPos pos) {
    }

    @Override
    public void onChunkUnloaded(ChunkPos pos) {
    }
}
