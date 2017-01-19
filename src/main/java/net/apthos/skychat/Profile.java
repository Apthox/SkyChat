package net.apthos.skychat;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class Profile {

    public Rank getDominantRank() {
        return DominantRank;
    }

    public void setDominantRank(Rank dominantRank) {
        DominantRank = dominantRank;
    }

    public boolean hasDominantRank(){
        if (DominantRank == null){
            return false;
        }
        return true;
    }

    Rank DominantRank = null;

    public Player getPlayer() {
        return player;
    }

    private Player player;

    public Profile(Player player){
        this.player = player;
        File file = new File(SkyChat.getInstance().getDataFolder() + "/Profiles/"
                + player.getUniqueId().toString() + ".yml");
        if (!file.exists()){
            return;
        }
        YamlConfiguration YAML = YamlConfiguration.loadConfiguration(file);
        setDominantRank(SkyChat.getInstance().getRank(YAML.getString("rank")));
    }

    public void setDominantSelection(Rank rank){
        setDominantRank(rank);
        File file = new File(SkyChat.getInstance().getDataFolder() + "/Profiles/"
                + player.getUniqueId().toString() + ".yml");
        if (file.exists()) file.delete();
        YamlConfiguration YAML = new YamlConfiguration();
        YAML.createSection("rank"); YAML.set("rank", rank.getRankName());
        try {
            YAML.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void destroyDominantSelection(){
        File file = new File(SkyChat.getInstance().getDataFolder() + "/Profiles/"
                + player.getUniqueId().toString() + ".yml");
        if (file.exists()) file.delete();
    }

}
