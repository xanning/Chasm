package com.xraymod.mixin;

import com.xraymod.XrayMod;
import net.minecraft.block.BlockWeb;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SideOnly(Side.CLIENT)
@Mixin({BlockWeb.class})
public abstract class MixinBlockWeb {
    @Inject(
            method = {"getBlockLayer"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void getBlockLayer(CallbackInfoReturnable<EnumWorldBlockLayer> callbackInfoReturnable) {
        if (XrayMod.xray != null && XrayMod.xray.isEnabled()) {
            callbackInfoReturnable.setReturnValue(EnumWorldBlockLayer.TRANSLUCENT);
        }
    }
}
