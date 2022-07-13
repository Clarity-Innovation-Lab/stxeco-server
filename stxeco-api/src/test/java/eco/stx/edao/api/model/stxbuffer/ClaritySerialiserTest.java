package eco.stx.edao.api.model.stxbuffer;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import eco.stx.edao.stacks.service.ClaritySerialiser;
import eco.stx.edao.stacks.service.ClarityType;

class ClaritySerialiserTest {
	                                           //           0123456789012345678901234567890123456789
	// 0x${serializeCV(intCV(1)).toString('hex') <= 2 =>  0x0000000000000000000000000000000001
	// 0x${serializeCV(uintCV(1)).toString('hex') <= 2 => 0x0100000000000000000000000000000001
	// 0x${serializeCV(intCV(2)).toString('hex') <= 2 =>  0x0000000000000000000000000000000002
	// 0x${serializeCV(uintCV(2)).toString('hex') <= 2 => 0x0100000000000000000000000000000002

	// 2000 = 00000000000000000000000000000007d0
	     // 0x00000000000000000000000000000007d0
	     // 0x00000000000000000000000000000007d0
	     // 0x01000000000000000000000000001e8480
	
	private String serialed = "0x02000000201c0b7b80e72eebf0f60a3ba6c53a99ba7bd5ef2ac4fec3be3e659fd1d6a1b44b"; 
	
	static ClaritySerialiser cs = new ClaritySerialiser();
	ClarityType ct = null;

	@Test
	void test_getSerialisedHash() {
		String arg1 = cs.serialiseHexString("1c0b7b80e72eebf0f60a3ba6c53a99ba7bd5ef2ac4fec3be3e659fd1d6a1b44b");
		assertTrue(arg1.equals(serialed));
	}

	@Test
	void test_getSerialisedInt() {
		String arg1 = cs.serialiseInt(BigInteger.valueOf(1));
		assertTrue(arg1.equals("0x0000000000000000000000000000000001"));
		arg1 = cs.serialiseInt(BigInteger.valueOf(2000));
		assertTrue(arg1.equals("0x00000000000000000000000000000007d0"));
		arg1 = cs.serialiseInt(BigInteger.valueOf(2000000));
		assertTrue(arg1.equals("0x00000000000000000000000000001e8480"));
	}
	@Test
	void test_getSerialisedUInt() {
		String arg1 = cs.serialiseUInt(BigInteger.valueOf(1));
		assertTrue(arg1.equals("0x0100000000000000000000000000000001"));
		arg1 = cs.serialiseUInt(BigInteger.valueOf(2000));
		assertTrue(arg1.equals("0x01000000000000000000000000000007d0"));
		arg1 = cs.serialiseUInt(BigInteger.valueOf(2000000));
		assertTrue(arg1.equals("0x01000000000000000000000000001e8480"));
	}
}
