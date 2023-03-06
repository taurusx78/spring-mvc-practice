package config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("real")
public class DsRealConfig {

	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		System.out.println("DsRealConfig 설정 클래스의 dataSource() 메서드 실행");
		DataSource ds = new DataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost/spring5?useSSL=false&characterEncoding=utf8");
		ds.setUsername("root");
		ds.setPassword("Mjsbjjm20");
		ds.setInitialSize(2);
		ds.setTestWhileIdle(true);
		ds.setMinEvictableIdleTimeMillis(60000 * 3);
		ds.setTimeBetweenEvictionRunsMillis(10 * 1000);
		return ds;
	}
}
