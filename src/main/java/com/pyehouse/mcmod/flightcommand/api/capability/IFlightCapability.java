package com.pyehouse.mcmod.flightcommand.api.capability;

import com.pyehouse.mcmod.flightcommand.common.network.ClientUpdateMessage;

import javax.annotation.Nonnull;

public interface IFlightCapability {
    boolean isAllowedFlight();
    void setAllowedFlight(boolean allowedFlight);
    boolean isShouldCheckFlight();
    void setShouldCheckFlight(boolean checkFlight);
    void copyFrom(@Nonnull IFlightCapability other);
    void copyFrom(@Nonnull ClientUpdateMessage other);
}
