package com.pyehouse.mcmod.flightcommand.server.handler;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerConfigHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final ServerConfig SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

    static {
        final Pair<ServerConfig, ForgeConfigSpec> serverSpecPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        SERVER_SPEC = serverSpecPair.getRight();
        SERVER = serverSpecPair.getLeft();
    }

    public static class ServerConfig {
        public ServerConfig(ForgeConfigSpec.Builder builder) {

        }
    }
}
