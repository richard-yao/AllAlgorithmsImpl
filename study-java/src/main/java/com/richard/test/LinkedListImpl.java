package com.richard.test;

/**
 * @author YaoXiansheng
 * @date 2017年2月17日
 * @title LinkedListImpl
 * @todo TODO
 */

public class LinkedListImpl {

	Node head = null; // 头节点

	/**
	 * 链表中的节点
	 * 
	 * @author YaoXiansheng
	 * @date 2017年2月17日
	 * @title Node
	 * @todo TODO
	 */
	class Node {
		Node next = null; // 节点的引用，指向下一个节点
		int data; // 节点的对象，即内容

		public Node(int data) {
			this.data = data;
		}
	}

	/**
	 * 在链表的最后位置插入一个节点
	 * 
	 * @param data
	 */
	public void addNode(int data) {
		Node newNode = new Node(data);
		if (head == null) {
			head = newNode;
			return;
		}
		Node temp = head;
		while (temp.next != null) { // 遍历链表到最后一个节点
			temp = temp.next;
		}
		temp.next = newNode;
	}

	/**
	 * 删除第index个节点
	 * 
	 * @param index
	 * @return
	 */
	public boolean deleteNode(int index) {
		if (index < 0 || index > length()) {
			return false;
		}
		if (index == 0) { // 删除头结点
			head = head.next;
			return true;
		}
		int i = 1;
		Node preNode = head;
		Node curNode = preNode.next;
		while (curNode != null) {
			if (i == index) {
				preNode.next = curNode.next;
				return true;
			}
			preNode = curNode;
			curNode = curNode.next;
			i++;
		}
		return false;
	}

	/**
	 * 删除指定节点
	 * @param n
	 * @return
	 */
	public boolean deleteSpecifyNode(Node n) {
		if (n == null) {
			return true;
		}
		if(n.next == null) {
			n = null;
			return true;
		}
		n.data = n.next.data;
		n.next = n.next.next;
		return true;
	}
	
	/**
	 * 得到指定的Node
	 * @param n
	 * @return
	 */
	public Node getNode(int n) {
		if(n > length() || n < 0) {
			return null;
		}
		Node curNode = head;
		while(n-- > 0 && curNode.next != null) {
			curNode = curNode.next;
		}
		return curNode;
	}

	public int length() {
		int length = 0;
		Node temp = head;
		while (temp != null) {
			length++;
			temp = temp.next;
		}
		return length;
	}

	public void printList() {
		Node tmp = head;
		while (tmp != null) {
			System.out.println(tmp.data);
			tmp = tmp.next;
		}
	}

	public static void main(String[] args) {
		LinkedListImpl listImpl = new LinkedListImpl();
		listImpl.addNode(5);
		listImpl.addNode(3);
		listImpl.addNode(1);
		listImpl.addNode(2);
		listImpl.addNode(55);
		System.out.println("LinkLenth: " + listImpl.length());
		System.out.println("Head data: " + listImpl.head.data);
		listImpl.printList();
		listImpl.deleteNode(4);
		System.out.println("After delete node(4): ");
		listImpl.printList();
		System.out.println("Get Node(2): "+listImpl.getNode(2).data);
		System.out.println("After delete Node(2): ");
		listImpl.deleteSpecifyNode(listImpl.getNode(2));
		listImpl.printList();
	}

}
