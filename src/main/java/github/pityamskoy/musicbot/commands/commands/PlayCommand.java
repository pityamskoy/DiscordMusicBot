package github.pityamskoy.musicbot.commands.commands;

import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import github.pityamskoy.musicbot.commands.MusicBotCommand;
import github.pityamskoy.musicbot.commands.lavaplayer.AudioLoadResultHandlerImpl;
import github.pityamskoy.musicbot.commands.lavaplayer.AudioPlayerSendHandler;
import github.pityamskoy.musicbot.commands.lavaplayer.TrackScheduler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static github.pityamskoy.musicbot.Utility.isMemberConnectedToVoiceChannel;
import static github.pityamskoy.musicbot.commands.commands.JoinCommand.connectToVoiceChannel;


@SuppressWarnings(value = {"DataFlowIssue"})
public final class PlayCommand implements MusicBotCommand {
    private void setEnvironment(
            Guild guild,
            AudioPlayerManager audioPlayerManager
    ) {
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        AudioPlayer audioPlayer = audioPlayerManager.createPlayer();
        AudioPlayerSendHandler audioPlayerSendHandler = new AudioPlayerSendHandler(audioPlayer);
        guild.getAudioManager().setSendingHandler(audioPlayerSendHandler);

        Long guildId = guild.getIdLong();
        TrackScheduler trackScheduler = new TrackScheduler(audioPlayer, guildId);
        audioPlayer.addListener(trackScheduler);
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        try {
            Guild guild = event.getGuild();
            AudioManager audioManager = guild.getAudioManager();
            GuildVoiceState memberVoiceState = event.getMember().getVoiceState();

            if (!isMemberConnectedToVoiceChannel(event)) {
                event.reply("You should be connected to a voice channel").setEphemeral(true).queue();
                return;
            }

            if (audioManager.isConnected()) {
                if (audioManager.getConnectedChannel() != memberVoiceState.getChannel()) {
                    event.reply("You should be in the same channel with me to call this command").setEphemeral(true).queue();
                    return;
                }
            } else {
                connectToVoiceChannel(event);
            }

            Long guildId = guild.getIdLong();
            TrackScheduler trackScheduler = TrackScheduler.getGuildScheduler(guildId);
            AudioLoadResultHandlerImpl audioLoadResultHandlerImpl = new AudioLoadResultHandlerImpl(guildId);
            AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();

            if (trackScheduler == null) {
                this.setEnvironment(guild, audioPlayerManager);
            }

            Message.Attachment file = event.getOption("file").getAsAttachment();
            //test in different guilds and try to put as key guildLongId
            audioPlayerManager.loadItemOrdered(guild, file.getUrl(), audioLoadResultHandlerImpl);

            event.reply(MessageFormat.format("Playing {0}", file.getFileName())).queue();
        } catch (NullPointerException e) {
            event.reply("I'm sorry. A error has been occurred").setEphemeral(true).queue();
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
        return "Plays music files";
    }

    @NotNull
    @Override
    public Optional<Collection<OptionData>> getOptions() {
        OptionData file = new OptionData(OptionType.ATTACHMENT, "file",
                "Support only .mp3 / .wav files", true);
        return Optional.of(List.of(file));
    }
}