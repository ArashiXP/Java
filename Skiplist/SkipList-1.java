// Implementation of the problematic data structure skiplist in Java with generics

import java.util.*;

class Node<t>
{
	private t data;
	public List<Node<t>> nextNode;

	// Constructor to make a new node with a specific height, usually the head*
	Node(int height)
	{
		this.data = null;
		this.nextNode = new ArrayList<>();
		for (int i = 0; i < height; i++) 
		{
			this.nextNode.add(null);
		}
	}

	// Constructor to make a new node with a specific height and data*
	Node(t data, int height)
	{
		this.data = data;
		this.nextNode = new ArrayList<>();
		for (int i = 0; i < height; i++)
		{
			this.nextNode.add(null);
		}
	}

	// Returns value stored in a node*
	public t value()
	{
		return this.data;
	}

	// Returns height of a node*
	public int height()
	{
		return this.nextNode.size();
	}

	// Returns the next node in the list*
	public Node<t> next(int level)
	{
		if (level < 0 || level > height() - 1) return null;
		return this.nextNode.get(level);
	}

}// End Node-Class

public class SkipList <t extends Comparable<t>>
{
	private Node<t> head;
	private int nTowers;

	// Constructor for new skip list*
	// I choose my skiplist to start from 0 rather than 1
	SkipList()
	{
		this.head = new Node<>(0);
		this.nTowers = 0;
	}

	// Constructor for new skip list and initialises the head node to have 
	// a height of the parameter*
	SkipList(int height)
	{
		if (height < 0) height = 0;
		this.head = new Node<>(height);
		this.nTowers = 0;
	}

	// Returns the number of nodes in the skiplist*
	public int size()
	{
		return this.nTowers;
	}

	// Returns current height of skiplist*
	public int height()
	{
		return this.head.height();
	}

	// Returns the current head of the skiplist*
	public Node<t> head()
	{
		return this.head;
	}

	// Inserts a new node at a random height*
	public void insert(t data)
	{
		int max = getMaxHeight(this.nTowers + 1);
		int randomLevel = generateRandomHeight(max);
		insert(data, randomLevel);
	}

	// Inserts a new node at a specific height less than the max height*
	public void insert(t data, int height)
	{
		// Initialising variables and increase the amount of towers
		this.nTowers++;
		Random r = new Random();
		int i, currHeight, currLevel, maxHeight = getMaxHeight(this.nTowers);
		currLevel = currHeight = this.height();

		// Made 2 nodes to iterate through the skip list and for growing the skiplist
		// and one to store the inserted value and height
		Node<t> currNode = this.head(), next = this.head().next(maxHeight);
		Node<t> newNode = new Node<>(data, height);

		// If current height is less than the max height of Skiplist, then we grow the SkipList
		if (currHeight < maxHeight)
		{
			// Always increase head
			this.head().nextNode.add(null);

			// Iterate to the end at the heighest level and increase the height of those nodes
			// by 1 with a probability of 50%
			while (next != null)
			{
				if (r.nextInt(2) == 1)
				{
					next.nextNode.add(null);
					currNode.nextNode.set(maxHeight, currNode);
					currNode = next;
				}
			}
		}

		currNode = this.head();
		next = this.head().next(currLevel);
		newNode = new Node<>(data, height);

		for (i = currLevel; i > -1; i--)
		{
			// Iterate to either the end of the skiplist at the highest level or if the value of 
			// the next node is greater than the value of insert we stop
			while (next != null && (next.value().compareTo(data)) < 0)
			{
				currNode = next;
				next = currNode.next(currLevel);
			}

			// Reached the height to insert, reference the new inserted node
			// as the current next node and the current node to the inserted node.
			if (i < height)
			{
				newNode.nextNode.set(currLevel, next);
				currNode.nextNode.set(currLevel, newNode);
			}
			// Go down a level
			currLevel--;
			next = currNode.next(currLevel);
		}
	}// end insert

	// Deletes a node from the skiplist*
	public void delete(t data)
	{
		// Initialised variables
		int i, j, maxHeight, currLevel;
		currLevel = this.height() - 1; 
		Node<t> currNode = this.head(), nextNode = this.head().next(currLevel);
		Node<t> trimCurrNode, tempNode;

		// Store the left and right references of the deleted node.
		List<Node<t>> left = new ArrayList<>(), right = new ArrayList<>();

		for (i = currLevel; i > -1; i--)
		{
			// Same way of iteration as insert
			while (nextNode != null && (nextNode.value().compareTo(data)) < 0)
			{
				currNode = nextNode;
				nextNode = nextNode.next(currLevel);
			}

			// Found the value to delete
			if (nextNode != null && (nextNode.value().compareTo(data)) == 0)
			{
				// store the references
				left.add(0, currNode);
				right.add(0, nextNode.next(currLevel));

				// Get to the bottom of the node
				if (currLevel == 0)
				{
					// Decrease the amount of towers
					this.nTowers--;
					maxHeight = getMaxHeight(this.nTowers);

					// Set our references to fill the gap of the deleted node
					for (j = 0; j < nextNode.height();j++)
					{
						left.get(j).nextNode.set(j,right.get(j));
					}
					// If the height is greater than the max height formula, we must trim
					while (this.height() > maxHeight)
					{
						trimCurrNode = this.head();
						while (trimCurrNode != null) 
						{
							tempNode = trimCurrNode.next(this.height() - 1);
							trimCurrNode.nextNode.remove(this.height() - 1);
							trimCurrNode = tempNode;
						}
					}
				}
			}
			
			// Go down a level
			currLevel--;
			nextNode = currNode.next(currLevel);
		}
	}// end delete

	// Returns true if the skiplist has data*
	public boolean contains(t data)
	{
		if (get(data) != null) return true;
			return false;
	}// end contains

	// Returns a reference to a node in the skiplist that contains the data parameter*
	public Node<t> get(t data)
	{
		// Variables
		int i, currLevel = this.height();
		Node<t> currNode = this.head(), nextNode = this.head().next(currLevel);
		
		for (i = currLevel; i > -1; i--)
		{
			// Same way of iterating as insert and delete
			while (nextNode != null && (nextNode.value().compareTo(data)) < 0)
			{
				currNode = nextNode;
				nextNode = currNode.next(currLevel);
			}

			// Found out value
			if (nextNode != null && (nextNode.value().compareTo(data)) == 0) 
				return currNode.next(currLevel);

			// Go down a level
			currLevel--;
			nextNode = currNode.next(currLevel);
		}

		return null;
	}// end get

	// Get max height of a skiplist with n nodes
	private static int getMaxHeight(int n)
	{
		// Must make sure the height is in scope, I run into a lot of
		// errors using <= or >= so I try my best not to use that operator
		if (n < 1) return 1;
		if (n == 1) return 1;

		// convert to log base 2 and find the ceiling to get the max height of skipList
		double base2 = Math.log(n) / Math.log(2);
		int maxHeight = (int)Math.ceil(base2);
		
		return maxHeight;
	}// end getMaxHeight

	// Returns 1,2,3,... with 50%, 25%, 12.5% probability respectively while being less
	// than height
	private static int generateRandomHeight(int maxHeight)
	{
		int randomHeight;
		Random r = new Random();
		for (randomHeight = 1; randomHeight < maxHeight; randomHeight++)
		{
			// 50% to stop increasing and break out of loop
			if (r.nextInt(2) == 1) break;
		}
		return randomHeight;
	}// end generateRandomHeight

}// End SkipList-class
