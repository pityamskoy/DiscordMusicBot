package github.pityamskoy.musicbot.commands.commands;

import github.pityamskoy.musicbot.commands.MusicBotCommand;
import github.pityamskoy.musicbot.commands.lavaplayer.TrackScheduler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

import static github.pityamskoy.musicbot.Utility.isPossibleToExecuteCommandAndReplyIfFalse;

@SuppressWarnings(value = {"DataFlowIssue"})
public class ClearCommand implements MusicBotCommand {
    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        try {
            if (!isPossibleToExecuteCommandAndReplyIfFalse(event)) {
                return;
            }

            Guild guild = event.getGuild();
            AudioManager audioManager = guild.getAudioManager();

            Long guildId = guild.getIdLong();
            TrackScheduler trackScheduler = TrackScheduler.getGuildScheduler(guildId);

            if (trackScheduler == null) {
                event.reply("I haven't played music on this server").setEphemeral(true).queue();
                return;
            }
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
