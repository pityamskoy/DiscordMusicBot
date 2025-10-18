package github.pityamskoy.musicbot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;

public interface MusicBotCommand {
    void execute(@NotNull SlashCommandInteractionEvent event);

    @NotNull
    String getName();

    @NotNull
    String getDescription();

    @Nullable
    Optional<Collection<OptionData>> getOptions();
}
