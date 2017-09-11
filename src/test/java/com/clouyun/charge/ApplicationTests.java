package com.clouyun.charge;

import com.clouyun.boot.common.utils.JsonUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

// 配置事务的回滚,对数据库的增删改都会回滚,便于测试用例的循环利用
// @TransactionConfiguration(transactionManager = "transactionManager",
// defaultRollback = true)
// @Transactional
// @AutoConfigureOrder
// @SpringApplicationConfiguration(classes = Application.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public abstract class ApplicationTests {
	
	public Object result = null;

	protected MockMvc mockMvc;
	
	public abstract void initialize();
	
	@Before
	public void setUp() throws Exception {
		try {
			//SessionFactoryManager.startService();
			initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@After
	public void tearDown() throws Exception {
		if (result != null) {
			JsonUtils.print(result);
		}
	}
	
}
