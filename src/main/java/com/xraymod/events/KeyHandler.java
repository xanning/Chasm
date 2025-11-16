package com.xraymod.events;

import com.xraymod.XrayMod;
import com.xraymod.util.ConfigManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class KeyHandler {

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        if (!Keyboard.getEventKeyState()) return;

        String pressed = Keyboard.getKeyName(Keyboard.getEventKey()).toUpperCase();

        if (pressed.equals(ConfigManager.bindKey)) {
            XrayMod.xray.setEnabled(!XrayMod.xray.isEnabled());
            ConfigManager.save();
        }
    }
}
