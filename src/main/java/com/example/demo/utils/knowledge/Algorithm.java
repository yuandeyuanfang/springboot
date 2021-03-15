package com.example.demo.utils.knowledge;

/**
 * 算法
 */
public class Algorithm {
    public static void main(String[] args) {
        int[] arr = {8, 5, 3, 2, 4};
//        bubbleLeft(arr);//冒泡排序-从左到右
//        bubbleRight(arr);//冒泡排序-从右到左
//        selectSort(arr);//选择排序
//        int low = 0;
//        int high = arr.length - 1;
//        quickSort(arr, low, high);//快速排序
//        System.out.println(Arrays.toString(arr));
//        reverse(-12);//整数反转
//        arraySum();//两数之和
        gameSeven();//逢七过游戏
    }

    /**
     * 冒泡排序-从左到右
     *a、冒泡排序，是通过每一次遍历获取最大/最小值
     *b、将最大值/最小值放在尾部/头部
     *c、然后除开最大值/最小值，剩下的数据在进行遍历获取最大/最小值
     * @param arr
     */
    public static void bubbleLeft(int[] arr) {
        for (int i = 0; i < arr.length-1; i++) {
            for (int j = i+1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }

    /**
     * 冒泡排序-从右到左
     *a、冒泡排序，是通过每一次遍历获取最大/最小值
     *b、将最大值/最小值放在尾部/头部
     *c、然后除开最大值/最小值，剩下的数据在进行遍历获取最大/最小值
     * @param arr
     */
    public static void bubbleRight(int[] arr){
        //冒泡
        for (int i = 0; i < arr.length; i++) {
            //外层循环，遍历次数
            for (int j = 0; j < arr.length - i - 1; j++) {
                //内层循环，升序（如果前一个值比后一个值大，则交换）
                //内层循环一次，获取一个最大值
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j + 1];
                    arr[j + 1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }

    /**
     * 选择排序
     * a、将第一个值看成最小值
     * b、然后和后续的比较找出最小值和下标
     * c、交换本次遍历的起始值和最小值
     * d、说明：每次遍历的时候，将前面找出的最小值，看成一个有序的列表，后面的看成无序的列表，然后每次遍历无序列表找出最小值
     * @param arr
     */
    public static void selectSort(int[] arr){
        //选择
        for (int i = 0; i < arr.length; i++) {
            //默认第一个是最小的。
            int min = arr[i];
            //记录最小的下标
            int index = i;
            //通过与后面的数据进行比较得出，最小值和下标
            for (int j = i + 1; j < arr.length; j++) {
                if (min > arr[j]) {
                    min = arr[j];
                    index = j;
                }
            }
            //然后将最小值与本次循环的，开始值交换
            int temp = arr[i];
            arr[i] = min;
            arr[index] = temp;
            //说明：将i前面的数据看成一个排好的队列，i后面的看成一个无序队列。每次只需要找无需的最小值，做替换
        }
    }

    /**
     * 快速排序
     * @param arr
     * @param low
     * @param high
     */
    public static void quickSort(int[] arr, int low, int high) {
        //如果指针在同一位置(只有一个数据时)，退出
        if (high - low < 1) {
            return;
        }
        //标记，从高指针开始，还是低指针（默认高指针）
        boolean flag = true;
        //记录指针的其实位置
        int start = low;
        int end = high;
        //默认中间值为低指针的第一个值
        int midValue = arr[low];
        while (true) {
            //高指针移动
            if (flag) {
                //如果列表右方的数据大于中间值，则向左移动
                if (arr[high] > midValue) {
                    high--;
                } else if (arr[high] < midValue) {
                    //如果小于，则覆盖最开始的低指针值，并且移动低指针，标志位改成从低指针开始移动
                    arr[low] = arr[high];
                    low++;
                    flag = false;
                }
            } else {
                //如果低指针数据小于中间值，则低指针向右移动
                if (arr[low] < midValue) {
                    low++;
                } else if (arr[low] > midValue) {
                    //如果低指针的值大于中间值，则覆盖高指针停留时的数据，并向左移动高指针。切换为高指针移动
                    arr[high] = arr[low];
                    high--;
                    flag = true;
                }
            }
            //当两个指针的位置相同时，则找到了中间值的位置，并退出循环
            if (low == high) {
                arr[low] = midValue;
                break;
            }
        }
        //然后出现有，中间值左边的小于中间值。右边的大于中间值。
        //然后在对左右两边的列表在进行快速排序
        quickSort(arr, start, low -1);
        quickSort(arr, low + 1, end);
    }

    /**
     * 整数反转
     * @param x
     * @return
     */
    public static int reverse(int x) {
//        System.out.println(x % 10);
//        return 0;
        long res = 0;
        while (x != 0) {
            res = res * 10 + x % 10;
            x /= 10;
        }
        return (int) res == res ? (int) res : 0;
    }

    /**
     * 两数之和，给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
     * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素不能使用两遍
     */
    public static void arraySum() {
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        for(int i=0;i<nums.length;i++){
            for(int j=i;j<nums.length;j++){
                if(nums[i]+nums[j]==target && i!=j){
                    System.out.println(i+""+j);
                }
            }
        }
        int a = -123;
        long b = 0;
        while(a!=0){
            b=b*10 + a%10;
            a= a/10;
        }
        System.out.println(b==(int)b ? (int)b : 0);
    }

    /**
     * 逢七过游戏
     */
    public static void gameSeven(){
        for (int i=0;i<100000;i++){
            if((i%7==0 || String.valueOf(i).indexOf("7")>=0) && ((i+1)%7==0 || String.valueOf((i+1)).indexOf("7")>=0) && ((i+2)%7==0 || String.valueOf((i+2)).indexOf("7")>=0)){
                System.out.println(i);
                System.out.println(i+1);
                System.out.println(i+2);
                break;
            }
        }
    }

}

