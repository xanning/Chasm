package com.xraymod.mixin;

import com.xraymod.XrayMod;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SideOnly(Side.CLIENT)
@Mixin({BlockRendererDispatcher.class})
public abstract class MixinBlockRendererDispatcher {
    @Inject(
            method = {"renderBlock"},
            at = {@At("HEAD")}
    )
    private void renderBlock(
            IBlockState iBlockState,
            BlockPos blockPos,
            IBlockAccess iBlockAccess,
            WorldRenderer worldRenderer,
            CallbackInfoReturnable<Boolean> callbackInfoReturnable
    ) {
        if (XrayMod.xray != null && XrayMod.xray.isEnabled() && XrayMod.xray.isXrayBlock(Block.getIdFromBlock(iBlockState.getBlock()))) {
            if (XrayMod.xray.checkBlock(blockPos)) {
                XrayMod.xray.trackedBlocks.add(new BlockPos(blockPos));
            } else {
                XrayMod.xray.trackedBlocks.remove(blockPos);
            }
        }
    }
}