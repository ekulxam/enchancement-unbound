package survivalblock.enchancement_unbound.mixin;

import com.google.common.collect.Iterables;
import net.minecraft.block.Block;
import net.minecraft.block.entity.ConduitBlockEntity;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("ShadowFinalModification")
@Mixin(ConduitBlockEntity.class)
public class ConduitBlockEntityMixin {

    @Shadow @Final
    private final static Block[] ACTIVATING_BLOCKS;

    static {
        ACTIVATING_BLOCKS = Iterables.toArray(Registries.BLOCK, Block.class);
    }
}
