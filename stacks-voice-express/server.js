import { verify_delete_action, unserialise_message, is_welformed_signed_message } from "./lib/messages.js";
//import type { Thread } from "./types/thread";
//import type { Reply } from "./types/reply";
import express from "express";
import mongoose from "mongoose";
import cors from "cors";
import dotenv from 'dotenv'
import crypto from 'crypto'

dotenv.config();
//const express = require('express');
//const mongoose = require("mongoose");
//var cors = require('cors');

const PORT = 8080;
//const client = mongodb.MongoClient;
const HOST = "0.0.0.0";
const tolerance = Number(process.env.TIMESTAMP_TOLERANCE) || 120000;

console.log('PUBLIC_APP_VERSION:' + process.env.PUBLIC_APP_VERSION)
console.log('PUBLIC_APP_NAME:' + process.env.PUBLIC_APP_NAME)
console.log('TIMESTAMP_TOLERANCE:' + process.env.TIMESTAMP_TOLERANCE)
/**
client.connect(config.DB, function(err, db) {
    if(err) {
        console.log('database is not connected')
    }
    else {
        console.log('connected!!')
    }
});
**/
const mongoDB = "mongodb://mongodb:27017/edaosv";
mongoose.connect(mongoDB, { useNewUrlParser: true, useUnifiedTopology: true });

// Get the default connection
const db = mongoose.connection;

// Bind connection to error event (to get notification of connection errors)
db.on("error", console.error.bind(console, "MongoDB connection error:"));
// Define a schema
const Schema = mongoose.Schema;
const ReplySchema = new Schema({
	reply_id: 'string',
	body: 'string',
	timestamp: 'string',
	signature: 'string',
	author: 'string'
});
const ThreadSchema = new Schema({
	subject: 'string',
	body: 'string',
	timestamp: { type : Number , required : true },
	author: 'string',
	signature: 'string',
  replies: {
    type: [ReplySchema],
    required: false
  }
});
// Compile model from schema
const ThreadModel = mongoose.model("Thread", ThreadSchema);
const ReplyModel = mongoose.model("Reply", ReplySchema);

function runAsyncWrapper(callback) {
  return function (req, res, next) {
    callback(req, res, next).catch((err) => {
      if (err && typeof err === "object" && err !== null) {
        // console.log(Object.keys(err));
        if (err.response) {
          console.log(err.response.data);
          res.status(500).send(err.response.data);
        } else {
          console.log(err);
          res.status(500).send(err);
        }
      } else {
        console.log("error not object: " + err);
        res.status(500).send(err);
      }
    });
  };
}

function handleDbErr(err, res, msg) {
  console.log(err);
  res.status(500).send({ error: msg });
  return;
}

// App
const app = express();
app.use(express.json());
app.use(cors());

app.get("/", (req, res) => {
  res.send("hi there...");
});

