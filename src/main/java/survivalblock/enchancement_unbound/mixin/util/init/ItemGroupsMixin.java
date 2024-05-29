package survivalblock.enchancement_unbound.mixin.util.init;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.IntStream;

@Debug(export = true)
@Mixin(ItemGroups.class)
@SuppressWarnings("SameParameterValue")
public class ItemGroupsMixin {

	/**
	 * Prevents Minecraft 1.20.6 from adding the enchantments to the ItemGroup so that I can do it myself
	 * This thing would work perfectly without parameters, but unfortunately that's not how WrapWithCondition works
	 * @param instance the Optional from which ifPresent is being called. It is fine to put the suppression here because I need the parameter
	 * @param action The consumer that I'm going to prevent from running
	 * @return false always, so that it never runs
	 */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @WrapWithCondition(method = "method_51321", at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V"))
	private static boolean cancelVanillaStuffToFixEnchantedBooks(Optional<?> instance, Consumer<?> action) {
		return false;
	}

	@Inject(at = @At(value = "INVOKE", target = "Ljava/util/Set;of([Ljava/lang/Object;)Ljava/util/Set;", shift = At.Shift.AFTER), method = "method_51321")
	private static void doEnchantmentsProperly(ItemGroup.DisplayContext displayContext, ItemGroup.Entries entries, CallbackInfo ci) {
		displayContext.lookup().getOptionalWrapper(RegistryKeys.ENCHANTMENT).ifPresent(impl -> {
			addMaxLevelEnchantedBooks(entries, impl, ItemGroup.StackVisibility.PARENT_TAB_ONLY);
			addAllLevelEnchantedBooks(entries, impl, ItemGroup.StackVisibility.SEARCH_TAB_ONLY);
		});
	}

	/**
	 * 24w21a's implementation of addMaxLevelEnchantedBooks
	 * @param entries The entries
	 * @param registryWrapper The RegistryWrapper
	 * @param stackVisibility How visible it should be. By default, 24w21a puts this as ItemGroup.StackVisibility.PARENT_TAB_ONLY so that the max level enchanted books are only available when in the tab.
	 */
	@Unique
	private static void addMaxLevelEnchantedBooks(ItemGroup.Entries entries, RegistryWrapper<Enchantment> registryWrapper, ItemGroup.StackVisibility stackVisibility) {
		registryWrapper.streamEntries().map(reference -> EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(reference.value(), reference.value().getMaxLevel()))).forEach(stack -> entries.add(stack, stackVisibility));
	}

	/**
	 * 24w21a's implementation of addAllLevelEnchantedBooks
	 * @param entries The entries
	 * @param registryWrapper The RegistryWrapper
	 * @param stackVisibility How visible it should be. By default, 24w21a puts this as ItemGroup.StackVisibility.SEARCH_TAB_ONLY so that the leveled enchanted books are only available in search. This also adds all max level enchantments.
	 */
	@Unique
	private static void addAllLevelEnchantedBooks(ItemGroup.Entries entries, RegistryWrapper<Enchantment> registryWrapper, ItemGroup.StackVisibility stackVisibility) {
		registryWrapper.streamEntries().flatMap(reference -> IntStream.rangeClosed(reference.value().getMinLevel(), reference.value().getMaxLevel()).mapToObj(i -> EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(reference.value(), i)))).forEach(stack -> entries.add(stack, stackVisibility));
	}
}