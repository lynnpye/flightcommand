package com.pyehouse.mcmod.flightcommand.api.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class FlightCapability implements IFlightCapability {
    public static final ResourceLocation FlightCapabilityResourceURL =
            new ResourceLocation("flightcommand:flight_capability_provider_entities");

    // capability registration
    @CapabilityInject(IFlightCapability.class)
    public static Capability<IFlightCapability> CAPABILITY_FLIGHT = null;

    public static void registerFlightCapability() {
        CapabilityManager.INSTANCE.register(
                IFlightCapability.class,
                new FlightCapabilityNBTStorage(),
                FlightCapability::createADefaultInstance
        );
    }

    // and implementation
    private boolean allowedFlight;
    private boolean worldFlightEnabled;
    private boolean shouldCheckFlight;

    public FlightCapability() { this(false, false); }
    public FlightCapability(boolean allowedFlight, boolean worldFlightEnabled) { this(allowedFlight, worldFlightEnabled, false); }
    public FlightCapability(boolean allowedFlight, boolean worldFlightEnabled, boolean shouldCheckFlight) {
        this.allowedFlight = allowedFlight;
        this.worldFlightEnabled = worldFlightEnabled;
        this.shouldCheckFlight = shouldCheckFlight;
    }

    public boolean isAllowedFlight() { return allowedFlight; }
    public void setAllowedFlight(boolean allowedFlight) { this.allowedFlight = allowedFlight; }
    public boolean isWorldFlightEnabled() { return worldFlightEnabled; }
    public void setWorldFlightEnabled(boolean worldFlightEnabled) { this.worldFlightEnabled = worldFlightEnabled; }
    public boolean isShouldCheckFlight() { return shouldCheckFlight; }
    public void setShouldCheckFlight(boolean shouldCheckFlight) { this.shouldCheckFlight = shouldCheckFlight; }

    public static class FlightCapabilityNBTStorage implements Capability.IStorage<IFlightCapability> {

        public final String KEY_ALLOWED_FLIGHT = "allowedFlight";
        public final String KEY_WORLDFLIGHT_ENABLED = "worldFlightEnabled";
        public final String KEY_CHECK_FLIGHT = "checkFlight";

        @Nullable
        @Override
        public INBT writeNBT(Capability<IFlightCapability> capability, IFlightCapability instance, Direction side) {
            CompoundNBT compoundNBT = new CompoundNBT();
            compoundNBT.put(KEY_ALLOWED_FLIGHT, IntNBT.valueOf(instance.isAllowedFlight() ? 1 : 0));
            compoundNBT.put(KEY_WORLDFLIGHT_ENABLED, IntNBT.valueOf(instance.isWorldFlightEnabled() ? 1 : 0));
            compoundNBT.put(KEY_CHECK_FLIGHT, IntNBT.valueOf(instance.isShouldCheckFlight() ? 1 : 0));
            return compoundNBT;
        }

        @Override
        public void readNBT(Capability<IFlightCapability> capability, IFlightCapability instance, Direction side, INBT nbt) {
            boolean allowedFlight = false;
            boolean worldFlightEnabled = false;
            boolean shouldCheckFlight = true;
            if (nbt instanceof CompoundNBT) {
                CompoundNBT compoundNBT = (CompoundNBT) nbt;
                allowedFlight = compoundNBT.getInt(KEY_ALLOWED_FLIGHT) != 0;
                worldFlightEnabled = compoundNBT.getInt(KEY_WORLDFLIGHT_ENABLED) != 0;
                shouldCheckFlight = compoundNBT.getInt(KEY_CHECK_FLIGHT) != 0;
            }
            instance.setAllowedFlight(allowedFlight);
            instance.setWorldFlightEnabled(worldFlightEnabled);
            instance.setShouldCheckFlight(shouldCheckFlight);
        }
    }

    public static IFlightCapability createADefaultInstance() {
        return new FlightCapability();
    }

}