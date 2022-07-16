package com.pyehouse.mcmod.flightcommand.api.capability;

import com.pyehouse.mcmod.flightcommand.common.network.ClientUpdateMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

public class FlightCapability implements IFlightCapability {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final ResourceLocation FlightCapabilityResourceURL =
            new ResourceLocation("flightcommand:flight_capability_provider_entities");

    // capability registration
    public static Capability<IFlightCapability> CAPABILITY_FLIGHT = CapabilityManager.get(new CapabilityToken<>(){});

    // and implementation
    private boolean allowedFlight;
    private boolean shouldCheckFlight;

    public FlightCapability() { this(false, false); }
    public FlightCapability(boolean allowedFlight, boolean shouldCheckFlight) {
        this.allowedFlight = allowedFlight;
        this.shouldCheckFlight = shouldCheckFlight;
    }

    public boolean isAllowedFlight() { return allowedFlight; }
    public void setAllowedFlight(boolean allowedFlight) { this.allowedFlight = allowedFlight; }
    public boolean isShouldCheckFlight() { return shouldCheckFlight; }
    public void setShouldCheckFlight(boolean shouldCheckFlight) { this.shouldCheckFlight = shouldCheckFlight; }

    @Override
    public void copyFrom(@Nonnull IFlightCapability other) {
        if (other == null) return;
        this.setAllowedFlight(other.isAllowedFlight());
        this.setShouldCheckFlight(other.isShouldCheckFlight());
    }

    @Override
    public void copyFrom(@Nonnull ClientUpdateMessage other) {
        if (other == null) return;
        this.setAllowedFlight(other.isFlightAllowed());
        this.setShouldCheckFlight(other.isCheckFlight());
    }

    @Override
    public String toString() {
        return String.format("FlightCapability{allowedFlight[%s]}", this.allowedFlight);
    }

}