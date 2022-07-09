package com.pyehouse.mcmod.flightcommand.common.command;

import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.BooleanValue;
import net.minecraft.world.level.GameRules.Category;
import net.minecraft.world.level.GameRules.Key;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;

public class GameruleRegistry {
    private static final Logger LOGGER = LogManager.getLogger();

    public static Key<BooleanValue> doCreativeFlight;

    public static void setupGamerules() {
        doCreativeFlight = createBoolean("doCreativeFlight", false, Category.PLAYER);
    }

    private static Key<BooleanValue> createBoolean(String id, boolean defaultVal, Category cat) {
        try {
            // SRG name: m_46250_(Z)Lnet/minecraft/world/level/GameRules$Type;
            Method m = //ObfuscationReflectionHelper.findMethod(GameRules.BooleanValue.class, "func_223571_a", boolean.class);
                    //GameRules.BooleanValue.class.getMethod("m_46250_", boolean.class);
                    ObfuscationReflectionHelper.findMethod(GameRules.BooleanValue.class, "m_46250_", boolean.class);
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
        setupGamerules();
    }

    public static boolean isEnabled(Level world, Key<BooleanValue> key) {
        return world.getGameRules().getBoolean(key);
    }

    public static boolean isEnabled(LevelAccessor world, Key<BooleanValue> key) {
        if (!(world instanceof Level)) {
            return false;
        }
        return ((Level) world).getGameRules().getBoolean(key);
    }
}
