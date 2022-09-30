import { tupleCV, stringUtf8CV, uintCV, stringAsciiCV } from "micro-stacks/clarity";
import { ChainID } from "micro-stacks/common";
//import type { Thread } from "../types/thread";
//import type { Reply } from "../types/reply";
import { verify_structured_data_signature } from "./structured-data.js";


export const domain = {
	name: process.env.PUBLIC_APP_NAME || 'Stacks Voice',
	version: process.env.PUBLIC_APP_VERSION || '1.0.0',
	'chain-id': process.env.PUBLIC_NETWORK === "mainnet" ? ChainID.Mainnet : ChainID.Testnet,
};

export const domainCV = tupleCV({
	name: stringAsciiCV(domain.name),
	version: stringAsciiCV(domain.version),
	'chain-id': uintCV(domain['chain-id']),
	});

export function message_to_tuple(message) {
	if ((message).subject) // message is Thread
		return tupleCV({
			subject: stringUtf8CV((message).subject),
			body: stringUtf8CV(message.body),
			timestamp: uintCV(message.timestamp)
		});
	return tupleCV({
		body: stringUtf8CV(message.body),
		timestamp: uintCV(message.timestamp),
		thread_id: stringAsciiCV(message._id)
	});
}

export function is_welformed_signed_message(message)
{
	console.log('is_welformed_signed_message: ', message);
	return message.body && message.timestamp && verify_signed_message(message);
}

export function verify_delete_action(message, signature)
	{
	if (!message.signature || !message.author)
		return false;
	const author = message.author // typeof message.author === "string" ? hexToBytes(message.author) : message.author;
	const sig = signature // typeof signature === "string" ? hexToBytes(signature) : signature;
	const is_reply = typeof message.reply_id !== "undefined";
	return verify_structured_data_signature(domainCV, delete_action_tuple(is_reply ? message.reply_id : message._id, is_reply), author, sig);
	}

export function delete_action_tuple(message_id, is_reply) {
	return tupleCV({
		action: stringAsciiCV("delete"),
		message_id: stringAsciiCV(message_id),
		kind: stringAsciiCV(is_reply ? "reply" : "thread")
	});
}

export function verify_signed_message(message) {
	if (!message.signature || !message.author)
		return false;
	const m = message_to_tuple(message);
	console.log('verify_signed_message: message=', m);
	console.log('verify_signed_message: domainCV=', domainCV);
	const author = message.author; //typeof message.author === "string" ? hexToBytes(message.author): message.author;
	const signature = message.signature; //typeof message.signature === "string" ? hexToBytes(message.signature): message.signature;
	return verify_structured_data_signature(domainCV, message_to_tuple(message), author, signature);
}

export function unserialise_message(message_json) {
	try {
		const message = message_json;
		//if (message.signature)
			//message.signature = hexToBytes(message.signature);
		//if (message.author)
			//message.author = hexToBytes(message.author);
		let result = { body: message.body, timestamp: message.timestamp, author: message.author, signature: message.signature };
		if (message.subject) // Thread
			result.subject = message.subject;
		else // Reply
			result._id = message._id;
		console.log('unserialise_message: message: ', message_json);
		return result;
	}
	catch (error)
	{
		console.log('thread err: ', error);
		return message_json;
	}
}