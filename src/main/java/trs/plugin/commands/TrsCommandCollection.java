package trs.plugin.commands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;

public class TrsCommandCollection extends AbstractCommandCollection {

    public TrsCommandCollection() {
        super("trs", "Commands provided by the TRS plugin.");
        addSubCommand(new PingCommand("ping", "Reports connection latency", false));
        addSubCommand(new HealCommand("heal", "Restores your health to max", false));
        addSubCommand(new TestCommand("test", "Runs the current experiment", false));
    }
}
