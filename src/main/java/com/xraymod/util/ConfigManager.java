package com.xraymod.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xraymod.XrayMod;
import com.xraymod.property.Property;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final File FILE =
            new File(Minecraft.getMinecraft().mcDataDir, "config/chasm.json");

    public static String bindKey = "NONE";

    // ====================================================
    // LOAD
    // ====================================================
    public static void load() {
        try {
            if (!FILE.exists()) {
                save(); // create default file
                return;
            }

            FileReader reader = new FileReader(FILE);
            Map<String, Object> map = gson.fromJson(reader, Map.class);
            reader.close();

            // Load enabled state
            if (map.containsKey("enabled")) {
                boolean enabled = Boolean.parseBoolean(map.get("enabled").toString());
                XrayMod.xray.setEnabled(enabled);
            }

            // Load bind
            if (map.containsKey("bind")) {
                bindKey = map.get("bind").toString().toUpperCase();
            }

            // Load all properties
            for (Field f : XrayMod.xray.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                Object o = f.get(XrayMod.xray);

                if (o instanceof Property) {
                    Property<?> p = (Property<?>) o;
                    Object raw = map.get(p.getName());

                    if (raw != null) {
                        // raw is clean: true, false, number, string
                        p.parseString(raw.toString());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ====================================================
    // SAVE
    // ====================================================
    public static void save() {
        try {
            if (!FILE.getParentFile().exists()) {
                FILE.getParentFile().mkdirs();
            }

            Map<String, Object> map = new HashMap<>();

            // Save enabled state
            map.put("enabled", XrayMod.xray.isEnabled());

            // Save all properties (RAW values only)
            for (Field f : XrayMod.xray.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                Object o = f.get(XrayMod.xray);

                if (o instanceof Property) {
                    Property<?> p = (Property<?>) o;
                    map.put(p.getName(), p.getValue());
                }
            }

            // Save bind
            map.put("bind", bindKey);

            FileWriter writer = new FileWriter(FILE);
            gson.toJson(map, writer);
            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
