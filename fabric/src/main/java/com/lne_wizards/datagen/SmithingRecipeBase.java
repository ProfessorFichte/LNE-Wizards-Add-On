package com.lne_wizards.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class SmithingRecipeBase implements DataProvider {

    protected final FabricDataOutput output;
    protected final String modId;
    private final List<RecipeData> recipes = new ArrayList<>();

    protected SmithingRecipeBase(FabricDataOutput output, String modId) {
        this.output = output;
        this.modId = modId;
    }

    public abstract void generate();

    public void createSmithingTransformRecipe(String name, Item base, Object template, Object addition,
                                              Item result, String[] requiredMods) {
        recipes.add(new RecipeData(name, base, template, addition, result, requiredMods));
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        generate();
        return CompletableFuture.allOf(recipes.stream().map(data -> {
            Path path = output.getResolver(DataOutput.OutputType.DATA_PACK, "recipes")
                    .resolveJson(new Identifier(modId, data.name));
            return DataProvider.writeToPath(writer, buildRecipeJson(data), path);
        }).toArray(CompletableFuture[]::new));
    }

    private JsonObject buildRecipeJson(RecipeData data) {
        JsonObject recipe = new JsonObject();

        if (data.requiredMods != null && data.requiredMods.length > 0) {
            JsonArray fabricLoadConditions = new JsonArray();
            JsonObject fabricCondition = new JsonObject();
            fabricCondition.addProperty("condition", "fabric:all_mods_loaded");
            JsonArray modValues = new JsonArray();
            for (String mod : data.requiredMods) modValues.add(mod);
            fabricCondition.add("values", modValues);
            fabricLoadConditions.add(fabricCondition);
            recipe.add("fabric:load_conditions", fabricLoadConditions);

            JsonArray forgeConditions = new JsonArray();
            if (data.requiredMods.length == 1) {
                forgeConditions.add(modLoaded(data.requiredMods[0]));
            } else {
                JsonObject and = new JsonObject();
                and.addProperty("type", "forge:and");
                JsonArray values = new JsonArray();
                for (String mod : data.requiredMods) values.add(modLoaded(mod));
                and.add("values", values);
                forgeConditions.add(and);
            }
            recipe.add("conditions", forgeConditions);
        }

        recipe.addProperty("type", "minecraft:smithing_transform");
        recipe.add("template", itemObject(data.template));
        recipe.add("base", itemObject(data.base));
        recipe.add("addition", itemObject(data.addition));

        JsonObject result = new JsonObject();
        result.addProperty("item", itemId(data.result));
        result.addProperty("count", 1);
        recipe.add("result", result);
        return recipe;
    }

    private static JsonObject modLoaded(String modId) {
        JsonObject condition = new JsonObject();
        condition.addProperty("type", "forge:mod_loaded");
        condition.addProperty("modid", modId);
        return condition;
    }

    private static JsonObject itemObject(Object itemOrId) {
        JsonObject object = new JsonObject();
        object.addProperty("item", itemId(itemOrId));
        return object;
    }

    private static String itemId(Object itemOrId) {
        if (itemOrId instanceof Identifier id) return id.toString();
        if (itemOrId instanceof String string) return string;
        if (itemOrId instanceof Item item) {
            var id = Registries.ITEM.getId(item);
            if (id.equals(new Identifier("minecraft", "air"))) {
                throw new IllegalStateException(
                        "Item resolved to minecraft:air - use an Identifier instead of an Item for cross-mod items");
            }
            return id.toString();
        }
        throw new IllegalArgumentException("Unsupported item reference: " + itemOrId);
    }

    @Override
    public String getName() {
        return "Smithing Recipes (" + modId + ")";
    }

    private record RecipeData(String name, Item base, Object template, Object addition, Item result,
                              String[] requiredMods) {}
}
