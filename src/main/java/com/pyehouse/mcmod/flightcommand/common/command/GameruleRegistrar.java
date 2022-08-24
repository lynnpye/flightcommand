package com.pyehouse.mcmod.flightcommand.common.command;

import com.pyehouse.mcmod.flightcommand.common.network.ClientUpdater;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.BooleanValue;
import net.minecraft.world.level.GameRules.Category;
import net.minecraft.world.level.GameRules.Key;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.plexus.util.ExceptionUtils;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;

public class GameruleRegistrar {
    private static final Logger LOGGER = LogManager.getLogger();


    public static Key<BooleanValue> doCreativeFlight;

    private static Key<BooleanValue> createBoolean(String id, boolean defaultVal, Category cat) {
        try {
            // SRG name: m_46252_(ZLjava/util/function/BiConsumer;)Lnet/minecraft/world/level/GameRules$Type;
            Method m = ObfuscationReflectionHelper
                    .findMethod(GameRules.BooleanValue.class, "m_46252_", boolean.class,
                        BiConsumer.class);
            m.setAccessible(true);
            final BiConsumer<MinecraftServer, GameRules.BooleanValue> newCallback = (minecraftServer, ruleValue) -> {
                ClientUpdater.sendFlightApplication(minecraftServer);
            };
            GameRules.Type<BooleanValue> ruleTypeBoolean =
                    (GameRules.Type<BooleanValue>) m.invoke(null, defaultVal, newCallback);
            Key<BooleanValue> rule = GameRules.register(id, cat, ruleTypeBoolean);
            return rule;
        } catch (Exception e) {
            LOGGER.error(String.format("Error setting up gamerules value: %s", ExceptionUtils.getStackTrace(e)));
        }
        return null;
    }

    @SubscribeEvent
    public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> registration());
    }

    public static void registration() {
        doCreativeFlight = createBoolean("doCreativeFlight", false, Category.PLAYER);
    }

    public static boolean isCreativeFlightEnabled(Player player) {
        try {
            return player.getCommandSenderWorld().getGameRules().getBoolean(GameruleRegistrar.doCreativeFlight);
        } catch (Exception e) {
            LOGGER.error(String.format("stacktrace=%s", ExceptionUtils.getStackTrace(e)));
            return false;
        }
    }
}
