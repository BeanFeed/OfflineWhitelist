package com.beanfeed.offlineWhitelist.utils;

import java.util.ArrayList;
import java.util.List;

public class WhitelistData {
    public List<String> whitelist;
    public List<String> floodgateWhitelist;
    public List<UserProfile> uuidWhitelist;

    // Default constructor (required for SnakeYAML deserialization)
    public WhitelistData() {
        this.whitelist = new ArrayList<>();
        this.floodgateWhitelist = new ArrayList<>();
        this.uuidWhitelist = new ArrayList<>();
    }

    // Constructor with parameters
    public WhitelistData(ArrayList<String> whitelist, ArrayList<String> floodgateWhitelist, ArrayList<UserProfile> uuidWhitelist) {
        this.whitelist = whitelist;
        this.floodgateWhitelist = floodgateWhitelist;
        this.uuidWhitelist = uuidWhitelist;
    }
}
