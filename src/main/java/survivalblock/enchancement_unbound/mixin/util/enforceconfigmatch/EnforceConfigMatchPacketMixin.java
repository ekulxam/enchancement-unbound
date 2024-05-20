package survivalblock.enchancement_unbound.mixin.util.enforceconfigmatch;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.client.packet.EnforceConfigMatchPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.enchancement_unbound.client.packet.UnboundConfigMatchPacket;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Debug(export = true)
@Mixin(EnforceConfigMatchPacket.class)
public class EnforceConfigMatchPacketMixin {

    @ModifyExpressionValue(method = "send", at = @At(value = "FIELD", target = "Lmoriyashiine/enchancement/client/packet/EnforceConfigMatchPacket;ID:Lnet/minecraft/util/Identifier;"))
    private static Identifier replaceWithUnboundId(Identifier original, @Local(argsOnly = true) int encoding) {
        return encoding == UnboundConfig.encode() ? UnboundConfigMatchPacket.ID : original;
    }
}
