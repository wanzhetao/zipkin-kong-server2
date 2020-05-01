package com.vispractice.zipkin;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class UtilId {

	  /**
     * 生成16位不重复的随机数，含数字+大小写
     * @return
     */
    public static String getGUID() {
    	StringBuilder uid = new StringBuilder();
        //产生16位的强随机数
        Random rd = new SecureRandom();
        for (int i = 0; i < 16; i++) {
            //产生0-2的3位随机数
            int type = rd.nextInt(3);
            switch (type){
                case 0:
                    //0-9的随机数
                    uid.append(rd.nextInt(10));
                	/*int random = ThreadLocalRandom.current().ints(0, 10)
                	.distinct().limit(1).findFirst().getAsInt();*/
                    break;
                case 1:
                    //ASCII在65-90之间为大写,获取大写随机
                    uid.append((char)(rd.nextInt(25)+65));
                    break;
                case 2:
                    //ASCII在97-122之间为小写，获取小写随机
                    uid.append((char)(rd.nextInt(25)+97));
                    break;
                default:
                    break;
            }
        }
        return uid.toString();
    }

    public static String getHex16()
    {
    	long ti=System.currentTimeMillis();
    	int re=Math.abs(new Random().nextInt());
    	int per=new Random().nextInt(10);
    	String pp=per+"001010200200";
    	long cu=Long.parseLong(pp);
    	String value =cu+ti+re+"";
    	String pre=Integer.toHexString(new Random().nextInt(16));
    	return pre+Long.toHexString(Long.parseLong(value));
    }

    public static String randomHexString(int len)  {
		try {
			StringBuffer result = new StringBuffer();
			for(int i=0;i<len;i++) {
				result.append(Integer.toHexString(new Random().nextInt(16)));
			}
			return result.toString();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
		return null;

	}

    /**
	 * @param md5L16
	 * @return
	 * @Date:2014-3-18
	 * @Author:lulei
	 * @Description: 将16位的md5转化为long值
	 */
	public static long parseMd5L16ToLong(String md5L16){
		if (md5L16 == null) {
			throw new NumberFormatException("null");
		}
		md5L16 = md5L16.toLowerCase();
		byte[] bA = md5L16.getBytes();
		long re = 0L;
		for (int i = 0; i < bA.length; i++) {
			//加下一位的字符时，先将前面字符计算的结果左移4位
			re <<= 4;
			//0-9数组
			byte b = (byte) (bA[i] - 48);
			//A-F字母
			if (b > 9) {
				b = (byte) (b - 39);
			}
			//非16进制的字符
			if (b > 15 || b < 0) {
				throw new NumberFormatException("For input string '" + md5L16);
			}
			re += b;
		}
		return re;
	}

	/**
	 * @param str16
	 * @return
	 * @Date:2014-3-18
	 * @Author:lulei
	 * @Description: 将16进制的字符串转化为long值
	 */
	public static long parseString16ToLong(String str16){
		if (str16 == null) {
			throw new NumberFormatException("null");
		}
		//先转化为小写
		str16 = str16.toLowerCase();
		//如果字符串以0x开头，去掉0x
		str16 = str16.startsWith("0x") ? str16.substring(2) : str16;
		if (str16.length() > 16) {
			throw new NumberFormatException("For input string '" + str16 + "' is to long");
		}
		return parseMd5L16ToLong(str16);
	}


}
