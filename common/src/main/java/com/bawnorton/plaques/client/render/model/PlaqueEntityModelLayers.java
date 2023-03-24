package com.bawnorton.plaques.client.render.model;

import com.bawnorton.plaques.Plaques;
import com.bawnorton.plaques.util.PlaqueType;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class PlaqueEntityModelLayers {
    public static EntityModelLayer createPlaque(PlaqueType type) {
        return create("plaque/" + type.getName());
    }

    private static EntityModelLayer create(String id) {
        return new EntityModelLayer(Plaques.id(id), "main");
    }
}
