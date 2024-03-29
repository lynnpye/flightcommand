package com.pyehouse.mcmod.flightcommand.api.capability;

import com.pyehouse.mcmod.flightcommand.api.util.DataHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityProviderPlayers implements ICapabilitySerializable<Tag> {

    private static final Logger LOGGER = LogManager.getLogger();

    private final static String FLIGHT_NBT = "flight";
    private final IFlightCapability flightCapability = new FlightCapability();
    private final LazyOptional<IFlightCapability> flightCapLazyOptional = LazyOptional.of(() -> this.flightCapability);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return FlightCapability.CAPABILITY_FLIGHT.orEmpty(cap, flightCapLazyOptional);
    }

    public IFlightCapability getFlightCapability() {
        return flightCapability;
    }

    public void invalidate() {
        flightCapLazyOptional.invalidate();
    }

    @Override
    public Tag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        Tag flightTag = this.writeTag();
        tag.put(FLIGHT_NBT, flightTag);
        return tag;
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        if (nbt.getId() != DataHelper.COMPOUND_NBT_ID) {
            LOGGER.warn("Unexpected NBT type:" + nbt);
            return;
        }
        CompoundTag compoundNBT = (CompoundTag) nbt;
        Tag flightTag = compoundNBT.get(FLIGHT_NBT);

        this.readTag(flightTag);
    }

    private final String KEY_ALLOWED_FLIGHT = "allowedFlight";
    private final String KEY_CHECK_FLIGHT = "checkFlight";
    private final String KEY_FLYING = "flying";

    private Tag writeTag() {
        CompoundTag tag = new CompoundTag();
        flightCapLazyOptional.ifPresent(fcap -> {
            tag.putBoolean(KEY_ALLOWED_FLIGHT, fcap.isAllowedFlight());
            tag.putBoolean(KEY_CHECK_FLIGHT, fcap.isShouldCheckFlight());
            tag.putBoolean(KEY_FLYING, fcap.isFlying());
        });
        return tag;
    }

    private void readTag(Tag tag) {
        flightCapLazyOptional.ifPresent(fcap -> {
            boolean allowedFlight = false;
            boolean shouldCheckFlight = true;
            boolean flying = false;
            if (tag instanceof CompoundTag) {
                CompoundTag compoundTag = (CompoundTag) tag;
                allowedFlight = compoundTag.getBoolean(KEY_ALLOWED_FLIGHT);
                shouldCheckFlight = compoundTag.getBoolean(KEY_CHECK_FLIGHT);
                flying = compoundTag.getBoolean(KEY_FLYING);
            }
            fcap.setAllowedFlight(allowedFlight);
            fcap.setShouldCheckFlight(shouldCheckFlight);
            fcap.setFlying(flying);
        });
    }
}
