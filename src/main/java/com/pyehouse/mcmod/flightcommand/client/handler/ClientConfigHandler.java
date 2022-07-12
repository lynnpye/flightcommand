package com.pyehouse.mcmod.flightcommand.client.handler;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientConfigHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final ClientConfig CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;

    static {
        final Pair<ClientConfig, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT_SPEC = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();

    }

    public static class ClientConfig {
        public final ForgeConfigSpec.BooleanValue bogusBooleanOption;

        public ClientConfig(ForgeConfigSpec.Builder builder) {
            builder.push("selection");
            bogusBooleanOption = builder
                    .comment("Some random bogus option. True or false, doesn't matter.")
                    .define("bogusBooleanOption", false);
            builder.pop();
        }
    }
}
