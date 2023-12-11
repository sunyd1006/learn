import com.config.SunConfig;
import com.pojo.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MyTest {

    @Test
    public void test(){
        ApplicationContext context = new AnnotationConfigApplicationContext(SunConfig.class);
        Object getUser = context.getBean("getUser", User.class);
        System.out.println(getUser.toString());
    }
}
