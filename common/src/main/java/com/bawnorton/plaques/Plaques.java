package com.bawnorton.plaques;

import com.bawnorton.plaques.block.PlaqueBlocks;
import com.bawnorton.plaques.block.entity.PlaqueBlockEntityTypes;
import com.bawnorton.plaques.item.PlaqueItems;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Plaques {
    public static final String MOD_ID = "plaques";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        PlaqueBlocks.register();
        PlaqueBlockEntityTypes.register();
        PlaqueItems.register();
    }

    public static Identifier id(String id) {
        return new Identifier(MOD_ID, id);
    }
}
