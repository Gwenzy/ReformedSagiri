package fr.gwenzy.bots.sagiri.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.gwenzy.bots.sagiri.ReformedSagiri;
import sx.blah.discord.handle.obj.IGuild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Holder for both the player and a track scheduler for one guild.
 */
public class GuildMusicManager {
  /**
   * Audio player for the guild.
   */
  private final AudioPlayer player;
  private int nextCount;
  private AudioTrack currentTrack;
  private long currentAuthor;
  private List<Long> authors;
  private HashMap<Long, List<String>> lastSearches;
  private boolean sneakyMode;
  /**
   * Track scheduler for the player.
   */
  public final TrackScheduler scheduler;

  /**
   * Creates a player and a track scheduler.
   * @param manager Audio player manager to use for creating the player.
   */
  public GuildMusicManager(AudioPlayerManager manager, IGuild guild) {
    player = manager.createPlayer();
    player.setVolume(5);
    scheduler = new TrackScheduler(player, guild);
    player.addListener(scheduler);
    authors = new ArrayList<>();
    lastSearches = new HashMap<>();
    this.sneakyMode = false;
    nextCount = 0;
  }

  public void addAuthor(long ID){
    this.authors.add(ID);
  }

  public HashMap<Long, List<String>> getLastSearches(){
    return this.lastSearches;
  }

  public void setLastSearches(HashMap<Long, List<String>> searches){
    this.lastSearches = searches;
  }


  public void replaceLastSearches(Long id, List<String> searches){
    if(this.lastSearches.containsKey(id))
      this.lastSearches.remove(id);
    this.lastSearches.put(id, searches);
  }

  public int getQueueSize()
  {
      if(authors.size()==1 && player.getPlayingTrack()!=null)
          return 2;

    return authors.size();
  }
  /**
   * @return Wrapper around AudioPlayer to use it as an AudioSendHandler.
   */

  public String getQueueDesc(){
      String queue = "";
      int rank = 1;
      for(AudioTrack track : scheduler.getQueue()){
          queue+="**#"+rank+"** Song added by "+ ReformedSagiri.logged.getUserByID(authors.get(rank-1)).getName()+": "+track.getInfo().title+"\n";
          rank++;
      }

      return queue;
  }
  public AudioProvider getAudioProvider() {
    return new AudioProvider(player);
  }

    public AudioPlayer getPlayer() {
        return player;
    }



  public long getCurrentAuthor() {
    return currentAuthor;
  }


  public void newTrack(AudioTrack track) {
    this.currentTrack = track;
    this.nextCount = 0;
    this.currentAuthor = this.authors.get(0);
    this.authors.remove(0);

  }

  public long getStartingTimestamp() {
    return this.scheduler.getStartingTimestamp();
  }

    public boolean isSneakyMode() {
        return sneakyMode;
    }

    public void setSneakyMode(boolean sneakyMode) {
        this.sneakyMode = sneakyMode;
    }

    public void stop() {
      this.player.stopTrack();
      this.scheduler.clear();
    }

    public void next(){
      this.scheduler.nextTrack();
    }
}
