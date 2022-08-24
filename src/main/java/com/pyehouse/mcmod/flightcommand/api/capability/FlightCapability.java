package com.pyehouse.mcmod.flightcommand.api.capability;

import com.pyehouse.mcmod.flightcommand.api.util.AccessHelper;
import com.pyehouse.mcmod.flightcommand.common.network.ClientUpdateMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
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
    private boolean flying;

    public FlightCapability() { this(false, false, false); }
    public FlightCapability(boolean allowedFlight, boolean shouldCheckFlight, boolean flying) {
        this.allowedFlight = allowedFlight;
        this.shouldCheckFlight = shouldCheckFlight;
        this.flying = flying;
    }

    public boolean isAllowedFlight() { return allowedFlight; }
    public void setAllowedFlight(boolean allowedFlight) { this.allowedFlight = allowedFlight; }
    public boolean isShouldCheckFlight() { return shouldCheckFlight; }
    public void setShouldCheckFlight(boolean shouldCheckFlight) { this.shouldCheckFlight = shouldCheckFlight; }
    public boolean isFlying() { return flying; }
    public void setFlying(boolean flying) { this.flying = flying; }

    @Override
    public void copyFrom(@Nonnull IFlightCapability other) {
        if (other == null) return;
        this.setAllowedFlight(other.isAllowedFlight());
        this.setShouldCheckFlight(other.isShouldCheckFlight());
        this.setFlying(other.isFlying());
    }

    @Override
    public void copyFrom(@Nonnull ClientUpdateMessage other) {
        if (other == null) return;
        this.setAllowedFlight(other.isFlightAllowed());
        this.setShouldCheckFlight(other.isCheckFlight());
        this.setFlying(other.isFlying());
    }

    @Override
    public String toString() {
        return String.format("FlightCapability{allowedFlight[%s] flying[%s]}", this.allowedFlight, this.flying);
    }

    public static void cloneForPlayer(Player oldPlayer, Player newPlayer) {
        oldPlayer.reviveCaps();
        newPlayer.getCapability(FlightCapability.CAPABILITY_FLIGHT).ifPresent(newCap -> {
            CapabilityDispatcher capabilityDispatcher = AccessHelper.getCapabilities(oldPlayer);
            for (var capPro : AccessHelper.caps(capabilityDispatcher)) {
                if (capPro instanceof CapabilityProviderPlayers) {
                    CapabilityProviderPlayers myPro = (CapabilityProviderPlayers) capPro;
                    newCap.copyFrom(myPro.getFlightCapability());
                }
            }
        });
        oldPlayer.invalidateCaps();
    }

}