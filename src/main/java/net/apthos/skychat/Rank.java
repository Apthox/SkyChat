package net.apthos.skychat;

import com.google.common.collect.ImmutableSet;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Set;


public class Rank {

    private final Set<String> Tags =
            ImmutableSet.of("{INFO}", "{PREFIX}", "{USERNAME}", "{MESSAGE}",
                    "{RAW_USERNAME}", "{RAW_MESSAGE}");

    private final Set<Character> ColorChars =
            ImmutableSet.of('a', 'b', 'c', 'd', 'e', 'f', '0', '1', '2', '3', '4', '5', '6'
                    , '7', '8', '9', 'k', 'l', 'n', 'm', 'o', 'r');

    public String getRankName() {
        return rankName;
    }

    private String rankName;
    private String format;
    private String username;
    private String prefix;
    private String info;
    private String message;

    public String getPermission() {
        return permission;
    }

    private String permission;

    public int getPriority() {
        return priority;
    }

    private int priority;

    public Rank(String name){
        this.rankName = name;
        YamlConfiguration YAML = YamlConfiguration.loadConfiguration
                (new File(SkyChat.getInstance().getDataFolder() + "/ranks.yml"));
        String dir = "ranks." + name + ".";
        format = YAML.getString(dir + "format");
        username = YAML.getString(dir + "username");
        prefix = YAML.getString(dir + "prefix");
        info = YAML.getString(dir + "info");
        message = YAML.getString(dir + "message");
        permission = YAML.getString(dir + "permission");
        priority = YAML.getInt(dir + "priority");
    }

    public TextComponent getCompletedTextComponent(Profile profile, String message){
        TextComponent textComponent = new TextComponent();
        String appendable = "";
        String format = this.format;

        message =

        for (int x = 0; x < format.toCharArray().length; x++){
            if (format.toCharArray()[x] == '{'){
                if (appendable.toCharArray().length > 0){
                    TextComponent component = new TextComponent
                            (ChatColor.translateAlternateColorCodes('&', appendable));
                    textComponent.addExtra(component);
                    appendable = "";
                }
                String FTag = null;
                for (String tag : Tags){
                    if (format.toCharArray().length < x + tag.toCharArray().length)
                        continue;
                    if (tag.equalsIgnoreCase
                            (format.substring(x, x + tag.toCharArray().length))){
                        FTag = tag;
                        break;
                    }
                }

                if (FTag.equalsIgnoreCase("{INFO}")){
                    TextComponent Info = new TextComponent(
                            ChatColor.translateAlternateColorCodes('&', info));
                    Info.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder("Player Information \n" +
                                    "will go here!").create() ));
                 textComponent.addExtra( Info );
                }

                if (FTag.equalsIgnoreCase("{USERNAME}")){
                    String s = username.replace("{RAW_USERNAME}",
                            profile.getPlayer().getDisplayName());
                    TextComponent component = new TextComponent(
                            ChatColor.translateAlternateColorCodes('&', s));
                    component.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder(
                                    ChatColor.translateAlternateColorCodes('&', "" +
                                            "&aClick to send &c&l" +
                                            profile.getPlayer().getName() + " &aa message!"
                                    )).create()));
                    component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                            "/msg " + profile.getPlayer().getName() + " "));
                    textComponent.addExtra(component);
                }

                if (FTag.equalsIgnoreCase("{PREFIX}")){
                    TextComponent component = new TextComponent(
                            ChatColor.translateAlternateColorCodes('&', prefix));
                    textComponent.addExtra(component);
                }

                if (FTag.equalsIgnoreCase("{MESSAGE}")){
                    String m = this.message.replace("{RAW_MESSAGE}", message);
                    TextComponent component = new TextComponent();
                    TextComponent additions = new TextComponent();
                    for (int y = 0; y < m.toCharArray().length; y++){
                        if (m.toCharArray()[y] == '&'){
                            if (m.toCharArray().length == y+1) continue;
                            if (ColorChars.contains(m.toCharArray()[y+1])){
                                char color = m.toCharArray()[y+1];
                                component.addExtra(additions);
                                net.md_5.bungee.api.ChatColor c = additions.getColor();
                                additions = new TextComponent();
                                additions.setColor(c);
                                if (color == 'k'){
                                    additions.setObfuscated(true);
                                } else if (color == 'l') {
                                    additions.setBold(true);
                                } else if (color == 'n') {
                                    additions.setUnderlined(true);
                                } else if (color == 'm') {
                                    additions.setStrikethrough(true);
                                } else if (color == 'o') {
                                    additions.setItalic(true);
                                } else if (color == 'r') {
                                    additions = new TextComponent();
                                } else {
                                    additions.setColor(
                                            net.md_5.bungee.api.ChatColor.getByChar(color));
                                }
                                y+=1;
                                continue;
                            }
                        }
                        additions.addExtra(String.valueOf(m.toCharArray()[y]));
                    }
                    component.addExtra(additions);
                    textComponent.addExtra(component);
                }

                if (FTag != null){
                    x = x + FTag.toCharArray().length-1;
                    continue;
                }
            }

            appendable = appendable + format.charAt(x);

            if (x == format.toCharArray().length-1){
                TextComponent component = new TextComponent
                        (ChatColor.translateAlternateColorCodes('&', appendable));
                textComponent.addExtra(component);

                appendable = "";
            }
        }
        return textComponent;
    }

    public String processWordFormatting(Profile profile, String Message){

    }

}
