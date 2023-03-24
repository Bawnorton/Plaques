package com.bawnorton.plaques.forge;

import com.bawnorton.plaques.PlaquesExpectPlatform;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class PlaquesExpectPlatformImpl {
    /**
     * This is our actual method to {@link PlaquesExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
