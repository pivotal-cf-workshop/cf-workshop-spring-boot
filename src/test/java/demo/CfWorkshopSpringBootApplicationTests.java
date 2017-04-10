package demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import io.pivotal.cf.workshop.CfWorkshopSpringBootApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CfWorkshopSpringBootApplication.class)
@WebAppConfiguration
public class CfWorkshopSpringBootApplicationTests {

	@Test
	public void contextLoads() {
	}

}
