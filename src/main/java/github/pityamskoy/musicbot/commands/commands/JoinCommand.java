package github.pityamskoy.musicbot.commands.commands;

import github.pityamskoy.musicbot.commands.MusicBotCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.Collection;

@SuppressWarnings(value = {"DataFlowIssue"})
public final class JoinCommand implements MusicBotCommand {
    static void connectToVoiceChannel(@NotNull SlashCommandInteractionEvent event) {
        long channelID = event.getMember().getVoiceState().getChannel().getIdLong();

        Guild guild = event.getGuild();
        AudioManager audioManager = guild.getAudioManager();
        VoiceChannel voiceChannel = guild.getVoiceChannelById(channelID);

        audioManager.openAudioConnection(voiceChannel);
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        try {
            connectToVoiceChannel(event);

            String voiceChannelName = event.getMember().getVoiceState().getChannel().getName();
            event.reply(MessageFormat.format("Успешно установлено соединение с голосовым каналом {0}",
                    voiceChannelName)).setEphemeral(true).queue();
        } catch (NullPointerException e) {
            event.reply("Вы не находитесь в голосовом канале").setEphemeral(true).queue();
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "join";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Если бот ещё не подключён к голосовому каналу, " +
                "то присоединяется к тому, в котором Вы находитесь";
    }

    @Nullable
    @Override
    public Collection<OptionData> getOptions() {
        return null;
    }
}
