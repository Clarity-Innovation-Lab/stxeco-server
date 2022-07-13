package eco.stx.edao.stacks.service;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.TypeAlias;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TypeAlias(value = "ClaritySerialiser")
public class ClaritySerialiser {

	@Autowired
	private ObjectMapper mapper;
	private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

	public String serialiseHexString(String hexString) {
		byte[] b = hexStringToByteArray(hexString);
		Integer[] output = new Integer[hexString.getBytes().length / 2];
		int counter = 0;
		for (byte by : b) {
			if (counter == output.length) break;
			Integer number = by & 0xff;
			output[counter] = number;
			counter++;
		}
		int[] output1 = new int[ (hexString.getBytes().length / 2) + 5];
		output1[0] = 2;
		output1[1] = 0;
		output1[2] = 0;
		output1[3] = 0;
		output1[4] = hexString.getBytes().length / 2;
		counter = 5;
		for (Integer number : output) {
			output1[counter] = number;
			counter++;
		}
		String content = convertToHex(output1);
		return "0x" + content;
	}
	
	public static long getUnsignedInt(int x) {
	    if(x > 0) return x;
	    long res = (long)(Math.pow(2, 32)) + x;
	    return res;
	}
	
    public static String convertToHex(int[] bytes) {
        StringBuilder result = new StringBuilder();
        for (int aByte : bytes) {
            result.append(String.format("%02x", aByte));
            // upper case
            // result.append(String.format("%02X", aByte));
        }
        return result.toString();
    }

	public byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}

	public String serialiseInt(BigInteger appCounter) {
		String s1 = String.format("%032x", appCounter);
		s1 = "0x00" + s1;
		return s1;
	}
	
	public String serialiseUInt(BigInteger appCounter) {
		String s1 = String.format("%032x", appCounter);
		s1 = "0x01" + s1;
		return s1;
	}

		
//		byte[] bytesValue = convertTwosCompliment(appCounter).toByteArray();
//		ByteBuffer bufferValue = ByteBuffer.allocate(16);
//		bufferValue.put(bytesValue);
//		byte[] val = appCounter.toByteArray();
//		for (int i=val.length-1; i>=0; i--) {
//			bufferValue.put(val[i]);
//		}
		// bufferValue.flip(); 0100000000000000000000000000000001

//	    byte[] bytesValue1 = appCounter.toByteArray();
//		ByteBuffer bufferValue1 = ByteBuffer.allocate(16);
//		bufferValue1.asIntBuffer();
//		bufferValue1.put(bytesValue1);

//		byte byteType = Integer.valueOf(0).byteValue();
//		ByteBuffer bufferType = ByteBuffer.allocate(1);
//		bufferType.asIntBuffer();
//		bufferType.put(byteType);
//		bufferType.flip();

//		ByteBuffer bufferCombo = combineBuffers(bufferType, bufferValue);
//		String bufferComboString = asHex(combineBuffers(bufferType, bufferValue));
//		ByteBuffer buf = ByteBuffer.allocate(17);
//		buf.put(b2.array());
//		buf.put(bufferType);
//		// byte ct = buf.get();
//		return ("0x" + asHex(buf));
		// return ("0x" + buf.asCharBuffer());
		// return = Hex.encode(bufferCombo1.array(/* charset */));
//	}

//	public String serialiseUInt(BigInteger appCounter) {
//
//		if (appCounter.compareTo(BigInteger.ZERO) < 0) {
//			appCounter = appCounter.add(BigInteger.ONE.shiftLeft(64));
//		}
//
////	    byte[] bytesValue = convertTwosCompliment(appCounter).toByteArray();
////		ByteBuffer bufferValue = ByteBuffer.allocate(16);
////		bufferValue.asIntBuffer();
////		bufferValue.put(bytesValue);
//
//		byte[] bytesValue1 = appCounter.toByteArray();
//		ByteBuffer bufferValue1 = ByteBuffer.allocate(16);
//		bufferValue1.asIntBuffer();
//		bufferValue1.put(bytesValue1);
//
//		byte byteType = Integer.valueOf(1).byteValue();
//		ByteBuffer bufferType = ByteBuffer.allocate(1);
//		bufferType.put(byteType);
//		bufferType.flip();
//
//		ByteBuffer bufferCombo = combineBuffers(bufferType, bufferValue1);
//		String bufferComboString = asHex(combineBuffers(bufferType, bufferValue1));
//		ByteBuffer bufferCombo1 = combineBuffers(bufferType, bufferValue1);
//		return ("0x" + asHex(bufferCombo1));
//		// return = Hex.encode(bufferCombo1.array(/* charset */));
//	}

	private String asHex(ByteBuffer bb) {
//		byte[] bytes = bb.array();
//	    char[] hexChars = new char[bytes.length * 2];
//	    for (int j = 0; j < bytes.length; j++) {
//	        int v = bytes[j] & 0xFF;
//	        hexChars[j * 2] = HEX_CHARS[v >>> 4];
//	        hexChars[j * 2 + 1] = HEX_CHARS[v & 0x0F];
//	    }
//	    return new String(hexChars);
		byte[] buf = bb.array();
	    char[] chars = new char[2 * buf.length];
	    for (int i = 0; i < buf.length; ++i)
	    {
	        chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
	        chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
	    }
	    return new String(chars);
    }

	public static BigInteger convertTwosCompliment(BigInteger appCounter) {
		if (appCounter.signum() == 1) {
			byte[] contents = appCounter.toByteArray();

			// prepend byte of opposite sign
			byte[] result = new byte[contents.length + 1];
			System.arraycopy(contents, 0, result, 1, contents.length);
			result[0] = (contents[0] < 0) ? 0 : (byte) -1;

			// this will be two's complement
			BigInteger result2 = new BigInteger(result);
			byte[] dd = result2.toByteArray();
			return result2;
		} else {
			return appCounter;
		}
	}

	private ByteBuffer combineBuffers(ByteBuffer bufType, ByteBuffer bufValue) {
		int length = bufType.capacity() + bufValue.capacity();
		ByteBuffer buffer = ByteBuffer.allocate(length);
		buffer.put(bufType);
		buffer.put(bufValue);
		return buffer;
	}
}
