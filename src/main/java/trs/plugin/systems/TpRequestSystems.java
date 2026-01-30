package trs.plugin.systems;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.modules.physics.component.Velocity;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import trs.plugin.components.TpRequestComponent;

public class TpRequestSystems {
	private static final ComponentType<EntityStore, TpRequestComponent> TP_REQUEST_COMPONENT = TpRequestComponent.getComponentType();
	private static final ComponentType<EntityStore, TransformComponent> TRANSFORM_COMPONENT = TransformComponent.getComponentType();
	private static final ComponentType<EntityStore, Velocity> VELOCITY_COMPONENT = Velocity.getComponentType();
	private static final ComponentType<EntityStore, Teleport> TELEPORT_COMPONENT = Teleport.getComponentType();
	private static final ComponentType<EntityStore, PlayerRef> PLAYER_REF_COMPONENT = PlayerRef.getComponentType();
	
	public static class TpRequestTicking extends EntityTickingSystem<EntityStore> {
		@Override
		public void tick(
			float dt,
			int index,
			@NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk,
			@NonNullDecl Store<EntityStore> store,
			@NonNullDecl CommandBuffer<EntityStore> commandBuffer
		) {
			TpRequestComponent tpRequestComp = archetypeChunk.getComponent(index, TP_REQUEST_COMPONENT);
			if (!tpRequestComp.getTpAccepted()) {
				tpRequestComp.decrementLifetime(dt);
				if (tpRequestComp.getTpLifetime() > 0) return;
				
				Ref<EntityStore> ref = archetypeChunk.getReferenceTo(index);
				HytaleLogger.forEnclosingClass().atInfo().log(String.format("Teleport component timed out for %s", ref));
				commandBuffer.removeComponent(ref, TP_REQUEST_COMPONENT);
			} else {
				tpRequestComp.decrementTpDelay(dt);
				
				if (tpRequestComp.getTpDelay() < 3 && archetypeChunk.getComponent(index, VELOCITY_COMPONENT).getSpeed() > 1) {
					Ref<EntityStore> ref = archetypeChunk.getReferenceTo(index);
					commandBuffer.removeComponent(ref, TP_REQUEST_COMPONENT);
					commandBuffer.getComponent(ref, PLAYER_REF_COMPONENT).sendMessage(Message.raw(
						"Your teleport request was cancelled because you moved!"
					));
					return;
				}
				
				if (tpRequestComp.getTpDelay() > 0) return;
				
				Ref<EntityStore> targetRef = commandBuffer.getExternalData().getRefFromUUID(tpRequestComp.getTpTarget());
				if (targetRef == null) return;
				TransformComponent transformComp = commandBuffer.getComponent(targetRef, TRANSFORM_COMPONENT);
				if (transformComp == null) return;
				
				Teleport teleportComp = Teleport.createForPlayer(transformComp.getTransform()).withoutVelocityReset();
				Ref<EntityStore> ref = archetypeChunk.getReferenceTo(index);
				
				commandBuffer.addComponent(ref, TELEPORT_COMPONENT, teleportComp);
				commandBuffer.getComponent(ref, PLAYER_REF_COMPONENT).sendMessage(Message.raw(
					"Teleporting..."
				));
				commandBuffer.removeComponent(ref, TP_REQUEST_COMPONENT);
			}
		}
		
		@Override
		public Query<EntityStore> getQuery() { return Query.and(TP_REQUEST_COMPONENT, VELOCITY_COMPONENT, PLAYER_REF_COMPONENT); }
	}

//	public static class TpRequestRef extends RefSystem<EntityStore> {
//		@Override
//		public void onEntityAdded(
//			@NonNullDecl Ref<EntityStore> ref,
//			@NonNullDecl AddReason addReason,
//			@NonNullDecl Store<EntityStore> store,
//			@NonNullDecl CommandBuffer<EntityStore> commandBuffer
//		) { }
//
//		@Override
//		public void onEntityRemove(
//			@NonNullDecl Ref<EntityStore> ref,
//			@NonNullDecl RemoveReason removeReason,
//			@NonNullDecl Store<EntityStore> store,
//			@NonNullDecl CommandBuffer<EntityStore> commandBuffer
//		) { }
//
//		@NullableDecl
//		@Override
//		public Query<EntityStore> getQuery() { return Query.and(null); }
//	}
}