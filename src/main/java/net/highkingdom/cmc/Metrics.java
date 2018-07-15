package net.highkingdom.cmc;

import java.util.zip.DeflaterOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import java.io.FilterOutputStream;
import java.util.zip.GZIPOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import org.bukkit.plugin.PluginDescriptionFile;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.net.URL;
import org.bukkit.configuration.InvalidConfigurationException;
import java.util.Iterator;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import java.io.IOException;
import java.util.UUID;
import java.util.Collections;
import java.util.HashSet;
import org.bukkit.scheduler.BukkitTask;
import java.io.File;
import org.bukkit.configuration.file.YamlConfiguration;
import java.util.Set;
import org.bukkit.plugin.Plugin;

public final class Metrics
{
    private static final int REVISION = 7;
    private static final String BASE_URL = "http://report.mcstats.org";
    private static final String REPORT_URL = "/plugin/%s";
    private static final int PING_INTERVAL = 10;
    private final Plugin plugin;
    private final Set<Graph> graphs;
    private final YamlConfiguration configuration;
    private final File configurationFile;
    private final String guid;
    private final boolean debug;
    private final Object optOutLock;
    private volatile BukkitTask task;
    
    public Metrics(final Plugin plugin) throws IOException {
        this.graphs = Collections.synchronizedSet(new HashSet<Graph>());
        this.optOutLock = new Object();
        this.task = null;
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        this.plugin = plugin;
        this.configurationFile = this.getConfigFile();
        (this.configuration = YamlConfiguration.loadConfiguration(this.configurationFile)).addDefault("opt-out", (Object)false);
        this.configuration.addDefault("guid", (Object)UUID.randomUUID().toString());
        this.configuration.addDefault("debug", (Object)false);
        if (this.configuration.get("guid", (Object)null) == null) {
            this.configuration.options().header("http://mcstats.org").copyDefaults(true);
            this.configuration.save(this.configurationFile);
        }
        this.guid = this.configuration.getString("guid");
        this.debug = this.configuration.getBoolean("debug", false);
    }
    
    public Graph createGraph(final String s) {
        if (s == null) {
            throw new IllegalArgumentException("Graph name cannot be null");
        }
        final Graph graph = new Graph(s);
        this.graphs.add(graph);
        return graph;
    }
    
