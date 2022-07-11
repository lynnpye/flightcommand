package com.pyehouse.mcmod.flightcommand.api.capability;

import com.pyehouse.mcmod.flightcommand.common.util.DataHelper;
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

    private final Direction NO_SPECIFIC_SIDE = null;

    private static final Logger LOGGER = LogManager.getLogger();

    private final static String FLIGHT_NBT = "flight";
    private FlightCapability flightCapability = new FlightCapability();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (FlightCapability.CAPABILITY_FLIGHT == cap) {
            return (LazyOptional<T>)LazyOptional.of(() -> flightCapability);
        }

        return LazyOptional.empty();
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
        tag.put(KEY_ALLOWED_FLIGHT, IntTag.valueOf(flightCapability.isAllowedFlight() ? 1 : 0));
        tag.put(KEY_WORLDFLIGHT_ENABLED, IntTag.valueOf(flightCapability.isWorldFlightEnabled() ? 1 : 0));
        tag.put(KEY_CHECK_FLIGHT, IntTag.valueOf(flightCapability.isShouldCheckFlight() ? 1 : 0));
        return tag;
    }

    private void readTag(Tag tag) {
        boolean allowedFlight = false;
        boolean worldFlightEnabled = false;
        boolean shouldCheckFlight = true;
        if (tag instanceof  CompoundTag) {
            CompoundTag compoundTag = (CompoundTag) tag;
            allowedFlight = compoundTag.getInt(KEY_ALLOWED_FLIGHT) != 0;
            worldFlightEnabled = compoundTag.getInt(KEY_WORLDFLIGHT_ENABLED) != 0;
            shouldCheckFlight = compoundTag.getInt(KEY_CHECK_FLIGHT) != 0;
        }
        flightCapability.setAllowedFlight(allowedFlight);
        flightCapability.setWorldFlightEnabled(worldFlightEnabled);
        flightCapability.setShouldCheckFlight(shouldCheckFlight);
    }
}
