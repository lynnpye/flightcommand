package com.pyehouse.mcmod.flightcommand.common.network;

import net.minecraft.network.PacketBuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FlightCommandMessageToClient {
    private static final Logger LOGGER = LogManager.getLogger();

    private boolean messageValid;
    private boolean flightAllowed;
    private boolean worldFlightEnabled;

    public boolean isMessageValid() { return messageValid; }
    public boolean isFlightAllowed() { return flightAllowed; }
    public boolean isWorldFlightEnabled() { return worldFlightEnabled; }

    public FlightCommandMessageToClient() {
        messageValid = false;
    }

    public FlightCommandMessageToClient(boolean flightAllowed, boolean worldFlightEnabled) {
        this.flightAllowed = flightAllowed;
        this.worldFlightEnabled = worldFlightEnabled;
        messageValid = true;
    }

    public static FlightCommandMessageToClient decode(PacketBuffer buf) {
        FlightCommandMessageToClient retval = new FlightCommandMessageToClient();
        try {
            retval.flightAllowed = buf.readBoolean();
            retval.worldFlightEnabled = buf.readBoolean();
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            LOGGER.warn("Exception while reading FlightCommandMessageToClient: " + e);
            return retval;
        }
        retval.messageValid = true;
        return retval;
    }

    public void encode(PacketBuffer buf) {
        if (!messageValid) return;

        buf.writeBoolean(flightAllowed);
        buf.writeBoolean(worldFlightEnabled);
    }

    @Override
    public String toString() {
        return "FlightCommandMessageToClient[flightAllowed=" + Boolean.toString(flightAllowed) + ",worldFlightEnabled=" + Boolean.toString(worldFlightEnabled) + "]";
    }
}
