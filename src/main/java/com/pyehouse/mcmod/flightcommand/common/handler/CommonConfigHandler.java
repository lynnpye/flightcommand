package com.pyehouse.mcmod.flightcommand.common.handler;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonConfigHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final CommonConfig COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    public static final String VAR_enableGameruleOnWorldStart = "enableGameruleOnWorldStart";
    public static final String VAR_flightCommand = "flightCommand";

    public static final String DEFAULT_flightCommand = "flight";

    static {
        final Pair<CommonConfig, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        COMMON_SPEC = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();
    }

    public static class CommonConfig {
        public final ForgeConfigSpec.BooleanValue enableGameruleOnWorldStart;
        public final ForgeConfigSpec.ConfigValue<String> flightCommand;

        public CommonConfig(ForgeConfigSpec.Builder builder) {
            builder.push("Enable Gamerule On Start");
            enableGameruleOnWorldStart = builder
                    .comment("Enable gamerule doCreativeFlight on world start")
                            .define(VAR_enableGameruleOnWorldStart, false);
            builder.pop();

            builder.push("Command to Enable Flight");
            flightCommand = builder
                    .comment("Change this if you want to use a different command to grant flight e.g. /goflynow playername true")
                            .define(VAR_flightCommand, DEFAULT_flightCommand);
            builder.pop();
        }

        public boolean isEnableGameruleOnWorldStart() {
            return this.enableGameruleOnWorldStart.get();
        }
    }
}
