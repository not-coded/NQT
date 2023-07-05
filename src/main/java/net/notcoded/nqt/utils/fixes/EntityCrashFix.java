package net.notcoded.nqt.utils.fixes;

import net.minecraft.entity.Entity;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;

public class EntityCrashFix {

    public static void createCrashReport(Throwable throwable, Entity entity) {
        CrashReport crashReport = CrashReport.create(throwable, "Ticking entity");
        CrashReportSection crashReportSection = crashReport.addElement("Entity being ticked");
        entity.populateCrashReport(crashReportSection);
        throw new CrashException(crashReport);
    }
}
