package trs.scratch.commands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;

public class TrsCollection extends AbstractCommandCollection {
	public TrsCollection() {
		super("trs", "Commands provided by the TRS plugin.");
		addSubCommand(new PingCommand("ping", "Report connection latency", false));
		addSubCommand(new TestCommand("test", "Run various experiments", false));
		addSubCommand(new MaturityCommand("maturity", "Modify and view maturity components", false));
	}
}
