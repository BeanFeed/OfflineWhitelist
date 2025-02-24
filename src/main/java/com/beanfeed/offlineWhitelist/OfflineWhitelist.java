package com.beanfeed.offlineWhitelist;

import com.beanfeed.offlineWhitelist.commands.IpWhitelistCommand;
import com.beanfeed.offlineWhitelist.listeners.ConnectionListener;
import com.beanfeed.offlineWhitelist.utils.WhitelistConfig;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(id = "offlinewhitelist", name = "OfflineWhitelist", version = BuildConstants.VERSION)
public class OfflineWhitelist {

    private final Logger logger;
    private final ProxyServer server;
    private final Path path;
    public static WhitelistConfig whitelistConfig;

    @Inject
    public OfflineWhitelist(Logger logger, ProxyServer server, @DataDirectory Path dataDirectory) {
        this.logger = logger;
        this.server = server;
        this.path = dataDirectory;
        whitelistConfig = new WhitelistConfig(dataDirectory);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server.getEventManager().register(this, new ConnectionListener());
        CommandManager manager = server.getCommandManager();
        CommandMeta meta = manager.metaBuilder("ipwhitelist")
                .plugin(this)
                .build();
        manager.register(meta, new IpWhitelistCommand());
    }
}
