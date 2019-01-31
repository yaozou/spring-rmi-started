import com.yao.api.config.MainConfig;
import com.yao.api.controller.TestController;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Description:
 * @author: yaozou
 * @Date: 2019/1/3 16:12
 */
public class TestRmi {
    volatile int j = 0;
    @Test
    public void clientTest() throws Exception{
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
        for(int i = 0;i < 1;i++){
            new Thread(new Runnable() {
                public void run() {
                    j += 1;
                    TestController controller = applicationContext.getBean(TestController.class);
                    controller.test(""+j , "test"+j);
                }
            }).start();
        }

        Thread.sleep(10000L);
    }
}
