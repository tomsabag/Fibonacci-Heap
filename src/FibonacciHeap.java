/**
 * FibonacciHeap
 *
 * An implementation of fibonacci heap over integers.
 */

//rotem nizhar -208646984 -rotemnizhar
// tom sabag -208845842 - tomsabag

public class FibonacciHeap
{
	private int size;
	public static int links;
	public static int cuts;
	private HeapNode min = null;
	private HeapNode head = null;
	public int marked = 0;
	public int trees=0;
   /**
    * public boolean isEmpty()
    *
    * precondition: none
    * 
    * The method returns true if and only if the heap
    * is empty.
    *   
    */
   public FibonacciHeap() {

   }

	public FibonacciHeap(HeapNode node) {
		this.min = node;
		this.head = node;
		this.size = 1;
		this.trees = 1;
	}
    public boolean isEmpty()
    {
		return this.min == null;
	}
	public HeapNode getMin() {return this.min;}
	public HeapNode getHead() {return this.head;}
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * 
    * Returns the new node created. 
    */
    public HeapNode insert(int key) // constructor
    {    
    	HeapNode node= new HeapNode(key);
    	this.trees++;
    	this.size++;
    	return (insertIn(node));
    }
    public HeapNode insertIn (HeapNode node) { // help method to the constructor
    	if (this.isEmpty()) { // case heap is empty
    		this.head=node;
    		this.min=node;
    		node.setNext(node);
    		node.setPrev(node);
    		return node;
    	}
    	HeapNode head= this.head;
    	HeapNode tail= this.head.getPrev();
    	node.setNext(head);
    	node.setPrev(tail);
    	tail.setNext(node);
    	head.setPrev(node);
    	this.head=node;
    	if(this.min.getKey()>node.getKey()) {
    		this.min=node;
    	}
    	return (node);
    }
   /**
    * public void deleteMin()
    *
    * Delete the node containing the minimum key.
    *
    */
    public void deleteMin() {
		if (this.size == 0) // case heap is empty
			return;

		// case heap contains only one heap node
		else if (this.size == 1) {
			this.min = null;
			this.head = null;
			this.trees = 0;
			this.marked = 0;
			this.size = 0;
		}
		// case one tree in heap
		else if (this.head.getNext() == this.head) {
			this.trees = this.head.getChild().getRank();
			this.head = this.head.getChild();
			HeapNode curr = this.head;
			HeapNode newMin = this.head;

			do {
				curr.setMark(0);
				curr.setParent(null);

				newMin = (curr.getKey() < newMin.getKey()) ? curr : newMin;
				curr = curr.getNext();
			}
			while (curr != head);
			setNewMin(this);
			this.consolidation();
			setNewMin(this);
		}

		// case head == min & more than one root in roots list
		else if (this.head == this.min && this.head.getNext() != this.head) {
			
			FibonacciHeap newHeap = new FibonacciHeap(this.head.getChild());
			if (!newHeap.isEmpty()) { // order the new heap
				HeapNode curr = this.head.getChild();
				HeapNode newMin = this.head.getChild();
				do {
					curr.setMark(0);
					curr.setParent(null);
					newMin = (curr.getKey() < newMin.getKey()) ? curr : newMin;
					curr = curr.getNext();
				}
				while (curr != newHeap.getHead());
				newHeap.min = newMin;
			}

			HeapNode headNext = this.head.getNext();
			HeapNode headPrev = this.head.getPrev();
			this.head = headNext;
			headNext.setPrev(headPrev);
			headPrev.setNext(headNext);
			this.min.setNext(this.min);
			this.min.setPrev(this.min);
			this.min.setChild(null);
			int currentSize = this.size;
			this.meld(newHeap); 
			this.size = currentSize;
			setNewMin(this);
			this.consolidation();
			setNewMin(this);
		}
		// case head != min & more than one root in roots list
		else if (this.head != this.min && this.head.getNext() != this.head) {
			FibonacciHeap newHeap = new FibonacciHeap(this.min.getChild());
			if (newHeap.isEmpty()) { // order the new heap
			}
			else {
				HeapNode curr = this.min.getChild();
				HeapNode newMin = this.min.getChild();
				do {
					curr.setMark(0);
					curr.setParent(null);
					newMin = (curr.getKey() < newMin.getKey()) ? curr : newMin;
					curr = curr.getNext();
				}
				while (curr != newHeap.getHead());
				newHeap.min = newMin;
			}
			HeapNode minNext = this.min.getNext();
			HeapNode minPrev = this.min.getPrev();
			minNext.setPrev(minPrev);
			minPrev.setNext(minNext);
			this.min.setNext(this.min);
			this.min.setPrev(this.min);
			this.min.setChild(null);
			//
			int currentSize = this.size;
			this.meld(newHeap);
			this.size = currentSize;
			setNewMin(this);
			this.consolidation();
			setNewMin(this);
		}
		size--;
	}
    
