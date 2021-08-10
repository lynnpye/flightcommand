package com.pyehouse.mcmod.flightcommand.api.capability;

import com.pyehouse.mcmod.flightcommand.common.util.DataHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityProviderPlayers implements ICapabilitySerializable<INBT> {

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
    public INBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        INBT flightNBT = FlightCapability.CAPABILITY_FLIGHT.writeNBT(flightCapability, NO_SPECIFIC_SIDE);
        nbt.put(FLIGHT_NBT, flightNBT);
        return nbt;
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        if (nbt.getId() != DataHelper.COMPOUND_NBT_ID) {
            LOGGER.warn("Unexpected NBT type:" + nbt);
            return;
        }
        CompoundNBT compoundNBT = (CompoundNBT) nbt;
        INBT flightNBT = compoundNBT.get(FLIGHT_NBT);

        FlightCapability.CAPABILITY_FLIGHT.readNBT(flightCapability, NO_SPECIFIC_SIDE, flightNBT);
    }
}
