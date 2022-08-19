package com.pyehouse.mcmod.flightcommand.api.util;

import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.plexus.util.ExceptionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AccessHelper {

    private static final Logger LOGGER = LogManager.getLogger();

    public static CapabilityDispatcher getCapabilities(CapabilityProvider capabilityProvider) {
        CapabilityDispatcher capabilityDispatcher = null;
        try {
            Class<CapabilityProvider> capabilityProviderClass = CapabilityProvider.class;
            Method getCapabilitiesMethod = ObfuscationReflectionHelper.findMethod(capabilityProviderClass, "getCapabilities");
            capabilityDispatcher = (CapabilityDispatcher) getCapabilitiesMethod.invoke(capabilityProvider);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error(String.format("stack trace=%s", ExceptionUtils.getStackTrace(e)));
        }

        return capabilityDispatcher;
    }

    public static ICapabilityProvider[] caps(CapabilityDispatcher capabilityDispatcher) {
        ICapabilityProvider[] capabilityProviders = null;

        try {
            Class<CapabilityDispatcher> capabilityDispatcherClass = CapabilityDispatcher.class;
            Field capsField = ObfuscationReflectionHelper.findField(capabilityDispatcherClass, "caps");
            capabilityProviders = (ICapabilityProvider[]) capsField.get(capabilityDispatcher);
        } catch (IllegalAccessException e) {
            LOGGER.error(String.format("stack trace=%s", ExceptionUtils.getStackTrace(e)));
        }

        return capabilityProviders;
    }
}
