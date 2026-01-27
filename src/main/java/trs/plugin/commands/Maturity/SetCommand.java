package trs.plugin.commands.Maturity;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractTargetEntityCommand;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import trs.plugin.components.MaturityComponent;

import javax.annotation.Nonnull;

import static trs.plugin.commands.Maturity.MaturityCollection.maturityDebugMessage;

public class SetCommand extends AbstractTargetEntityCommand {

    private final RequiredArg<Float> seconds;

    public SetCommand(@Nonnull String name, @Nonnull String description, boolean requiresConfirmation) {
        super(name, description, requiresConfirmation);
        this.seconds = withRequiredArg("seconds", "Length of childhood in seconds", ArgTypes.FLOAT);
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
            maturityComp.setCreatureChildhood(commandContext.get(seconds));
            maturityComp.setCreatureAge(0f);
            commandContext.sendMessage(maturityDebugMessage(maturityComp, npcComp));
        }
    }

}
