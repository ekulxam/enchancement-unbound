/*
 * All Rights Reserved (c) MoriyaShiine
 */

package survivalblock.enchancement_unbound.mixin.util.enforceconfigmatch;

import com.bawnorton.mixinsquared.TargetHandler;
import moriyashiine.enchancement.client.packet.EnforceConfigMatchPacket;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.enchancement_unbound.client.packet.UnboundConfigMatchPacket;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = PlayerManager.class, priority = 1500)
public class PlayerManagerMixinMixin {

	@TargetHandler(
			mixin = "moriyashiine.enchancement.mixin.util.enforceconfigmatch.PlayerManagerMixin",
			name = "Lmoriyashiine/enchancement/mixin/util/enforceconfigmatch/PlayerManagerMixin;enchancement$enforceConfigMatch(Lnet/minecraft/network/ClientConnection;Lnet/minecraft/server/network/ServerPlayerEntity;Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfo;)V"
	)
	@Inject(
			method = "@MixinSquared:Handler",
			at = @At(value = "INVOKE", target = "Lmoriyashiine/enchancement/client/packet/EnforceConfigMatchPacket;send(Lnet/minecraft/server/network/ServerPlayerEntity;I)V",
                    shift = At.Shift.AFTER)
	)
	private void enforceUnboundConfigMatch(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci, CallbackInfo myCi) {
		UnboundConfigMatchPacket.send(player, UnboundConfig.encode());
	}
}
