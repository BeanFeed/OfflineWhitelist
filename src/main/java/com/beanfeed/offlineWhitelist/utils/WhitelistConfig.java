package com.beanfeed.offlineWhitelist.utils;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WhitelistConfig {
    private WhitelistData whitelist;
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
            whitelist = new WhitelistData();
            this.saveConfig();
        }
        //Load the whitelist.yml file
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        Yaml yaml = new Yaml(options);
        this.whitelist =  yaml.loadAs(new FileInputStream(new File(path + "/whitelist.yml")), WhitelistData.class);

    }

    private void saveConfig() throws IOException {
        //convert yaml to string and save to whitelist.yml
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        Yaml yaml = new Yaml(options);
        StringWriter writer = new StringWriter();
        yaml.dump(this.whitelist, writer);
        var fileWriter = new FileWriter(path + "/whitelist.yml");
        fileWriter.write(writer.toString());
        fileWriter.close();
    }

    public void addWhitelist(String ipAddress) {
        whitelist.whitelist.add(ipAddress);
        try {
            this.saveConfig();
        } catch (IOException e) {
        }
    }

    public void removeWhitelist(String ipAddress) {
        whitelist.whitelist.remove(ipAddress);
        try {
            this.saveConfig();
        } catch (IOException e) {
        }
    }

    public void addFloodgateWhitelist(String ipAddress) {
        whitelist.floodgateWhitelist.add(ipAddress);
        try {
            this.saveConfig();
        } catch (IOException e) {
        }
    }

    public void removeFloodgateWhitelist(String ipAddress) {
        whitelist.floodgateWhitelist.remove(ipAddress);
        try {
            this.saveConfig();
        } catch (IOException e) {
        }
    }

    public void addUUIDWhitelist(UserProfile user) {
        whitelist.uuidWhitelist.add(user);
        try {
            this.saveConfig();
        } catch (IOException e) {
        }
    }

    public void removeUUIDWhitelist(UserProfile user) {
        whitelist.uuidWhitelist.remove(user);
        try {
            this.saveConfig();
        } catch (IOException e) {
        }
    }

    public ArrayList<String> getWhitelist() throws IOException {
        this.loadConfig();
        return new ArrayList<>(whitelist.whitelist);
    }

    public ArrayList<String> getFloodgateWhitelist() throws IOException {
        this.loadConfig();
        return new ArrayList<>(whitelist.floodgateWhitelist);
    }

    public ArrayList<UserProfile> getUUIDWhitelist() throws IOException {
        this.loadConfig();
        return new ArrayList<>(whitelist.uuidWhitelist);
    }
}