package trs.plugin.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractTargetEntityCommand;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import trs.plugin.components.TpRequestComponent;

import javax.annotation.Nonnull;
import java.util.UUID;

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
				TpRequestComponent tpRequestComp = new TpRequestComponent();
				UUID targetUUID = store.getComponent(objectList.get(0), UUIDComponent.getComponentType()).getUuid();
				
				tpRequestComp.setTpTarget(targetUUID);
				tpRequestComp.setTpAccepted(true);
				HytaleLogger.forEnclosingClass().atInfo().log(String.format(
					"%s %s %s %s",
					tpRequestComp.getTpAccepted(),
					tpRequestComp.getTpDelay(),
					tpRequestComp.getTpLifetime(),
					tpRequestComp.getTpTarget()
				));
				Ref<EntityStore> ref = commandContext.senderAsPlayerRef();
				world.execute(() -> {
					store.addComponent(ref, TpRequestComponent.getComponentType(), tpRequestComp);
				});
				break;
			}
			case 1: {
				TpRequestComponent tpRequestComp = new TpRequestComponent();
				UUID targetUUID = store.getComponent(commandContext.senderAsPlayerRef(), UUIDComponent.getComponentType()).getUuid();
				
				tpRequestComp.setTpTarget(targetUUID);
				tpRequestComp.setTpAccepted(true);
				HytaleLogger.forEnclosingClass().atInfo().log(String.format(
					"%s %s %s %s",
					tpRequestComp.getTpAccepted(),
					tpRequestComp.getTpDelay(),
					tpRequestComp.getTpLifetime(),
					tpRequestComp.getTpTarget()
				));
				Ref<EntityStore> ref = objectList.get(0);
				world.execute(() -> {
					store.addComponent(ref, TpRequestComponent.getComponentType(), tpRequestComp);
				});
				break;
			}
		}
	}
	
}
