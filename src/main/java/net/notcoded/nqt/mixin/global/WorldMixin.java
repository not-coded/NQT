package net.notcoded.nqt.mixin.global;

import net.fabricmc.api.EnvType;
import net.minecraft.entity.Entity;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.world.World;
import net.notcoded.nqt.NQT;
import net.notcoded.nqt.utils.fixes.EntityCrashFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.function.Consumer;

@Mixin(World.class)
public class WorldMixin {

    /**
     * @author NotCoded
     * @reason Avoid crashing the game because of erroring entities
     */
    @Overwrite
    public void tickEntity(Consumer<Entity> tickConsumer, Entity entity){
        try {
            tickConsumer.accept(entity);
        } catch (Throwable throwable) {
            if((NQT.type == EnvType.CLIENT && NQT.clientModConfig.isEnabled && NQT.clientModConfig.fixes.entityCrashFix) || NQT.type == EnvType.SERVER && NQT.serverModConfig.isEnabled && NQT.serverModConfig.fixes.entityCrashFix){
                try {
                    entity.remove(Entity.RemovalReason.DISCARDED);
                } catch(Exception e) {
                    EntityCrashFix.createCrashReport(throwable, entity);
                }
            } else {
                EntityCrashFix.createCrashReport(throwable, entity);
            }

        }
    }

}
