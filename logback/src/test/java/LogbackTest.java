import com.vdrips.test.logback.LogMail;
import com.vdrips.test.logback.LogWrite;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by baixf on 2017/3/27.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:eventContext.xml")
public class LogbackTest {

    @Test
    public void test(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml");
        LogWrite logWrite = (LogWrite) ctx.getBean("logWrite");
        logWrite.info();
        logWrite.error();
        logWrite.debug();

        LogMail logMail = (LogMail) ctx.getBean("logMail");
        logMail.info();
        logMail.error();
        logMail.debug();
    }
}
