package com.tomato.util.primitive;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * @author caibo
 * @version $Id$
 * @since 2017/10/9 下午8:40
 */
public class JdkProxyTest extends TestCase {

	@Test
	public void testProxyName() {
		ProxyInterface proxy = (ProxyInterface) Proxy
				.newProxyInstance(ProxyInterface.class.getClassLoader(), new Class[] { ProxyInterface.class }, new ProxyInvocationHandler());
		proxy.test();
		System.out.println(proxy == null);
		String name = proxy.getClass().getName();
		System.out.println(name);
	}

	public interface ProxyInterface {

		public String test();

	}

	public class ProxyInvocationHandler implements InvocationHandler {
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			System.out.println("invoke");
			return null;
		}
	}
}
