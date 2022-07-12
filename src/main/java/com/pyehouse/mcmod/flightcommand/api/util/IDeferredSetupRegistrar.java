package com.pyehouse.mcmod.flightcommand.api.util;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.ModLoadingStage;

public interface IDeferredSetupRegistrar {
    void registration();

    default void common() {
        ModLoadingStage.COMMON_SETUP.getDeferredWorkQueue().enqueueWork(
                ModLoadingContext.get().getActiveContainer().getModInfo(),
                () -> this.registration()
        );
    }
}
