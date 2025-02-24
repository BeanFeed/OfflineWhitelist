package com.beanfeed.offlineWhitelist.commands;

import com.beanfeed.offlineWhitelist.OfflineWhitelist;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;

import java.util.List;

public class IpWhitelistCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        var mode = invocation.arguments()[0];
        var ip = invocation.arguments()[1];
        switch (mode) {
            case "add":
                OfflineWhitelist.whitelistConfig.addWhitelist(ip);
                break;
            case "remove":
                OfflineWhitelist.whitelistConfig.removeWhitelist(ip);
                break;
            case "list":
                try {
                    invocation.source().sendMessage(Component.text(String.join(",",OfflineWhitelist.whitelistConfig.getWhitelist())));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                // Display usage
                invocation.source().sendMessage(Component.text("Usage: /ipwhitelist <add|remove|list> <ip>"));
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
    /*
    public static BrigadierCommand create(ProxyServer server) {
        LiteralCommandNode<CommandSource> whitelist = BrigadierCommand.literalArgumentBuilder("ipwhitelist")
                .requires(commandSource -> commandSource.hasPermission("minecraft.command.whitelist"))
                .executes(context -> {
                    var mode = context.getArgument("mode", String.class);
                    var ip = context.getArgument("ip", String.class);
                    switch (mode) {
                        case "add":
                            OfflineWhitelist.whitelistConfig.addWhitelist(ip);
                            // Add the IP to the whitelist
                            break;
                        case "remove":
                            OfflineWhitelist.whitelistConfig.removeWhitelist(ip);
                            // Remove the IP from the whitelist
                            break;
                        case "list":
                            StringBuilder ips = new StringBuilder();
                            try {
                                context.getSource().sendMessage(Component.text(String.join(",",OfflineWhitelist.whitelistConfig.getWhitelist())));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            // List all IPs in the whitelist
                            break;
                        default:
                            // Display usage
                            context.getSource().sendMessage(Component.text("Usage: /ipwhitelist <add|remove|list> <ip>"));
                            break;
                    }
                    return Command.SINGLE_SUCCESS;
                })
                .build();

        return new BrigadierCommand(whitelist);

    }

     */
}
