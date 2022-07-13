package eco.stx.edao.stacks.service;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.TypeAlias;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eco.stx.edao.stacks.model.stxbuffer.StacksResponse;
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
@TypeAlias(value = "ClarityDeserialiser")
public class ClarityDeserialiser {

    private static final Logger logger = LogManager.getLogger(ClarityDeserialiser.class);
	@Autowired private ObjectMapper mapper;

	public Map<String, Object> deserialise(String mapKey, String jsonResp) throws JsonMappingException, JsonProcessingException {
		ByteBuffer buf = stripHexString(jsonResp);
		Map<String, Object> data = new HashMap<String, Object>();
		if (buf != null) {
			int okay = Byte.toUnsignedInt(buf.get());
			if (okay == ClarityTypes.ResponseErr.ordinal()) {
				// get the error
			} else if (okay == ClarityTypes.ResponseOk.ordinal()) {
				// okay - but get-base-token-uri returns the message without a response code!
				// so just read the next byte and carry ion from there..
				okay = Byte.toUnsignedInt(buf.get());
			}
			boolean remaining = true;
			while (remaining) {
				Object o = deserializeCV(okay, buf);
				data.put(mapKey, o);
				if (okay > ClarityTypes.values().length) {
					logger.warn("Clarity type overrun at buffer position=" + buf.position() + " with mapKey=" + mapKey + " and okay=" + okay);
					throw new RuntimeException("Clarity value overrun..");
				}
				remaining = buf.hasRemaining();
				if (remaining) {
					okay = Byte.toUnsignedInt(buf.get());
					while (okay == 0) {
						remaining = buf.hasRemaining();
						if (!remaining) {
							break;
						}
						okay = Byte.toUnsignedInt(buf.get());
					}
				}
			}
		}
		return data;
	}

	public Object deserializeCV(int ctype, ByteBuffer buf) {
		List<Object> ctList = new ArrayList<Object>();
		for (ClarityTypes type : ClarityTypes.values()) {
			if (type.ordinal() == ctype) {
				if (type == ClarityTypes.Int) {
					return readInt(ctype, buf);
				} else if (type == ClarityTypes.UInt) {
					return readUInt(ctype, buf);
				} else if (type == ClarityTypes.Buffer) {
					ClarityType clarityType = readBufferAsContent(buf, false);
					return clarityType;
				} else if (type == ClarityTypes.BoolTrue) {
					return readUInt(ctype, buf);
				} else if (type == ClarityTypes.BoolFalse) {
					return readUInt(ctype, buf);
				} else if (type == ClarityTypes.PrincipalStandard) {
					return readPrincipalStandard(buf);
				} else if (type == ClarityTypes.PrincipalContract) {
					return readPrincipalContract(ctype, buf);
				} else if (type == ClarityTypes.OptionalNone) {
					return new ClarityType(ctype);
				} else if (type == ClarityTypes.OptionalSome) {
					int next = Byte.toUnsignedInt(buf.get());
					return deserializeCV(next, buf);
				} else if (type == ClarityTypes.List) {
					return readList(ctype, buf);
				} else if (type == ClarityTypes.Tuple) {
					return readTuple(ctype, buf);
				} else if (type == ClarityTypes.StringASCII) {
					return readBufferAsContent(buf, false);
				} else if (type == ClarityTypes.StringUTF8) {
					return readBufferAsContent(buf, false);
				}
			}
		}
		return ctList;
	}
	
	private ClarityType readPrincipalStandard(ByteBuffer buf) {
		int bvl = Byte.toUnsignedInt(buf.get());
		byte[] bytes = new byte[20];  // number of values in the tuple
		buf.get(bytes);
		String hexString = convertToHex(bytes);
		return new ClarityType(ClarityTypes.PrincipalStandard.ordinal(), bvl, null, hexString);
	}

	private ClarityType readPrincipalContract(int numbBytes, ByteBuffer buf) {
		ClarityType ctContractAddress = readPrincipalContractAddress(buf);
		// ClarityType ctContractName = readBufferAsContent(buf, false);
		ClarityType ctContractName = readPrincipalContractName(numbBytes, buf);
		return new ClarityType(ClarityTypes.PrincipalContract.ordinal(), 0, ctContractAddress, ctContractName );
	}

	private ClarityType readPrincipalContractName(int numbBytes, ByteBuffer buf) {
		int bvl = Byte.toUnsignedInt(buf.get());
		byte[] bytes = new byte[bvl];  // number of values in the tuple
		buf.get(bytes);
		String hexString = convertToString(bytes);
//		ByteBuffer buf2 = ByteBuffer.allocate(bytes.length);
//		buf2.asIntBuffer();
//		buf2.put(bytes);
//		buf2.flip();
//		String content = StandardCharsets.UTF_8.decode(buf2).toString();
		return new ClarityType(ClarityTypes.Buffer.ordinal(), bvl, hexString, hexString);
	}

	private ClarityType readPrincipalContractAddress(ByteBuffer buf) {
		int bvl = Byte.toUnsignedInt(buf.get());
		byte[] bytes = new byte[20];  // number of values in the tuple
		buf.get(bytes);
		String hexString = convertToHex(bytes);
		return new ClarityType(ClarityTypes.PrincipalStandard.ordinal(), bvl, null, hexString);
	}

