/**
 * Created by 1115 on 2016/9/21.
 */
public class Test {
    public static void main(String[] args) {
        /*ChatServerHandler ch =
        get(1500);*/
        System.out.println(ChannelManager.getInstance().size());
    }
    /*private UserProto.User get(int i) {
        UserProto.User.Builder builder = UserProto.User.newBuilder();
        builder.setId(i);
        builder.setName("Name_" + i);
        return builder.build();
    }*/
}
