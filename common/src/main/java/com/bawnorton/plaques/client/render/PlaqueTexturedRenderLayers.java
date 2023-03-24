package com.bawnorton.plaques.client.render;

import com.bawnorton.plaques.Plaques;
import com.bawnorton.plaques.util.PlaqueType;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PlaqueTexturedRenderLayers {
    public static final Identifier PLAQUE_ATLAS_TEXTURE = Plaques.id("textures/atlas/plaques.png");
    public static final Map<PlaqueType, SpriteIdentifier> STONE_TYPE_TEXTUES;

    public static SpriteIdentifier getPlaqueTextureId(PlaqueType plaqueType) {
        return STONE_TYPE_TEXTUES.get(plaqueType);
    }

    private static SpriteIdentifier createPlaqueTextureId(PlaqueType type) {
        return new SpriteIdentifier(PLAQUE_ATLAS_TEXTURE, Plaques.id("entity/plaques/" + type.getName()));
    }

    static {
        STONE_TYPE_TEXTUES = Arrays.stream(PlaqueType.values()).collect(Collectors.toMap(Function.identity(), PlaqueTexturedRenderLayers::createPlaqueTextureId));
    }
}
