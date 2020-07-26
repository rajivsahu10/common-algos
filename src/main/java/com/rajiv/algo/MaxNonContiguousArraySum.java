package com.rajiv.algo;

public class MaxNonContiguousArraySum {

    static int maxSubsetSum(int[] arr) {

        if(arr.length == 1) {
            return arr[0];
        }
        int sum = arr[0];
        int allowed = 0;
        int notAllowed = arr[0];
        for(int i=1; i<arr.length; i++) {

            allowed = Math.max(arr[i] + allowed, arr[i]);
//            if (arr[i] > allowed) {
//                allowed = arr[i];
//            } else if (arr[i] + allowed > allowed){
//                allowed = arr[i] + allowed;
//            }
            int temp = notAllowed;
            if (notAllowed < allowed) {
                notAllowed =allowed;
            }
            allowed = temp;
        }

        sum = Math.max(allowed, notAllowed );
        return sum;
    }

    public  static void main(String args[]) {
        int arr[]  = {3, 5, -7, 8, 10};

        int sum = maxSubsetSum(arr);
        System.out.println("sum = " + sum);
    }
}
