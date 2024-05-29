/*
 * All Rights Reserved (c) MoriyaShiine
 */

package survivalblock.enchancement_unbound.mixin.util.enforceconfigmatch;

import com.bawnorton.mixinsquared.TargetHandler;
import moriyashiine.enchancement.common.event.EnforceConfigMatchEvent;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.enchancement_unbound.client.payload.UnboundConfigMatchPayload;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = EnforceConfigMatchEvent.class, priority = 1500)
public class EnforceConfigMatchEventMixin {

	@Inject(
			method = "onPlayReady",
			at = @At(value = "INVOKE", target = "Lmoriyashiine/enchancement/client/payload/EnforceConfigMatchPayload;send(Lnet/minecraft/server/network/ServerPlayerEntity;I)V",
					shift = At.Shift.AFTER)
	)
	private void enforceUnboundConfigMatch(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server, CallbackInfo ci) {
		UnboundConfigMatchPayload.send(handler.getPlayer(), UnboundConfig.encode());
	}
}
