package com.xraymod.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class BindManager {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(
            Minecraft.getMinecraft().mcDataDir, "config/chasm.json"
    );

    private static Map<String, String> binds = new HashMap<>();

    public static void load() {
        try {
            if (!CONFIG_FILE.exists()) {
                save();
                return;
            }

            FileReader reader = new FileReader(CONFIG_FILE);
            Type type = new TypeToken<Map<String, String>>(){}.getType();
            binds = gson.fromJson(reader, type);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            if (!CONFIG_FILE.getParentFile().exists())
                CONFIG_FILE.getParentFile().mkdirs();

            FileWriter writer = new FileWriter(CONFIG_FILE);
            gson.toJson(binds, writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setBind(String keyName, String action) {
        binds.put(keyName.toUpperCase(), action.toLowerCase());
        save();
    }

    public static String getBind(int keyCode) {
        String name = Keyboard.getKeyName(keyCode);
        return binds.getOrDefault(name.toUpperCase(), null);
    }

    public static Map<String, String> getAll() {
        return binds;
    }
}
