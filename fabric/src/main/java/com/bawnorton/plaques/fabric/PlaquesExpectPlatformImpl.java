package com.bawnorton.plaques.fabric;

import com.bawnorton.plaques.PlaquesExpectPlatform;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class PlaquesExpectPlatformImpl {
    /**
     * This is our actual method to {@link PlaquesExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
