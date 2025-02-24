package com.beanfeed.offlineWhitelist.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WhitelistConfig {
    private Map<String, Object> whitelist;
    private final Path path;
    public WhitelistConfig(Path path) {
        this.path = path;
        try {
            this.loadConfig();
        } catch (IOException e) {

        }
    }

    private void loadConfig() throws IOException {
        //Check if the offlinewhitelist/whitelist.yml file exists, if not create it
        if (!new File(path.toString()).exists()) {
            new File(path.toString()).mkdir();
        }
        if (!new File(path + "/whitelist.yml").exists()) {
            new File(path + "/whitelist.yml").createNewFile();
            whitelist = new HashMap<>();
            whitelist.put("whitelist", new ArrayList<String>());
            whitelist.put("floodgateWhitelist", new ArrayList<String>());
            whitelist.put("uuidWhitelist", new ArrayList<UserProfile>());
            this.saveConfig();
        }
        //Load the whitelist.yml file
        Yaml yaml = new Yaml();
        this.whitelist =  yaml.load(new FileInputStream(new File(path + "/whitelist.yml")));
        if(!whitelist.containsKey("whitelist")) {
            whitelist.put("whitelist", new ArrayList<String>());
            this.saveConfig();
        }
        if(!whitelist.containsKey("floodgateWhitelist")) {
            whitelist.put("floodgateWhitelist", new ArrayList<String>());
            this.saveConfig();
        }
        if(!whitelist.containsKey("uuidWhitelist")) {
            whitelist.put("uuidWhitelist", new ArrayList<UserProfile>());
            this.saveConfig();
        }
    }

    private void saveConfig() throws IOException {
        //convert yaml to string and save to whitelist.yml
        Yaml yaml = new Yaml();
        StringWriter writer = new StringWriter();
        yaml.dump(this.whitelist, writer);
        var fileWriter = new FileWriter(path + "/whitelist.yml");
        fileWriter.write(writer.toString());
        fileWriter.close();
    }

    public void addWhitelist(String ipAddress) {
        ((ArrayList<String>)whitelist.get("whitelist")).add(ipAddress);
        try {
            this.saveConfig();
        } catch (IOException e) {
        }
    }

    public void removeWhitelist(String ipAddress) {
        ((ArrayList<String>)whitelist.get("whitelist")).remove(ipAddress);
        try {
            this.saveConfig();
        } catch (IOException e) {
        }
    }

    public void addFloodgateWhitelist(String ipAddress) {
        ((ArrayList<String>)whitelist.get("floodgateWhitelist")).add(ipAddress);
        try {
            this.saveConfig();
        } catch (IOException e) {
        }
    }

    public void removeFloodgateWhitelist(String ipAddress) {
        ((ArrayList<String>)whitelist.get("floodgateWhitelist")).remove(ipAddress);
        try {
            this.saveConfig();
        } catch (IOException e) {
        }
    }

    public void addUUIDWhitelist(UserProfile user) {
        ((ArrayList<UserProfile>)whitelist.get("uuidWhitelist")).add(user);
        try {
            this.saveConfig();
        } catch (IOException e) {
        }
    }

    public void removeUUIDWhitelist(UserProfile user) {
        ((ArrayList<UserProfile>)whitelist.get("uuidWhitelist")).remove(user);
        try {
            this.saveConfig();
        } catch (IOException e) {
        }
    }

    public ArrayList<String> getWhitelist() throws IOException {
        this.loadConfig();
        return new ArrayList<>((ArrayList<String>)whitelist.get("whitelist"));
    }

    public ArrayList<String> getFloodgateWhitelist() throws IOException {
        this.loadConfig();
        return new ArrayList<>((ArrayList<String>)whitelist.get("floodgateWhitelist"));
    }

    public ArrayList<UserProfile> getUUIDWhitelist() throws IOException {
        this.loadConfig();
        return new ArrayList<>((ArrayList<UserProfile>)whitelist.get("uuidWhitelist"));
    }
}