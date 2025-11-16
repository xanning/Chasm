package com.xraymod;

import com.xraymod.command.XrayCommand;
import com.xraymod.event.EventManager;
import com.xraymod.events.KeyHandler;
import com.xraymod.module.Xray;
import com.xraymod.util.ConfigManager;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import com.xraymod.util.BindManager;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = XrayMod.MODID, version = XrayMod.VERSION, clientSideOnly = true)
public class XrayMod {
    public static final String MODID = "xraymod";
    public static final String VERSION = "1.0.0";

    public static Xray xray;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        xray = new Xray();
        EventManager.register(xray);
        ConfigManager.load();
        BindManager.load();

        MinecraftForge.EVENT_BUS.register(new KeyHandler());

        ClientCommandHandler.instance.registerCommand(new XrayCommand());
    }
}