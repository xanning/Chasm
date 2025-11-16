package com.xraymod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class ChatUtil {
    public static void sendMessage(String message) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(
                    new ChatComponentText(message.replace("&", "§"))
            );
        }
    }
}