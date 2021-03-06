package com.jamieswhiteshirt.clotheslinefabric.util;

import com.jamieswhiteshirt.clotheslinefabric.api.*;
import com.jamieswhiteshirt.clotheslinefabric.api.util.MutableSortedIntMap;
import com.jamieswhiteshirt.clotheslinefabric.common.impl.NetworkImpl;
import com.jamieswhiteshirt.clotheslinefabric.common.impl.NetworkStateImpl;
import com.jamieswhiteshirt.clotheslinefabric.common.util.ChunkSpan;
import com.jamieswhiteshirt.clotheslinefabric.common.util.PathBuilder;
import com.jamieswhiteshirt.clotheslinefabric.internal.PersistentNetwork;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Assertions;

import java.util.Collections;
import java.util.UUID;

class NetworkTests {
    static BlockPos posA = new BlockPos(0, 0, 0);
    static BlockPos posB = new BlockPos(1, 0, 0);

    static class NetworkDataSet {
        final BlockPos from;
        final BlockPos to;
        final Tree tree;
        final MutableSortedIntMap<ItemStack> attachments;
        final NetworkState state;
        final PersistentNetwork persistentNetwork;
        final NetworkImpl network;

        NetworkDataSet(BlockPos from, BlockPos to) {
            this.from = from;
            this.to = to;
            this.tree = new Tree(
                from,
                Collections.singletonList(
                    new Tree.Edge(to.subtract(from), AttachmentUnit.lengthBetween(from, to), 0, Tree.empty(to, AttachmentUnit.UNITS_PER_BLOCK, 0))
                ),
                0, AttachmentUnit.UNITS_PER_BLOCK * 2, 0
            );
            Path path = PathBuilder.buildPath(tree);
            LongSet chunkSpan = ChunkSpan.ofPath(path);
            this.attachments = MutableSortedIntMap.empty(AttachmentUnit.UNITS_PER_BLOCK * 2);
            this.state = new NetworkStateImpl(0, 0, 0, 0, tree, path, chunkSpan, attachments);
            this.persistentNetwork = new PersistentNetwork(new UUID(0, 0), state);
            this.network = new NetworkImpl(0, persistentNetwork);
        }
    }

    static NetworkDataSet ab = new NetworkDataSet(posA, posB);

    static void assertNetworksEquivalent(NetworkImpl expected, NetworkImpl actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getUuid(), actual.getUuid());
        assertNetworkStatesEquivalent(expected.getState(), actual.getState());
    }

    static void assertNetworkStatesEquivalent(NetworkState expected, NetworkState actual) {
        Assertions.assertEquals(expected.getShift(), actual.getShift());
        Assertions.assertEquals(expected.getMomentum(), actual.getMomentum());
        Assertions.assertEquals(expected.getTree(), actual.getTree());
        Assertions.assertEquals(expected.getAttachments(), actual.getAttachments());
    }
}
