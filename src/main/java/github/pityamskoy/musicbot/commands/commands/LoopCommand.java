package github.pityamskoy.musicbot.commands.commands;

import github.pityamskoy.musicbot.commands.MusicBotCommand;
import github.pityamskoy.musicbot.commands.lavaplayer.PlayerManager;
import github.pityamskoy.musicbot.commands.lavaplayer.TrackScheduler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static github.pityamskoy.musicbot.Utility.isPossibleToExecuteCommandAndReplyIfFalse;


@SuppressWarnings(value = {"DataFlowIssue"})
public final class LoopCommand implements MusicBotCommand {
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

            //fix input. Make only two options.
            String type = event.getOption("type").getAsString();

            boolean isQueueRepeat = trackScheduler.isQueueRepeat();
            boolean isTrackRepeat = trackScheduler.isTrackRepeat();

            if (PlayerManager.getInstance().getGuildMusicManager(event.getGuild()).audioPlayer.getPlayingTrack() == null) {
                event.reply("I cannot loop the queue or a track because I'm not playing anything").setEphemeral(true).queue();
                return;
            }

            if (type.equals("queue")) {
                if (!isQueueRepeat) {
                    event.reply("Repeating the queue").queue();
                } else {
                    event.reply("Stopped repeating the queue").queue();
                }
                trackScheduler.setQueueRepeat(!isQueueRepeat);
            } else if (type.equals("track")) {
                if (!isTrackRepeat) {
                    event.reply("Repeating the current track").queue();
                } else {
                    event.reply("Stopped repeating the current track").queue();
                }
                trackScheduler.setTrackRepeat(!isTrackRepeat);
            }
        } catch (NullPointerException e) {
            event.reply("I'm sorry. A error has been occurred").setEphemeral(true).queue();
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "loop";
    }


    @NotNull
    @Override
    public String getDescription() {
        return "Repeats current track or queue";
    }

    @NotNull
    @Override
    public Optional<Collection<OptionData>> getOptions() {
        OptionData loopType = new OptionData(OptionType.STRING, "type",
                "Choose track/queue", false)
                .addChoice("track", "track").addChoice("queue", "queue").setAutoComplete(true);

        return Optional.of(List.of(loopType));
    }
}
