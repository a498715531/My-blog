import org.apache.commons.lang3.RandomUtils;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

public class Test {
    public static void main(String[] args) {
        System.out.println(RandomUtils.nextInt(60,100000));
        System.out.println(RandomUtils.nextInt(5, 100000));
        System.out.println(RandomUtils.nextInt(5, 100000));

    }
}
