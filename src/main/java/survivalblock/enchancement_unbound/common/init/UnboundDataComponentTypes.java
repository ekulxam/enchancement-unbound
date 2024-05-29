package survivalblock.enchancement_unbound.common.init;

import com.mojang.serialization.Codec;
import net.minecraft.component.DataComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;

public class UnboundDataComponentTypes {
    public static final DataComponentType<Integer> USE_TICKS = new DataComponentType.Builder<Integer>().codec(Codec.INT).packetCodec(PacketCodecs.INTEGER).build();

    public static void init() {
        Registry.register(Registries.DATA_COMPONENT_TYPE, EnchancementUnbound.id("ticks_used"), USE_TICKS);
    }
}
