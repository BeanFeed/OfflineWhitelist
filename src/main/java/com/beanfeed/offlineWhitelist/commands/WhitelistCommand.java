package com.beanfeed.offlineWhitelist.commands;

import com.beanfeed.offlineWhitelist.OfflineWhitelist;
import com.beanfeed.offlineWhitelist.utils.UserProfile;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import net.kyori.adventure.text.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class WhitelistCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        var mode = invocation.arguments()[0];
        var username = invocation.arguments()[1];
        switch (mode) {
            case "add":
                OfflineWhitelist.whitelistConfig.addUUIDWhitelist(getUserProfile(username));
                break;
            case "remove":
                OfflineWhitelist.whitelistConfig.removeUUIDWhitelist(getUserProfile(username));
                break;
            case "list":
                try {
                    var users = OfflineWhitelist.whitelistConfig.getUUIDWhitelist();
                    StringBuilder sb = new StringBuilder();
                    for (var user : users) {
                        sb.append(user.Username).append(",");
                    }
                    //remove last character in sb
                    sb.deleteCharAt(sb.length() - 1);
                    invocation.source().sendMessage(Component.text(sb.toString()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                // Display usage
                invocation.source().sendMessage(Component.text("Usage: /whitelist <add|remove|list> <username>"));
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

    private UserProfile getUserProfile(String username) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("accept", "application/json");

            int responseCode = connection.getResponseCode();
            if(responseCode == 200) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));

                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                connection.disconnect();
                JsonParser parser = new JsonParser();
                var id = ((JsonObject) JsonParser.parseString(response.toString())).get("id").getAsString();
                return new UserProfile(username, id);
            }
        } catch (ProtocolException e) {
            return null;
        } catch (MalformedURLException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }
}
