package com.github.tiagograveto.headcreator.config;

import com.github.tiagograveto.headcreator.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Configuration {

    private Main main;

    public Configuration(Main main, String name) {
        this.charset = "UTF-8";
        this.main = main;
        this.folder = main.getDataFolder();
        this.name = name.concat(".yml");
        reload();
    }

    private String name;
    private File file;
    private YamlConfiguration yaml;
    private String charset;
    private File folder;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public YamlConfiguration getYaml() {
        return yaml;
    }

    public void setYaml(YamlConfiguration yaml) {
        this.yaml = yaml;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public File getFolder() {
        return folder;
    }

    public void setFolder(File folder) {
        this.folder = folder;
    }


    public boolean save(){
        try {
            this.yaml.save(this.file);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean reload() {
        try {
            this.file = new File(this.folder, this.name);
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(this.file), this.charset);
            this.yaml = YamlConfiguration.loadConfiguration(inputStreamReader);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void saveDefault(boolean replace) {
        if (!this.file.exists() || replace) {
            main.saveResource(this.name, replace);
        }
    }

}
