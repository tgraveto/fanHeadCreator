package me.fanjoker.headcreator.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class ItemBuilder {

	private ItemStack is;

	public ItemBuilder(Material m) {
		this(m, 1, (short) 0);
	}

	public ItemBuilder(ItemStack is) {
		this.is = is.clone();
	}

	public ItemBuilder(Material m, int amount, short data) {
		is = new ItemStack(m, amount, data);
	}

	public ItemBuilder clone() {
		return new ItemBuilder(is);
	}

	public ItemBuilder durability(int dur) {
		is.setDurability((short) dur);
		return this;
	}

	public ItemBuilder name(String name) {
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		is.setItemMeta(im);
		return this;
	}

	public ItemBuilder unsafeEnchantment(Enchantment ench, int level) {
		is.addUnsafeEnchantment(ench, level);
		return this;
	}

	public ItemBuilder enchant(Enchantment ench, int level) {
		ItemMeta im = is.getItemMeta();
		im.addEnchant(ench, level, true);
		is.setItemMeta(im);
		return this;
	}

	public ItemBuilder removeEnchantment(Enchantment ench) {
		is.removeEnchantment(ench);
		return this;
	}

	public ItemBuilder owner(String owner) {
		try {
			SkullMeta im = (SkullMeta) is.getItemMeta();
			im.setOwner(owner);
			is.setItemMeta(im);
		} catch (ClassCastException expected) {
		}
		return this;
	}

	public ItemBuilder infinityDurabilty() {
		is.setDurability(Short.MAX_VALUE);
		return this;
	}

	public ItemBuilder lore(String... lore) {
		ItemMeta im = is.getItemMeta();
		List<String> out = im.getLore() == null ? new ArrayList<>() : im.getLore();
		for (String string : lore)
			out.add(ChatColor.translateAlternateColorCodes('&', string));
		im.setLore(out);
		is.setItemMeta(im);
		return this;
	}

	@SuppressWarnings("deprecation")
	public ItemBuilder woolColor(DyeColor color) {
		if (!is.getType().equals(Material.WOOL))
			return this;
		is.setDurability(color.getDyeData());
		return this;
	}

	public ItemBuilder amount(int amount) {
		if (amount > 64)
			amount = 64;
		is.setAmount(amount);
		return this;
	}

	public ItemBuilder removeAttributes() {
		ItemMeta meta = is.getItemMeta();
		meta.addItemFlags(ItemFlag.values());
		is.setItemMeta(meta);
		return this;
	}

	public ItemStack build() {
		return is;
	}

	public ItemBuilder color(Color color) {
		if (!is.getType().name().contains("LEATHER_"))
			return this;
		LeatherArmorMeta meta = (LeatherArmorMeta) is.getItemMeta();
		meta.setColor(color);
		is.setItemMeta(meta);
		return this;
	}

	public ItemBuilder head(String texture) {

		is = getSkull(texture);
		return this;
	}

	public ItemStack getSkull(String url) {
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
		profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
		Field profileField;
		try {
			profileField = skullMeta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(skullMeta, profile);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		skull.setItemMeta(skullMeta);
		return skull;
	}

//	public ItemStack getSkull(String url) {
//		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
//		SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
//		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
//		byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
//		profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
//		Field profileField = null;
//		try {
//			profileField = skullMeta.getClass().getDeclaredField("profile");
//		} catch (NoSuchFieldException | SecurityException e) {
//			e.printStackTrace();
//		}
//		profileField.setAccessible(true);
//		try {
//			profileField.set(skullMeta, profile);
//		} catch (IllegalArgumentException | IllegalAccessException e) {
//			e.printStackTrace();
//		}
//		skull.setItemMeta(skullMeta);
//		return skull;
//	}


}