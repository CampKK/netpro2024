const express = require('express');
const http = require('http');
const { connect } = require('http2');
const socketIo = require('socket.io');

const app = express();
const server = http.createServer(app);
const io = socketIo(server);

let waitingPlayer = null;
let games = {};
let readyPlayers = {};
let handsConfirmedPlayers = {};
let playerHands = {};
let players = [];

app.use(express.static('public'));

io.on('connection', (socket) => {
    socket.on('joinGame', (name) => {
        socket.username = name;
        if (waitingPlayer === null) {
            waitingPlayer = { id: socket.id, name: name, points: 0 };
            socket.emit('waiting', '他のプレーヤーの参加を待っています');
        } else {
            const gameId = `game-${waitingPlayer.id}-${socket.id}`;
            games[gameId] = {
                player1: waitingPlayer,
                player2: { id: socket.id, name: name, points: 0 },
                hands: {},
                rounds: 0
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

            if (handsConfirmedPlayers[gameId].length === 2) {
                io.to(game.player1.id).emit('bothHandsConfirmed', game);
                io.to(game.player2.id).emit('bothHandsConfirmed', game);
                handsConfirmedPlayers[gameId] = [];
            }
        }
    });

    socket.on('battle', (data) => {
        const handSum = data.handSum;
        playerHands[socket.id] = handSum;

        players.push(socket.id);
        if (players.length === 2) {
            const [player1Id, player2Id] = players;
            const player1HandSum = playerHands[player1Id];
            const player2HandSum = playerHands[player2Id];
            let winner = '';

            const gameId = getGameIdByPlayerId(player1Id);
            const game = games[gameId];

            if (player1HandSum > player2HandSum) {
                winner = game.player1.name;
                game.player1.points += 3;
            } else if (player1HandSum < player2HandSum) {
                winner = game.player2.name;
                game.player2.points += 3;
            } else {
                winner = 'いません。引き分け';
            }

            const result = {
                message: `勝者は${winner}です。`,
                player1HandSum,
                player2HandSum,
                player1Name: game.player1.name,
                player2Name: game.player2.name,
                player1Points: game.player1.points,
                player2Points: game.player2.points,
                player1Id: game.player1.id, // 追加
                player2Id: game.player2.id  // 追加
            };

            io.to(player1Id).emit('battleResult', result);
            io.to(player2Id).emit('battleResult', result);

            playerHands = {};
            players = [];

            game.rounds += 1;
            console.log(`ラウンド ${game.rounds}`);
            if (game.rounds >= 5) {
                const finalWinner = game.player1.points > game.player2.points ? game.player1.name : game.player2.name;
                io.to(game.player1.id).emit('gameOver', { winner: finalWinner, player1Points: game.player1.points, player2Points: game.player2.points });
                io.to(game.player2.id).emit('gameOver', { winner: finalWinner, player1Points: game.player1.points, player2Points: game.player2.points });
            }
        }
    });

    socket.on('fold', (data) => {
        const gameId = getGameIdByPlayerId(socket.id);
        if (gameId) {
            const game = games[gameId];
            const opponentId = game.player1.id === socket.id ? game.player2.id : game.player1.id;
            const nonFoldPlayer = game.player1.id === socket.id ? game.player2 : game.player1;

            nonFoldPlayer.points += 1; // フォールドしていないプレイヤーのポイントを増やす

            io.to(opponentId).emit('opponentFolded', { handIndex: data.handIndex });
            socket.emit('playerFolded', { handIndex: data.handIndex });
            
            const result = {
                message: `相手がフォールドしました。`,
                player1Id: game.player1.id,
                player2Id: game.player2.id,
                player1Points: game.player1.points,
                player2Points: game.player2.points
            };
            io.to(game.player1.id).emit('updatePoints', result);
            io.to(game.player2.id).emit('updatePoints', result);
            io.to(game.player1.id).emit('updatePoints', result);
            io.to(game.player2.id).emit('updatePoints', result);

            game.rounds += 1;
            console.log(`ラウンド ${game.rounds}`);
            if (game.rounds >= 5) {
                const finalWinner = game.player1.points > game.player2.points ? game.player1.name : game.player2.name;
                io.to(game.player1.id).emit('gameOver', { winner: finalWinner, player1Points: game.player1.points, player2Points: game.player2.points });
                io.to(game.player2.id).emit('gameOver', { winner: finalWinner, player1Points: game.player1.points, player2Points: game.player2.points });
            }
        }
    });

    socket.on('sendMessage', (message) => {
        const gameId = getGameIdByPlayerId(socket.id);
        if (gameId) {
            const game = games[gameId];
            const sender = socket.username;
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