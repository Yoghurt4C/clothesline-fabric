package com.jamieswhiteshirt.clotheslinefabric.mixin.client.gui.hud;

import com.jamieswhiteshirt.clotheslinefabric.api.*;
import com.jamieswhiteshirt.clotheslinefabric.client.raytrace.NetworkRaytraceHit;
import com.jamieswhiteshirt.clotheslinefabric.client.raytrace.NetworkRaytraceHitEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.text.TextFormat;
import net.minecraft.util.HitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;

@Mixin(DebugHud.class)
public class DebugHudMixin extends Drawable {
    @Shadow @Final private MinecraftClient client;
    @Shadow private HitResult blockHit;

    @Inject(
        at = @At("RETURN"),
        method = "method_1839()Ljava/util/List;"
    )
    private void method_1830(CallbackInfoReturnable<List<String>> cir) {
        if (!client.hasReducedDebugInfo()) {
            if (client.field_1692 instanceof NetworkRaytraceHitEntity) {
                NetworkRaytraceHit hit = ((NetworkRaytraceHitEntity) client.field_1692).getHit();
                NetworkEdge edge = hit.edge;
                Network network = edge.getNetwork();
                cir.getReturnValue().addAll(Arrays.asList(
                    "",
                    TextFormat.UNDERLINE + "Targeted Clothesline",
                    "Edge ID: " + edge.getIndex() + "@" + network.getId(),
                    "Span: " + edge.getPathEdge().getFromOffset() + " to " + edge.getPathEdge().getToOffset(),
                    hit.getDebugString()
                ));
            }
            if (blockHit != null && blockHit.type == HitResult.Type.BLOCK) {
                NetworkManager manager = ((NetworkManagerProvider) client.world).getNetworkManager();
                NetworkNode node = manager.getNetworks().getNodes().get(blockHit.getBlockPos());
                if (node != null) {
                    cir.getReturnValue().addAll(Arrays.asList(
                        "",
                        TextFormat.UNDERLINE + "Targeted Clothesline Anchor",
                        "Network ID: " + node.getNetwork().getId()
                    ));
                }
            }
        }
    }
}
