package group.sam;

import javax.security.auth.login.LoginException;

import club.minnced.discord.jdave.interop.JDaveSessionFactory;
import group.sam.interaction.listeners.AdminListener;
import group.sam.interaction.listeners.GuildListener;
import net.dv8tion.jda.api.audio.AudioModuleConfig;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import group.sam.interaction.CommandManager;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings(value ={"ArraysAsListWithZeroOrOneArgument"})
public final class Sam {
    public Sam() throws LoginException {
        final List<GatewayIntent> GATEWAY_INTENTS = Arrays.asList(
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.MESSAGE_CONTENT
        );
        final List<CacheFlag> CACHE_FLAGS = Arrays.asList(
                CacheFlag.ONLINE_STATUS
        );

        // Creating the builder
        final String TOKEN = System.getenv("DISCORD_MUSIC_BOT_TOKEN");
        final DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(TOKEN);

        // Setting the builder
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("music | /help"));
        builder.enableIntents(GATEWAY_INTENTS);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.enableCache(CACHE_FLAGS);
        builder.setAudioModuleConfig(new AudioModuleConfig().withDaveSessionFactory(new JDaveSessionFactory()));

        // Building the shardManager
        final ShardManager shardManager = builder.build();

        shardManager.addEventListener(
                new CommandManager(),
                new GuildListener(),
                new AdminListener()
        );
    }
}