package eu.pb4.honeytech.item.other;

import eu.pb4.honeytech.gui.PortableCraftingScreenHandler;
import eu.pb4.honeytech.item.general.HeadItem;
import eu.pb4.honeytech.other.HTUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;


public class PortableCraftingItem extends HeadItem {
    private static Text TITLE = HTUtils.getText("gui", "portable_crafting");
    private static String TEXTURE = "ewogICJ0aW1lc3RhbXAiIDogMTYxODM5MDc0NDcxNiwKICAicHJvZmlsZUlkIiA6ICI0ZWQ4MjMzNzFhMmU0YmI3YTVlYWJmY2ZmZGE4NDk1NyIsCiAgInByb2ZpbGVOYW1lIiA6ICJGaXJlYnlyZDg4IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2E4MWI1MjdlYWRkMjkwZGI5Y2ZjZjdjZTZiMWEyZWQ0OGFlNGYwNjIyYzcyMTRiZmY5ZGFmMDFiM2RkNzY3ODYiCiAgICB9CiAgfQp9";

    public PortableCraftingItem(Settings settings) {
    super(settings, TEXTURE);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) return TypedActionResult.fail(user.getStackInHand(hand));
        user.openHandledScreen(
                new SimpleNamedScreenHandlerFactory(
                        (i, playerInventory, playerEntity) -> new PortableCraftingScreenHandler(i, playerInventory, ScreenHandlerContext.create(world, user.getBlockPos()))
                        , TITLE)
        );
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        this.use(context.getWorld(), context.getPlayer(), context.getHand());
        return super.useOnBlock(context);
    }
}
