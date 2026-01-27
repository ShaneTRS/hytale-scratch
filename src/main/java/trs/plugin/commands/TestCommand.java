package trs.plugin.commands;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
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
import javax.swing.text.html.parser.Entity;

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
                ComponentType<EntityStore, MaturityComponent> maturityComponent = MaturityComponent.getComponentType();
                ComponentType<EntityStore, NPCEntity> npcComponent = NPCEntity.getComponentType();
                for (Ref<EntityStore> ref : objectList) {
                    MaturityComponent compMat = store.getComponent(ref, maturityComponent);
                    NPCEntity compNpc = store.getComponent(ref, npcComponent);
                    if (compMat == null) break;
                    commandContext.sendMessage(maturityDebugMessage(compMat, compNpc));
                }
                break;
            }
            case 1: {
                ComponentType<EntityStore, MaturityComponent> maturityComponent = MaturityComponent.getComponentType();
                ComponentType<EntityStore, NPCEntity> npcComponent = NPCEntity.getComponentType();
                for (Ref<EntityStore> ref : objectList) {
                    MaturityComponent compMat = store.getComponent(ref, maturityComponent);
                    NPCEntity compNpc = store.getComponent(ref, npcComponent);
                    if (compMat == null) break;
                    compMat.setCreatureChildhood(10f);
                    commandContext.sendMessage(maturityDebugMessage(compMat, compNpc));
                }
                break;
            }
        }
    }

    private Message maturityDebugMessage(MaturityComponent maturity, NPCEntity npc) {
        String adultRole = NPCPlugin.get().getName(maturity.getCreatureAdultRole());
        return Message.raw(String.format(
                "%s will grow up into %s after %s. Lived for %s",
                npc.getRoleName(),
                adultRole,
                maturity.getCreatureChildhood(),
                maturity.getCreatureAge()
        ));
    }

}
