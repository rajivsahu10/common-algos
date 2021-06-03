package com.rajiv.algo;

public class LongestIncreasingSubsequence {

	public static void main(String[] args) {

		int arr[] = { 6, 2, 4, 3, 7, 4, 5 };
//		int arr[] = {10,29471,5242,21175,28931,2889,7275,19159,21773,1325,6901};
		int result = longestIncreasingSubsequence(arr);
		System.out.println("result = " + result);
	}

	private static int longestIncreasingSubsequence(int[] arr) {

		if (arr.length == 1) {
			return 1;
		}

		int gen[] = new int[arr.length];
		int max = 0;
		gen[0] = 1;
		for (int i = 1; i < arr.length; i++) {
			gen[i] = 1;
			for (int j = 0; j <= i - 1; j++) {
				if (arr[j] < arr[i]) {
					if (gen[j]+1 > gen[i]) {
						gen[i] = gen[j]+1;
					} 
					if (gen[i] > max) {
						max = gen[i];
					}
				}

			}
		}

		return max;
//		return getMax(gen);
	}

	private static int longestIncreasingSubsequence_(int[] arr) {

		if (arr.length == 1) {
			return 1;
		}

		int gen[] = new int[arr.length];
		int max = 0;
		gen[0] = 1;
		for (int i = 1; i < arr.length; i++) {
			gen[i] = 1;
//			int lastSmall=arr[0];
			int lastSmall = 0;
			for (int j = 0; j <= i - 1; j++) {
				if (arr[i] > arr[j]) {
					if (arr[j] > lastSmall) {
						gen[i]++;
					} else if (arr[j] > lastSmall) {
						gen[i] = arr[j] + 1;
					}
					lastSmall = arr[j];
					if (gen[i] > max) {
						max = gen[i];
					}
				}

			}
		}

		return max;
//		return getMax(gen);
	}

	private static int getMax(int[] arr) {
		int max = 0;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > max) {
				max = arr[i];
			}
		}
		return max;
	}

}
