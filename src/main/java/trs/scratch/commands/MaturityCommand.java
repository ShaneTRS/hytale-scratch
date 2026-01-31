package trs.scratch.commands;

import com.hypixel.hytale.component.ComponentType;
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
import trs.scratch.components.MaturityComponent;

import javax.annotation.Nonnull;


public class MaturityCommand extends AbstractTargetEntityCommand {
	private static final ComponentType<EntityStore, MaturityComponent> MATURITY_COMPONENT = MaturityComponent.getComponentType();
	private static final ComponentType<EntityStore, NPCEntity> NPC_ENTITY_COMPONENT = NPCEntity.getComponentType();
	
	public MaturityCommand(@Nonnull String name, @Nonnull String description, boolean requiresConfirmation) {
		super(name, description, requiresConfirmation);
		this.seconds = withOptionalArg("seconds", "Length of childhood in seconds", ArgTypes.FLOAT);
		this.role = withOptionalArg("role", "Role requested when mature", ArgTypes.STRING);
		this.create = withFlagArg("create", "Create component if missing");
	}
	
	private final OptionalArg<Float> seconds;
	private final OptionalArg<String> role;
	private final FlagArg create;
	
	@Override
	protected void execute(
		@NonNullDecl CommandContext commandContext,
		@NonNullDecl ObjectList<Ref<EntityStore>> objectList,
		@NonNullDecl World world,
		@NonNullDecl Store<EntityStore> store
	) {
		for (Ref<EntityStore> ref : objectList) {
			NPCEntity npcComp = store.getComponent(ref, NPC_ENTITY_COMPONENT);
			if (npcComp == null) continue;
			
			MaturityComponent maturityComp = store.getComponent(ref, MATURITY_COMPONENT);
			if (commandContext.get(create) && maturityComp == null) {
				maturityComp = new MaturityComponent();
				maturityComp.setAdultRoleId(npcComp.getRoleIndex());
				store.addComponent(ref, MaturityComponent.getComponentType(), maturityComp);
			}
			if (maturityComp == null) continue;
			
			if (commandContext.get(seconds) != null) {
				maturityComp.setChildhood(commandContext.get(seconds));
				maturityComp.setAge(0f);
			}
			if (commandContext.get(role) != null) {
				maturityComp.setAdultRole(commandContext.get(role));
			}
			
			commandContext.sendMessage(Message.raw(String.format(
				"%s will grow up into %s. %.3f/%.3f",
				npcComp.getRoleName(),
				NPCPlugin.get().getName(maturityComp.getAdultRole()),
				maturityComp.getAge(),
				maturityComp.getChildhood()
			)));
		}
	}
}
