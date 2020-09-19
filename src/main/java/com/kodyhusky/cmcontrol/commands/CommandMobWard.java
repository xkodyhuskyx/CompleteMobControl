/*
 * Copyright (C) 2012-2020 Jeffery Hancock (xkodyhuskyx)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.kodyhusky.cmcontrol.commands;

import com.kodyhusky.cmcontrol.CompleteMobControl;
import com.kodyhusky.cmcontrol.objects.MobWard;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author xkodyhuskyx
 */
public class CommandMobWard implements CommandExecutor {
    
    private final CompleteMobControl plugin;
    
    public CommandMobWard(CompleteMobControl plugin) {
        this.plugin = plugin;
    }
    
    @Override
    @SuppressWarnings("null")
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                //<editor-fold defaultstate="collapsed" desc="Command: list">
                if (args[0].equalsIgnoreCase("list")) {
                    List<MobWard> wards = plugin.getWardManager().getAllMobWards(player);
                    player.sendMessage(plugin.getLanguage().getFormattedMiltiString("mw-list-title"));
                    for (int i = 0; i < wards.size() && i < 5; i++) {
                        MobWard ward = wards.get(i);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguage().getString("mw-list-value")
                                .replace("{id}", Integer.toString(i + 1))
                                .replace("{LX}", Double.toString(ward.getLocation().getX()))
                                .replace("{LY}", Double.toString(ward.getLocation().getY()))
                                .replace("{LZ}", Double.toString(ward.getLocation().getZ()))
                                .replace("{R}", Integer.toString(plugin.getWardManager().getMaterialRadius(ward.getMaterial())))
                                .replace("{A}", ward.isActive() ? plugin.getLanguage().getString(label) : "NO")));
                    }
                    if (wards.size() >= 5) {
                        // ADD LINK TO NEXT PAGE HERE
                    }
                    sender.sendMessage(plugin.getLanguage().getFormattedString("mw-list-footer"));
                    return true;
                }
                //</editor-fold>
            }
            if (args.length == 2) {
                if ("remove".equalsIgnoreCase(args[0])) {
                    
                }
            }
        } else {
            sender.sendMessage("You must be a player!");
            return false;
        }
        // do something
        return false;
    }
    
    
    public void showHelp(int page) {
        
    }
}