package com.kodyhusky.cmc.old;

public class DelayLoadRunnable implements Runnable {

    private CompleteMobControl plugin;

    public DelayLoadRunnable(CompleteMobControl plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.loadPlugin();
    }
}
