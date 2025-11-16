package com.xraymod.command;

import com.xraymod.XrayMod;
import com.xraymod.property.Property;
import com.xraymod.util.BindManager;
import com.xraymod.util.ChatUtil;
import com.xraymod.util.ConfigManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class XrayCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "xray";
    }
    private static final String[] VALID_KEYS = {
            "F1","F2","F3","F4","F5","F6","F7","F8","F9","F10","F11","F12",
            "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
            "0","1","2","3","4","5","6","7","8","9",
            "ESC","SPACE","ENTER","SHIFT","CTRL","ALT","TAB","BACKSPACE","DELETE",
            "HOME","END","PAGE_UP","PAGE_DOWN",
            "UP","DOWN","LEFT","RIGHT",
            "NUMPAD0","NUMPAD1","NUMPAD2","NUMPAD3","NUMPAD4","NUMPAD5","NUMPAD6","NUMPAD7","NUMPAD8","NUMPAD9",
            "NUMPAD_ADD","NUMPAD_SUBTRACT","NUMPAD_MULTIPLY","NUMPAD_DIVIDE",
            "CAPS_LOCK","SCROLL_LOCK","INSERT","PRINT_SCREEN","PAUSE"
    };

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/xray [on|off|<property> <value>]";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            ChatUtil.sendMessage("§6§lChasm");

            for (Field field : XrayMod.xray.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    Object obj = field.get(XrayMod.xray);
                    if (obj instanceof Property) {
                        Property<?> prop = (Property<?>) obj;
                        ChatUtil.sendMessage("§e" + prop.getName() + ": §r" + prop.formatValue());
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            return;
        }


        String subCommand = args[0].toLowerCase();
        if (subCommand.equals("bind")) {
            if (args.length < 2) {
                ChatUtil.sendMessage("§cUsage: §r/xray bind <KEY>");
                return;
            }

            String key = args[1].toUpperCase();
            ConfigManager.bindKey = key;
            ConfigManager.save();

            ChatUtil.sendMessage("§aBind set to: §e" + key);
            return;
        }


        if (subCommand.equals("on")) {
            XrayMod.xray.setEnabled(true);
            ChatUtil.sendMessage("Xray: §a§lON");
            return;
        }

        if (subCommand.equals("off")) {
            XrayMod.xray.setEnabled(false);
            ChatUtil.sendMessage("Xray: §c§lOFF");
            return;
        }

        // Handle property setting
        Property<?> property = getProperty(subCommand);
        if (property == null) {
            ChatUtil.sendMessage("§cUnknown property: " + subCommand);
            return;
        }

        if (args.length == 1) {
            ChatUtil.sendMessage("§e" + property.getName() + ": §r" + property.formatValue());
            return;
        }

        String value = args[1];
        if (property.parseString(value)) {
            ChatUtil.sendMessage("§aSet §e" + property.getName() + " §ato §r" + property.formatValue());
            XrayMod.xray.verifyValue(property.getName());
        } else {
            ChatUtil.sendMessage("§cInvalid value for " + property.getName() + ". Expected: " + property.getValuePrompt());
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, net.minecraft.util.BlockPos pos) {
        if (args.length == 1) {
            List<String> options = new ArrayList<>();
            options.add("on");
            options.add("off");
            options.add("bind");
            // Add all properties
            for (Field field : XrayMod.xray.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    Object obj = field.get(XrayMod.xray);
                    if (obj instanceof Property) {
                        options.add(((Property<?>) obj).getName());
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            return getListOfStringsMatchingLastWord(args, options.toArray(new String[0]));
        }



        if (args.length == 2) {
            Property<?> property = getProperty(args[0]);
            if (property != null) {
                String prompt = property.getValuePrompt();
                // For boolean properties
                if (prompt.equals("true/false")) {
                    return getListOfStringsMatchingLastWord(args, "true", "false");
                }
                // For mode properties
                if (prompt.contains(",")) {
                    return getListOfStringsMatchingLastWord(args, prompt.split(", "));
                }
            }
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("bind")) {
            return getListOfStringsMatchingLastWord(args, VALID_KEYS);
        }
        return null;
    }

    private Property<?> getProperty(String name) {
        for (Field field : XrayMod.xray.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object obj = field.get(XrayMod.xray);
                if (obj instanceof Property) {
                    Property<?> prop = (Property<?>) obj;
                    if (prop.getName().replace("-", "").equalsIgnoreCase(name.replace("-", ""))) {
                        return prop;
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }}