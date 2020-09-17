package smallPrograms.day1;

import java.util.Arrays;

public class ArrayPractice {

	public static void main(String[] args) {

		Integer[] randArray = new Integer[20];

		for (int i = 0; i < 20; i++) {
			randArray[i] = (int) Math.round(Math.random() * 399) + 1;
		}

		System.out.println("Unsorted:" + Arrays.toString(randArray));
		
		quicksort(randArray, 0, randArray.length - 1);
		
		System.out.println("sorted:" + Arrays.toString(randArray));
	}

	// based off pseudocode from https://en.wikipedia.org/wiki/Quicksort
	// I managed to sort 20 million numbers in < 1 second when not printing the arrays, just an end message
	static void quicksort(Integer[] array, int low, int high) {
		if (low < high) {
			int p = partition(array, low, high);
			quicksort(array, low, p);
			quicksort(array, p + 1, high);
		}
	}

	static int partition(Integer[] array, int low, int high) {
		int pivot = array[(int) Math.floor((low + high) / 2)];
		int i = low -1;
		int j = high + 1;
		while(true) {
			do {
				i = i+ 1;
			} while( array[i] < pivot);
			do {
				j = j - 1;
			} while( array[j] > pivot);
			if( i >= j) {
				return j;
			}
			int temp = array[i];
			array[i] = array[j];
			array[j] = temp;
		}
	}
}
