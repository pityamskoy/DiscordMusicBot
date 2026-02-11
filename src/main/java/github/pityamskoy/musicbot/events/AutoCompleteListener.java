package github.pityamskoy.musicbot.events;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.List;
import java.util.stream.Stream;


public final class AutoCompleteListener extends ListenerAdapter {
    private final String[] loopTypes = {"track", "queue"};

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        if (event.getName().equals("loop") && event.getFocusedOption().getName().equals("type")) {
            List<Command.Choice> options = Stream.of(loopTypes)
                    .filter(type -> type.startsWith(event.getFocusedOption().getValue()))
                    .map(type -> new Command.Choice(type, type))
                    .toList();

            event.replyChoices(options).queue();
        }
    }
}
