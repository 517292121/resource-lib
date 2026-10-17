package cn.com.adtech.riss2.core;

import java.util.Arrays;

public class TagsList {
	private String[] data;
	private int size = 0;

	public TagsList(int size) {
		data = new String[size];
	}

	public TagsList() {
		this(10);
	}

	public void add(String str) {
		ensureCapacity(size + 1);
		data[size++] = str;
	}

	public String get(int index) {
		if (index < size)
			return data[index];
		else
			return null;
	}

	// 为了提高效率，只将其置为null
	public boolean remove(String str) {
		for (int index = size - 1; index >= 0; index--) {
			if (str.equals(data[index])) {
				data[index] = null;
				return true;
			}
		}
		return false;
	}

	public boolean remove(int index) {
		if (index < data.length) {
			data[index] = null;
			return true;
		}
		return false;
	}

	public int size() {
		return this.size;
	}

	// 扩展容量
	public void ensureCapacity(int minSize) {
		int oldCapacity = data.length;
		if (minSize > oldCapacity) {
			int newCapacity = (oldCapacity * 3 / 2 + 1) > minSize ? oldCapacity * 3 / 2 + 1
					: minSize;
			data = Arrays.copyOf(data, newCapacity);
		}
	}

	public static void main(String[] args) {
		System.out.println("--功能测试--");
		String str1 = "tt</u>ss</a>aa<div name='<test>' id='3' other='<test>'><b>sff";
		String str2 = "tt<u>ss</u><div id=test name='<test>'><a>fds</a></div>";
		System.out.println("检查文本 " + str1);
		System.out.println("结果：" + TagsChecker.check(str1));
		System.out.println("检查文本 " + str2);
		System.out.println("结果：" + TagsChecker.check(str2));
		System.out.println("修复文本 " + str1);
		System.out.println("结果：" + TagsChecker.fix(str1));
		for (int i = 0; i < 10; i++) {
			str1 += str1;
		}
		System.out.println();
		System.out.println("--效率测试--");
		System.out.println("文本长度：" + str1.length());
		long t1 = System.currentTimeMillis();
		boolean closed = TagsChecker.check(str1);
		long t2 = System.currentTimeMillis();
		long t3 = System.currentTimeMillis();
		System.out.println("检查用时：" + (t2 - t1) + " 毫秒 结果：" + closed);
		System.out.println("修复用时：" + (t3 - t2) + " 毫秒");
	}
}
