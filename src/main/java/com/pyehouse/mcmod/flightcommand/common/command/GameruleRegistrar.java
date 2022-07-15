package com.pyehouse.mcmod.flightcommand.common.command;

import com.pyehouse.mcmod.flightcommand.common.network.ClientUpdater;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.BooleanValue;
import net.minecraft.world.GameRules.Category;
import net.minecraft.world.GameRules.RuleKey;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.plexus.util.ExceptionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;

public class GameruleRegistrar {
    private static final Logger LOGGER = LogManager.getLogger();

    public static RuleKey<BooleanValue> doCreativeFlight;

    private static RuleKey<BooleanValue> createBoolean(String id, boolean defaultVal, Category cat) {
        try {
            // SRG name: func_223567_b
            Method m = ObfuscationReflectionHelper
                    .findMethod(GameRules.BooleanValue.class, "func_223567_b", boolean.class,
                        BiConsumer.class);
            m.setAccessible(true);
            final BiConsumer<MinecraftServer, GameRules.BooleanValue> newCallback = (minecraftServer, ruleValue) -> {
                ClientUpdater.sendFlightApplication(minecraftServer);
            };
            GameRules.RuleType<BooleanValue> ruleTypeBoolean =
                    (GameRules.RuleType<BooleanValue>) m.invoke(null, defaultVal, newCallback);
            RuleKey<BooleanValue> rule = GameRules.register(id, cat, ruleTypeBoolean);
            return rule;
        } catch (Exception e) {
            LOGGER.error("Error setting up gamerules value", e);
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

    public static boolean isCreativeFlightEnabled(PlayerEntity player) {
        try {
            return player.getCommandSenderWorld().getGameRules().getBoolean(GameruleRegistrar.doCreativeFlight);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
    }
}
