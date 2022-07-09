package com.pyehouse.mcmod.flightcommand.api.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.IntTag;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
//import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

import javax.annotation.Nullable;

public class FlightCapability implements IFlightCapability {
    public static final ResourceLocation FlightCapabilityResourceURL =
            new ResourceLocation("flightcommand:flight_capability_provider_entities");

    // capability registration
    //@CapabilityInject(IFlightCapability.class)
    //public static Capability<IFlightCapability> CAPABILITY_FLIGHT = null;
    public static Capability<IFlightCapability> CAPABILITY_FLIGHT = CapabilityManager.get(new CapabilityToken<>(){});

    /*
    public static void registerFlightCapability() {
        CapabilityManager.INSTANCE.register(
                IFlightCapability.class,
                //new FlightCapabilityNBTStorage(),
                FlightCapability::createADefaultInstance
        );
    }

     */

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

    /*
    public static class FlightCapabilityNBTStorage implements Capability.IStorage<IFlightCapability> {

        public final String KEY_ALLOWED_FLIGHT = "allowedFlight";
        public final String KEY_WORLDFLIGHT_ENABLED = "worldFlightEnabled";
        public final String KEY_CHECK_FLIGHT = "checkFlight";

        @Nullable
        @Override
        public Tag writeNBT(Capability<IFlightCapability> capability, IFlightCapability instance, Direction side) {
            CompoundTag compoundNBT = new CompoundTag();
            compoundNBT.put(KEY_ALLOWED_FLIGHT, IntTag.valueOf(instance.isAllowedFlight() ? 1 : 0));
            compoundNBT.put(KEY_WORLDFLIGHT_ENABLED, IntTag.valueOf(instance.isWorldFlightEnabled() ? 1 : 0));
            compoundNBT.put(KEY_CHECK_FLIGHT, IntTag.valueOf(instance.isShouldCheckFlight() ? 1 : 0));
            return compoundNBT;
        }

        @Override
        public void readNBT(Capability<IFlightCapability> capability, IFlightCapability instance, Direction side, Tag nbt) {
            boolean allowedFlight = false;
            boolean worldFlightEnabled = false;
            boolean shouldCheckFlight = true;
            if (nbt instanceof CompoundTag) {
                CompoundTag compoundNBT = (CompoundTag) nbt;
                allowedFlight = compoundNBT.getInt(KEY_ALLOWED_FLIGHT) != 0;
                worldFlightEnabled = compoundNBT.getInt(KEY_WORLDFLIGHT_ENABLED) != 0;
                shouldCheckFlight = compoundNBT.getInt(KEY_CHECK_FLIGHT) != 0;
            }
            instance.setAllowedFlight(allowedFlight);
            instance.setWorldFlightEnabled(worldFlightEnabled);
            instance.setShouldCheckFlight(shouldCheckFlight);
        }
    }

     */

    public static IFlightCapability createADefaultInstance() {
        return new FlightCapability();
    }

}