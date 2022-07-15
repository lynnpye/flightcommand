package com.pyehouse.mcmod.flightcommand.api.capability;

import com.pyehouse.mcmod.flightcommand.api.util.DataHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
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
    private IFlightCapability flightCapability = new FlightCapability();
    private final LazyOptional<IFlightCapability> flightCapLazyOptional = LazyOptional.of(() -> this.flightCapability);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        var x = FlightCapability.CAPABILITY_FLIGHT.orEmpty(cap, flightCapLazyOptional);
        x.isPresent();
        return x;
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
    private final String KEY_WORLDFLIGHT_ENABLED = "worldFlightEnabled";
    private final String KEY_CHECK_FLIGHT = "checkFlight";

    private Tag writeTag() {
        CompoundTag tag = new CompoundTag();
        flightCapLazyOptional.ifPresent(fcap -> {
            tag.put(KEY_ALLOWED_FLIGHT, IntTag.valueOf(fcap.isAllowedFlight() ? 1 : 0));
            tag.put(KEY_WORLDFLIGHT_ENABLED, IntTag.valueOf(fcap.isWorldFlightEnabled() ? 1 : 0));
            tag.put(KEY_CHECK_FLIGHT, IntTag.valueOf(fcap.isShouldCheckFlight() ? 1 : 0));
        });
        return tag;
    }

    private void readTag(Tag tag) {
        flightCapLazyOptional.ifPresent(fcap -> {
            boolean allowedFlight = false;
            boolean worldFlightEnabled = false;
            boolean shouldCheckFlight = true;
            if (tag instanceof CompoundTag) {
                CompoundTag compoundTag = (CompoundTag) tag;
                allowedFlight = compoundTag.getInt(KEY_ALLOWED_FLIGHT) != 0;
                worldFlightEnabled = compoundTag.getInt(KEY_WORLDFLIGHT_ENABLED) != 0;
                shouldCheckFlight = compoundTag.getInt(KEY_CHECK_FLIGHT) != 0;
            }
            fcap.setAllowedFlight(allowedFlight);
            fcap.setWorldFlightEnabled(worldFlightEnabled);
            fcap.setShouldCheckFlight(shouldCheckFlight);
        });
    }
}
