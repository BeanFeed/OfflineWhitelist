package com.beanfeed.offlineWhitelist.commands;

import com.beanfeed.offlineWhitelist.OfflineWhitelist;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;

import java.util.List;

public class FloodgateWhitelist implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        var mode = invocation.arguments()[0];
        String name;
        switch (mode) {
            case "add":
                name = invocation.arguments()[1];
                OfflineWhitelist.whitelistConfig.addFloodgateWhitelist(name);
                break;
            case "remove":
                name = invocation.arguments()[1];
                OfflineWhitelist.whitelistConfig.removeFloodgateWhitelist(name);
                break;
            case "list":
                try {
                    invocation.source().sendMessage(Component.text(String.join(",",OfflineWhitelist.whitelistConfig.getFloodgateWhitelist())));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                // Display usage
                invocation.source().sendMessage(Component.text("Usage: /floodgatewhitelist <add|remove|list> <ip>"));
                break;
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("minecraft.command.whitelist");
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        if(invocation.arguments().length == 0) {
            return List.of("add", "remove", "list");
        } else {
            return List.of();
        }
    }
}
