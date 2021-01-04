// FCM
const admin = require('firebase-admin')

const serverKeyPath = './fcmtest-4071d-firebase-adminsdk-yfu5b-e27b312dc1.json'
let serAccount = require(serverKeyPath)

admin.initializeApp({
  credential: admin.credential.cert(serAccount),
})

// File
const fs = require('fs')
const fileName = 'token.txt'

function openFile(flag, block) {
  console.log('open file');
  fs.open(fileName, flag, function (err, fd) {
    if (err) throw err;
    console.log('file open complete');
    block();
    fs.close(fd, function () {
      console.log('close file');
    });
  });
}

function readFile() {
  openFile('a+', function () {
    fs.readFile(fileName, 'utf8', function (err, data) {
      console.log('read token: ' + data);
      pushMessage(data);
    });
  });
}

function writeFile(token) {
  openFile('w+', function () {
    fs.writeFile(fileName, token, 'utf8', function (err) {
      if (err) throw err;
      console.log('write token: ' + token);
    })
  });
}

// Server
const express = require("express")
const server = express()

server.post('/', (req, res) => {
  let token = req.query.token;
  writeFile(token);
  res.end();
})

server.get('/push', (req, res) => {
  readFile();
  res.end();
})

server.listen(3000, () => {
  console.log("The server is running on Port 3000")
});

function pushMessage(token) {
  let fcm_message = {
    notification: {
      title: '시범 데이터 발송',
      body: '클라우드 메시지 전송이 잘 되는지 확인하기 위한, 메시지 입니다.'
    },
    data: {
      fileno: '44',
      style: 'good 입니다요~'
    },
    token: token
  };

  // 메시지를 보내는 부분 입니다.
  admin.messaging().send(fcm_message)
    .then(function (response) {
      console.log('보내기 성공 메시지:' + response);
    })
    .catch(function (error) {
      console.log('보내기 실패 메시지:' + error);
    });
}