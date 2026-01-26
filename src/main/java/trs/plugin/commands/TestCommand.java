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
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import trs.plugin.components.MaturityComponent;

import javax.annotation.Nonnull;

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
                for (Ref<EntityStore> ref : objectList) {
                    MaturityComponent comp = store.getComponent(ref, maturityComponent);
                    if (comp != null) {
                        commandContext.sendMessage(Message.raw(String.format(
                                "Will grow up into %s after %s. Lived for %s",
                                comp.getCreatureAdultRole(),
                                comp.getCreatureChildhood(),
                                comp.getCreatureAge()
                        )));
                    } else {
                        commandContext.sendMessage(Message.raw("Will not mature"));
                    }
                }
                break;
            }
            case 1: {
                ComponentType<EntityStore, MaturityComponent> maturityComponent = MaturityComponent.getComponentType();
                for (Ref<EntityStore> ref : objectList) {
                    MaturityComponent comp = store.getComponent(ref, maturityComponent);
                    if (comp != null) {
                        comp.setCreatureChildhood(10f);
                        commandContext.sendMessage(Message.raw(String.format(
                                "Will grow up into %s after %s. Lived for %s",
                                comp.getCreatureAdultRole(),
                                comp.getCreatureChildhood(),
                                comp.getCreatureAge()
                        )));
                    } else {
                        commandContext.sendMessage(Message.raw("Will not mature"));
                    }
                }
            }
        }
    }

}
