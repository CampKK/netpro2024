const express = require('express');
const http = require('http');
const socketIo = require('socket.io');

const app = express();
const server = http.createServer(app);
const io = socketIo(server);

let waitingPlayer = null;
let games = {};
let readyPlayers = {};
let players = {};

app.use(express.static('public'));

io.on('connection', (socket) => {
    socket.on('joinGame', (name) => {
        socket.username = name;

        if (waitingPlayer === null) {
            waitingPlayer = { id: socket.id, name: name, chips: 5 };
            socket.emit('waiting', '他のプレーヤーの参加を待っています');
        } else {
            const gameId = `game-${waitingPlayer.id}-${socket.id}`;
            games[gameId] = {
                player1: waitingPlayer,
                player2: { id: socket.id, name: name, chips: 5 },
                hands: [[], []],
                currentBet: [0, 0],
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

    socket.on('handSelected', (data) => {
        const playerId = socket.id;
        players[playerId] = data;

        if (Object.keys(players).length === 2) {
            const gameId = getGameIdByPlayerId(socket.id);
            const game = games[gameId];
            const player1 = game.player1;
            const player2 = game.player2;

            const opponentId = Object.keys(players).find(id => id !== playerId);
            const opponentData = players[opponentId];

            let result;
            if (data.folded) {
                result = {
                    winner: opponentId,
                    chipsWon: data.bet,
                    opponentFolded: true,
                    message: `${socket.username}がフォールドしました`
                };
            } else if (opponentData.folded) {
                result = {
                    winner: playerId,
                    chipsWon: opponentData.bet,
                    opponentFolded: true,
                    message: `${players[opponentId].username}がフォールドしました`
                };
            } else {
                // 勝敗判定ロジックを追加する
                result = determineWinner(playerId, opponentId, data, opponentData);
            }

            if (result.winner === playerId) {
                player1.chips += result.chipsWon;
                player2.chips -= result.chipsWon;
            } else {
                player2.chips += result.chipsWon;
                player1.chips -= result.chipsWon;
            }

            io.to(player1.id).emit('showdown', { yourHand: players[player1.id].hand, opponentHand: players[player2.id].hand, opponentFolded: players[player2.id].folded });
            io.to(player2.id).emit('showdown', { yourHand: players[player2.id].hand, opponentHand: players[player1.id].hand, opponentFolded: players[player1.id].folded });

            io.to(player1.id).emit('roundResult', result);
            io.to(player2.id).emit('roundResult', result);

            players = {};
        }
    });

    socket.on('bet', (handIndex) => {
        const gameId = getGameIdByPlayerId(socket.id);
        if (gameId) {
            const game = games[gameId];
            game.currentBet[handIndex] += 1;
            const opponentId = game.player1.id === socket.id ? game.player2.id : game.player1.id;
            io.to(opponentId).emit('opponentBet', handIndex);
        }
    });

    socket.on('fold', (handIndex) => {
        const gameId = getGameIdByPlayerId(socket.id);
        if (gameId) {
            const game = games[gameId];
            const opponentId = game.player1.id === socket.id ? game.player2.id : game.player1.id;
            io.to(opponentId).emit('opponentFold', handIndex);
        }
    });

    socket.on('roundComplete', () => {
        const gameId = getGameIdByPlayerId(socket.id);
        if (gameId) {
            const game = games[gameId];
            const result = {
                winner: game.player1.id, // 仮にプレイヤー1が勝者とします
                chipsWon: game.currentBet.reduce((a, b) => a + b, 0),
                message: 'ラウンドの勝者が決定しました'
            };
            io.to(game.player1.id).emit('roundResult', result);
            io.to(game.player2.id).emit('roundResult', result);
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

    socket.on('startGame', () => {
        const gameId = getGameIdByPlayerId(socket.id);
        if (gameId) {
            io.to(gameId).emit('gameStart', games[gameId]);
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

function determineWinner(player1Id, player2Id, player1Data, player2Data) {
    // 勝敗判定ロジックをここに追加します
    // 以下は仮のロジックです
    const player1Score = player1Data.hand.reduce((sum, card) => sum + parseInt(card), 0);
    const player2Score = player2Data.hand.reduce((sum, card) => sum + parseInt(card), 0);

    if (player1Score > player2Score) {
        return {
            winner: player1Id,
            chipsWon: player2Data.bet,
            opponentFolded: false,
            message: 'プレイヤー1が勝利しました'
        };
    } else {
        return {
            winner: player2Id,
            chipsWon: player1Data.bet,
            opponentFolded: false,
            message: 'プレイヤー2が勝利しました'
        };
    }
}

server.listen(3000, () => {
    console.log('Server is running on port 3000');
});