    // update the number of tress and the new min of the heap after we delete the min
	public void setNewMin(FibonacciHeap heap) {

    	int treesCounter = 0;
    	HeapNode curr = heap.head;
    	HeapNode newMin = curr;
    	do {
			
    		treesCounter++;
			newMin = (curr.getKey() < newMin.getKey()) ? curr : newMin;
			curr = curr.getNext();
			if( curr.getMark()==1) {
				curr.setMark(0);
				this.marked--;
			}
		}
    	while (curr != heap.head);
    	heap.trees = treesCounter;
    	heap.min = newMin;
	}


	public HeapNode link(HeapNode x, HeapNode y) {
    	// no links are made
    	if (x == null || y == null) {
    		if (x == null) return y;
    		return x;
		}
    	links++;
		HeapNode bigger = (x.getKey() > y.getKey()) ? x : y;
		HeapNode smaller = (x.getKey() < y.getKey()) ? x : y;
		// both nodes are of rank 0 - we link nodes of same rank
		if (bigger.getRank() == 0) {
			smaller.setChild(bigger);
			bigger.setParent(smaller);
			smaller.setRank(1);
			bigger.setNext(bigger);
			bigger.setPrev(bigger);
			return smaller;
		}
		// biggerChild exists, since bigger.getRank() != 0. here, we make connect bigger and smaller, smaller as parent node
		HeapNode smallerChild = smaller.getChild();
		HeapNode smallerChildPrev = smallerChild.getPrev();

		bigger.setParent(smaller);
		smaller.setChild(bigger);

		bigger.setNext(smallerChild);
		smallerChild.setPrev(bigger);

		bigger.setPrev(smallerChildPrev);
		smallerChildPrev.setNext(bigger);

		smaller.setRank(smaller.getRank() + 1);

		return smaller;
	}

