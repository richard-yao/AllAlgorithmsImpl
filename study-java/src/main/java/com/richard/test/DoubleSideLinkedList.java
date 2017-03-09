package com.richard.test;

import java.io.Serializable;
import java.util.NoSuchElementException;

/**
 * @author RichardYao richardyao@tvunetworks.com
 * @date Feb 27, 2017 9:53:42 AM 双向链表的Java实现 实现参考博客:
 *       http://benjaminwhx.com/2016/01/18/Java实现单向双向链表原理分析/
 */
public class DoubleSideLinkedList<E> implements Serializable {

	private static final long serialVersionUID = 1631340744645741790L;

	/**
	 * 节点类, 包含双向指针链接
	 * 
	 * @author RichardYao
	 * @param <E>
	 */
	private static class Node<E> {
		E item;
		Node<E> next;
		Node<E> prev;

		/**
		 * 新增一个节点时, 自动添加该节点前置和后置指针
		 * 
		 * @param prev
		 * @param item
		 * @param next
		 */
		Node(Node<E> prev, E item, Node<E> next) {
			this.prev = prev;
			this.item = item;
			this.next = next;
		}
	}

	/** 第一个节点的引用 **/
	transient Node<E> first; // 头结点的前置指针为null

	/** 最后一个节点的引用 **/
	transient Node<E> last; // 尾节点的后置指针为null

	/** list中元素的数量 **/
	transient int size = 0;

	/** 操作次数 **/
	transient int modCount = 0;

	public DoubleSideLinkedList() {

	}

	/**
	 * 设置新的头结点
	 * 
	 * @param item
	 */
	private void linkFirst(E item) {
		Node<E> fNode = first;
		Node<E> newNode = new Node<E>(null, item, fNode);
		if (fNode == null) {
			last = newNode;
		} else {
			fNode.prev = newNode; // 更新原本头结点的前置指针
		}
		first = newNode;

		size++;
		modCount++;
	}

	/**
	 * 设置新的尾节点
	 * 
	 * @param item
	 */
	private void linkLast(E item) {
		Node<E> lNode = last;
		Node<E> newNode = new Node<E>(lNode, item, null);
		if (lNode == null) {
			first = newNode;
		} else {
			lNode.next = newNode; // 更新原本尾节点的后置指针
		}
		last = newNode;

		size++;
		modCount++;
	}

	/**
	 * 在指定节点前插入新节点
	 * 
	 * @param item
	 * @param specify
	 */
	private void linkBefore(E item, Node<E> specify) {
		final Node<E> pred = specify.prev;
		final Node<E> newNode = new Node<E>(pred, item, specify);
		specify.prev = newNode; // 更新后置节点的前置指针
		if (pred == null) {
			first = newNode;
		} else {
			pred.next = newNode; // 更新前置节点的后置指针
		}

		size++;
		modCount++;
	}

	/**
	 * 删除头结点
	 * 
	 * @param first
	 * @return 头结点的值
	 */
	private E unlinkFirst(Node<E> first) {
		final E element = first.item;
		final Node<E> nextNode = first.next;
		first.item = null;
		first.next = null;
		first = nextNode;
		if (nextNode == null) {
			last = null;
		} else {
			nextNode.prev = null;
		}

		size--;
		modCount++;
		return element;
	}

	/**
	 * 删除尾节点
	 * 
	 * @param last
	 * @return 尾节点的值
	 */
	private E unlinkLast(Node<E> last) {
		final E element = last.item;
		final Node<E> prevNode = last.prev;
		last.item = null;
		last.prev = null;
		last = prevNode;
		if (prevNode == null) {
			first = null;
		} else {
			prevNode.next = null;
		}

		size--;
		modCount++;
		return element;
	}

	/**
	 * 删除某一个节点
	 * 
	 * @param someone
	 * @return 该节点的值
	 */
	private E unlink(Node<E> someone) {
		final E element = someone.item;
		final Node<E> prevNode = someone.prev;
		final Node<E> nextNode = someone.next;

		if (prevNode == null) { // 删除节点为头结点
			first = nextNode;
		} else {
			prevNode.next = nextNode; // 前置节点后置指针指向后置节点
			someone.prev = null;
		}

		if (nextNode == null) { // 删除节点为尾节点
			last = prevNode;
		} else {
			nextNode.prev = prevNode; // 后置节点的前置指针指向前置节点
			someone.next = null;
		}

		someone.item = null;
		size--;
		modCount++;
		return element;
	}

	/**
	 * 取得头结点的值
	 * 
	 * @return
	 */
	public E getFirst() {
		final Node<E> fNode = first;
		if (fNode == null) {
			throw new NoSuchElementException();
		}
		return fNode.item;
	}

	/**
	 * 取得未节点的值
	 * 
	 * @return
	 */
	public E getLast() {
		final Node<E> lNode = last;
		if (lNode == null) {
			throw new NoSuchElementException();
		}
		return lNode.item;
	}

	/**
	 * 删除第一个元素
	 * 
	 * @return
	 */
	public E removeFirst() {
		final Node<E> fNode = first;
		if (fNode == null) {
			throw new NoSuchElementException();
		}
		return unlinkFirst(fNode);
	}

	/**
	 * 删除最后一个元素
	 * 
	 * @return
	 */
	public E removeLast() {
		final Node<E> lNode = last;
		if (lNode == null) {
			throw new NoSuchElementException();
		}
		return unlinkLast(lNode);
	}

