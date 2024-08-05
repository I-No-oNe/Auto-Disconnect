package net.i_no_am.auto_disconnect.config.settings;

public class ConfigSettings<T> {
    private T value;

    public ConfigSettings(Class<T> type, T value) {
        this.value = value;
    }

    public T getVal() {
        return value;
    }

    public void setVal(T value) {
        this.value = value;
    }
}
