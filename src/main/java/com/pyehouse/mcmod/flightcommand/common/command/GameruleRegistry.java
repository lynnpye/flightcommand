package com.pyehouse.mcmod.flightcommand.common.command;

import com.sun.org.apache.xpath.internal.operations.Bool;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.BooleanValue;
import net.minecraft.world.GameRules.Category;
import net.minecraft.world.GameRules.RuleKey;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;

public class GameruleRegistry {
    private static final Logger LOGGER = LogManager.getLogger();

    public static RuleKey<BooleanValue> doCreativeFlight;

    public static void setupGamerules() {
        doCreativeFlight = createBoolean("doCreativeFlight", false, Category.PLAYER);
    }

    private static RuleKey<BooleanValue> createBoolean(String id, boolean defaultVal, Category cat) {
        try {
            Method m = ObfuscationReflectionHelper.findMethod(GameRules.BooleanValue.class, "func_223571_a", boolean.class);
            m.setAccessible(true);
            GameRules.RuleType<BooleanValue> ruleTypeBoolean = (GameRules.RuleType<BooleanValue>) m.invoke(null, defaultVal);
            RuleKey<BooleanValue> rule = GameRules.register(id, cat, ruleTypeBoolean);
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

    public static boolean isEnabled(World world, RuleKey<BooleanValue> key) {
        return world.getGameRules().getBoolean(key);
    }

    public static boolean isEnabled(IWorld world, RuleKey<BooleanValue> key) {
        if (!(world instanceof World)) {
            return false;
        }
        return ((World) world).getGameRules().getBoolean(key);
    }
}