	/**
	 * 增加一个元素到list的第一个位置
	 * 
	 * @param e
	 */
	public void addFirst(E e) {
		linkFirst(e);
	}

	/**
	 * 增加一个元素到list的结尾
	 * 
	 * @param e
	 */
	public void addLast(E e) {
		linkLast(e);
	}

	/**
	 * 增加一个元素(默认增加在结尾)
	 * 
	 * @param e
	 * @return true
	 */
	public boolean add(E e) {
		linkLast(e);
		return true;
	}

	/**
	 * 删除list中存在传入的对象
	 * 
	 * @param o
	 * @return 如果改变了list,返回true,否则false
	 */
	public boolean remove(Object o) {
		if (o == null) {
			for (Node<E> temp = first; temp != null; temp = temp.next) {
				if (temp.item == null) {
					unlink(temp);
					return true;
				}
			}
		} else {
			for (Node<E> temp = first; temp != null; temp = temp.next) {
				if (o.equals(temp.item)) {
					unlink(temp);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 清空list,所有引用置为null
	 */
	public void clear() {
		for (Node<E> temp = first; temp != null;) { // 注意这里的循环, 如果填写temp = temp.next,
													// 则在代码块中temp.next=null会导致循环结束
			Node<E> next = temp.next;
			temp.item = null;
			temp.prev = null;
			temp.next = null;
			temp = next;
		}
		first = null;
		last = null;
		size = 0;
		modCount++;
	}

	/**
	 * 获取索引处的元素值
	 * 
	 * @param index
	 *            索引
	 * @return 元素值
	 */
	public E get(int index) {
		checkIndex(index);
		return node(index).item;
	}

	/**
	 * 替换索引处的值为element
	 * 
	 * @param index
	 *            索引
	 * @param element
	 *            新值
	 * @return 旧值
	 */
	public E set(int index, E element) {
		checkIndex(index);
		Node<E> oldNode = node(index);
		E oldValue = oldNode.item;
		oldNode.item = element;
		return oldValue;
	}

	/**
	 * 在指定索引的地方插入元素element,原来的元素以及之后的元素后移
	 * 
	 * @param index
	 *            插入元素的索引
	 * @param element
	 *            插入的元素
	 */
	public void add(int index, E element) {
		checkIndex(index);

		if (index == size)
			linkLast(element);
		else
			linkBefore(element, node(index));
	}

	/**
	 * 移除索引处的元素
	 * 
	 * @param index
	 *            索引
	 * @return 删除的元素
	 */
	public E remove(int index) {
		checkIndex(index);
		return unlink(node(index));
	}

	/**
	 * 获取对象在list中的索引
	 * 
	 * @param o
	 *            要查找的对象
	 * @return 如果找到了对象,返回对应的索引值,否则返回-1
	 */
	public int indexOf(Object o) {
		int index = 0;
		if (o == null) {
			for (Node<E> x = first; x != null; x = x.next) {
				if (x.item == null)
					return index;
				index++;
			}
		} else {
			for (Node<E> x = first; x != null; x = x.next) {
				if (o.equals(x.item))
					return index;
				index++;
			}
		}
		return -1;
	}

	/**
	 * 是否包含元素
	 * 
	 * @param o
	 * @return 如果包含返回true
	 */
	public boolean contains(Object o) {
		return indexOf(o) != -1;
	}

	/**
	 * 返回list中元素的大小
	 * 
	 * @return 元素的大小
	 */
	public int getSize() {
		return size;
	}

	/**
	 * 根据索引得到节点
	 * 
	 * @param index
	 *            索引
	 * @return 节点
	 */
	Node<E> node(int index) {
		if (index < (size >> 1)) { // 比较index和size/2的值
			Node<E> temp = first;
			for (int i = 0; i < index; i++) {
				temp = temp.next;
			}
			return temp;
		} else {
			Node<E> temp = last;
			for (int i = size - 1; i > index; i--) {
				temp = temp.prev;
			}
			return temp;
		}
	}

	/**
	 * 检查索引是否正确,不正确抛出 IndexOutOfBoundsException 异常
	 * 
	 * @param index
	 */
	private void checkIndex(int index) {
		if (!isElementIndex(index))
			throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
	}

	/**
	 * 越界的错误信息
	 * 
	 * @param index
	 *            越界的错误索引
	 * @return 越界的msg
	 */
	private String outOfBoundsMsg(int index) {
		return "Index: " + index + ", Size: " + size;
	}

	/**
	 * 检查索引是否没有溢出
	 * 
	 * @param index
	 *            索引
	 * @return 如果索引正确返回true
	 */
	private boolean isElementIndex(int index) {
		return index >= 0 && index < size;
	}
	
	public static void main(String[] args) {
		Object[] testList = null;
		String[] test = new String[]{"0", "1", "2"};
		testList = test;
		testList[2] = 1; //编译时这里可以通过, 但是运行时抛出异常, 因为实际上数组接收的只能是String类型数据
		for(int i=0;i<testList.length;i++) {
			System.out.println(testList[i]);
		}
		DoubleSideLinkedList<String> list = new DoubleSideLinkedList<String>();
		list.add("123");
		list.add("abc");
		list.add(0, "456");
		for(int i=0;i<list.getSize();i++) {
			System.out.println(list.get(i));
		}
		System.out.println(list.indexOf("abc"));
	}

}
