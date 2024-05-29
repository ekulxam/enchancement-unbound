package survivalblock.enchancement_unbound.mixin.vanillachanges.allcrossbowshavemultishot;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModDataComponentTypes;
import moriyashiine.enchancement.common.init.ModTags;
import net.minecraft.component.ComponentHolder;
import net.minecraft.component.DataComponentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ComponentHolder {
    @Shadow
    public abstract boolean hasEnchantments();
    @Shadow
    public abstract boolean isIn(TagKey<Item> tag);

    @Shadow
    @Nullable
    public abstract <T> T set(DataComponentType<? super T> type, @Nullable T value);

    @Inject(method = "addEnchantment", at = @At("TAIL"))
    private void enchantedCrossbowsHaveMultishot(Enchantment enchantment, int level, CallbackInfo ci) {
        if (UnboundConfig.allCrossbowsHaveMultishot && hasEnchantments() && isIn(ItemTags.CROSSBOW_ENCHANTABLE) && !Registries.ENCHANTMENT.getEntry(enchantment).isIn(ModTags.Enchantments.DISALLOWS_TOGGLEABLE_PASSIVE) && !contains(ModDataComponentTypes.TOGGLEABLE_PASSIVE)) {
            set(ModDataComponentTypes.TOGGLEABLE_PASSIVE, true);
        }
    }
}