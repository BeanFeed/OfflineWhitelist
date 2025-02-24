package com.beanfeed.offlineWhitelist.listeners;

import com.beanfeed.offlineWhitelist.OfflineWhitelist;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import net.kyori.adventure.text.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Objects;

public class ConnectionListener {

    @Subscribe
    public void onPlayerConnect(PreLoginEvent event) {
        if(event.getResult().isForceOfflineMode()) {
            try {
                if(OfflineWhitelist.whitelistConfig.getFloodgateWhitelist().contains(event.getUsername()))
                return;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + event.getUsername());
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
                assert event.getUniqueId() != null;
                var eventId = event.getUniqueId().toString().replace("-", "");
                if(!Objects.equals(id, eventId)) {
                    event.setResult(PreLoginEvent.PreLoginComponentResult.denied(Component.text("Username belongs to online player")));
                }
            } else {
                if(OfflineWhitelist.whitelistConfig.getWhitelist().contains(event.getConnection().getRemoteAddress().getAddress().getHostAddress())) {
                    event.setResult(PreLoginEvent.PreLoginComponentResult.forceOfflineMode());
                } else {
                    event.setResult(PreLoginEvent.PreLoginComponentResult.denied(Component.text("You are not whitelisted")));
                }
            }
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //event.setResult(PreLoginEvent.PreLoginComponentResult.forceOfflineMode());

    }

    @Subscribe
    public void onPlayerAuthenticated(LoginEvent event) throws IOException {
        if(event.getPlayer().isOnlineMode()) {
            if(OfflineWhitelist.whitelistConfig.getUUIDWhitelist().stream().noneMatch(userProfile -> userProfile.UUID.equals(event.getPlayer().getUniqueId().toString().replace("-", "")))) {
                event.setResult(ResultedEvent.ComponentResult.denied(Component.text("You are not whitelisted")));
            }
        }
    }
}
