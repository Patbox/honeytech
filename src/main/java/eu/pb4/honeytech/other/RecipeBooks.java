package eu.pb4.honeytech.other;

import eu.pb4.honeytech.recipe_types.GrinderRecipe;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class RecipeBooks {

    public static void openGrinderRecipeBook(ServerPlayerEntity player, int grinderPower, BackCallback callback) {
        List<GrinderRecipe> recipeList = player.world.getRecipeManager()
                .listAllOfType(GrinderRecipe.Type.INSTANCE)
                .stream().filter((recipe) -> recipe.getRequiredPower() <= grinderPower).collect(Collectors.toList());

        final int recipeSize = recipeList.size();

        AtomicInteger page = new AtomicInteger();
        final int maxPage = (int) Math.ceil((double) recipeSize / 9);

        SimpleGui gui = new SimpleGui(ScreenHandlerType.GENERIC_9X3, player, false) {
            @Override
            public void onUpdate(boolean firstUpdate) {
                super.onUpdate(firstUpdate);

                if (firstUpdate) {
                    for (int x = 0; x < 9; x++) {
                        this.setSlot(x + 18, new GuiElementBuilder(Items.GRAY_STAINED_GLASS_PANE).setName(new LiteralText("")));
                    }
                }

                for (int x = 0; x < 9; x++) {
                    this.clearSlot(x);
                    this.clearSlot(x + 9);


                    if (page.get() * 9 + x < recipeSize) {
                        GrinderRecipe recipe = recipeList.get(page.get() * 9 + x);

                        this.setSlot(x, recipe.getInput().getMatchingStacksClient()[0]);
                        this.setSlot(x + 9, recipe.getOutput().copy());
                    }

                    this.setSlot(this.size - 5, new GuiElementBuilder(Items.BARRIER)
                            .setName(new TranslatableText("dataPack.validation.back").setStyle(Style.EMPTY.withItalic(false)))
                            .setCallback((index, type, action) -> {
                                this.close();
                                callback.run();
                            }));
                    this.setSlot(this.size - 7, new GuiElementBuilder(Items.ARROW)
                            .setName(new TranslatableText("spectatorMenu.previous_page").setStyle(Style.EMPTY.withItalic(false)))
                            .setCallback((index, type, action) -> {
                                if (page.addAndGet(-1) < 0) {
                                    page.set(0);
                                }

                                this.onUpdate(false);
                            }));
                    this.setSlot(this.size - 3, new GuiElementBuilder(Items.ARROW)
                            .setName(new TranslatableText("spectatorMenu.next_page").setStyle(Style.EMPTY.withItalic(false)))
                            .setCallback((index, type, action) -> {
                                if (page.addAndGet(1) >= maxPage) {
                                    page.set(maxPage - 1);
                                }

                                this.onUpdate(false);
                            }));
                }
            }
        };
        gui.setTitle(new TranslatableText("gui.grinder.recipes"));

        gui.open();
    }

    @FunctionalInterface
    public interface BackCallback {
        void run();
    }
}
