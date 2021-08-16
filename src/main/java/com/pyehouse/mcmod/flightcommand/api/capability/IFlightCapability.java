package com.pyehouse.mcmod.flightcommand.api.capability;

public interface IFlightCapability {
    boolean isAllowedFlight();
    void setAllowedFlight(boolean allowedFlight);
    boolean isWorldFlightEnabled();
    void setWorldFlightEnabled(boolean worldFlightEnabled);
}