    public void addGraph(final Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null");
        }
        this.graphs.add(graph);
    }
    
    public boolean start() {
        synchronized (this.optOutLock) {
            if (this.isOptOut()) {
                return false;
            }
            if (this.task != null) {
                return true;
            }
            this.task = this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(this.plugin, (Runnable)new Runnable() {
                private boolean firstPost = true;
                
                @Override
                public void run() {
                    try {
                        synchronized (Metrics.this.optOutLock) {
                            if (Metrics.this.isOptOut() && Metrics.this.task != null) {
                                Metrics.this.task.cancel();
                                Metrics.this.task = null;
                                final Iterator<Graph> iterator = Metrics.this.graphs.iterator();
                                while (iterator.hasNext()) {
                                    iterator.next().onOptOut();
                                }
                            }
                        }
                        Metrics.this.postPlugin(!this.firstPost);
                        this.firstPost = false;
                    }
                    catch (IOException ex) {
                        if (Metrics.this.debug) {
                            Bukkit.getLogger().log(Level.INFO, "[Metrics] {0}", ex.getMessage());
                        }
                    }
                }
            }, 0L, 12000L);
            return true;
        }
    }
    
    public boolean isOptOut() {
        synchronized (this.optOutLock) {
            try {
                this.configuration.load(this.getConfigFile());
            }
            catch (IOException | InvalidConfigurationException ex2) {
                final InvalidConfigurationException ex = null;
                final Throwable t = (Throwable)ex;
                if (this.debug) {
                    Bukkit.getLogger().log(Level.INFO, "[Metrics] {0}", t.getMessage());
                }
                return true;
            }
            return this.configuration.getBoolean("opt-out", false);
        }
    }
    
    public void enable() throws IOException {
        synchronized (this.optOutLock) {
            if (this.isOptOut()) {
                this.configuration.set("opt-out", (Object)false);
                this.configuration.save(this.configurationFile);
            }
            if (this.task == null) {
                this.start();
            }
        }
    }
    
    public void disable() throws IOException {
        synchronized (this.optOutLock) {
            if (!this.isOptOut()) {
                this.configuration.set("opt-out", (Object)true);
                this.configuration.save(this.configurationFile);
            }
            if (this.task != null) {
                this.task.cancel();
                this.task = null;
            }
        }
    }
    
    public File getConfigFile() {
        return new File(new File(this.plugin.getDataFolder().getParentFile(), "CompleteMobControl"), "metrics.yml");
    }
    
    private void postPlugin(final boolean b) throws IOException {
        final PluginDescriptionFile description = this.plugin.getDescription();
        final String name = description.getName();
        final boolean onlineMode = Bukkit.getServer().getOnlineMode();
        final String version = description.getVersion();
        final String version2 = Bukkit.getVersion();
        final int size = Bukkit.getServer().getOnlinePlayers().size();
        final StringBuilder sb = new StringBuilder(1024);
        sb.append('{');
        appendJSONPair(sb, "guid", this.guid);
        appendJSONPair(sb, "plugin_version", version);
        appendJSONPair(sb, "server_version", version2);
        appendJSONPair(sb, "players_online", Integer.toString(size));
        final String property = System.getProperty("os.name");
        String property2 = System.getProperty("os.arch");
        final String property3 = System.getProperty("os.version");
        final String property4 = System.getProperty("java.version");
        final int availableProcessors = Runtime.getRuntime().availableProcessors();
        if (property2.equals("amd64")) {
            property2 = "x86_64";
        }
        appendJSONPair(sb, "osname", property);
        appendJSONPair(sb, "osarch", property2);
        appendJSONPair(sb, "osversion", property3);
        appendJSONPair(sb, "cores", Integer.toString(availableProcessors));
        appendJSONPair(sb, "auth_mode", onlineMode ? "1" : "0");
        appendJSONPair(sb, "java_version", property4);
        if (b) {
            appendJSONPair(sb, "ping", "1");
        }
        if (this.graphs.size() > 0) {
            synchronized (this.graphs) {
                sb.append(',');
                sb.append('\"');
                sb.append("graphs");
                sb.append('\"');
                sb.append(':');
                sb.append('{');
                int n = 1;
                for (final Graph graph : this.graphs) {
                    final Object o = new StringBuilder();
                    ((StringBuilder)o).append('{');
                    for (final Plotter plotter : graph.getPlotters()) {
                        appendJSONPair((StringBuilder)o, plotter.getColumnName(), Integer.toString(plotter.getValue()));
                    }
                    ((StringBuilder)o).append('}');
                    if (n == 0) {
                        sb.append(',');
                    }
                    sb.append(escapeJSON(graph.getName()));
                    sb.append(':');
                    sb.append((CharSequence)o);
                    n = 0;
                }
                sb.append('}');
            }
        }
        sb.append('}');
        final URL url = new URL("http://report.mcstats.org" + String.format("/plugin/%s", urlEncode(name)));
        URLConnection urlConnection;
        if (this.isMineshafterPresent()) {
            urlConnection = url.openConnection(Proxy.NO_PROXY);
        }
        else {
            urlConnection = url.openConnection();
        }
        final byte[] bytes = sb.toString().getBytes();
        final byte[] gzip = gzip(sb.toString());
        urlConnection.addRequestProperty("User-Agent", "MCStats/7");
        urlConnection.addRequestProperty("Content-Type", "application/json");
        urlConnection.addRequestProperty("Content-Encoding", "gzip");
        urlConnection.addRequestProperty("Content-Length", Integer.toString(gzip.length));
        urlConnection.addRequestProperty("Accept", "application/json");
        urlConnection.addRequestProperty("Connection", "close");
        urlConnection.setDoOutput(true);
        if (this.debug) {
            System.out.println("[Metrics] Prepared request for " + name + " uncompressed=" + bytes.length + " compressed=" + gzip.length);
        }
        Object o;
        Object o2;
        try (final OutputStream outputStream = urlConnection.getOutputStream()) {
            outputStream.write(gzip);
            outputStream.flush();
            o = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            o2 = ((BufferedReader)o).readLine();
        }
        ((BufferedReader)o).close();
        if (o2 == null || ((String)o2).startsWith("ERR") || ((String)o2).startsWith("7")) {
            if (o2 == null) {
                o2 = "null";
            }
            else if (((String)o2).startsWith("7")) {
                o2 = ((String)o2).substring(((String)o2).startsWith("7,") ? 2 : 1);
            }
            throw new IOException((String)o2);
        }
        if (((String)o2).equals("1") || ((String)o2).contains("This is your first update this hour")) {
            synchronized (this.graphs) {
                final Iterator<Graph> iterator2 = this.graphs.iterator();
                while (iterator2.hasNext()) {
                    final Iterator<Plotter> iterator3 = iterator2.next().getPlotters().iterator();
                    while (iterator3.hasNext()) {
                        iterator3.next().reset();
                    }
                }
            }
        }
    }
    
    public static byte[] gzip(final String s) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FilterOutputStream filterOutputStream = null;
        try {
            filterOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            filterOutputStream.write(s.getBytes("UTF-8"));
        }
        catch (IOException ex) {}
        finally {
            if (filterOutputStream != null) {
                try {
                    ((DeflaterOutputStream)filterOutputStream).close();
                }
                catch (IOException ex2) {}
            }
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    private boolean isMineshafterPresent() {
        try {
            Class.forName("mineshafter.MineServer");
            return true;
        }
        catch (ClassNotFoundException ex) {
            return false;
        }
    }
    
    private static void appendJSONPair(final StringBuilder sb, final String s, final String s2) throws UnsupportedEncodingException {
        boolean b = false;
        try {
            if (s2.equals("0") || !s2.endsWith("0")) {
                Double.parseDouble(s2);
                b = true;
            }
        }
        catch (NumberFormatException ex) {
            b = false;
        }
        if (sb.charAt(sb.length() - 1) != '{') {
            sb.append(',');
        }
        sb.append(escapeJSON(s));
        sb.append(':');
        if (b) {
            sb.append(s2);
        }
        else {
            sb.append(escapeJSON(s2));
        }
    }
    
    private static String escapeJSON(final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append('\"');
        for (int i = 0; i < s.length(); ++i) {
            final char char1 = s.charAt(i);
            switch (char1) {
                case 34:
                case 92: {
                    sb.append('\\');
                    sb.append(char1);
                    break;
                }
                case 8: {
                    sb.append("\\b");
                    break;
                }
                case 9: {
                    sb.append("\\t");
                    break;
                }
                case 10: {
                    sb.append("\\n");
                    break;
                }
                case 13: {
                    sb.append("\\r");
                    break;
                }
                default: {
                    if (char1 < ' ') {
                        final String string = "000" + Integer.toHexString(char1);
                        sb.append("\\u").append(string.substring(string.length() - 4));
                        break;
                    }
                    sb.append(char1);
                    break;
                }
            }
        }
        sb.append('\"');
        return sb.toString();
    }
    
    private static String urlEncode(final String s) throws UnsupportedEncodingException {
        return URLEncoder.encode(s, "UTF-8");
    }
    
    public static class Graph
    {
        private final String name;
        private final Set<Plotter> plotters;
        
        private Graph(final String name) {
            this.plotters = new LinkedHashSet<Plotter>();
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }
        
        public void addPlotter(final Plotter plotter) {
            this.plotters.add(plotter);
        }
        
        public void removePlotter(final Plotter plotter) {
            this.plotters.remove(plotter);
        }
        
        public Set<Plotter> getPlotters() {
            return Collections.unmodifiableSet((Set<? extends Plotter>)this.plotters);
        }
        
        @Override
        public int hashCode() {
            return this.name.hashCode();
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof Graph && ((Graph)o).name.equals(this.name);
        }
        
        protected void onOptOut() {
        }
    }
    
    public abstract static class Plotter
    {
        private final String name;
        
        public Plotter() {
            this("Default");
        }
        
        public Plotter(final String name) {
            this.name = name;
        }
        
        public abstract int getValue();
        
        public String getColumnName() {
            return this.name;
        }
        
        public void reset() {
        }
        
        @Override
        public int hashCode() {
            return this.getColumnName().hashCode();
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Plotter)) {
                return false;
            }
            final Plotter plotter = (Plotter)o;
            return plotter.name.equals(this.name) && plotter.getValue() == this.getValue();
        }
    }
}
