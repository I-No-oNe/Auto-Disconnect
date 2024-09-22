package net.i_no_am.auto_disconnect.config.settings;

import net.i_no_am.auto_disconnect.config.Config;

@SuppressWarnings("unused")
public class ConfigSettings<T> {
    private final T value;
    private final Class<T> type;

    public ConfigSettings(Class<T> type, T value) {
        this.type = type;
        this.value = value;
    }

    public T getVal() {
        return value;
    }

    public void setVal(String name, T value) {
        Config.ADconfig.write(name,value);
    }

    public Class<T> getType() {
        return type;
    }
}
