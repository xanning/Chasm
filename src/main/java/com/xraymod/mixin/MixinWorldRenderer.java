package com.xraymod.mixin;

import com.xraymod.XrayMod;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.nio.IntBuffer;

@SideOnly(Side.CLIENT)
@Mixin({WorldRenderer.class})
public abstract class MixinWorldRenderer {
    @Redirect(
            method = {"putColorMultiplier"},
            at = @At(
                    value = "INVOKE",
                    target = "java/nio/IntBuffer.put(II)Ljava/nio/IntBuffer;",
                    remap = false
            )
    )
    private IntBuffer putColorMultiplier(IntBuffer intBuffer, int integer2, int integer3) {
        if (XrayMod.xray == null || !XrayMod.xray.isEnabled()) {
            return intBuffer.put(integer2, integer3);
        } else {
            return intBuffer.put(integer2, integer3 & 16777215 | (int) ((float) XrayMod.xray.opacity.getValue().intValue() * 255.0F / 100.0F) << 24);
        }
    }
}