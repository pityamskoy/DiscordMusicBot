package github.pityamskoy.musicbot.commands.commands;

import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import github.pityamskoy.musicbot.commands.MusicBotCommand;
import github.pityamskoy.musicbot.commands.lavaplayer.AudioPlayerSendHandler;
import github.pityamskoy.musicbot.commands.lavaplayer.TrackScheduler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;

import static github.pityamskoy.musicbot.commands.commands.JoinCommand.connectToVoiceChannel;

@SuppressWarnings(value = {"ArraysAsListWithZeroOrOneArgument", "DataFlowIssue"})
public final class PlayCommand implements MusicBotCommand {

    private final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        try {
            //optimize openAudioconnection if bot is connected
            connectToVoiceChannel(event);

            Guild guild = event.getGuild();
            Message.Attachment file = event.getOption("file").getAsAttachment();

            AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();
            AudioSourceManagers.registerRemoteSources(audioPlayerManager);

            AudioPlayer audioPlayer = this.playerManager.createPlayer();
            AudioPlayerSendHandler audioPlayerSendHandler = new AudioPlayerSendHandler(audioPlayer);
            guild.getAudioManager().setSendingHandler(audioPlayerSendHandler);

            TrackScheduler trackScheduler = new TrackScheduler(audioPlayer);
            audioPlayer.addListener(trackScheduler);
            audioPlayerManager.loadItemOrdered(event.getGuild(), file.getUrl(), new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack track) {
                    trackScheduler.queue(track);
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {
                    trackScheduler.queue(playlist.getTracks().get(0));
                }

                @Override
                public void noMatches() {

                }

                @Override
                public void loadFailed(FriendlyException exception) {

                }
            });

            event.reply(MessageFormat.format("Воспроизводится {0}", file.getFileName())).setEphemeral(true).queue();
        } catch (NullPointerException e) {
            event.reply("Вы не находитесь в голосовом канале").setEphemeral(true).queue();
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "play";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Проигрывает .mp3 / .wav файл, который Вы укажете";
    }

    @NotNull
    @Override
    public Collection<OptionData> getOptions() {
        OptionData file = new OptionData(OptionType.ATTACHMENT, "file",
                "Поддерживает только .mp3 / .wav файлы", true);
        return Arrays.asList(file);
    }
}