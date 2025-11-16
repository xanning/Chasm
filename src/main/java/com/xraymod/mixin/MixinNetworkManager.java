package com.xraymod.mixin;

import com.xraymod.event.EventManager;
import com.xraymod.event.types.EventType;
import com.xraymod.events.PacketEvent;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SideOnly(Side.CLIENT)
@Mixin({NetworkManager.class})
public abstract class MixinNetworkManager {
    @Inject(
            method = {"channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void channelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo callbackInfo) {
        PacketEvent event = new PacketEvent(EventType.RECEIVE, packet);
        EventManager.call(event);
        if (event.isCancelled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(
            method = {"sendPacket(Lnet/minecraft/network/Packet;)V"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void sendPacket(Packet<?> packet, CallbackInfo callbackInfo) {
        PacketEvent event = new PacketEvent(EventType.SEND, packet);
        EventManager.call(event);
        if (event.isCancelled()) {
            callbackInfo.cancel();
        }
    }
}
