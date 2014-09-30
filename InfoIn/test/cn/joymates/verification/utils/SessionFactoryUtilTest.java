package cn.joymates.verification.utils;

import junit.framework.Assert;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import cn.joymates.infoin.utils.SessionFactoryUtil;


public class SessionFactoryUtilTest {
	@Test
	public void testSessionFactoryUtil() {
		SqlSession sess = SessionFactoryUtil.getSession();
		if (sess == null) {
			Assert.fail("为获取链接");
		}
	}
}
