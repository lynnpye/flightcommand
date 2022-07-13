package com.pyehouse.mcmod.flightcommand.api.capability;

import com.pyehouse.mcmod.flightcommand.common.network.ClientUpdateMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.IntTag;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FlightCapability implements IFlightCapability {
    public static final ResourceLocation FlightCapabilityResourceURL =
            new ResourceLocation("flightcommand:flight_capability_provider_entities");

    // capability registration
    public static Capability<IFlightCapability> CAPABILITY_FLIGHT = CapabilityManager.get(new CapabilityToken<>(){});

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

    @Override
    public void copyFrom(@Nonnull IFlightCapability other) {
        if (other == null) return;
        this.setAllowedFlight(other.isAllowedFlight());
        this.setWorldFlightEnabled(other.isWorldFlightEnabled());
        this.setShouldCheckFlight(other.isShouldCheckFlight());
    }

    @Override
    public void copyFrom(@Nonnull ClientUpdateMessage other) {
        if (other == null) return;
        this.setAllowedFlight(other.isFlightAllowed());
        this.setWorldFlightEnabled(other.isWorldFlightEnabled());
        this.setShouldCheckFlight(other.isCheckFlight());
    }

    @Override
    public String toString() {
        return String.format("FlightCapability{allowedFlight[%s] worldFlightEnabled[%s]}", this.allowedFlight, this.worldFlightEnabled);
    }

}