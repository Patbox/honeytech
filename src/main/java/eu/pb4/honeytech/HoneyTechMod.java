package eu.pb4.honeytech;

import eu.pb4.honeytech.advancements.HTCriteria;
import eu.pb4.honeytech.block.HTBlocks;
import eu.pb4.honeytech.blockentity.HTBlockEntities;
import eu.pb4.honeytech.gui.guide.GuideGui;
import eu.pb4.honeytech.item.HTItems;
import eu.pb4.honeytech.recipe_types.GrinderRecipe;
import eu.pb4.honeytech.recipe_types.TableSawRecipe;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class HoneyTechMod implements ModInitializer {
	public static final String ID = "honeytech";

	@Override
	public void onInitialize() {
		Registry.register(Registry.RECIPE_SERIALIZER, GrinderRecipe.Serializer.ID,
				GrinderRecipe.Serializer.INSTANCE);

		Registry.register(Registry.RECIPE_SERIALIZER, TableSawRecipe.Serializer.ID,
				TableSawRecipe.Serializer.INSTANCE);

		HTCriteria.register();

		Registry.register(Registry.RECIPE_TYPE, new Identifier(ID, GrinderRecipe.Type.ID), GrinderRecipe.Type.INSTANCE);
		Registry.register(Registry.RECIPE_TYPE, new Identifier(ID, TableSawRecipe.Type.ID), TableSawRecipe.Type.INSTANCE);

		HTBlocks.register();
		HTBlockEntities.register();
		HTItems.register();


		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, serverResourceManager, success) -> {
			if (success) {
				GuideGui.buildOther(server.getRecipeManager());
			}
		});

		ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
			GuideGui.buildOther(server.getRecipeManager());
		});
	}
}
