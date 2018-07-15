package net.highkingdom.cmc;

public class DelayLoadRunnable implements Runnable
{
    private final CompleteMobControl plugin;
    
    public DelayLoadRunnable(final CompleteMobControl plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void run() {
        this.plugin.loadPlugin();
    }
}