	public void consolidation() { // succesive linking process
		HeapNode[] bucket = toBuckets();
		fromBuckets(bucket);
	}
    public HeapNode[] toBuckets() { // consolidating
    	// initialize an empty bucket
    	double log =( Math.log10(this.size))/(Math.log10(2));
    	int length =(int)(2*1.4404 * log) + 1;
    	HeapNode[] bucket;
    	if (this.min.getOrginal()==null) {
    		 bucket = new HeapNode[length];
    	}
    	else {
    		 bucket = new HeapNode[this.FindMaxRank()*2];
    	}
		HeapNode curr = this.head.next;
		bucket[this.head.getRank()] = this.head;
		this.head.setNext(null);
		this.head.setPrev(null);
		HeapNode linked;
		int currentRank;
		// consolidating loop
		for (int i=0; i<this.trees-1;i++) {
			linked = curr;
			currentRank = curr.getRank();
			
			HeapNode next = curr.getNext();
			curr.setNext(null);
			curr.setPrev(null);
			while (bucket[currentRank] != null) {
				if (linked.getOrginal()==null) {
					linked = link(linked, bucket[currentRank]); // link
				}
				else {
					linked= other_link(linked, bucket[currentRank]);
				}
				bucket[currentRank] = null; // deplete the empty bucket
				currentRank++; // for next links, if exist
			}
			bucket[currentRank] = linked;
			curr = next;
		}
		return bucket;
	}
	public void fromBuckets(HeapNode[] bucket) {
		HeapNode current = null;
		int trees = 0;
		boolean val = true; // indicator for head pointer
		HeapNode newMin = null;
		// initialize head
		HeapNode newHead = null;
		HeapNode head1 =this.head;
		for (HeapNode node : bucket) {
			if (node != null) { // set new head
				if (val) {
					val=false;
					head1 =node;
				}
				// initialize first node
				if (current == null) {
					current = node;
					current.setPrev(current);
					current.setNext(current);
					newMin = node;
					newHead = node;
				// continue inserting nodes
				} else {
					current.setNext(node);
					node.setPrev(current);
					if (node.getKey() < current.getKey()) // set new minimum
						newMin = node;
					current = node;
				}

				trees++;
			}
		}
		current.setNext(head1);
		head1.setPrev(current);
		this.trees = trees;
		this.head = newHead;
		this.min = newMin;
	}
	// help method for k-min, to get the size of the bucket array in to buckets() --- o(n)
	public int FindMaxRank () {
		HeapNode node= this.min;
		int rank =node.getRank();
		HeapNode cur = node.getNext();
		while (node!= cur) {   // get the highst rank in the heap 
			if (cur.getRank()> rank) {
				rank =cur.getRank();
				cur=cur.getNext();
			}
		}
		return (rank+1);
	}
   /**
    * public HeapNode findMin()
    *
    * Return the node of the heap whose key is minimal. 
    *
    */
    public HeapNode findMin()
    {
    	return (this.min);
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Meld the heap with heap2
    *
    */
   	
    public void meld (FibonacciHeap heap2) {
		// if other heap is empty - nothing to do
		if (heap2.isEmpty())
			return;

		this.marked = this.marked + heap2.marked;
		this.trees = this.trees + heap2.trees;
		// if this is empty
		if (this.isEmpty()) {
			this.head = heap2.head;
			this.size = heap2.size;
			this.min = heap2.min;
			return;
		}
		// both heaps not empty
		HeapNode tail1 = this.head.getPrev();
		HeapNode head1 = this.head;
		HeapNode head2 = heap2.head;
		HeapNode tail2 = heap2.head.getPrev();
		head1.setPrev(tail2);
		head2.setPrev(tail1);
		tail1.setNext(head2);
		tail2.setNext(head1);
		this.size = this.size + heap2.size;
		this.min = (this.min.getKey() <= heap2.min.getKey()) ? this.min : heap2.min;

	}

   /**
    * public int size()
    *
    * Return the number of elements in the heap
    *   
    */
    public int size()
    {
    	return (this.size);
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return a counters array, where the value of the i-th entry is the number of trees of order i in the heap. 
    * 
    */
    public int[] countersRep() {
    	//A unique case
		if (this.isEmpty()) {
			return new int[0];
		}
		int headKey = this.head.getKey();
		HeapNode curr = this.head.getNext();
		int maxRank = this.head.getRank();

		// get max rank in roots list
		while (curr.getKey() != headKey) {
			maxRank = Math.max(maxRank, curr.getRank());
			curr = curr.next;
		}
		//create the array
		int[] arr = new int[maxRank+1];
		arr[this.head.getRank()] = 1;
		curr = this.head.getNext();
		// update the elements of the array
		while (curr.getKey() != headKey) {
			arr[curr.getRank()] += 1;
			curr = curr.next;
		}
		return arr;
	}
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap. 
    *
    */
    public void delete(HeapNode x)	// we are using functions: decrease key and delete min 
    {    
    	int minKey = this.min.getKey();
    	int delta = Math.abs(x.getKey()-minKey)+1; // x =min-1
    	this.decreaseKey(x, delta);
    	this.deleteMin();

    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * The function decreases the key of the node x by delta. The structure of the heap should be updated
    * to reflect this chage (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta)
    {
    	x.setKey(x.getKey() - delta);
    	if (x.getParent()==null ) {
    	}
    	else if (x.getKey() > x.getParent().getKey()){
    	}
    	else {
    		if( x.getMark()==1) {
    			x.setMark(0);
    			this.marked--;
    		}
    		this.cascadingCut(x, x.parent);
    	}
		if (x.getKey()<this.min.getKey()) {
			this.min=x;
		}
    	return; 
    }

   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * The potential equals to the number of trees in the heap plus twice the number of marked nodes in the heap. 
    */
    public int potential() 
    {    
    	return (this.trees + (2 * this.marked));
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the run-time of the program.
    * A link operation is the operation which gets as input two trees of the same rank, and generates a tree of 
    * rank bigger by one, by hanging the tree which has larger value in its root on the tree which has smaller value 
    * in its root.
    */
    public static int totalLinks()
    {    
    	return (links);
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the run-time of the program.
    * A cut operation is the operation which diconnects a subtree from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts()
    {    
    	return (cuts);
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k minimal elements in a binomial tree H.
    * The function should run in O(k*deg(H)). 
    * You are not allowed to change H.
    */
    public static int[] kMin(FibonacciHeap H, int k)
    {    
    	int linking= totalLinks(); 	// saving the cuts an links
    	int cutting= totalCuts();
    	int [] arr= new int [k];
    	int i =1;
    	FibonacciHeap Help1= new FibonacciHeap ();
    	FibonacciHeap Help2= new FibonacciHeap ();
    	if (k==0) {
    		return null;
    	}
    	if(k==1) {  //A unique case
    		arr[0]= H.min.getKey();
    		return (arr);
    	}
    	arr[0]= H.min.getKey(); 
    	HeapNode cur= H.min;
    	InsertToHelpHeap(Help1,cur ); // create fake nodes
    	Help1.size= ( cur.getRank());
    	Help1.trees=( cur.getRank());
    	while (i<k) {
    		Help2.meld(Help1);
    		HeapNode NewMin= Help2.findMin();
    		arr[i]=NewMin.getKey(); // adding the i'th min index from the heap
    		i++;
    		Help1=  new FibonacciHeap (); // delete Help1
    		HeapNode orginal= NewMin.getOrginal(); // Get the new candidates to be the minimum
    		Help2.deleteMin();
    		InsertToHelpHeap(Help1,orginal );  // create fake nodes
    		Help1.size=(int) orginal.getRank();
    		Help1.trees=(int) orginal.getRank();
    	}
    	links= linking; 
    	cuts=cutting;
    	return (arr);
    }
    
    public static void InsertToHelpHeap (FibonacciHeap H, HeapNode node) { // create fake nodes
    	if (node.getChild()==null) {
    		return ;
    	}
    	HeapNode first= node.getChild();
    	HeapNode cur= node.getChild();
    	do {
    		H.insertIn(HeapNode.CreateCopy(cur)); // crate a fake node and insert it to the heap
    		cur= cur.getNext();
    	}
    	while (cur.getKey()!=first.getKey()); /// adding all the fake sones of the node node
    }

    // clears all pointers (nexts, prevs) from a heap node
    public void clearPointers(HeapNode x) {
    	// case x has a next node
    	if (x.getNext() != x) {
    		HeapNode next = x.getNext();
    		x.setNext(x);
    		next.setPrev(next);
		}
    	// case x has a prev node
    	if (x.getPrev() != x) {
    		HeapNode prev = x.getPrev();
    		x.setPrev(x);
    		prev.setNext(prev);
		}
	}
    // link two heap nodes
	//self note - remember to take care of ranks



	public HeapNode other_link(HeapNode x, HeapNode y) {
    	// no links are made
    	if (x == null || y == null) {
    		if (x == null) return y;
    		return x;
		}
		HeapNode bigger = (x.getKey() > y.getKey()) ? x : y;
		HeapNode smaller = (x.getKey() < y.getKey()) ? x : y;
		// both nodes are of rank 0 - we link nodes of same rank
		if ( (smaller.getChild()==null)){
			smaller.setChild(bigger);
			bigger.setParent(smaller);
			smaller.setRank(smaller.getRank()+1);
			bigger.setNext(bigger);
			bigger.setPrev(bigger);
			return smaller;
		}
		else if (bigger.getChild() == null) {
			HeapNode  tmp= smaller.getChild();
			smaller.setChild(bigger);
			bigger.setParent(smaller);
			smaller.setRank(smaller.getRank()+1);
			bigger.setNext(tmp);
			bigger.setPrev(tmp.getPrev());
			tmp.setPrev(bigger);
			return (smaller);
		}		
		// biggerChild exists, since bigger.getRank() != 0. here, we make connect bigger and smaller, smaller as parent node
		HeapNode smallerChild = smaller.getChild();
		HeapNode smallerChildPrev = smallerChild.getPrev();

		bigger.setParent(smaller);
		smaller.setChild(bigger);

		bigger.setNext(smallerChild);
		smallerChild.setPrev(bigger);

		bigger.setPrev(smallerChildPrev);
		smallerChildPrev.setNext(bigger);

		smaller.setRank(smaller.getRank() + 1);
		return smaller;
	}

	public void insertAfter(HeapNode before, HeapNode after) {
    	if (before.getNext() == before && before.getNext() == before) {
			before.setNext(after);
			before.setPrev(after);

			after.setNext(before);
			after.setPrev(before);
		}
    	else {
    		HeapNode beforeNext = before.getNext();
			before.getNext().setPrev(after);
    		before.setNext(after);

    		after.setPrev(before);
    		after.setNext(beforeNext);
		}
	}

    // x - child, y - parent
	
    public void cut(HeapNode x, HeapNode y) {
    	cuts++;
    	x.setParent(null);
    	x.setMark(0);
    	y.setRank(y.getRank() - 1);
    	if (x.getNext() == x)
    		y.setChild(null); 
    	else {
    		HeapNode p= x.getPrev();
    		HeapNode n= x.getNext();
    		y.setChild(x.getNext());
    		x.getNext().setParent(y);
    		x.getPrev().setNext(x.getNext());
    		n.setPrev(p);
    		p.setNext(n);
		}
    	HeapNode H= this.head;
    	HeapNode T= this.head.getPrev();
    	x.setNext(H);
    	x.setPrev(T);
    	this.head=x;
    	T.setNext(x);
    	H.setPrev(x);
    	if(x.getKey()<this.min.getKey()) {
    		this.min=x;
    	}
    	this.trees++;
    	
	}

	public void cascadingCut(HeapNode x, HeapNode y) {
		if (x.getParent() == null) return; // no cuts of roots allowed
		cut(x, y);
		if (y.getParent() != null) {
			if (y.getMark() == 0) {
				y.setMark(1);
				this.marked++;
			} else if (y.getMark() == 1) {
				this.marked--;
				this.cascadingCut(y, y.getParent());

			}
		}
	}
   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in 
    * another file 
    *  
    */
    public static class HeapNode{

	private int key;
	private int rank=0;
	private int mark = 0;
	private String info;
	private HeapNode prev =null;
	private HeapNode next= null;
	private HeapNode child= null;
	private HeapNode parent= null;
	private HeapNode orginal =null;
	
	public HeapNode(int key, String info) {
		this.key=key;
		this.info=info;
	}
	
  	public HeapNode(int key) {
	    this.key = key;
      }
	// setters functions
  	public void setKey(int key) {
	    this.key=key;
      }
  	
  	public void setRank (int rank) {
  		this.rank=rank;
  	}
  	
  	public void setMark (int mark) {
  		this.mark=mark;
  	}
  	
  	public void setInfo (String info) {
  		this.info=info;
  	}
  	
  	public void setPrev ( HeapNode prev) {
  		this.prev=prev;
  	}
  	
  	public void setNext (HeapNode next) {
  		this.next=next;
  	}
  	
  	public void setParent (HeapNode parent) {
  		this.parent=parent;
  	}
  	
  	public void setChild (HeapNode child) {
		this.child = child;
  	}

  	// getter functions

 	public int getKey() {
	    return this.key;
      }

  	public int getRank () {
  		return (this.rank);
  	}

  	public int getMark () {
  		return (this.mark);
  	}
  	
  	public String getInfo () {
  		return (this.info);
  	}
  	
  	public HeapNode getPrev () {
  		return (this.prev);
  	}
  	
  	public HeapNode getNext () {
  		return (this.next);
  	}
  	
  	public HeapNode getParent () {
  		return (this.parent);
  	}
  	
  	public HeapNode getChild () {
  		return (this.child);
  	}
  	
  	public void setOrginal (HeapNode node) {
  		this.orginal=node;
  	}
  	public HeapNode getOrginal () {
  		return (this.orginal);
  	}
  	public  static HeapNode CreateCopy (HeapNode min) { // create a fake node
  		HeapNode node = new HeapNode (min.getKey(), min.getInfo());
  		node.setRank(min.getRank());
  		node.setOrginal(min); // point the fake node to his original node
  		node.setNext(node);
  		node.setPrev(node);
  		return (node);
  	}
  	
    }
}


