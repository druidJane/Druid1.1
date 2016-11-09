import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by 1115 on 2016/9/21.
 */
public class Test {
    @Autowired
    protected static RedisTemplate<String, String> redisTemplate;
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        RedisTemplate redis = applicationContext.getBean(RedisTemplate.class);
        System.out.println(redisTemplate.boundValueOps("test").get());
        System.out.println(ChannelManager.getInstance().size());
    }
    /*private UserProto.User get(int i) {
        UserProto.User.Builder builder = UserProto.User.newBuilder();
        builder.setId(i);
        builder.setName("Name_" + i);
        return builder.build();
    }*/
}
