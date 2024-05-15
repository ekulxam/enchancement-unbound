package survivalblock.enchancement_unbound.mixin.midastouch.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.client.reloadlisteners.FrozenReloadListener;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.enchantment.MidasTouchCurse;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {

    @ModifyExpressionValue(method = "getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/PlayerEntityRenderer;getTexture(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)Lnet/minecraft/util/Identifier;"))
    private Identifier replaceSkinWithGold(Identifier original, Entity entity){
        if (!(entity instanceof LivingEntity living)) {
            return original;
        }
        if (!UnboundEntityComponents.MIDAS_TOUCH.get(living).isGolden()) {
            return original;
        }
        if (original == null) {
            return null;
        }
        MidasTouchCurse.golden = true;
        Identifier texture = FrozenReloadListener.INSTANCE.getTexture(DefaultSkinHelper.getTexture());
        MidasTouchCurse.golden = false;
        return texture;
    }
}
