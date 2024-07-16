const socket = io();

document.addEventListener('DOMContentLoaded', () => {
    const lobbyContainer = document.getElementById('lobby');
    const setupContainer = document.getElementById('setup');
    const waitingContainer = document.getElementById('waiting');
    const gameContainer = document.getElementById('game');
    const cardContainer = document.getElementById('cards');
    const battleButton = document.getElementById('battle');
    const foldButton = document.getElementById('foldButton');
    const showdownContainer = document.getElementById('showdown');
    const gametimeContainer = document.getElementById('gametime');
    const foldContainer = document.getElementById('fold');
    const yourSelectedHandContainer = document.getElementById('yourSelectedHandContainer');
    const opponentHandContainer = document.getElementById('opponentHandContainer');
    const yourSelectedHandSumContainer = document.getElementById('yourSelectedHandSum');
    const handContainers = {
        hand1: document.getElementById('hand1Container'),
        hand2: document.getElementById('hand2Container'),
        hand3: document.getElementById('hand3Container'),
        hand4: document.getElementById('hand4Container'),
        hand5: document.getElementById('hand5Container')
    };
    const handSums = {
        hand1: document.getElementById('hand1Sum'),
        hand2: document.getElementById('hand2Sum'),
        hand3: document.getElementById('hand3Sum'),
        hand4: document.getElementById('hand4Sum'),
        hand5: document.getElementById('hand5Sum')
    };

    const chatInput = document.getElementById('chatInput');
    const chatSend = document.getElementById('chatSend');
    const chatMessages = document.getElementById('chatMessages');
    const pointsContainer = document.getElementById('points'); // ポイント表示要素

    let selectedHandIndex = null;
    let points = 0; // 初期ポイント
    let usedHands = []; // 使用済みハンドのリスト

    function openModal() {
        var modal = document.getElementById('checkrulesModal');
        modal.style.display = 'block';
    }

    function closeModal() {
        var modal = document.getElementById('checkrulesModal');
        modal.style.display = 'none';
    }

    var openModalBtn = document.getElementById('checkrules');
    openModalBtn.addEventListener('click', openModal);

    var closeModalBtn = document.getElementsByClassName('close')[0];
    closeModalBtn.addEventListener('click', closeModal);

    window.addEventListener('click', function (event) {
        var modal = document.getElementById('checkrulesModal');
        if (event.target == modal) {
            closeModal();
        }
    });

    document.getElementById('startgame').addEventListener('click', () => {
        const name = prompt('名前を入力してください:');
        if (name) {
            socket.emit('joinGame', name);
        }
    });

    socket.on('waiting', (message) => {
        document.getElementById('waitingMessage').textContent = message;
        lobbyContainer.style.display = 'none';
        waitingContainer.style.display = 'block';
    });

    socket.on('gameStart', (gameState) => {
        lobbyContainer.style.display = 'none';
        waitingContainer.style.display = 'none';
        setupContainer.style.display = 'block';
    });

    const cardValues = ['A', '2', '3', '4', '5', '6', '7', '8', '9', '10', 'J', 'Q', 'K', '0', '0'];
    cardValues.forEach(value => {
        const card = document.createElement('div');
        card.classList.add('card');
        card.textContent = value;
        card.dataset.value = (value === 'A' ? '1' : (['J', 'Q', 'K'].includes(value) ? '10' : value));
        card.addEventListener('click', () => {
            card.classList.toggle('selected');
        });
        cardContainer.appendChild(card);
    });

    Object.keys(handContainers).forEach(hand => {
        document.getElementById(hand).addEventListener('click', () => {
            const selectedCards = document.querySelectorAll('.card.selected');
            if (selectedCards.length === 3) {
                let sum = 0;
                const newCards = [];
                selectedCards.forEach(card => {
                    if (card.textContent === 'A') {
                        const aceValue = prompt('Aを1としてカウントしますか？11としてカウントしますか？(1または11を入力)');
                        card.dataset.value = aceValue === '11' ? '11' : '1';
                    }
                    newCards.push(card);
                    sum += parseInt(card.dataset.value);
                });

                if (sum <= 21) {
                    handContainers[hand].innerHTML = '';  // 既存のカードをクリア
                    newCards.forEach(card => {
                        card.classList.remove('selected');
                        handContainers[hand].appendChild(card);
                    });
                    updateHandSum(hand);
                } else {
                    alert('選択されたカードの合計が21を超えています。再度選択してください。');
                }
            } else {
                alert('3枚のカードを選択してください');
            }
        });
    });

    function updateHandSum(hand) {
        const cards = Array.from(handContainers[hand].children);
        let sum = 0;
        cards.forEach(card => {
            sum += parseInt(card.dataset.value);
        });
        handSums[hand].textContent = `合計: ${sum}`;
    }

    document.getElementById('start').addEventListener('click', () => {
        const handsCompleted = Object.values(handContainers).every(container => container.children.length === 3);
        if (handsCompleted) {
            const hands = {};
            Object.keys(handContainers).forEach(hand => {
                hands[hand] = Array.from(handContainers[hand].children).map(card => card.textContent);
            });
            socket.emit('readyToStart', hands);
        } else {
            alert('すべてのハンドに3枚のカードを割り当ててください');
        }
    });

    chatSend.addEventListener('click', () => {
        const message = chatInput.value;
        if (message.trim() !== '') {
            socket.emit('sendMessage', message);
            chatInput.value = '';
        }
    });

    socket.on('bothReady', (gameState) => {
        const hands = gameState.hands[socket.id];
        if (hands) {
            Object.keys(hands).forEach((hand) => {
                const container = handContainers[hand];
                container.innerHTML = '';  // 既存のカードをクリア
                hands[hand].forEach(cardValue => {
                    const card = document.createElement('div');
                    card.classList.add('card');
                    card.textContent = cardValue;
                    card.dataset.value = (cardValue === 'A' ? '1' : (['J', 'Q', 'K'].includes(cardValue) ? '10' : cardValue));
                    container.appendChild(card);
                });
                updateHandSum(hand);
            });
        }
        waitingContainer.style.display = 'none';
        setupContainer.style.display = 'none';
        gameContainer.style.display = 'block';
        displayYourHands();
    });

    socket.on('receiveMessage', ({ sender, message }) => {
        const messageDiv = document.createElement('div');
        messageDiv.textContent = `${sender}: ${message}`;
        chatMessages.appendChild(messageDiv);
        chatMessages.scrollTop = chatMessages.scrollHeight;
    });

    socket.on('updateState', (gameState) => { });

    socket.on('opponentDisconnected', () => {
        alert('対戦相手が切断しました。');
        setupContainer.style.display = 'block';
        gameContainer.style.display = 'none';
    });

    const yourHandsContainer = document.getElementById('yourHandsContainer');

    function displayYourHands() {
        yourHandsContainer.innerHTML = '';
        Object.keys(handContainers).forEach(hand => {
            const handWrapper = document.createElement('div');
            handWrapper.classList.add('handWrapper');

            const handButton = document.createElement('button');
            handButton.textContent = `${hand}`;
            handButton.type = 'button';
            handButton.id = `${hand}Button`;
            handWrapper.appendChild(handButton);

            const handContainer = document.createElement('div');
            handContainer.classList.add('handContainer');
            handContainers[hand].querySelectorAll('.card').forEach(card => {
                const cardCopy = card.cloneNode(true);
                handContainer.appendChild(cardCopy);
            });
            handWrapper.appendChild(handContainer);

            const handSum = document.createElement('p');
            handSum.textContent = `合計: ${handSums[hand].textContent}`;
            handWrapper.appendChild(handSum);

            yourHandsContainer.appendChild(handWrapper);

            // 使用済みハンドのチェック
            if (usedHands.includes(hand)) {
                handButton.disabled = true; // ボタンを無効化
                handButton.textContent = `${hand} (使用済み)`; // ボタンのテキストを変更
            } else {
                handButton.addEventListener('click', () => {
                    if (!usedHands.includes(hand)) {
                        selectedHandIndex = hand;
                        const selectedHandCards = Array.from(handContainers[hand].querySelectorAll('.card'))
                            .map(card => card.textContent);
                        if (confirm(`${hand} で勝負しますか？`)) {
                            socket.emit('confirmHand', { handIndex: selectedHandIndex, cards: selectedHandCards });
                            usedHands.push(hand); // 使用済みリストに追加
                            handButton.disabled = true; // ボタンを無効化
                            handButton.textContent = `${hand} (使用済み)`; // ボタンのテキストを変更
                        }
                    } else {
                        alert('このハンドは既に使用されています。');
                    }
                });
            }
        });
    }

    battleButton.addEventListener('click', () => {
        if (confirm('勝負しますか？')) {
            const playerHandSum = parseInt(yourSelectedHandSumContainer.textContent.split(': ')[1]);
            waitingContainer.style.display = 'block';
            socket.emit('battle', { handSum: playerHandSum });
        }
    });

    foldButton.addEventListener('click', () => {
        if (confirm('フォールドしますか？')) {
            socket.emit('fold', { handIndex: selectedHandIndex });
            gametimeContainer.style.display = 'none';
            foldContainer.style.display = 'block';
        }
    });

    socket.on('opponentFolded', (data) => {
        alert('相手がフォールドしました。ポイントを1獲得しました。');
        points += 1; // フォールドしていないプレイヤーのポイントを更新
        updatePoints();
        gametimeContainer.style.display = 'none';
        foldContainer.style.display = 'block';
    });

    // 次のラウンドへボタンのイベントリスナー
    const nextRoundButton = document.getElementById('nextRoundButton');
    nextRoundButton.addEventListener('click', () => {
        // 使用済みハンドを非表示にする
        const usedHandContainer = handContainers[selectedHandIndex];
        usedHandContainer.style.display = 'none';
        foldContainer.style.display = 'none';
        gameContainer.style.display = 'none';
        gametimeContainer.style.display = 'none';
        gameContainer.style.display = 'block'; // ハンド選択画面に戻る
        showdownContainer.style.display = 'none';
        resetForNextRound(); // 次のラウンドの準備
    });

    socket.on('battleResult', (result) => {
        // 対戦結果を表示する
        document.getElementById('opponentHandContainer').textContent = result.player2HandSum;
        document.getElementById('yourSelectedHandContainer').textContent = result.player1HandSum;
        document.getElementById('resultMessage').textContent = result.message;
        document.getElementById('player1Points').textContent = `${result.player1Name}のポイント: ${result.player1Points}`;
        document.getElementById('player2Points').textContent = `${result.player2Name}のポイント: ${result.player2Points}`;

        gametimeContainer.style.display = 'none';
        waitingContainer.style.display = 'none';
        showdownContainer.style.display = 'block';
    });

    socket.on('bothHandsConfirmed', (gameState) => {
        waitingContainer.style.display = 'none';
        gameContainer.style.display = 'none';
        gametimeContainer.style.display = 'block';
        displaySelectedHand();
    });

    function displaySelectedHand() {
        const selectedHand = Array.from(handContainers[selectedHandIndex].querySelectorAll('.card'))
            .map(card => card.textContent);
        const selectedHandDiv = document.getElementById('yourSelectedHand');
        selectedHandDiv.innerHTML = '';
        let sum = 0;
        selectedHand.forEach(card => {
            const cardDiv = document.createElement('div');
            cardDiv.classList.add('card');
            cardDiv.textContent = card;
            cardDiv.dataset.value = (card === 'A' ? '1' : (['J', 'Q', 'K'].includes(card) ? '10' : card));
            selectedHandDiv.appendChild(cardDiv);
            sum += parseInt(cardDiv.dataset.value);
        });
        yourSelectedHandSumContainer.textContent = `合計: ${sum}`;
    }

    socket.on('updatePoints', (data) => {
        points = data.player1Points; // プレイヤー1のポイントを更新
        updatePoints();
    });

    socket.on('gameOver', (data) => {
        alert(`ゲーム終了！ 勝者は ${data.winner} です。\n${data.player1Name}のポイント: ${data.player1Points}\n${data.player2Name}のポイント: ${data.player2Points}`);
        location.reload();
    });

    function updatePoints() {
        pointsContainer.textContent = points;
    }

    function resetForNextRound() {
        selectedHandIndex = null;
        displayYourHands();
    }
});
