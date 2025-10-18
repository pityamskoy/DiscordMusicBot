package github.pityamskoy.musicbot.commands;

import github.pityamskoy.musicbot.commands.commands.JoinCommand;
import github.pityamskoy.musicbot.commands.commands.LeaveCommand;
import github.pityamskoy.musicbot.commands.commands.LoopCommand;
import github.pityamskoy.musicbot.commands.commands.PlayCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public final class CommandManager extends ListenerAdapter {
    private final List<MusicBotCommand> COMMAND_LIST = Arrays.asList(
            new JoinCommand(), new PlayCommand()
    );

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        final List<SlashCommandData> commands = new LinkedList<>();

        for (MusicBotCommand command : COMMAND_LIST) {
            String name = command.getName();
            String description = command.getDescription();
            Optional<Collection<OptionData>> options = command.getOptions();

            if (options.isEmpty()) {
                commands.add(Commands.slash(name, description));
            } else {
                commands.add(Commands.slash(name, description).addOptions(options.get()));
            }
        }

        event.getJDA().updateCommands().addCommands(commands).queue();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String name = event.getName();

        for (MusicBotCommand command : COMMAND_LIST) {
            if (command.getName().equals(name)) {
                command.execute(event);
            }
        }
    }
}