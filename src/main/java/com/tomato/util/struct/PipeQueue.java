package com.tomato.util.struct;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/**
 * 管道队列，管道内最多存放N个元素，当元素个数超过N时，首部的元素将被挤出<br>
 * <p>
 * 用于存放元素的数组的大小始终等于N，当元素超出时，新的元素将存放在被舍弃的元素的位置<br>
 * <pre>
 * #################################
 *   head
 *    v
 *  0 1 2 3 4 5 6
 *        ^
 *       tail
 * ##################################
 * </pre>
 * <p>
 * 当head > tail: 有效元素个数是[head,tail)<br>
 * 当head == tail: 有效元素个数为0
 * 当head < tail: 有效元素个数为[head,size)和[0,tail)
 *
 * @author CaiBo
 * @version $Id$
 * @since 2017/8/6 下午4:27
 */
public class PipeQueue<E> implements Queue<E> {

	// 存放具体元素的数据
	private final Object[] arr;
	// 用于标记数组中有效元素的起始位置下标
	private int head = -1;
	private int tail = -1;
	private int size;

	private static final int MIN_INITIAL_CAPACITY = 8;

	/**
	 * @param pipeLen 管道的最大长度
	 */
	public PipeQueue(int pipeLen) {
		arr = new Object[pipeLen];
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public boolean contains(Object o) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == null) {
				break;
			} else if (arr[i].equals(o)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Iterator<E> iterator() {
		return null;
	}

	@Override
	public Object[] toArray() {
		Object[] result = new Object[size()];
		System.arraycopy(arr, 0, result, 0, size());
		return result;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return null;
	}

	@Override
	public boolean add(E e) {
		return false;
	}

	@Override
	public boolean remove(Object o) {
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return false;
	}

	@Override
	public void clear() {

	}

	@Override
	public boolean offer(E e) {
		return false;
	}

	@Override
	public E remove() {
		return null;
	}

	@Override
	public E poll() {
		return null;
	}

	@Override
	public E element() {
		return null;
	}

	@Override
	public E peek() {
		return null;
	}
}
