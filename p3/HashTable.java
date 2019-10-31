/*
 * Filename:   HashTable.java
 * Project:    p3
 * Authors:    Austin Wilson
 * Lecture:    002
 *
 * Semester:   Fall 2018
 * Course:     CS400
 * 
 * Due Date:   Friday October 19th, 10 PM
 * Version:    1.0
 * 
 * Credits:    N/A
 * 
 * Bugs:       None Known
 */


import java.util.NoSuchElementException;

// This implementation uses open addressing linear search.
//
// This implementation uses the built in Java hashCode function 
public class HashTable<K extends Comparable<K>, V> implements HashTableADT<K, V> {
	
		//stores size of table
		private int tableSize;
		//stores given load factor
		private double loadFactor;
		//tracks number of elements in table
		private int size;	
		//Refernece to hashtable array
		private Pair[] hashTable;

	//No argument constructor if no initialCapacity and loadFactor are given
	public HashTable() {
		tableSize = 11;
		loadFactor = 0.75;
		size = 0;
		hashTable = new Pair[tableSize];
	}
	
	// Construct new hashtable given initialCapacity and loadFactor
	public HashTable(int initialCapacity, double loadFactor) {
		this.tableSize = initialCapacity;
		this.loadFactor = loadFactor;
		this.size = 0;
		this.hashTable = new Pair[tableSize];
	}

	//Private class to store key and value, and an instance of this class is stored in the 
	//hash table
	private class Pair<K, V> {
		private K key;
		private V value;
		private boolean flag;
		
		Pair() {
		}

		Pair(K key, V value) {
			this.key = key;
			this.value = value;
		}

		public K getKey() {
			return this.key;
		}
		public V getValue() {
			return this.value;
		}
		public boolean getFlag() {
			return this.flag;
		}
		public void setKey(K key) {
			this.key = key;
		}
		public void setValue(V value) {
			this.value = value;
		}
		public void setFlag(Boolean flag) {
			this.flag = flag;
		}
	}

	//Private method to resize and rehash elemnts if size/tableSize > loadFactor
	private void resize() {
		int newTableSize = 2*tableSize + 1;
		Pair[] tempHashTable = new Pair[newTableSize];
		for (int i = 0; i < tableSize; i++) {
			if (hashTable[i] != null) {
				int tempKeyIndex = hashTable[i].getKey().hashCode() % newTableSize;
				//If element at index is null, insert Pair object
				if (tempHashTable[tempKeyIndex] == null) {
					tempHashTable[tempKeyIndex] = hashTable[i];
				}
				//Not null, there was a collision, check next spots until it is null
				else {
					while (tempHashTable[tempKeyIndex] != null) {
						++tempKeyIndex;
					}
					tempHashTable[tempKeyIndex] = hashTable[i];
				}
			}
		}
		//Re-assign referneces to updates hashTable
		this.tableSize = newTableSize;
		this.hashTable = tempHashTable;
	}

	// Given a key and value, insert into hashTable 
	@Override
	public void put(K key, V value) throws IllegalArgumentException {
		if (key == null) {
			throw new IllegalArgumentException();
		}
		if ((this.size/this.tableSize) > this.loadFactor) {
			resize();
		}
		int tempKeyIndex = key.hashCode() % tableSize;
		Pair pair = new Pair(key, value);
		put(pair, tempKeyIndex);
	}
	
	// Private helper method to recursively insert into hashTable
	private void put(Pair pair, int tempKeyIndex) {
		if (tempKeyIndex < tableSize) {
			if (hashTable[tempKeyIndex] == null) {
				hashTable[tempKeyIndex] = pair;
				size++;
				return;
			}
			else {	
				put(pair, ++tempKeyIndex);
			}
		}
		else {
			put(pair, 0);
		}
	}
	
	// Search hashTable given a specific key. Throw IllegalArgumentException if key is null
	// and throw NoSuchElementException if key is not found in the table
	@Override
	public V get(K key) throws IllegalArgumentException, NoSuchElementException {
		if (key == null) {
			throw new IllegalArgumentException();
		}
		//If key exists
		int tempKeyIndex = key.hashCode() % tableSize;
		return get(key, tempKeyIndex);
	}

	// Private recursive helper method to search for key in hashTable
	private V get(K key, int tempKeyIndex) {
		if (tempKeyIndex < tableSize) {
			if (hashTable[tempKeyIndex] == null) {
				throw new NoSuchElementException();
			}
			K toCheck = (K)hashTable[tempKeyIndex].getKey();
			if (toCheck.compareTo(key) == 0) {
				return (V)hashTable[tempKeyIndex].getValue(); 
			}
			get(key, ++tempKeyIndex);
		}
		else {
			get(key, 0);
		}
		return null;
	}
	
	// Given a specific key, searches hashTable to remove that element. Throw IllegalArgumentException
	// if key is null. Throw NoSuchElementException if the element to be deleted doesnt exist in the table.
	@Override
	public void remove(K key) throws IllegalArgumentException, NoSuchElementException {
		if (key == null) {
			throw new IllegalArgumentException();
		}
		int tempKeyIndex = key.hashCode() % tableSize;
		remove(key, tempKeyIndex);
	}

	// Private helper method to recursively search table to remove the given key.
	private void remove(K key, int tempKeyIndex) {
		if (tempKeyIndex < tableSize) {
			if (hashTable[tempKeyIndex] == null) {
				throw new NoSuchElementException();
			}
			K toCheck = (K)hashTable[tempKeyIndex].getKey();
			if (toCheck.compareTo(key) == 0) {
				hashTable[tempKeyIndex].setKey(null);
				hashTable[tempKeyIndex].setValue(null);
				hashTable[tempKeyIndex].setFlag(true);
				size--;
			}
			remove(key, ++tempKeyIndex);
		}
		else {
			remove(key, 0);
		}

	}
	
	// Return the size of the table stored in the private field.
	@Override
	public int size() {
		return this.size;
	}
		
}