app.get("/svapi/v1/thread/:thread_id", (req, res) => {
  ThreadModel.findById(req.params.thread_id, (err, threads) => {
    if (err) return handleDbErr(err, res, 'error getting thread');
    res.send(threads);
  });
});
app.get("/svapi/v1/replies/:thread_id", (req, res) => {
  ReplyModel.find({ thread_id: req.params.thread_id }, (err, threads) => {
    if (err) return handleDbErr(err, res, 'error getting replies');
    res.send(threads);
  });
});
app.get("/svapi/v1/author/:public_key", (req, res) => {
  ThreadModel.find({ author: req.params.public_key }, (err, threads) => {
    ThreadModel.find({ 'replies.author': req.params.public_key }, (err, replies) => {
      console.log('threads: ', threads);
      console.log('replies: ', replies);
      if (err) return handleDbErr(err, res, 'error getting author');
      res.send({ threads, replies });
    });
  });
});
app.get("/svapi/v1/threads", (req, res) => {
  console.log('/svapi/v1/threads');
  ThreadModel.find((err, threads) => {
    if (err) return handleDbErr(err, res, 'error getting threads');
    // console.log('thread before: ', threads);
    res.send(threads);
  }).sort({ timestamp: -1 });
});
app.post("/svapi/v1/thread",
  runAsyncWrapper(async (req, res) => {
    const message = unserialise_message(req.body);
    if (!message)
      return handleDbErr('invalid_message', res, 'invalid_message');
    if (!message.subject || !is_welformed_signed_message(message))
      return handleDbErr('invalid_signature', res, 'invalid_signature');
    const now = Date.now();
    if (message.timestamp < now - tolerance || message.timestamp > now + tolerance)
      return handleDbErr('invalid_timestamp', res, 'invalid_timestamp');

    ThreadModel.create(message, (err, thread) => {
      if (err) return handleDbErr(err, res, 'post_failed');
      res.send(thread);
    });
  })
);
app.post("/svapi/v1/thread/:thread_id/post",
  runAsyncWrapper(async (req, res) => {
    const message = unserialise_message(req.body);
    if (!message)
      return handleDbErr('invalid_message', res, 'invalid_message');
    if (!is_welformed_signed_message(message))
      return handleDbErr('invalid_signature', res, 'invalid_signature');
    const now = Date.now();
    if (message.timestamp < now - tolerance || message.timestamp > now + tolerance)
      return handleDbErr('invalid_timestamp', res, 'invalid_timestamp');

    ThreadModel.findById(req.params.thread_id, (err, thread) => {
      if (err) return handleDbErr(err, res, 'findById');
      if (!thread.signature) return handleDbErr('thread_deleted', res, 'thread_deleted');
      if (!thread.replies) thread.replies = [];
      message.reply_id = crypto.randomBytes(16).toString("hex");
      thread.replies.push(message);
      ThreadModel.updateOne({
        "_id": req.params.thread_id
      },
      {
        "$push": {
          "replies": message
        }
      }, (err, thread) => {
        if (err) return handleDbErr(err, res, 'post_reply_failed');
        res.send(thread);
      });
    });
  })
);
app.delete("/svapi/v1/reply/:thread_id/:reply_id",
  runAsyncWrapper(async (req, res) => {
    const delete_data = req.body;
    ThreadModel.findById(req.params.thread_id, (err, thread) => {
      if (err) return handleDbErr(err, res, 'findById');
      const toVerify = {
        _id: req.params.thread_id,
        author: thread.author,
        signature: thread.signature,
        reply_id: req.params.reply_id
      }
      if (!verify_delete_action(toVerify, delete_data.signature)) {
        console.log('sig not verified: ' + delete_data.signature);
        return {status: 400, body: { error: 'invalid_signature' }};
      }
      console.log('sig verified');
      ThreadModel.updateOne(
        { _id: req.params.thread_id, 'replies.reply_id': req.params.reply_id, },
        { $set: { 'replies.$.body': null, 'replies.$.signature': null } }, (err, thread) => {
          if (err) return handleDbErr(err, res, 'delete_reply_failed');
          res.send(thread);
      });
    });
  })
);
app.delete("/svapi/v1/thread/:thread_id",
  runAsyncWrapper(async (req, res) => {
    const delete_data = req.body;
    ThreadModel.findById(req.params.thread_id, (err, thread) => {
      if (err) return handleDbErr(err, res, 'findById');
      console.log('located thread: ', thread);
      thread._id = req.params.thread_id;
      const toVerify ={
        _id: req.params.thread_id,
        author: thread.author,
        signature: thread.signature
      }
      if (!verify_delete_action(toVerify, delete_data.signature)) {
        return {status: 400, body: { error: 'invalid_signature' }};
      }
      console.log('sig verified');
      ThreadModel.updateOne({
        "_id": req.params.thread_id
      },
      {$set: { body: null, signature: null }}, (err, thread) => {
        if (err) return handleDbErr(err, res, 'delete_thread_failed');
        console.log('updated thread: ', thread);
        res.send(thread);
      });
    });
  })
);
app.get('*', function(req, res) {
  res.status(404).send({ error: 'Nothing here for that request' });
});

app.listen(PORT, HOST);
console.log(`Running db ${db}\n`);
console.log(`Running on http://${HOST}:${PORT}\n\n`);
