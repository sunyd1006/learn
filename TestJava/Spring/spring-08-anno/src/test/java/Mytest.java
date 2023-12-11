import com.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Mytest {
    @Test
    public void test(){
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

//      1  beans.xml 开启包注解
//      2  @Component ：标注User类
//            @Value： 定义值
//       就可以自动装配了
        System.out.println(context.getBean("user", User.class).toString());
    }
}
