package io.github.t0xictyler.bakedflesh;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmokingRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.permission.ChildPermission;
import org.bukkit.plugin.java.annotation.permission.Permission;
import org.bukkit.plugin.java.annotation.permission.Permissions;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.Website;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import org.bukkit.plugin.java.annotation.plugin.author.Authors;


@Plugin(name = "BakedFlesh", version = "1.0.0")
@Description("Bake rotten flesh into useful leather!")
@Authors({@Author("T0xicTyler")})
@Website("https://github.com/T0xicTyler/BakedFlesh")
@Permissions({
        @Permission(name = "bakedflesh.*", children = {@ChildPermission(name = "bakedflesh.command")}),
        @Permission(name = "bakedflesh.command")
})
@Commands({
        @Command(name = "bakedflesh", aliases = {"bf"})
})
public class BakedFleshPlugin extends JavaPlugin {

    @Getter
    private NamespacedKey furnaceKey, smokerKey;

    private String getDisplayName() {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("name", "&7Baked Flesh"));
    }

    private float getExperience() {
        return (float) getConfig().getDouble("xp", 10);
    }

    private int getFurnaceCookTime() {
        return getConfig().getInt("furnaceCookTime", 160);
    }

    private int getSmokerCookTime() {
        return getConfig().getInt("smokerCookTime", 80);
    }

    private boolean shouldRegisterFurnaceRecipe() {
        return getFurnaceCookTime() > 0;
    }

    private boolean shouldRegisterSmokerRecipe() {
        return getSmokerCookTime() > 0;
    }

    private ItemStack getResult() {
        ItemStack result = new ItemStack(Material.LEATHER); {
            ItemMeta m = result.getItemMeta();

            if (m != null) {
                m.setDisplayName(ChatColor.translateAlternateColorCodes('&', getDisplayName()));
            }

            result.setItemMeta(m);
        }

        return result;
    }

    private FurnaceRecipe getFurnaceRecipe() {
        return new FurnaceRecipe(getFurnaceKey(), getResult(), Material.ROTTEN_FLESH, getExperience(), getFurnaceCookTime());
    }

    private SmokingRecipe getSmokerRecipe() {
        return new SmokingRecipe(getSmokerKey(), getResult(), Material.ROTTEN_FLESH, getExperience(), getSmokerCookTime());
    }

    private void registerRecipes() {
        if (!shouldRegisterSmokerRecipe() && !shouldRegisterFurnaceRecipe()) {
            Bukkit.getLogger().warning("Didn't register either baked flesh recipe.");
        } else {
            if (shouldRegisterFurnaceRecipe()) {
                getServer().addRecipe(getFurnaceRecipe());
                Bukkit.getLogger().info("Registered baked flesh furnace recipe.");
            }

            if (shouldRegisterSmokerRecipe()) {
                getServer().addRecipe(getSmokerRecipe());
                Bukkit.getLogger().info("Registered baked flesh smoker recipe.");
            }
        }
    }

    private void unregisterRecipes() {
        getServer().removeRecipe(getFurnaceKey());
        getServer().removeRecipe(getSmokerKey());
        Bukkit.getLogger().info("Unregistered baked flesh recipes.");
    }

    public void reload(CommandSender sender) {
        sender.sendMessage("Reloading BakedFlesh...");

        unregisterRecipes();
        reloadConfig();
        registerRecipes();

        sender.sendMessage("Reloaded BakedFlesh!");
    }

    private void registerCommand() {
        BakedFleshCommand cmd = new BakedFleshCommand(this);
        PluginCommand command = getCommand("bakedflesh");

        if (command != null) {
            command.setExecutor(cmd);
            command.setTabCompleter(cmd);
        }
    }

    @Override
    public void onEnable() {
        this.furnaceKey = new NamespacedKey(this, "bakedfleshfurnace");
        this.smokerKey = new NamespacedKey(this, "bakedfleshsmoker");

        registerCommand();

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        registerRecipes();
    }

    @Override
    public void onDisable() {
        unregisterRecipes();

        HandlerList.unregisterAll(this);
    }
}
