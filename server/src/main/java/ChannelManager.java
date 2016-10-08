import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 1115 on 2016/9/21.
 */
public class ChannelManager {
    private static ChannelManager CHANNEL_MANAGER = new ChannelManager();

    private static final Map<String, ChatServerHandler> handlers = new ConcurrentHashMap<String, ChatServerHandler>();

    public static ChannelManager getInstance(){
        return CHANNEL_MANAGER;
    }
    public void add(final ChatServerHandler handler,String id) {
        this.handlers.put(id, handler);
    }
    public ChatServerHandler get(String id){
        return this.handlers.get(id);
    }
    public int size(){
        return handlers.size();
    }
}
