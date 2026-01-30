package trs.plugin.commands;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import trs.plugin.components.TpRequestComponent;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.UUID;

public class TpRequestCommands {
	private static final ComponentType<EntityStore, TpRequestComponent> TP_REQUEST_COMPONENT = TpRequestComponent.getComponentType();
	private static final ComponentType<EntityStore, PlayerRef> PLAYER_REF_COMPONENT = PlayerRef.getComponentType();
	
	public static class TpRequestCommand extends AbstractPlayerCommand {
		private final RequiredArg<UUID> target;
		
		public TpRequestCommand(@Nonnull String name, @Nonnull String description, boolean requiresConfirmation) {
			super(name, description, requiresConfirmation);
			this.target = withRequiredArg("target", "Player you wish to teleport to", ArgTypes.PLAYER_UUID);
		}
		
		@Override
		protected void execute(
			@Nonnull CommandContext commandContext,
			@Nonnull Store<EntityStore> store,
			@Nonnull Ref<EntityStore> ref,
			@Nonnull PlayerRef playerRef,
			@Nonnull World world
		) {
			TpRequestComponent tpRequestComp = new TpRequestComponent();
			UUID targetUUID = commandContext.get(target);
			PlayerRef targetRef = store.getComponent(world.getEntityRef(targetUUID), PLAYER_REF_COMPONENT);
			tpRequestComp.setTpTarget(targetUUID);
			
			store.addComponent(ref, TP_REQUEST_COMPONENT, tpRequestComp);
			String playerUsername = playerRef.getUsername();
			Message playerReassure = Message.raw("You sent a teleport request to ")
				.insert(Message.raw(targetRef.getUsername()).color(Color.WHITE))
				.insert(String.format("! They have %.0f seconds to accept..", tpRequestComp.getTpLifetime()))
				.color(Color.LIGHT_GRAY);
			playerRef.sendMessage(playerReassure);
			Message targetNotify = Message.raw(playerUsername + " wants to teleport to you! You may use ")
				.insert(Message.raw("/tpaccept " + playerUsername).color(Color.WHITE))
				.insert(" to accept their request.")
				.color(Color.LIGHT_GRAY);
			targetRef.sendMessage(targetNotify);
		}
	}
	
	public static class TpAcceptAllCommand extends AbstractPlayerCommand {
		public TpAcceptAllCommand(@Nonnull String name, @Nonnull String description, boolean requiresConfirmation) {
			super(name, description, requiresConfirmation);
		}
		public TpAcceptAllCommand(@Nonnull String description) { super(description); }
		
		/** Constructor with specific variant */
		public TpAcceptAllCommand(@Nonnull String name, @Nonnull String description, boolean requiresConfirmation, @Nonnull String variantDescription) {
			super(name, description, requiresConfirmation);
			this.addUsageVariant(new TpAcceptSpecificCommand(variantDescription));
		}
		
		@Override
		protected void execute(
			@Nonnull CommandContext commandContext,
			@Nonnull Store<EntityStore> store,
			@Nonnull Ref<EntityStore> ref,
			@Nonnull PlayerRef playerRef,
			@Nonnull World world
		) {
			world.getPlayerRefs().forEach(targetRef -> {
				acceptRequest(store, targetRef.getReference(), targetRef, playerRef);
			});
		}
		
	}
	
	public static class TpAcceptSpecificCommand extends AbstractPlayerCommand {
		private final RequiredArg<UUID> target;
		
		public TpAcceptSpecificCommand(@Nonnull String name, @Nonnull String description, boolean requiresConfirmation) {
			super(name, description, requiresConfirmation);
			this.target = withRequiredArg("target", "Player you wish to accept", ArgTypes.PLAYER_UUID);
		}
		public TpAcceptSpecificCommand(@Nonnull String description) {
			super(description);
			this.target = withRequiredArg("target", "Player you wish to accept", ArgTypes.PLAYER_UUID);
		}
		
		@Override
		protected void execute(
			@Nonnull CommandContext commandContext,
			@Nonnull Store<EntityStore> store,
			@Nonnull Ref<EntityStore> ref,
			@Nonnull PlayerRef playerRef,
			@Nonnull World world
		) {
			UUID targetUUID = commandContext.get(target);
			if (targetUUID == null) return;
			
			Ref<EntityStore> targetRef = world.getEntityRef(targetUUID);
			PlayerRef targetPlayerRef = store.getComponent(targetRef, PLAYER_REF_COMPONENT);
			acceptRequest(store, targetRef, targetPlayerRef, playerRef);
		}
	}
	
	private static void acceptRequest(
		Store<EntityStore> store,
		Ref<EntityStore> targetRef,
		PlayerRef targetPlayerRef,
		PlayerRef playerRef
	) {
		TpRequestComponent tpRequestComp = store.getComponent(targetRef, TP_REQUEST_COMPONENT);
		if (tpRequestComp == null || tpRequestComp.getTpTarget() != playerRef.getUuid()) return;
		tpRequestComp.setTpAccepted(true);
		Message targetNotify = Message.raw(playerRef.getUsername()).color(Color.WHITE)
			.insert(Message.raw(" accepted your teleport request! Make sure to stop moving if you want to teleport!").color(Color.LIGHT_GRAY));
		Message playerNotify = Message.raw("You accepted ")
			.insert(Message.raw(targetPlayerRef.getUsername()).color(Color.WHITE)).insert("'s teleport request! They'll be teleported momentarily..").color(Color.LIGHT_GRAY);
		playerRef.sendMessage(playerNotify);
		targetPlayerRef.sendMessage(targetNotify);
	}
}
