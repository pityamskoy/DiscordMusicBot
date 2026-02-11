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
public final class SkipCommand implements MusicBotCommand {
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        try {
            if (!isPossibleToExecuteCommandAndReplyIfFalse(event)) {
                return;
            }

            if (!event.getGuild().getAudioManager().isConnected()) {
                event.reply("I'm not connected to a voice channel").setEphemeral(true).queue();
                return;
            }

            TrackScheduler trackScheduler = PlayerManager.getInstance().getGuildMusicManager(event.getGuild()).trackScheduler;

            if (PlayerManager.getInstance().getGuildMusicManager(event.getGuild()).audioPlayer.getPlayingTrack() == null) {
                event.reply("I'm not playing anything").setEphemeral(true).queue();
                return;
            }

            trackScheduler.skip();
            event.reply("Current track has been successfully skipped").queue();
        } catch (NullPointerException e) {
            event.reply("I'm sorry, a error has been occurred").setEphemeral(true).queue();
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "skip";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Skips current track";
    }

    @NotNull
    @Override
    public Optional<Collection<OptionData>> getOptions() {
        return Optional.empty();
    }
}
