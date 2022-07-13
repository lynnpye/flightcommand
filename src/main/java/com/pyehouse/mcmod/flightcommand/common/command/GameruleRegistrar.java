package com.pyehouse.mcmod.flightcommand.common.command;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.BooleanValue;
import net.minecraft.world.level.GameRules.Category;
import net.minecraft.world.level.GameRules.Key;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.plexus.util.ExceptionUtils;

import java.lang.reflect.Method;

public class GameruleRegistrar {
    private static final Logger LOGGER = LogManager.getLogger();

    public static Key<BooleanValue> doCreativeFlight;

    private static Key<BooleanValue> createBoolean(String id, boolean defaultVal, Category cat) {
        try {
            // SRG name: m_46250_(Z)Lnet/minecraft/world/level/GameRules$Type;
            Method m = ObfuscationReflectionHelper.findMethod(GameRules.BooleanValue.class, "m_46250_", boolean.class);
            m.setAccessible(true);
            GameRules.Type<BooleanValue> ruleTypeBoolean = (GameRules.Type<BooleanValue>) m.invoke(null, defaultVal);
            Key<BooleanValue> rule = GameRules.register(id, cat, ruleTypeBoolean);
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

    public static boolean isCreativeFlightEnabled(Player player) {
        try {
            return player.getCommandSenderWorld().getGameRules().getBoolean(GameruleRegistrar.doCreativeFlight);
        } catch (Exception e) {
            LOGGER.warn(ExceptionUtils.getStackTrace(e));
            return false;
        }
    }
}
