package com.jec.protocol.unit;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Arrays;

@JsonSerialize(using = BCDSerializer.class)
public class BCD implements Serializable{
	
	
    final static char[] digitMap = {
    	'0' , '1' , '2' , '3' , '4' , 
    	'5' , '6' , '7' , '8' , '9' ,
        };
	
	
	byte[] digits;
	
	int count = 0;

	public BCD(int size) {
		digits = new byte[size];
	}
	
	public BCD() {
		this(32);
	}
	
	public int getDigitCount() {
		return count;
	}
	
	public int getDigit(int index) {
		return digits[index];
	}

	public void addDigit(int value) {
		
		int newcount = count + 1;
		if (newcount > digits.length) {
			digits = Arrays.copyOf(digits, Math.max(digits.length << 1, newcount));
		}
		
		digits[count] = (byte)(value & 0xF);
		count = newcount;
		
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + count;
		result = prime * result + Arrays.hashCode(digits);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BCD other = (BCD) obj;
		if (count != other.count)
			return false;
		if (!Arrays.equals(digits, other.digits))
			return false;
		return true;
	}
	
	/*
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj == null) {
			return false;
		}
		
		if(!(obj instanceof BCD)) {
			return false;
		}
		
		BCD bcd = (BCD)obj;
		if(this.count != bcd.count) {
			return false;
		}
				
        for (int i=0; i<count; i++) {
            if (digits[i] != bcd.digits[i]) {
                return false;
            }
        }
        
		return true;
	}
	*/
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < count; i++) {
			sb.append(digitMap[digits[i]]);
		}
		if(count == 0) {
			return "";
		}
		return sb.toString();
	}
	
	/**
	 * 从BCD对象得到byte[]
	 * @param length 指定byte[]的长度。如果BCD对象产生的byte数目超过指定长度，多余部分会被截除。
	 * @return
	 */
	public byte[] toBytes(int length) {
		
		byte[] data = new byte[length];
		Arrays.fill(data, (byte)0xff);
		
		for(int i = 0; i < length; i++) {
			
			boolean needBreak = false;
			
			byte b1 = 0x0f;
			byte b2 = 0x0f;
			
			if((i * 2) < count) {
				b1 = digits[i * 2];
			} else {
				needBreak = true; 
			}
			
			if((i * 2 + 1) < count) {
				b2 = digits[i * 2 + 1];
			} else {
				needBreak = true; 
			}
			
			data[i] = (byte)((b1 << 4) + b2);

			if(needBreak) {
				break;
			}
		}
		
		return data;
		
	}



	/**
	 * 判断字节串是否符合BCD标准
	 * @param data
	 * @param offset
	 * @param length
	 * @return <B>boolean</B>
	 */
	public static boolean isBCD(byte[] data, int offset, int length) {
		
		/*
		 * "F"标志,表示后面所有字节的高4位值和低四位值都为0xF
		 */
		boolean bF = false;
		
		for(int i = offset; i < offset+ length; i++) {
			
			if(bF) {
				
				/*
				 * bF被设置为true,表示后面所有的byte都必是0xFF
				 */
				
				if(data[i] != (byte)0xff)
					return false;
				
			} else {
				
				/*
				 * 取字节的高四位, 
				 * 如果0~9, 正常
				 * 如果0xF, 标志bF为true
				 * 如果其他,返回失败
				 */
				
				byte high = (byte)((data[i]&0xFF) >> 4);
				if(high >= 0 && high <= 9) {
					// 正常,不做任何事情
				} else if(high == (byte)0xf) {
					bF = true;
				} else {
					return false;
				}
				
				/*
				 * 取字节的低四位, 
				 */
				byte low = (byte)((data[i]&0xFF) & 0xf);
				if(bF) {
					
					/*
					 * 当前bF标志已经被置为true,
					 * 低四位的值必须为0xF
					 */
					
					if(low != (byte)0xf) {
						return false;
					}
					
				} else {
					
					/*
					 * 当前bF标志未被置为true,
					 * 如果0~9, 正常
					 * 如果0xF, 标志bF为true
					 * 如果其他,返回失败
					 */
					
					if(low >= 0 && low <= 9) {
						// 正常,不做任何事情
					} else if(low == (byte)0xf) {
						bF = true;
					} else {
						return false;
					}
					
				}
				
			}
			
			
		}
		
		return true;
		
	}
	
	/**
	 * 从字节串中得到BCD对象
	 * @param data
	 * @param offset
	 * @param length
	 * @return BCD 
	 */
	public static BCD fromBytes(byte[] data, int offset, int length) {
		
		if(!isBCD(data, offset, length)) {
			return null;
		}
		
		BCD bcd = new BCD();

		for(int i = offset; i < (offset + length); i++) {
			
			int temp;
			
			/*
			 * 取字节的高四位, 
			 * 如果0~9, 添加数字
			 * 如果0xF, 退出循环
			 * 如果其他,返回失败
			 */
				
			temp = (data[i]&0xFF) >> 4;
			if(temp >= 0 && temp <= 9) {
				bcd.addDigit(temp);
			} else if(temp == (byte)0xf) {
				break;
			} else {
				// 因为在函数前面进行了验证,所以这里不会进来
				return null;
			}
			
			/*
			 * 取字节的低四位,
			 * 如果0~9, 添加数字
			 * 如果0xF, 退出循环
			 * 如果其他,返回失败
			 */
			temp = data[i] & 0xf;
			if(temp >= 0 && temp <= 9) {
				bcd.addDigit(temp);
			} else if(temp == (byte)0xf) {
				break;
			} else {
				// 因为在函数前面进行了验证,所以这里不会进来
				return null;
			}	
			
		}
		
		return bcd;
		
	}
	
	public static BCD fromString(String bcdString) {
		if(bcdString == null || bcdString.isEmpty()) {
			return new BCD();
		}
		BCD bcd = new BCD();
		int length = bcdString.length();
		for(int i = 0; i < length; i++) {
			char c = bcdString.charAt(i);
			int v = Character.digit(c, 10);
			if(v < 0 || v > 9) {
				System.err.println("字符串中存在非数字字符！");
				return null;
			}
			bcd.addDigit(v);
		}
		return bcd;
	}
	
	/*
	public static void main(String[] arg) {
		String a = "123456789AAA";
		BCD bcd = BCD.fromString(a);
		if(bcd != null) {
			System.out.println(bcd.toString());
		}
	}
	*/

}
