package fr.gwenzy.bots.sagiri.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.audio.IAudioManager;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.MissingPermissionsException;

import java.util.HashMap;
import java.util.Map;

public class MusicManager {
  private static final Logger log = LoggerFactory.getLogger(MusicManager.class);


  private static AudioPlayerManager playerManager;
  private static Map<Long, GuildMusicManager> musicManagers;

  public MusicManager() {
    this.musicManagers = new HashMap<>();

    this.playerManager = new DefaultAudioPlayerManager();
    AudioSourceManagers.registerRemoteSources(playerManager);
    AudioSourceManagers.registerLocalSource(playerManager);
  }

  public static synchronized GuildMusicManager getGuildAudioPlayer(IGuild guild) {
    long guildId = guild.getLongID();
    GuildMusicManager musicManager = musicManagers.get(guildId);

    if (musicManager == null) {
      musicManager = new GuildMusicManager(playerManager, guild);
      musicManagers.put(guildId, musicManager);
    }

    guild.getAudioManager().setAudioProvider(musicManager.getAudioProvider());

    return musicManager;
  }

  public static void loadAndPlay(final IChannel channel, final String trackUrl, final long userID, final IGuild guild) {
    final GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

    playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
      @Override
      public void trackLoaded(AudioTrack track) {
        musicManager.addAuthor(userID);
        if(!musicManager.isSneakyMode())
        if(musicManager.getQueueSize()==1)
          sendMessageToChannel(channel, "Now playing "+track.getInfo().title);
        else
          sendMessageToChannel(channel, "Adding to queue (Rank #"+ (musicManager.getQueueSize()-1) +") "+track.getInfo().title);
        play(channel.getGuild(), musicManager, track);
      }

      @Override
      public void playlistLoaded(AudioPlaylist playlist) {

        sendMessageToChannel(channel, "Playlists are not available yet");


      }

      @Override
      public void noMatches() {
        sendMessageToChannel(channel, "Nothing found by " + trackUrl);
      }

      @Override
      public void loadFailed(FriendlyException exception) {
        sendMessageToChannel(channel, "Could not play: " + exception.getMessage());
      }
    });
  }

  private static void play(IGuild guild, GuildMusicManager musicManager, AudioTrack track) {
    musicManager.scheduler.queue(track);
  }

  private void skipTrack(IChannel channel, IGuild guild) {
    GuildMusicManager musicManager = getGuildAudioPlayer(guild);
    musicManager.scheduler.nextTrack();

    sendMessageToChannel(channel, "Skipping track");
  }

  private static void sendMessageToChannel(IChannel channel, String message) {
    try {
      channel.sendMessage(message);
    } catch (Exception e) {
      log.warn("Failed to send message {} to {}", message, channel.getName(), e);
    }
  }

  private static void connectToFirstVoiceChannel(IAudioManager audioManager) {
    for (IVoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
      if (voiceChannel.isConnected()) {
        return;
      }
    }

    for (IVoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
      try {
        voiceChannel.join();
      } catch (MissingPermissionsException e) {
        log.warn("Cannot enter voice channel {}", voiceChannel.getName(), e);
      }
    }
  }
}
