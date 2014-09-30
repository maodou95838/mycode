package cn.joymates.infoin.utils;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * mybatis SqlSession工具类
 * 
 * @author Jackie Hou
 *
 */
public class SessionFactoryUtil {
	private static final String RESOURCE = "config/mybatis-conf.xml";
	
	private static SqlSessionFactory sqlSessionFactory = null;
	
	private static ThreadLocal<SqlSession> threadLocal = new ThreadLocal<SqlSession>();

	static {
		Reader reader = null;
		try {
			reader = Resources.getResourceAsReader(RESOURCE);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		} catch (IOException e) {
			throw new RuntimeException("未找到mybatis配置文件" + RESOURCE, e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public static SqlSessionFactory getSqlSessionFactory() {   
		if (sqlSessionFactory == null) {
			rebuildSqlSessionFactory();
		}
		
        return sqlSessionFactory;   
    }
	
	/**
	 * 重建session工厂
	 */
	public static void rebuildSqlSessionFactory() {
		Reader reader = null;
		try {
			reader = Resources.getResourceAsReader(RESOURCE);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		} catch (IOException e) {
			throw new RuntimeException("Get resource error:"+RESOURCE, e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}	

	}
	
	public static SqlSession getSession() {
		SqlSession session = threadLocal.get();
		
		if(session == null){
			session = getSqlSessionFactory().openSession();
			threadLocal.set(session);
		}
		
		return session;
	}
	
	public static void closeSession() {
		SqlSession session = threadLocal.get();
		threadLocal.set(null);
		
		if(session != null){
			session.close();
		}
	}
	
}
