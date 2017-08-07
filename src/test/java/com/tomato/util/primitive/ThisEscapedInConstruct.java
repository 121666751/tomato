package com.tomato.util.primitive;

import java.lang.reflect.Field;

/**
 * @author CaiBo
 * @version $Id$
 * @since 2017/8/7 下午4:51
 */
public class ThisEscapedInConstruct {

	public int code = -1;

	public ThisEscapedInConstruct(MyRegister reg) {
		reg.reg(new MyListener() {
			@Override
			public void onEvent() {
				System.out.println("on event");
			}
		});
		code = 2;
	}

	public static void main(String[] args) {
		while (true) {
			MyRegister reg = new MyRegister();
			ThisEscapedInConstruct a = new ThisEscapedInConstruct(reg);
		}
	}

	static class MyRegister {
		public void reg(MyListener listener) {
			try {
				Field c = listener.getClass().getDeclaredField("this$0");
				ThisEscapedInConstruct t = (ThisEscapedInConstruct) c.get(listener);
				System.out.println(t.code);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	interface MyListener {
		void onEvent();
	}
}
