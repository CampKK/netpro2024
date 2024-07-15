const express = require('express');
const http = require('http');
const socketIo = require('socket.io');

const app = express();
const server = http.createServer(app);
const io = socketIo(server);

let waitingPlayer = null;
let games = {};
let readyPlayers = {};
let handsConfirmedPlayers = {};
let betsConfirmedPlayers = {};
let playerHands = {}; // プレイヤーのハンドの合計値を格納するオブジェクト
let players = []; // プレイヤーの配列

app.use(express.static('public'));

io.on('connection', (socket) => {
    socket.on('joinGame', (name) => {
        socket.username = name;  // ユーザー名を保存
        console.log(`Player ${name} joined the game`);

        if (waitingPlayer === null) {
            waitingPlayer = { id: socket.id, name: name, chips: 5 };
            socket.emit('waiting', '他のプレーヤーの参加を待っています');
        } else {
            const gameId = `game-${waitingPlayer.id}-${socket.id}`;
            games[gameId] = {
                player1: waitingPlayer,
                player2: { id: socket.id, name: name, chips: 5 },
                hands: {},
                currentBet: [null, null],
                waitingForBet: [],
                betsConfirmed: [false, false],
            };

            io.to(waitingPlayer.id).emit('gameStart', games[gameId]);
            io.to(socket.id).emit('gameStart', games[gameId]);

            waitingPlayer = null;
        }
    });

    socket.on('readyToStart', (hands) => {
        const gameId = getGameIdByPlayerId(socket.id);
        if (gameId) {
            readyPlayers[socket.id] = true;
            games[gameId].hands[socket.id] = hands;
            console.log(`Player ${socket.id} is ready with hands:`, hands);

            const game = games[gameId];
            const player1Ready = readyPlayers[game.player1.id];
            const player2Ready = readyPlayers[game.player2.id];

            if (player1Ready && player2Ready) {
                io.to(game.player1.id).emit('bothReady', { hands: games[gameId].hands });
                io.to(game.player2.id).emit('bothReady', { hands: games[gameId].hands });
                delete readyPlayers[game.player1.id];
                delete readyPlayers[game.player2.id];
            } else {
                socket.emit('waiting', '相手が準備完了するのを待っています');
            }
        }
    });

    socket.on('confirmHand', (data) => {
        const gameId = getGameIdByPlayerId(socket.id);
        if (gameId) {
            const game = games[gameId];
            if (!handsConfirmedPlayers[gameId]) handsConfirmedPlayers[gameId] = [];
            handsConfirmedPlayers[gameId].push(socket.id);
            console.log(`Player ${socket.id} confirmed hand:`, data);

            if (handsConfirmedPlayers[gameId].length === 2) {
                io.to(game.player1.id).emit('bothHandsConfirmed', game);
                io.to(game.player2.id).emit('bothHandsConfirmed', game);
                handsConfirmedPlayers[gameId] = [];
            }
        }
    });

    socket.on('bet', (data) => {
        const gameId = getGameIdByPlayerId(socket.id);
        if (gameId) {
            const game = games[gameId];
            const playerIndex = game.player1.id === socket.id ? 0 : 1;
            console.log(`Player ${socket.id} bet:`, data);

            game.currentBet[playerIndex] = { amount: data.amount, action: 'bet' };
            const opponentId = game.player1.id === socket.id ? game.player2.id : game.player1.id;
            io.to(opponentId).emit('opponentBet', { handIndex: data.handIndex, amount: data.amount });

            game.betsConfirmed[playerIndex] = true;

            if (game.betsConfirmed[0] && game.betsConfirmed[1]) {
                io.to(game.player1.id).emit('bothBetsConfirmed', game);
                io.to(game.player2.id).emit('bothBetsConfirmed', game);
                game.betsConfirmed = [false, false];  // リセット
            }
        }
    });

    socket.on('fold', (data) => {
        console.log('プレイヤーがこの勝負を降りました', data);
        const gameId = getGameIdByPlayerId(socket.id);
        if (gameId) {
            const game = games[gameId];
            const opponentId = game.player1.id === socket.id ? game.player2.id : game.player1.id;
            io.to(opponentId).emit('opponentFolded', { handIndex: data.handIndex });
            socket.emit('playerFolded', { handIndex: data.handIndex });
        }
    });

    socket.on('battle', (data) => {
        const handSum = data.handSum;
        playerHands[socket.id] = handSum; // プレイヤーのハンドの合計値を保存

        console.log(`プレイヤー ${socket.id} の合計値を受け取りました:`, handSum);
        players.push(socket.id);
        console.log('現在のプレイヤー配列:', players); // プレイヤー配列の状態をログに出力
        if (players.length === 2) {
            const [player1Id, player2Id] = players;
            const player1HandSum = playerHands[player1Id];
            const player2HandSum = playerHands[player2Id]
            let winner = ''; // 勝者を格納する変数
            // 勝者の判定
            if (player1HandSum > player2HandSum) {
                winner = 'プレーヤー1';
            } else if (player1HandSum < player2HandSum) {
                winner = 'プレーヤー2';
            } else {
                winner = '引き分け';
            }
            // 勝負の結果を作成
            const result = {
                message: `勝者は${winner}です。`, // 勝者のメッセージを作成
                player1HandSum,
                player2HandSum
            };
            console.log('送信する結果:', result);
            io.emit('battleResult', result);

            // ハンドの合計値とプレイヤー配列をリセット
            playerHands = {};
            players = [];
        }

    });

    socket.on('roundResult', (result) => {
        const gameId = getGameIdByPlayerId(socket.id);
        if (gameId) {
            const game = games[gameId];
            const player1Id = game.player1.id;
            const player2Id = game.player2.id;

            io.to(player1Id).emit('roundResult', result);
            io.to(player2Id).emit('roundResult', result);
        }
    });

    socket.on('sendMessage', (message) => {
        const gameId = getGameIdByPlayerId(socket.id);
        if (gameId) {
            const game = games[gameId];
            const sender = socket.username; // ユーザー名を取得
            io.to(game.player1.id).emit('receiveMessage', { sender, message });
            io.to(game.player2.id).emit('receiveMessage', { sender, message });
        }
    });

    socket.on('disconnect', () => {
        if (waitingPlayer && waitingPlayer.id === socket.id) {
            waitingPlayer = null;
        } else {
            const gameId = getGameIdByPlayerId(socket.id);
            if (gameId) {
                const game = games[gameId];
                const opponentId = game.player1.id === socket.id ? game.player2.id : game.player1.id;
                io.to(opponentId).emit('opponentDisconnected');

                delete games[gameId];
            }
        }
    });
});

function getGameIdByPlayerId(playerId) {
    for (let gameId in games) {
        if (games[gameId].player1.id === playerId || games[gameId].player2.id === playerId) {
            return gameId;
        }
    }
    return null;
}

server.listen(3000, () => {
    console.log('Server is running on port 3000');
});