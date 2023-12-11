import com.People;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest {
    @Test
    public void Test(){
        ApplicationContext context =  new ClassPathXmlApplicationContext("beans.xml");
        People people = context.getBean("people", People.class);
        
        people.getCat().shout();
        System.out.println(people.getCat().toString());
        
        people.getDog().shout();
        System.out.println(people.getDog().toString());
    }
}
