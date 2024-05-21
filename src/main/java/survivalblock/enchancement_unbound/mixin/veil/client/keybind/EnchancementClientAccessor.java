package survivalblock.enchancement_unbound.mixin.veil.client.keybind;

import moriyashiine.enchancement.client.EnchancementClient;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Supplier;

@Mixin(EnchancementClient.class)
public interface EnchancementClientAccessor {

    @Invoker
    static KeyBinding invokeRegisterKeyBinding(Supplier<KeyBinding> supplier) {
        throw new UnsupportedOperationException();
    }
}
