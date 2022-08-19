package com.pyehouse.mcmod.flightcommand.api.capability;

import com.pyehouse.mcmod.flightcommand.common.network.ClientUpdateMessage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FlightCapability implements IFlightCapability {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final ResourceLocation FlightCapabilityResourceURL =
            new ResourceLocation("flightcommand:flight_capability_provider_entities");

    // capability registration
    @CapabilityInject(IFlightCapability.class)
    public static Capability<IFlightCapability> CAPABILITY_FLIGHT = null;

    public static void registerFlightCapability() {
        CapabilityManager.INSTANCE.register(
                IFlightCapability.class,
                new FlightCapabilityNBTStorage(),
                FlightCapability::new
        );
    }

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

    public static class FlightCapabilityNBTStorage implements Capability.IStorage<IFlightCapability> {

        private static final Logger LOGGER = LogManager.getLogger();

        private static final String KEY_ALLOWED_FLIGHT = "allowedFlight";
        private static final String KEY_CHECK_FLIGHT = "checkFlight";
        private static final String KEY_FLYING = "flying";

        @Nullable
        @Override
        public INBT writeNBT(Capability<IFlightCapability> capability, IFlightCapability instance, Direction side) {
            CompoundNBT compoundNBT = new CompoundNBT();
            if (instance != null) {
                compoundNBT.putBoolean(KEY_ALLOWED_FLIGHT, instance.isAllowedFlight());
                compoundNBT.putBoolean(KEY_CHECK_FLIGHT, instance.isShouldCheckFlight());
                compoundNBT.putBoolean(KEY_FLYING, instance.isFlying());
            }
            return compoundNBT;
        }

        @Override
        public void readNBT(Capability<IFlightCapability> capability, IFlightCapability instance, Direction side, INBT nbt) {
            if (instance != null) {
                boolean allowedFlight = false;
                boolean shouldCheck = false;
                boolean flying = false;
                if (nbt instanceof CompoundNBT) {
                    CompoundNBT compoundNBT = (CompoundNBT) nbt;
                    allowedFlight = compoundNBT.getBoolean(KEY_ALLOWED_FLIGHT);
                    shouldCheck = compoundNBT.getBoolean(KEY_CHECK_FLIGHT);
                    flying = compoundNBT.getBoolean(KEY_FLYING);
                }
                instance.setAllowedFlight(allowedFlight);
                instance.setShouldCheckFlight(shouldCheck);
                instance.setFlying(flying);
            }
        }
    }

    @Override
    public String toString() {
        return String.format("FlightCapability{allowedFlight[%s] flying[%s]}", this.allowedFlight, this.flying);
    }

}