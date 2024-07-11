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
                betsConfirmed: [],
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

            if (data.action === 'fold') {
                game.currentBet[playerIndex] = -1;  // フォールドの場合は-1
            } else if (data.action === 'bet') {
                game.currentBet[playerIndex] = data.amount || 0;  // ベット額を保存
            } else if (data.action === 'confirm') {
                game.betsConfirmed.push(socket.id);
            }

            if (game.betsConfirmed.length === 2) {
                let result;
                const player1Hand = game.hands[game.player1.id][`hand${game.currentHandIndex + 1}`];
                const player2Hand = game.hands[game.player2.id][`hand${game.currentHandIndex + 1}`];

                const player1Sum = calculateHandSum(player1Hand);
                const player2Sum = calculateHandSum(player2Hand);

                if (game.currentBet[0] === -1) {
                    result = { winner: game.player2.id, chipsWon: game.currentBet[1], message: '相手がフォールドしました', player1Hand, player2Hand };
                } else if (game.currentBet[1] === -1) {
                    result = { winner: game.player1.id, chipsWon: game.currentBet[0], message: '相手がフォールドしました', player1Hand, player2Hand };
                } else if (player1Sum > player2Sum) {
                    result = { winner: game.player1.id, chipsWon: game.currentBet[1], message: 'あなたが勝ちました', player1Hand, player2Hand };
                } else if (player2Sum > player1Sum) {
                    result = { winner: game.player2.id, chipsWon: game.currentBet[0], message: 'あなたが負けました', player1Hand, player2Hand };
                } else {
                    result = { winner: null, chipsWon: 0, message: '引き分けです', player1Hand, player2Hand };
                }

                io.to(game.player1.id).emit('roundResult', result);
                io.to(game.player2.id).emit('roundResult', result);

                // 現在のベットをリセット
                game.currentBet = [null, null];
                game.betsConfirmed = [];
            } else {
                socket.emit('waiting', '相手のベットを待っています');
            }
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

function calculateHandSum(hand) {
    return hand.reduce((sum, card) => {
        if (card === 'A') return sum + 11; // Aは11としてカウント
        if (['J', 'Q', 'K'].includes(card)) return sum + 10;
        return sum + parseInt(card);
    }, 0);
}

server.listen(3000, () => {
    console.log('Server is running on port 3000');
});
