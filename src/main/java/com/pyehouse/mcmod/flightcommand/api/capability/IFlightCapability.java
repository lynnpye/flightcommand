package com.pyehouse.mcmod.flightcommand.api.capability;

public interface IFlightCapability {
    boolean isAllowedFlight();
    void setAllowedFlight(boolean allowedFlight);
    boolean isShouldCheckFlight();
    void setShouldCheckFlight(boolean checkFlight);
    boolean isWorldFlightEnabled();
    void setWorldFlightEnabled(boolean worldFlightEnabled);
}
