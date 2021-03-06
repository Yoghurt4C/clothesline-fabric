package com.jamieswhiteshirt.clotheslinefabric.util;

import com.jamieswhiteshirt.clotheslinefabric.common.util.BasicPersistentNetwork;
import com.jamieswhiteshirt.clotheslinefabric.common.util.NBTSerialization;
import net.minecraft.nbt.CompoundTag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NBTSerializationTest {
    @Test
    void persistsPersistentNetworkEquality() {
        BasicPersistentNetwork written = BasicPersistentNetwork.fromAbsolute(NetworkTests.ab.persistentNetwork);
        CompoundTag nbtTagCompound = NBTSerialization.writePersistentNetwork(written);
        BasicPersistentNetwork read = NBTSerialization.readPersistentNetwork(nbtTagCompound);
        Assertions.assertEquals(written, read);
    }
}
