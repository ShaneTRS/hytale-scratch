package trs.plugin.commands.Maturity;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractTargetEntityCommand;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import trs.plugin.components.MaturityComponent;

import javax.annotation.Nonnull;

import static trs.plugin.commands.Maturity.MaturityCollection.maturityDebugMessage;

public class QueryCommand extends AbstractTargetEntityCommand {

    public QueryCommand(@Nonnull String name, @Nonnull String description, boolean requiresConfirmation) {
        super(name, description, requiresConfirmation);
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
            if (maturityComp == null) break;
            commandContext.sendMessage(maturityDebugMessage(maturityComp, npcComp));
        }
    }

}
