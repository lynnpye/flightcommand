package com.pyehouse.mcmod.flightcommand.common.handler;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonConfigHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final CommonConfig COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        final Pair<CommonConfig, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        COMMON_SPEC = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();
    }

    public static class CommonConfig {
        public final ForgeConfigSpec.BooleanValue enableGameruleOnWorldStart;

        public CommonConfig(ForgeConfigSpec.Builder builder) {
            builder.push("Flight Command");
            enableGameruleOnWorldStart = builder
                    .comment("Enable gamerule doCreativeFlight on world start")
                            .define("enableGameruleOnWorldStart", false);
            builder.pop();
        }

        public boolean isEnableGameruleOnWorldStart() {
            return this.enableGameruleOnWorldStart.get();
        }
    }
}
