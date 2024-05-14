package survivalblock.enchancement_unbound.mixin.midastouch.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.client.reloadlisteners.FrozenReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;
import survivalblock.enchancement_unbound.common.enchantment.MidasTouchCurse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Debug(export = true)
@Mixin(value = FrozenReloadListener.class, remap = false, priority = 5000)
public class FrozenReloadListenerMixin {

    @Unique
    private static final Identifier GOLD_BLOCK_TEXTURE = new Identifier("textures/block/gold_block.png");

    @Unique
    private final Map<Identifier, Identifier> GOLDEN_TEXTURE_CACHE = new HashMap<>();

    @ModifyExpressionValue(method = "generateTexture", at = @At(value = "FIELD", target = "Lmoriyashiine/enchancement/client/reloadlisteners/FrozenReloadListener;PACKED_ICE_TEXTURE:Lnet/minecraft/util/Identifier;"))
    private static Identifier replaceWithGoldenTouch(Identifier original){
        return MidasTouchCurse.golden ? GOLD_BLOCK_TEXTURE : original;
    }

    @ModifyExpressionValue(method = "generateTexture", at = @At(value = "INVOKE", target = "Lmoriyashiine/enchancement/common/Enchancement;id(Ljava/lang/String;)Lnet/minecraft/util/Identifier;"))
    private static Identifier replaceFrozenTextureIdWithGold(Identifier original, @Local(ordinal = 2) int width, @Local(ordinal = 3) int height){
        return MidasTouchCurse.golden ? EnchancementUnbound.id(String.format("textures/generated/golden_%sx%s", width, height)) : original;
    }

    @Inject(method = "getTexture", at = @At("RETURN"))
    private void resetGolden(Identifier original, CallbackInfoReturnable<Identifier> cir){
        MidasTouchCurse.golden = false;
    }

    /**
     * Fixes an issue where the map for the frozen texture conflicts with the map for the golden texture (due to the frozen textures and golden textures being mapped to the same key)
     * @param original The original map of the mob's texture (such as the warden texture) to the generated frozen texture
     * I override this to return a new map that stores the golden textures
     * @see FrozenReloadListener#getTexture(Identifier)
    */
    @ModifyExpressionValue(method = "getTexture", at = @At(value = "FIELD", target = "Lmoriyashiine/enchancement/client/reloadlisteners/FrozenReloadListener;TEXTURE_CACHE:Ljava/util/Map;", opcode = Opcodes.GETFIELD))
    private Map<Identifier, Identifier> replaceFrozenTextureCacheWithGoldenOne(Map<Identifier, Identifier> original){
        return MidasTouchCurse.golden ? GOLDEN_TEXTURE_CACHE : original;
    }

    @WrapWithCondition(method = "lambda$getTexture$0", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Throwable;)V"))
    private static boolean changeLogger(Logger instance, String s, Throwable throwable, Identifier original, @Local IOException e){
        if (MidasTouchCurse.golden) {
            EnchancementUnbound.LOGGER.warn("Unable to generate golden texture for {}", original, e);
            return false;
        }
        return true;
    }

    @Inject(method = "reload", at = @At("RETURN"))
    private void reloadGoldenTextureCache(ResourceManager manager, CallbackInfo ci){
        this.GOLDEN_TEXTURE_CACHE.clear();
    }
}
