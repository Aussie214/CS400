/**
 * Filename:   TestAVLTree.java
 * Project:    p2
 * Authors:    Debra Deppeler, Austin Wilson
 *
 * Semester:   Fall 2018
 * Course:     CS400
 * Lecture:    002
 * 
 * Due Date:   Before 10pm on September 24, 2018
 * Version:    1.0
 * 
 * Credits:    N/A
 * 
 * Bugs:       No Known Bugs
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.lang.IllegalArgumentException;
import java.lang.IllegalArgumentException;
import org.junit.Test;
import java.util.Random;

/** TODO: add class header comments here*/
public class TestAVLTree {

	Random rand = new Random();

	/**
	 * Tests that an AVLTree is empty upon initialization.
	 */
	@Test
	public void test01isEmpty() {
		AVLTree<Integer> tree = new AVLTree<Integer>();
		assertTrue(tree.isEmpty());
	}

	/**
	 * Tests that an AVLTree is not empty after adding a node.
	 */
	@Test
	public void test02isNotEmpty() {
		AVLTree<Integer> tree = new AVLTree<Integer>();
		try {
			tree.insert(1);
			assertFalse(tree.isEmpty());
		} catch (DuplicateKeyException e) {
			System.out.println(e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Tests functionality of a single delete following several inserts.
	 */
	@Test
	public void test03insertManyDeleteOne() {
		AVLTree<Integer> tree = new AVLTree<Integer>();
		try {
			tree.insert(5);
			tree.insert(6);
			tree.insert(2);
			tree.insert(8);
			tree.insert(4);
			tree.insert(15);
			tree.insert(1);
			tree.insert(3);
			tree.insert(7);
			tree.delete(8);
			String toCheck = tree.print();
			assertTrue("Inserting several items and then deleting one failed to do so in the correct oder.", toCheck.equals("12456715"));
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		fail("test03insertManyDeleteOne failed");
	}
	
	/**
	 * Tests functionality of many deletes following several inserts.
	 */
	@Test
	public void test04insertManyDeleteMany() {
		try {
			AVLTree<Integer> tree = new AVLTree<Integer>();
                        tree.insert(6);
                        tree.insert(2);
                        tree.insert(12);
                        tree.insert(4);
                        tree.insert(15);
                        tree.insert(0);
                        tree.insert(3);
                        tree.insert(7);
                        tree.insert(13);
			tree.insert(1);
			tree.insert(18);
			tree.insert(20);
			//Delete
			tree.delete(12);
			tree.delete(0);
			tree.delete(1);
			tree.delete(6);
			tree.delete(7);
			tree.delete(3);
			tree.delete(15);
			String toCheck = tree.print();
			assertTrue("Inserting several items and deleting several items into the tree failed to maintain order.", toCheck.equals("24131820"));
	        }
		catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                }
                catch (DuplicateKeyException e) {
                        System.out.println(e.getMessage());
                }
                catch(Exception e) {
                        System.out.println(e.getMessage());
                }
		fail("test04insertManyDeleteMany failed.");
	}

	/**
         * Tests functionality of inserting a duplicate key. Tree should correctly throw a DuplicateKeyException
         */
        @Test
	public void test05insertDuplicateKey(){
		try {
			AVLTree<Integer> tree = new AVLTree<Integer>();
			tree.insert(3);
			tree.insert(3);
		}
		catch(DuplicateKeyException e) {
			 assertTrue("Tree correctly caught DuplicateKeyException. TEST PASSES.", true);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		fail("test05insertDuplicateKey failed.");
	}

 	/**
         * Tests functionality of inserting a null key. Tree should throw an IllegalArgument Exception
         */
        @Test
        public void test06insertNull(){
		try {
			AVLTree<Integer> tree = new AVLTree<Integer>();
			tree.insert(null);
		}
		catch (IllegalArgumentException e) {
			assertTrue("Tree correctly caught IllegalArgumentExcpetion. TEST PASSES.", true);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		fail("test06insertNull failed.");
	}

 	/**
         * Tests functionality of trying to delete a null element. Tree should correctly throw an IllegalArgumentException
         */
        @Test
        public void test07deleteNull(){
		try {
			AVLTree<Integer> tree = new AVLTree<Integer>();
			tree.delete(null);
		}
		catch (IllegalArgumentException e) {
			assertTrue("Tree correctly threw an IllegalArgumentException. TEST PASSES.", true);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		fail("test07deleteNull failed.");
	}

 	/**
         * Tests functionality of deleting an element on an empty tree.
         */
        @Test
        public void test08deleteOnEmptyTree(){
		try {
			AVLTree<Integer> tree = new AVLTree<Integer>();
			tree.delete(4);
			assertTrue(tree.isEmpty());
		}
		 catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                }
                catch(Exception e) {
                        System.out.println(e.getMessage());
                }
		fail("test08deleteOnEmptyTree");
	}

	 /**
         * Tests functionality of the print method
         */
        @Test
        public void test09checkPrint(){
		try {
			AVLTree<Integer> tree = new AVLTree<Integer>();
			String check = "";
			for (int i = 0; i < 25; i++) {
				Integer temp = rand.nextInt();
				tree.insert(temp);
				check += Integer.toString(temp);
			}
			String print = tree.print();
			assertTrue("Print correctly traversed the tree. TEST PASSED.", print.equals(check));
		}
		 catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                }
                catch (DuplicateKeyException e) {
                        System.out.println(e.getMessage());
                }
                catch(Exception e) {
                        System.out.println(e.getMessage());
                } 
		fail("test09checkPrint failed.");
	}

 	/**
         * Tests functionality of tree in terms of if it can expand to hold a large number of elements
         */
        @Test
        public void test10treeExpansion(){
		try {
			AVLTree<Integer> tree = new AVLTree<Integer>();
			for (int i = 0; i < 50000; i++) {
				tree.insert(rand.nextInt());
			}	
			assertTrue("Tree was able to expand with a large number of elements. TEST PASSED.", true);
		}
		catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                }
                catch (DuplicateKeyException e) {
                        System.out.println(e.getMessage());
                }
                catch(Exception e) {
                        System.out.println(e.getMessage());
                }
		fail("test10treeExpansion failed.");
	}

 	/**
         * Tests functionality of search method to return the right value
         */
        @Test
        public void test11checkSearch(){
		try {
			AVLTree<Integer> tree = new AVLTree<Integer>();
			tree.insert(10);
			assertTrue(tree.search(10));
		}
		catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}	
		fail("test11checkSearch failed.");
	}

 	/**
         * Tests functionality of search method to throw an IllegalArgumentException
         */
        @Test
        public void test12checkNullSearch(){
		try {
			AVLTree<Integer> tree = new AVLTree<Integer>();
			tree.search(null);
		}
		catch (IllegalArgumentException e) {
			assertTrue("Tree correctly threw an IllegalArgumentException on a null search", true);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		fail("test12checkNullSearch failed.");
	}
}
