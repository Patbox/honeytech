package eu.pb4.honeytech.gui.guide;

import eu.pb4.honeytech.item.HTItems;
import eu.pb4.honeytech.mixin.SmithingRecipeAccessor;
import eu.pb4.honeytech.other.HTUtils;
import eu.pb4.honeytech.recipe_types.GrinderRecipe;
import eu.pb4.honeytech.recipe_types.TableSawRecipe;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.AnimatedGuiElement;
import eu.pb4.sgui.api.elements.GuiElement;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.*;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class GuideGui extends SimpleGui {
    private static final Style STYLE = Style.EMPTY.withItalic(false).withColor(Formatting.GOLD);
    private static final Style LORE_STYLE = Style.EMPTY.withItalic(false).withColor(Formatting.GRAY);
    private static final Style IC_STYLE = Style.EMPTY.withItalic(true).withColor(Formatting.DARK_PURPLE);

    private static final Style CRAFT_ACTION_STYLE = Style.EMPTY.withItalic(false).withColor(Formatting.GREEN);

    public static GuideTab MAIN_GUIDE = new GuideTab(HTUtils.getText("gui", "guide/main/name"), new ArrayList<>(), null);
    public static GuideTab BASIC_MACHINES = new GuideTab(HTUtils.getText("gui", "guide/basic_machines/name"), new ArrayList<>(), null);
    public static GuideTab RESOURCES = new GuideTab(HTUtils.getText("gui", "guide/resources/name"), new ArrayList<>(), null);
    public static GuideTab USEFUL_ITEMS = new GuideTab(HTUtils.getText("gui", "guide/useful_items/name"), new ArrayList<>(), null);
    public static GuideTab ELECTRICAL_COMPONENTS = new GuideTab(HTUtils.getText("gui", "guide/electrical_components/name"), new ArrayList<>(), null);
    public static GuideTab ELECTRICITY = new GuideTab(HTUtils.getText("gui", "guide/electricity/name"), new ArrayList<>(), null);

    public static GuideTab STORAGE = new GuideTab(HTUtils.getText("gui", "guide/storage/name"), new ArrayList<>(), null);

    public static GuideTab VANILLA = new GuideTab(new LiteralText("Minecraft"), new ArrayList<>(), null);

    public static HashMap<Item, GuideTab> ITEM_RECIPES = new HashMap<>();

    private final List<GuideTab> tabList = new ArrayList<>();
    private final HashMap<GuideTab, Integer> tabPage = new HashMap<>();

    private int page = 0;
    private GuideTab activeTab;
    private int maxPage;

    public GuideGui(ServerPlayerEntity player, GuideTab tab) {
        super(ScreenHandlerType.GENERIC_9X4, player, false);
        this.openTab(tab);
    }

    public static void buildMain() {
        MAIN_GUIDE.items.clear();

        MAIN_GUIDE.items.add(
                ((GuideElementBuilder) (new GuideElementBuilder(Items.FURNACE)
                        .setName(HTUtils.getText("gui", "guide/basic_machines/name").setStyle(STYLE))
                        .addLoreLine(HTUtils.getText("gui", "guide/basic_machines/description").setStyle(LORE_STYLE))
                        .addLoreLine(HTUtils.getText("gui", "guide/item_count", BASIC_MACHINES.items.size()).setStyle(IC_STYLE)))
                ).build(BASIC_MACHINES));

        MAIN_GUIDE.items.add(
                ((GuideElementBuilder) (new GuideElementBuilder(Items.IRON_INGOT)
                        .setName(HTUtils.getText("gui", "guide/resources/name").setStyle(STYLE))
                        .addLoreLine(HTUtils.getText("gui", "guide/resources/description").setStyle(LORE_STYLE))
                        .addLoreLine(HTUtils.getText("gui", "guide/item_count", RESOURCES.items.size()).setStyle(IC_STYLE)))
                ).build(RESOURCES));

        MAIN_GUIDE.items.add(
                ((GuideElementBuilder) (new GuideElementBuilder(Items.SHEEP_SPAWN_EGG)
                        .setName(HTUtils.getText("gui", "guide/useful_items/name").setStyle(STYLE))
                        .addLoreLine(HTUtils.getText("gui", "guide/useful_items/description").setStyle(LORE_STYLE))
                        .addLoreLine(HTUtils.getText("gui", "guide/item_count", USEFUL_ITEMS.items.size()).setStyle(IC_STYLE)))
                ).build(USEFUL_ITEMS));

        MAIN_GUIDE.items.add(
                ((GuideElementBuilder) (new GuideElementBuilder(Items.REDSTONE)
                        .setName(HTUtils.getText("gui", "guide/electrical_components/name").setStyle(STYLE))
                        .addLoreLine(HTUtils.getText("gui", "guide/electrical_components/description").setStyle(LORE_STYLE))
                        .addLoreLine(HTUtils.getText("gui", "guide/item_count", ELECTRICAL_COMPONENTS.items.size()).setStyle(IC_STYLE)))
                ).build(ELECTRICAL_COMPONENTS));

        MAIN_GUIDE.items.add(
                ((GuideElementBuilder) (new GuideElementBuilder(HTItems.GUIDE_ITEM_ELECTRONICS)
                        .setName(HTUtils.getText("gui", "guide/electricity/name").setStyle(STYLE))
                        .addLoreLine(HTUtils.getText("gui", "guide/electricity/description").setStyle(LORE_STYLE))
                        .addLoreLine(HTUtils.getText("gui", "guide/item_count", ELECTRICITY.items.size()).setStyle(IC_STYLE)))
                ).build(ELECTRICITY));

        MAIN_GUIDE.items.add(
                ((GuideElementBuilder) (new GuideElementBuilder(Items.CHEST)
                        .setName(HTUtils.getText("gui", "guide/storage/name").setStyle(STYLE))
                        .addLoreLine(HTUtils.getText("gui", "guide/storage/description").setStyle(LORE_STYLE))
                        .addLoreLine(HTUtils.getText("gui", "guide/item_count", STORAGE.items.size()).setStyle(IC_STYLE)))
                ).build(STORAGE));

        MAIN_GUIDE.items.add(
                ((GuideElementBuilder) (new GuideElementBuilder(Items.GRASS_BLOCK)
                        .setName(new LiteralText("Minecraft").setStyle(STYLE))
                        .addLoreLine(HTUtils.getText("gui", "guide/vanilla/description").setStyle(LORE_STYLE))
                        .addLoreLine(HTUtils.getText("gui", "guide/item_count", VANILLA.items.size()).setStyle(IC_STYLE)))
                ).build(VANILLA));
    }

    public static void buildOther(RecipeManager manager) {
        try {
            BASIC_MACHINES.items.clear();
            USEFUL_ITEMS.items.clear();
            RESOURCES.items.clear();
            STORAGE.items.clear();
            VANILLA.items.clear();
            ELECTRICAL_COMPONENTS.items.clear();
            ELECTRICITY.items.clear();

            ITEM_RECIPES.clear();

            for (Item item : TagRegistry.item(HTUtils.id("basic_machines")).values()) {
                BASIC_MACHINES.items.add(new GuideElementBuilder(item, 1).build(createRecipeTab(manager, item, true).left));
            }

            for (Item item : TagRegistry.item(HTUtils.id("useful_items")).values()) {
                USEFUL_ITEMS.items.add(new GuideElementBuilder(item, 1).build(createRecipeTab(manager, item, true).left));
            }

            for (Item item : TagRegistry.item(HTUtils.id("resources")).values()) {
                RESOURCES.items.add(new GuideElementBuilder(item, 1).build(createRecipeTab(manager, item, true).left));
            }

            for (Item item : TagRegistry.item(HTUtils.id("storage")).values()) {
                STORAGE.items.add(new GuideElementBuilder(item, 1).build(createRecipeTab(manager, item, true).left));
            }

            for (Item item : TagRegistry.item(HTUtils.id("electrical_components")).values()) {
                ELECTRICAL_COMPONENTS.items.add(new GuideElementBuilder(item, 1).build(createRecipeTab(manager, item, true).left));
            }

            for (Item item : TagRegistry.item(HTUtils.id("electricity")).values()) {
                ELECTRICITY.items.add(new GuideElementBuilder(item, 1).build(createRecipeTab(manager, item, true).left));
            }

            for (Item item : Registry.ITEM) {
                if (!Registry.ITEM.getId(item).getNamespace().equals("minecraft")) {
                    continue;
                }

                ImmutablePair<GuideTab, Integer> val = createRecipeTab(manager, item, false);

                if (val.right > 0) {
                    VANILLA.items.add(new GuideElementBuilder(item, 1).build(val.left));
                }
            }

            buildMain();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ImmutablePair<GuideTab, Integer> createRecipeTab(RecipeManager recipeManager, Item item, boolean allowVanilla) {
        List<GuiElementInterface> items = new ArrayList<>();

        GuiElement fill = new GuiElementBuilder(Items.WHITE_STAINED_GLASS_PANE).setName(new LiteralText("")).build();

        List<Recipe<?>> recipes = recipeManager.values().stream().filter((recipe) -> recipe.getOutput().getItem() == item).collect(Collectors.toList());
        int offset = 0;
        int recipeCount = 0;
        for (Recipe<?> recipe : recipes) {
            try {
                for (int i = offset; i < 27 + offset; i++) {
                    items.add(fill);
                }

                GuiElement logo = null;

                boolean checkCustom = !allowVanilla;

                if (allowVanilla) {
                    if (recipe instanceof CraftingRecipe) {
                        CraftingRecipe craftingRecipe = (CraftingRecipe) recipe;
                        logo = new GuiElementBuilder(Items.CRAFTING_TABLE).build();
                        boolean isShaped = craftingRecipe instanceof ShapedRecipe;

                        GuiElement type;

                        if (craftingRecipe instanceof ShapedRecipe) {
                            type = new GuiElementBuilder(Items.SLIME_BALL).setName(HTUtils.getText("gui", "guide/recipe/type/shaped").setStyle(Style.EMPTY.withItalic(false))).build();
                        } else if (craftingRecipe instanceof ShapelessRecipe) {
                            type = new GuiElementBuilder(Items.ORANGE_DYE).setName(HTUtils.getText("gui", "guide/recipe/type/shapeless").setStyle(Style.EMPTY.withItalic(false))).build();
                        } else {
                            type = new GuiElementBuilder(Items.DIAMOND).setName(HTUtils.getText("gui", "guide/recipe/type/special").setStyle(Style.EMPTY.withItalic(false))).build();
                        }

                        items.set(9 + offset, type);

                        DefaultedList<Ingredient> ingredients = craftingRecipe.getIngredients();

                        for (int i = 0; i < 9; i++) {
                            ItemStack[] stacks = new ItemStack[0];
                            if (isShaped) {
                                ShapedRecipe shapedRecipe = (ShapedRecipe) craftingRecipe;
                                if (i % 3 < shapedRecipe.getWidth() && i / 3 < shapedRecipe.getHeight()) {
                                    stacks = readIngredient(ingredients.get(i % shapedRecipe.getWidth() + (shapedRecipe.getWidth() * (i / 3))));
                                }
                            } else if (ingredients.size() > i && craftingRecipe instanceof ShapelessRecipe) {
                                stacks = readIngredient(ingredients.get(i));
                            }

                            items.set(i % 3 + (9 * (i / 3)) + 2 + offset, stacks.length > 0 ? new AnimatedGuiElement(stacks, 20, false, GuideGui::emptyCallback) : new GuiElement(ItemStack.EMPTY, GuideGui::emptyCallback));
                        }


                        items.set(15 + offset, new GuiElement(craftingRecipe.getOutput(), GuideGui::emptyCallback));
                    } else if (recipe instanceof AbstractCookingRecipe) {
                        AbstractCookingRecipe cookingRecipe = (AbstractCookingRecipe) recipe;
                        if (recipe instanceof SmeltingRecipe) {
                            logo = new GuiElementBuilder(Items.FURNACE).build();
                        } else if (recipe instanceof CampfireCookingRecipe) {
                            logo = new GuiElementBuilder(Items.CAMPFIRE).build();
                        } else if (recipe instanceof BlastingRecipe) {
                            logo = new GuiElementBuilder(Items.BLAST_FURNACE).build();
                        } else if (recipe instanceof SmokingRecipe) {
                            logo = new GuiElementBuilder(Items.SMOKER).build();
                        } else {
                            logo = new GuiElementBuilder(Items.BARRIER).setName(new LiteralText("Unknown")).build();
                        }

                        items.set(11 + offset, getRecipeIngredientElement(cookingRecipe.getIngredients().get(0)));
                        items.set(13 + offset, new GuiElementBuilder(Items.FIRE_CHARGE).setName(HTUtils.getText("gui", "guide/recipe/smelts", cookingRecipe.getCookTime() / 20).setStyle(CRAFT_ACTION_STYLE)).build());
                        items.set(15 + offset, new GuiElement(cookingRecipe.getOutput(), GuideGui::emptyCallback));
                    } else if (recipe instanceof StonecuttingRecipe) {
                        StonecuttingRecipe stonecuttingRecipe = (StonecuttingRecipe) recipe;
                        logo = new GuiElementBuilder(Items.STONECUTTER).build();

                        items.set(11 + offset, getRecipeIngredientElement(stonecuttingRecipe.getIngredients().get(0)));
                        items.set(13 + offset, new GuiElementBuilder(Items.ARROW).setName(new LiteralText("")).build());
                        items.set(15 + offset, new GuiElement(stonecuttingRecipe.getOutput(), GuideGui::emptyCallback));
                    } else if (recipe instanceof SmithingRecipe) {
                        SmithingRecipe smithingRecipe = (SmithingRecipe) recipe;
                        logo = new GuiElementBuilder(Items.STONECUTTER).build();

                        items.set(11 + offset, getRecipeIngredientElement(((SmithingRecipeAccessor) smithingRecipe).getBase()));
                        items.set(13 + offset, getRecipeIngredientElement(((SmithingRecipeAccessor) smithingRecipe).getAddition()));
                        items.set(14 + offset, new GuiElementBuilder(Items.ARROW).setName(new LiteralText("")).build());
                        items.set(15 + offset, new GuiElement(smithingRecipe.getOutput(), GuideGui::emptyCallback));
                    } else {
                        checkCustom = true;
                    }
                }

                if (checkCustom) {
                    if (recipe instanceof GrinderRecipe) { // Custom recipe types start here
                        GrinderRecipe grinderRecipe = (GrinderRecipe) recipe;
                        switch (grinderRecipe.getRequiredPower()) {
                            case 0:
                            case 1:
                                logo = new GuiElementBuilder(HTItems.BASIC_GRINDER).build();
                                break;
                            default:
                                logo = new GuiElementBuilder(Items.BARRIER).setName(new LiteralText("Unknown")).build();
                        }

                        items.set(11 + offset, getRecipeIngredientElement(grinderRecipe.getInput()));
                        items.set(13 + offset, new GuiElementBuilder(Items.LEVER).setName(HTUtils.getText("gui", "guide/recipe/clicks", grinderRecipe.getRequiredClicks()).setStyle(CRAFT_ACTION_STYLE)).build());
                        items.set(15 + offset, new GuiElement(grinderRecipe.getOutput(), GuideGui::emptyCallback));
                    } else if (recipe instanceof TableSawRecipe) {
                        TableSawRecipe tableSawRecipe = (TableSawRecipe) recipe;
                        logo = new GuiElementBuilder(HTItems.TABLE_SAW).build();

                        items.set(11 + offset, getRecipeIngredientElement(tableSawRecipe.getInput()));
                        items.set(13 + offset, new GuiElementBuilder(Items.ARROW).setName(new LiteralText("")).build());
                        items.set(15 + offset, new GuiElement(tableSawRecipe.getOutput(), GuideGui::emptyCallback));
                    }
                }

                if (logo == null) {
                    for (int i = offset; i < 27 + offset; i++) {
                        items.remove(items.size() - 1);
                    }
                    continue;
                } else {
                    recipeCount += 1;
                }

                items.set(offset, logo);
                offset += 27;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (recipes.size() == 0) {
            for (int i = offset; i < 27; i++) {
                items.add(fill);
            }
            items.set(13, new GuiElementBuilder(Items.BOOK).setName(HTUtils.getText("gui", "guide/no_recipes").setStyle(CRAFT_ACTION_STYLE)).build());
        }

        GuideTab tab = new GuideTab(HTUtils.getText("gui", "guide/recipe/for", new TranslatableText(item.getTranslationKey())), items, item);

        ITEM_RECIPES.put(item, tab);
        return ImmutablePair.of(tab, recipeCount);
    }

    private static GuiElementInterface getRecipeIngredientElement(Ingredient ingredient) {
        ItemStack[] stacks = ingredient.getMatchingStacks();

        return stacks.length > 0 ? new AnimatedGuiElement(stacks, 20, false, GuideGui::emptyCallback) : new GuiElement(ItemStack.EMPTY, GuideGui::emptyCallback);
    }

    private static ItemStack[] readIngredient(Ingredient ingredient) {
        ItemStack[] stacks = ingredient.getMatchingStacks();
        if (stacks.length > 0) {
            return stacks;
        } else {
            return new ItemStack[]{};
        }
    }

    private static void emptyCallback(int index, ClickType type, SlotActionType action) {
    }

    public void openTab(GuideTab tab) {
        this.autoUpdate = false;
        this.tabPage.put(this.activeTab, this.page);
        this.activeTab = tab;
        this.page = 0;
        this.tabList.add(tab);
        this.onUpdate(false);
        this.autoUpdate = true;
        this.setTitle(this.activeTab.title);
        this.player.playSound(SoundEvents.ITEM_BOOK_PAGE_TURN, SoundCategory.PLAYERS, 0.2f, 1f);
    }

    public void goBack() {
        if (this.tabList.size() - 1 > 0) {
            this.autoUpdate = false;
            this.tabList.remove(this.tabList.size() - 1);
            this.activeTab = this.tabList.get(this.tabList.size() - 1);
            this.page = this.tabPage.get(this.activeTab);
            this.tabPage.remove(this.activeTab);
            this.onUpdate(false);
            this.autoUpdate = true;
            this.setTitle(this.activeTab.title);
            return;
        }
        this.close(false);
    }

    @Override
    public void onUpdate(boolean firstUpdate) {
        this.maxPage = (int) Math.ceil((double) this.activeTab.items.size() / 27);

        int start = this.page * 27;

        if (firstUpdate) {
            GuiElementBuilder pane = new GuiElementBuilder(Items.GRAY_STAINED_GLASS_PANE).setName(new LiteralText(""));
            for (int x = 0; x < 9; x++) {
                this.setSlot(x + 27, pane);
            }

            this.setSlot(this.size - 5, new GuiElementBuilder(Items.BARRIER)
                    .setName(new TranslatableText("dataPack.validation.back").setStyle(Style.EMPTY.withItalic(false)))
                    .setCallback((index, type, action) -> {
                        this.goBack();
                    }));
        }

        Style sel = Style.EMPTY.withItalic(false);
        Style nonSel = Style.EMPTY.withItalic(false).withColor(Formatting.DARK_GRAY);


        this.setSlot(this.size - 8, new GuiElementBuilder(this.page != 0 ? Items.ARROW : Items.BLACK_STAINED_GLASS_PANE)
                .setName(new TranslatableText("spectatorMenu.previous_page").setStyle(this.page != 0 ? sel : nonSel))
                .setCallback((index, type, action) -> {
                    this.page += -1;
                    if (this.page < 0) {
                        this.page = 0;
                    }

                    this.onUpdate(false);
                }));
        this.setSlot(this.size - 2, new GuiElementBuilder(this.page != this.maxPage - 1 ? Items.ARROW : Items.BLACK_STAINED_GLASS_PANE)
                .setName(new TranslatableText("spectatorMenu.next_page").setStyle(this.page != this.maxPage - 1 ? sel : nonSel))
                .setCallback((index, type, action) -> {
                    this.page += 1;
                    if (this.page >= this.maxPage) {
                        this.page = this.maxPage - 1;
                    }

                    this.onUpdate(false);
                }));

        for (int x = 0; x < 27; x++) {
            if (x + start < this.activeTab.items.size()) {
                this.setSlot(x, this.activeTab.items.get(x + start));
            } else {
                this.clearSlot(x);
            }
        }

        super.onUpdate(firstUpdate);
    }

    @Override
    public boolean onClick(int index, ClickType type, SlotActionType action, GuiElementInterface element) {
        if (element instanceof GuideElement guideElement) {
            if (type.isMiddle && this.player.isCreative() && guideElement.tab.item != null) {
                this.screenHandler.setCursorStack(guideElement.tab.item.getDefaultStack());
                return true;
            }
            this.openTab(guideElement.tab);
            return true;
        }

        return super.onClick(index, type, action, element);
    }

    public record GuideTab(Text title, List<GuiElementInterface> items, @Nullable Item item) {
    }
}
