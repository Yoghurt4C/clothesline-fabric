package com.jamieswhiteshirt.clotheslinefabric.client.audio;

import com.jamieswhiteshirt.clotheslinefabric.api.NetworkState;
import com.jamieswhiteshirt.clotheslinefabric.api.Path;
import com.jamieswhiteshirt.clotheslinefabric.common.sound.ClotheslineSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.audio.MovingSoundInstance;
import net.minecraft.sound.SoundCategory;

@Environment(EnvType.CLIENT)
public class ClotheslineRopeSoundInstance extends MovingSoundInstance {
    private final NetworkState state;
    private final Path.Node node;

    public ClotheslineRopeSoundInstance(NetworkState state, Path.Node node) {
        super(ClotheslineSoundEvents.BLOCK_CLOTHESLINE_ANCHOR_ROPE, SoundCategory.BLOCK);
        this.state = state;
        this.node = node;

        this.repeat = true;
        this.x = node.getPos().getX() + 0.5F;
        this.y = node.getPos().getY() + 0.5F;
        this.z = node.getPos().getZ() + 0.5F;

        // tick();
    }

    @Override
    public void tick() {
        float momentum = Math.abs((float) state.getMomentum()) / NetworkState.MAX_MOMENTUM;
        this.volume = (2 + node.getEdges().size()) * momentum * 0.2F;
        this.pitch = 0.25F + momentum * 0.75F;
    }

    @Override
    public boolean isDone() {
        return false;
    }
}