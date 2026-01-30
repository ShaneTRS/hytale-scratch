package trs.scratch.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.connection.PongType;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.EventTitleUtil;

import javax.annotation.Nonnull;

public class PingCommand extends AbstractPlayerCommand {
	public PingCommand(@Nonnull String name, @Nonnull String description, boolean requiresConfirmation) {
		super(name, description, requiresConfirmation);
	}
	
	@Override
	protected void execute(
		@Nonnull CommandContext commandContext,
		@Nonnull Store<EntityStore> store,
		@Nonnull Ref<EntityStore> ref,
		@Nonnull PlayerRef playerRef,
		@Nonnull World world
	) {
		Message pingMessage = Message.raw(String.format(
			"Your ping is %.1fms!",
			playerRef.getPacketHandler().getPingInfo(PongType.Direct).getPingMetricSet().getAverage(0) / 1000d
		));
		
		EventTitleUtil.hideEventTitleFromPlayer(playerRef, 0.5f);
		EventTitleUtil.showEventTitleToPlayer(
			playerRef,
			pingMessage,
			Message.raw(playerRef.getUsername() + " - " + world.getName()),
			true, null,
			4.0f, 0.5f, 0.5f
		);
		commandContext.sendMessage(pingMessage);
	}
}