	private void readOptionalSome(int numbBytes, ByteBuffer buf) {
		return;
	}

	private String convertToString(byte[] str) {
		ByteBuffer buf2 = ByteBuffer.allocate(str.length);
		buf2.asIntBuffer();
		buf2.put(str);
		buf2.flip();
		if (str.length > 1000000) {
			throw new RuntimeException("Buffers must be less than 1M");
		}
		return StandardCharsets.UTF_8.decode(buf2).toString();
	}

	private Map<String, Object> readTuple(int ctype, ByteBuffer buf) {
		Map<String, Object> tuple = new HashMap();
		int tupleSize = buf.getInt();
		String key = null;
		Object val = null;
		for (int i = 0; i < tupleSize; i++) {
			// 1. get type 2. get key, 3. get value
//			int bvl = Byte.toUnsignedInt(buf.get());
//			while (bvl == 0) {
//				bvl = Byte.toUnsignedInt(buf.get());
//			}
			ClarityType clar = readBufferAsContent(buf, true);
			key = (String)clar.getValue();
			int ct = Byte.toUnsignedInt(buf.get());
			if (key.contentEquals("asset-hash")) {
				clar = (ClarityType) deserializeCV(ct, buf);
				tuple.put(key, clar);
			} else {
				tuple.put(key, deserializeCV(ct, buf));
			}
//			if (ct == 2) {
//				bvl = buf.getInt();
//				val = readBufferAsContent(bvl, buf);
//			} else if (ct == 0) {
//				val = convertToInt(buf, 2);
//			}
//			ClarityType clarityType = new ClarityType(ct, val);
//			tuple.put(key, clarityType);
		}
		return tuple;
	}
	
	private List<ClarityType> readList(int ctype, ByteBuffer buf) {
		List<ClarityType> list = new ArrayList<>();
		int listLength = buf.getInt();
		for (int i = 0; i < listLength; i++) {
			int ct = Byte.toUnsignedInt(buf.get());
			ClarityType clar = (ClarityType) deserializeCV(ct, buf);
			list.add(clar);
		}
		return list;
	}
	
	private ClarityType readBufferAsContent(ByteBuffer buf, boolean keyName) {
		int bvl = 0;
		if (keyName) {
			bvl = Byte.toUnsignedInt(buf.get());
		} else {
			bvl = buf.getInt();
		}
		byte[] bytes = new byte[bvl];
		buf.get(bytes);
		ByteBuffer buf2 = ByteBuffer.allocate(bytes.length);
		buf2.asIntBuffer();
		buf2.put(bytes);
		buf2.flip();
		if (bytes.length > 1000000) {
			throw new RuntimeException("Buffers must be less than 1M");
		}
		String content1 = convertToHex(bytes);
		String content = StandardCharsets.UTF_8.decode(buf2).toString();
		//return new BufferString(content, content1);
		return new ClarityType(ClarityTypes.Buffer.ordinal(), content, content1);
	}
		
	private ClarityType readUInt(int ctype, ByteBuffer buf) {
		byte[] bytes2 = new byte[16];
		if ((buf.limit() - buf.position()) >= 16) {
			buf.get(bytes2).array();
			//return new ClarityType(ctype, ClaritySerialiser.convertTwosCompliment(new BigInteger(bytes2)));
			return new ClarityType(ctype, new BigInteger(bytes2));
		}

		return null;
	}
	
	private ClarityType readInt(int ctype, ByteBuffer buf) {
		byte[] bytes2 = new byte[16];
		buf.get(bytes2).array();
		return new ClarityType(ctype, new BigInteger(bytes2));
	}
	
	public ByteBuffer stripHexString(String jsonResp) throws JsonMappingException, JsonProcessingException {
		StacksResponse sr = mapper.readValue(jsonResp, new TypeReference<StacksResponse>() {});
		if (sr.getOkay().equals("false")) {
			return null;
		}
		byte[] bytie = decodeHexString(sr.getResultWithoutPrefix());
		ByteBuffer buf = ByteBuffer.allocate(bytie.length);
		buf.asIntBuffer();
		buf.put(bytie);
		buf.flip();
		return buf;
	}

    public static String convertToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(String.format("%02x", aByte));
            // upper case
            // result.append(String.format("%02X", aByte));
        }
        return result.toString();
    }

    public byte[] decodeHexString(String hexString) {
	    if (hexString.length() % 2 == 1) {
	        throw new IllegalArgumentException(
	          "Invalid hexadecimal String supplied.");
	    }
	    
	    byte[] bytes = new byte[hexString.length() / 2];
	    for (int i = 0; i < hexString.length(); i += 2) {
	        bytes[i / 2] = hexToByte(hexString.substring(i, i + 2));
	    }
	    return bytes;
	}
	
	private byte hexToByte(String hexString) {
	    int firstDigit = toDigit(hexString.charAt(0));
	    int secondDigit = toDigit(hexString.charAt(1));
	    return (byte) ((firstDigit << 4) + secondDigit);
	}
	
	private int toDigit(char hexChar) {
	    int digit = Character.digit(hexChar, 16);
	    if(digit == -1) {
	        throw new IllegalArgumentException(
	          "Invalid Hexadecimal Character: "+ hexChar);
	    }
	    return digit;
	}
}
