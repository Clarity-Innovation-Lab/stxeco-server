import { serializeCV } from "micro-stacks/clarity";
import { sha256 } from "@noble/hashes/sha256";
import { bytesToHex, concatByteArrays } from "micro-stacks/common";
import { verifyMessageSignature } from "micro-stacks/connect";

const prefix = Uint8Array.from([0x53, 0x49, 0x50, 0x30, 0x31, 0x38]); // SIP018

export function hash_cv(clarityValue)
	{
	return sha256(serializeCV(clarityValue));
	}

export function structured_data_hash(domain, message)
	{
	return sha256(concatByteArrays([prefix, hash_cv(domain), hash_cv(message)]));
	}

export function verify_structured_data_signature(domain, message, public_key, signature)
	{
	return verifyMessageSignature({
		message: structured_data_hash(domain, message),
		signature: signature, // bytesToHex(signature),
		publicKey: public_key //bytesToHex(public_key)
		});
	}
