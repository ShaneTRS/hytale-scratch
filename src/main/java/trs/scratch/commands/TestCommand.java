package trs.scratch.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractTargetEntityCommand;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;

public class TestCommand extends AbstractTargetEntityCommand {
	private final RequiredArg<Integer> targetArg;
	
	public TestCommand(@Nonnull String name, @Nonnull String description, boolean requiresConfirmation) {
		super(name, description, requiresConfirmation);
		this.targetArg = withRequiredArg("id", "Test ID", ArgTypes.INTEGER);
	}
	
	@Override
	protected void execute(
		@NonNullDecl CommandContext commandContext,
		@NonNullDecl ObjectList<Ref<EntityStore>> objectList,
		@NonNullDecl World world,
		@NonNullDecl Store<EntityStore> store
	) {
		switch (this.targetArg.get(commandContext)) {
			case 0: {
				commandContext.sendMessage(Message.raw("0"));
				break;
			}
			case 1: {
				commandContext.sendMessage(Message.raw("1"));
				break;
			}
		}
	}
}
