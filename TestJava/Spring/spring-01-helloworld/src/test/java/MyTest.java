import com.dao.UserDaoImpl;
import com.service.UserService;
import com.service.UserServiceImpl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest {
	public static void main(String[] args) {
		
		// public class UserServiceImpl implements UserService {
		//     private UserDao userDao = new UserDaoImpl();
		//     private UserDao userDao = new UserService();
		//
		
		// A. 没有依赖注入，没有IOC的时候：
		// UserService service = new UserServiceImpl();
		// service.getUser();
		
		// B. 有了依赖注入后，用户（调用者）可以配置调用的方法
		UserService has_di_service = new UserServiceImpl();
		((UserServiceImpl) has_di_service).setUserDao(new UserDaoImpl());
		has_di_service.getUser();
		
		// C. 有了IOC容器，Spring 创建、管理、装配对象
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
		UserService ioc_service = (UserService) context.getBean("my_ioc_UserServiceImpl");
		ioc_service.getUser();
	}
}
