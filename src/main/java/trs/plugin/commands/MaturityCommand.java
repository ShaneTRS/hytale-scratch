package trs.plugin.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.FlagArg;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractTargetEntityCommand;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.NPCPlugin;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import trs.plugin.components.MaturityComponent;

import javax.annotation.Nonnull;

public class MaturityCommand extends AbstractTargetEntityCommand {
	private final OptionalArg<Float> seconds;
	private final OptionalArg<String> role;
	private final FlagArg create;
	
	public MaturityCommand(@Nonnull String name, @Nonnull String description, boolean requiresConfirmation) {
		super(name, description, requiresConfirmation);
		this.seconds = withOptionalArg("seconds", "Length of childhood in seconds", ArgTypes.FLOAT);
		this.role = withOptionalArg("role", "Role requested when mature", ArgTypes.STRING);
		this.create = withFlagArg("create", "Create component if missing");
	}
	
	@Override
	protected void execute(
		@NonNullDecl CommandContext commandContext,
		@NonNullDecl ObjectList<Ref<EntityStore>> objectList,
		@NonNullDecl World world,
		@NonNullDecl Store<EntityStore> store
	) {
		for (Ref<EntityStore> ref : objectList) {
			MaturityComponent maturityComp = store.getComponent(ref, MaturityComponent.getComponentType());
			NPCEntity npcComp = store.getComponent(ref, NPCEntity.getComponentType());
			
			if (npcComp == null) continue;
			
			if (commandContext.get(create) && maturityComp == null) {
				maturityComp = new MaturityComponent();
				maturityComp.setCreatureAdultRoleId(npcComp.getRoleIndex());
				store.addComponent(ref, MaturityComponent.getComponentType(), maturityComp);
			}
			
			if (maturityComp == null) continue;
			
			if (commandContext.get(seconds) != null) {
				maturityComp.setCreatureChildhood(commandContext.get(seconds));
				maturityComp.setCreatureAge(0f);
			}
			if (commandContext.get(role) != null) {
				maturityComp.setCreatureAdultRole(commandContext.get(role));
			}
			
			commandContext.sendMessage(Message.raw(String.format(
				"%s will grow up into %s. %.3f/%.3f",
				npcComp.getRoleName(),
				NPCPlugin.get().getName(maturityComp.getCreatureAdultRole()),
				maturityComp.getCreatureAge(),
				maturityComp.getCreatureChildhood()
			)));
		}
	}
}
