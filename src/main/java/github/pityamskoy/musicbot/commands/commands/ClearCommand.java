package github.pityamskoy.musicbot.commands.commands;

import github.pityamskoy.musicbot.commands.MusicBotCommand;
import github.pityamskoy.musicbot.commands.lavaplayer.PlayerManager;
import github.pityamskoy.musicbot.commands.lavaplayer.TrackScheduler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

import static github.pityamskoy.musicbot.Utility.isPossibleToExecuteCommandAndReplyIfFalse;

@SuppressWarnings(value = {"DataFlowIssue"})
public final class ClearCommand implements MusicBotCommand {
    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        try {
            if (!isPossibleToExecuteCommandAndReplyIfFalse(event)) {
                return;
            }

            if (!event.getGuild().getAudioManager().isConnected()) {
                event.reply("I'm not connected to a voice channel").setEphemeral(true).queue();
                return;
            }

            TrackScheduler trackScheduler = PlayerManager.getInstance().getGuildMusicManager(event.getGuild()).trackScheduler;
            trackScheduler.clear();

            event.reply("The queue has been successfully cleared.").queue();
        } catch (NullPointerException e) {
            event.reply("I'm sorry, a error has been occurred").setEphemeral(true).queue();
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "clear";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Clears all songs in the enqueue";
    }

    @NotNull
    @Override
    public Optional<Collection<OptionData>> getOptions() {
        return Optional.empty();
    }
}
