package trs.plugin.systems;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefSystem;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import com.hypixel.hytale.server.npc.systems.RoleChangeSystem;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import trs.plugin.assets.MaturityAsset;
import trs.plugin.components.MaturityComponent;

import java.util.logging.Level;

import static com.hypixel.hytale.math.vector.Vector3d.formatShortString;

public class MaturitySystems {
	private static final ComponentType<EntityStore, MaturityComponent> maturityCompType = MaturityComponent.getComponentType();
	private static final ComponentType<EntityStore, NPCEntity> npcCompType = NPCEntity.getComponentType();
	
	public static class MaturityTicking extends EntityTickingSystem<EntityStore> {
		@Override
		public void tick(
			float dt,
			int index,
			@NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk,
			@NonNullDecl Store<EntityStore> store,
			@NonNullDecl CommandBuffer<EntityStore> commandBuffer
		) {
			MaturityComponent maturityComp = archetypeChunk.getComponent(index, maturityCompType);
			maturityComp.incrementAge(dt);
			
			if (!maturityComp.completedChildhood()) return;
			
			NPCEntity npcComp = archetypeChunk.getComponent(index, npcCompType);
			Ref<EntityStore> ref = archetypeChunk.getReferenceTo(index);
			
			RoleChangeSystem.requestRoleChange(ref, npcComp.getRole(), maturityComp.getCreatureAdultRole(), true, store);
			HytaleLogger.forEnclosingClass().at(Level.INFO).log(String.format("Requested role change for %s at position %s", npcComp.getRoleName(), formatShortString(npcComp.getOldPosition())));
			commandBuffer.removeComponent(ref, maturityCompType);
		}
		
		@Override
		public Query<EntityStore> getQuery() {
			return Query.and(maturityCompType, npcCompType);
		}
	}
	
	public static class MaturityRef extends RefSystem<EntityStore> {
		@Override
		public void onEntityAdded(
			@NonNullDecl Ref<EntityStore> ref,
			@NonNullDecl AddReason addReason,
			@NonNullDecl Store<EntityStore> store,
			@NonNullDecl CommandBuffer<EntityStore> commandBuffer
		) {
			NPCEntity npcComp = store.getComponent(ref, npcCompType);
			MaturityAsset maturityAsset = MaturityAsset.getAssetMap().getAsset(npcComp.getRoleName());
			if (maturityAsset == null) return;
			
			commandBuffer.addComponent(ref, maturityCompType, MaturityComponent.fromAsset(maturityAsset));
		}
		
		@Override
		public void onEntityRemove(
			@NonNullDecl Ref<EntityStore> ref,
			@NonNullDecl RemoveReason removeReason,
			@NonNullDecl Store<EntityStore> store,
			@NonNullDecl CommandBuffer<EntityStore> commandBuffer
		) { }
		
		@NullableDecl
		@Override
		public Query<EntityStore> getQuery() {
			return Query.and(npcCompType, Query.not(maturityCompType));
		}
	}
}