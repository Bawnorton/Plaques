package com.bawnorton.plaques.fabric;

import com.bawnorton.plaques.Plaques;
import net.fabricmc.api.ModInitializer;

public class PlaquesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Plaques.init();
    }
}
