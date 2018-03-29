import java.util.ArrayList;
import java.util.List;

/**
 * 	ArrayStack - a Stack represented by a List
 *
 *  @author Mayank Singamreddy
 *  @since 	February 13, 2018
 */
 
 public class ArrayStack<E> implements Stack<E> {
 
	private List<E> elements;				//the elemnts on the stack
 
	//no-args constructor
	public ArrayStack(){
	   elements = new ArrayList<E>();
	}
	
	/**@return true if the ArrayStack is empty; false otherwise;
	 */
	public boolean isEmpty(){ return elements.isEmpty(); }
	
	/** @return the objects at the top of the stack */
	public E peek() {return elements.get(elements.size() - 1); }
	
	/** @param obj push the object obj onto the top of the stack */
	public void push(E obj) {elements.add(obj); }
	
	/**@return the object on top of the stack, and remove it 
	 * from the stack
	 */
	public E pop() {return elements.remove(elements.size()-1);}
 }