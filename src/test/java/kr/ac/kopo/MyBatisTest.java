package kr.ac.kopo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.sql.DataSource;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:config/spring/spring-mvc.xml"})
public class MyBatisTest {

	@Autowired
	private DataSource ds;
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Disabled
	@Test
	public void ds테스트() throws Exception {
		assertNotNull(ds);
	}
	
	@Test
	public void sqlSessionTemplate테스트() throws Exception {
		assertNotNull(sqlSession);
	}
}
