package com.evcar.subsidy.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 数学工具类，如随机数、幂计算等
 *
 * @author	shu
 */
public class MathUtil {

	/**
	 * 返回的范围是[0,num)
	 * @Title: random
	 * @param @param num
	 * @return int    返回类型
	 */
	public static int random(int num){
		//Math.random() 返回的范围是[0,1)
		return (int)(Math.random()*num);
	}
	
	/**
	 * 返回的范围是[start,end]
	 * @Title: random
	 * @param  start	范围起始值
	 * @param  end	范围终止值
	 * @return int    返回类型
	 */
	public static int random(int start,int end){
		return (int) (Math.random() * (end - start + 1)) + start;
	}
	
	/**
	 * 返回的几位数之间的值, length范围 [1,10], 超出范围 <br>
	 * 如length=1,返回[0, 10) 即0-9之间的值<br>
	 * 如length=2,返回[10, 100) 即10-99之间的值<br>
	 * 如length=3,返回[100, 1000) 即100-999之间的值<br>
	 * 
	 * @param @param length	几位数
	 * @return int
	 * @throws Exception 
	 */
	public static int randomMax10(int length) {
		int start = 0;
		int end = 0;
		try {
			if (length > 1 && length <= 10) {
				// 由于10^0 =1,所以单独处理
				start = (int) Math.pow(10, (length - 1));

			} else {
				throw new Exception("超过范围, length范围是[1,10]");
			}
			end = (int) Math.pow(10, length);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return (int) ((Math.random() * (end - start)) + start);

	}
	
	/**
	 * 返回的几位数之间的值, length范围 [1, +∞] <br>
	 * 如length=1,返回[0, 10) 即0-9之间的值<br>
	 * 如length=2,返回[10, 100) 即10-99之间的值<br>
	 * 如length=3,返回[100, 1000) 即100-999之间的值<br>
	 * 
	 * @param @param length	几位数
	 * @return int
	 * @throws Exception 
	 */
	public static String random2(int length){
		String randomId = "";
		int restLength = 0;
		if(length < 10){
			restLength = length;
		}else{
			int count = length / 10;
			for (int i = 0; i < count; i++) {
				randomId = randomId + MathUtil.randomMax10(10);
			}
			//加上不满长度为10的部分
			restLength = length - count * 10;
		}
		
		if(restLength != 0){
			randomId = randomId + MathUtil.randomMax10(restLength);
		}
		
		return randomId;
	}
	
	/**
	 * 显示某个范围内的质数
	 * @param range
	 * @return
	 */
	public static List<Integer> showPrimeList(int range){
		
		return showPrimeList(2, range);
	}
	
	/**
	 * 显示某个范围内的质数
	 * @param startRange
	 * @param endRange
	 * @return
	 */
	public static List<Integer> showPrimeList(int startRange, int endRange){
		List<Integer> primeList = new ArrayList<>();
		if (startRange < 2 || endRange < 2 ){
			System.out.println("求质数的范围range必须大于2！");
			return null;
		}
		
		for (int i = startRange; i <= endRange; i++) {
			if(isPrime(i)){
				primeList.add(i);
			}
		}
		
		return primeList;
	}

	/**
	 * 判断某个数是不是质数
	 * @param num
	 * @return
	 */
	public static boolean isPrime(int num) {
		boolean flag = true;
		//Math的sqrt方法用于求开方(
		for (int j = 2; j <= Math.sqrt(num); j++) {
			if (num % j == 0) {
				flag = false;
				break;// 跳出循环
			}
		}
		return flag;
	}
	
	/**
	 * 显示某个范围内的质数(第二种方法 效率太低)
	 * @param range
	 * @return
	 */
	@Deprecated
	private static List<Integer> showPrimeList2(int range){
		List<Integer> primeList = new ArrayList<>();
		long startTime =System.currentTimeMillis();
		for (int i = 2; i <= range; i++) {
			for (int j = 2; j <= i; j++) {
				if (i % j == 0 && i != j) {
					break;
				}
				// 输出所有在 i=k 且 i%k=0的数
				if (i % j == 0 && i == j) {
					primeList.add(i);
				}
			}
		}
		long endTime =System.currentTimeMillis();
		System.out.println(endTime - startTime);
		return primeList;
	}

}
