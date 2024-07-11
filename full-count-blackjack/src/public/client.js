const socket = io();

document.addEventListener('DOMContentLoaded', () => {
    const lobbyContainer = document.getElementById('lobby');
    const setupContainer = document.getElementById('setup');
    const waitingContainer = document.getElementById('waiting');
    const gameContainer = document.getElementById('game');
    const cardContainer = document.getElementById('cards');
    const bettimeContainer = document.getElementById('bettime');
    const showdownContainer = document.getElementById('showdown');
    const yourSelectedHandContainer = document.getElementById('yourSelectedHandContainer');
    const opponentHandContainer = document.getElementById('opponentHandContainer');
    const currentBetContainer = document.getElementById('currentBet');
    const chipsContainer = document.getElementById('chips');
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
    const betButton = document.getElementById('betButton');
    const foldButton = document.getElementById('foldButton');
    const confirmBetButton = document.getElementById('confirmBet');

    let selectedHandIndex = null;
    let currentBet = 0;
    let chips = 5; // 初期チップ数

    function openModal() {
        const modal = document.getElementById('checkrulesModal');
        modal.style.display = 'block';
    }

    function closeModal() {
        const modal = document.getElementById('checkrulesModal');
        modal.style.display = 'none';
    }

    const openModalBtn = document.getElementById('checkrules');
    openModalBtn.addEventListener('click', openModal);

    const closeModalBtn = document.getElementsByClassName('close')[0];
    closeModalBtn.addEventListener('click', closeModal);

    window.addEventListener('click', function (event) {
        const modal = document.getElementById('checkrulesModal');
        if (event.target == modal) {
            closeModal();
        }
    });

    document.getElementById('startgame').addEventListener('click', () => {
        const name = prompt('名前を入力してください:');
        if (name) {
            console.log(`${name}がゲームに参加しました`);
            socket.emit('joinGame', name);
        }
    });

    socket.on('waiting', (message) => {
        console.log(message);
        document.getElementById('waitingMessage').textContent = message;
        lobbyContainer.style.display = 'none';
        waitingContainer.style.display = 'block';
    });

    socket.on('gameStart', (gameState) => {
        console.log('ゲームが開始されました:', gameState);
        lobbyContainer.style.display = 'none';
        waitingContainer.style.display = 'none';
        setupContainer.style.display = 'block';
        updateChips();
    });

    const cardValues = ['A', '2', '3', '4', '5', '6', '7', '8', '9', '10', 'J', 'Q', 'K', '0', '0'];
    cardValues.forEach(value => {
        const card = document.createElement('div');
        card.classList.add('card');
        card.textContent = value;
        card.dataset.value = (value === 'A' ? '1' : (['J', 'Q', 'K'].includes(value) ? '10' : value));
        card.addEventListener('click', () => {
            card.classList.toggle('selected');
            console.log(`Card ${value} selected`);
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
                        console.log(`Card added to ${hand}:`, card.textContent);
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
            console.log('Starting game...');
            const hands = {};
            Object.keys(handContainers).forEach(hand => {
                hands[hand] = Array.from(handContainers[hand].children).map(card => card.textContent);
            });
            socket.emit('readyToStart', hands); // ゲーム開始準備完了をサーバーに通知
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
        console.log('両プレイヤーが準備完了:', gameState);
        const hands = gameState.hands[socket.id];
        console.log('hands:', hands);  // hands オブジェクトの内容を確認
        if (hands) {
            Object.keys(hands).forEach((hand) => {
                const container = handContainers[hand];
                container.innerHTML = '';  // 既存のカードをクリア
                console.log(`container for ${hand}:`, container);  // container の内容を確認
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
        gameContainer.style.display = 'block'; // ゲーム画面に移動
        displayYourHands();
    });

    socket.on('receiveMessage', ({ sender, message }) => {
        console.log('Received message:', sender, message);
        const messageDiv = document.createElement('div');
        messageDiv.textContent = `${sender}: ${message}`;
        chatMessages.appendChild(messageDiv);
        chatMessages.scrollTop = chatMessages.scrollHeight;
    });

    socket.on('updateState', (gameState) => {
        console.log('ゲームの状態が更新されました:', gameState);
    });

    socket.on('opponentDisconnected', () => {
        alert('対戦相手が切断しました。');
        setupContainer.style.display = 'block';
        gameContainer.style.display = 'none';
    });

    const yourHandsContainer = document.getElementById('yourHandsContainer');
    let currentHandIndex = 0;
    let opponentFolded = false;

    function displayYourHands() {
        yourHandsContainer.innerHTML = '';
        Object.keys(handContainers).forEach(hand => {
            const handWrapper = document.createElement('div');
            handWrapper.classList.add('handWrapper');

            const handButton = document.createElement('button');
            handButton.textContent = `${hand}`;
            handButton.type = 'button';
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

            handButton.addEventListener('click', () => {
                selectedHandIndex = hand; 
                const selectedHandCards = Array.from(handContainers[hand].querySelectorAll('.card'))
                    .map(card => card.textContent);
                console.log(`${hand} のカード情報:`, selectedHandCards);
                if (confirm(`${hand} で勝負しますか？`)) {
                    console.log('ハンド確定:', selectedHandIndex, selectedHandCards);
                    socket.emit('confirmHand', { handIndex: selectedHandIndex, cards: selectedHandCards }); // 確定したハンドをサーバーに通知
                    waitingContainer.style.display = 'block'; // 待機画面に移動
                    bettimeContainer.style.display = 'none';
                    gameContainer.style.display = 'none';
                }
            });
        });
    }

    function updateChips() {
        chipsContainer.textContent = `残りチップ: ${chips}`;
    }

    betButton.addEventListener('click', () => {
        if (selectedHandIndex !== null) {
            const betAmount = parseInt(prompt('ベット額を入力してください:'));
            if (betAmount && betAmount <= chips) {
                currentBet += betAmount;
                chips -= betAmount;
                updateChips();
                currentBetContainer.textContent = `現在のベット額: ${currentBet}`;
                socket.emit('bet', { handIndex: selectedHandIndex, amount: currentBet, action: 'bet' });
                console.log(`現在のベット額: ${currentBet}, 残りチップ数: ${chips}`);
            } else {
                alert('無効なベット額です。');
            }
        } else {
            alert('ハンドを選択してください');
        }
    });

    foldButton.addEventListener('click', () => {
        if (selectedHandIndex !== null) {
            socket.emit('bet', { handIndex: selectedHandIndex, action: 'fold' });
            waitingContainer.style.display = 'block'; // 待機画面に移動
            bettimeContainer.style.display = 'none';
        } else {
            alert('ハンドを選択してください');
        }
    });

    confirmBetButton.addEventListener('click', () => {
        socket.emit('bet', { handIndex: selectedHandIndex, amount: currentBet, action: 'confirm' });
        waitingContainer.style.display = 'block'; // 待機画面に移動
        bettimeContainer.style.display = 'none';
        currentBet = 0; // ベット額をリセット
        currentBetContainer.textContent = `現在のベット額: ${currentBet}`;
    });

    socket.on('bothHandsConfirmed', (gameState) => {
        console.log('両プレイヤーがハンドを確定:', gameState);
        waitingContainer.style.display = 'none';
        bettimeContainer.style.display = 'block'; // ベット画面に移動
        displaySelectedHand();
        updateChips();
    });

    socket.on('opponentBet', (betData) => {
        console.log(`相手がベットしました。ハンド: ${betData.handIndex}, ベット額: ${betData.amount}`);
        const message = document.createElement('div');
        message.textContent = `相手が${betData.amount}枚をベットしました。`;
        chatMessages.appendChild(message);
        chatMessages.scrollTop = chatMessages.scrollHeight;
    });

    socket.on('opponentFold', (handIndex) => {
        opponentFolded = true;
        console.log(`相手がフォールドしました。ハンド: ${handIndex}`);
        const message = document.createElement('div');
        message.textContent = `相手がフォールドしました。ハンド: ${handIndex}`;
        chatMessages.appendChild(message);
        chatMessages.scrollTop = chatMessages.scrollHeight;
    });

    socket.on('bothBetsConfirmed', (gameState) => {
        console.log('両プレイヤーがベットを確定:', gameState);
        waitingContainer.style.display = 'none';
        showdownContainer.style.display = 'block'; // 勝敗画面に移動
        calculateWinner(gameState);
    });

    socket.on('roundResult', (result) => {
        waitingContainer.style.display = 'none';
        showdownContainer.style.display = 'block';
        
        const message = result.winner ? `勝者: ${result.winner}, 獲得チップ: ${result.chipsWon}` : result.message;
        alert(message);

        if (result.winner === socket.id) {
            chips += result.chipsWon;
        } else if (result.winner) {
            chips -= result.chipsWon;
        }
        updateChips();
        
        // 自分のハンドと相手のハンドを表示
        displayShowdown(result);

        // 次のラウンドに進む
        checkRoundCompletion();
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

    function displayShowdown(result) {
        yourSelectedHandContainer.innerHTML = '';
        opponentHandContainer.innerHTML = '';
        
        const yourHand = result.player1Hand;
        const opponentHand = result.player2Hand;
        
        yourHand.forEach(card => {
            const cardDiv = document.createElement('div');
            cardDiv.classList.add('card');
            cardDiv.textContent = card;
            cardDiv.dataset.value = (card === 'A' ? '1' : (['J', 'Q', 'K'].includes(card) ? '10' : card));
            yourSelectedHandContainer.appendChild(cardDiv);
        });

        opponentHand.forEach(card => {
            const cardDiv = document.createElement('div');
            cardDiv.classList.add('card');
            cardDiv.textContent = card;
            cardDiv.dataset.value = (card === 'A' ? '1' : (['J', 'Q', 'K'].includes(card) ? '10' : card));
            opponentHandContainer.appendChild(cardDiv);
        });
    }

    function checkRoundCompletion() {
        if (currentHandIndex < 4) {
            currentHandIndex += 1;
            displayYourHands();
        } else {
            socket.emit('roundComplete');
            alert('全てのラウンドが完了しました。結果を待っています...');
        }
    }

    function calculateWinner(gameState) {
        const yourHandSum = calculateHandSum(gameState.hands[socket.id][selectedHandIndex]);
        const opponentHandSum = calculateHandSum(gameState.hands[gameState.opponentId][selectedHandIndex]);

        let winner = null;
        let chipsWon = 0;

        if (yourHandSum > opponentHandSum) {
            winner = socket.id;
            chipsWon = gameState.currentBet[1].amount; // 相手のベット額を獲得
        } else if (yourHandSum < opponentHandSum) {
            winner = gameState.opponentId;
            chipsWon = gameState.currentBet[0].amount; // 自分のベット額を相手が獲得
        }

        socket.emit('roundResult', { winner, chipsWon, player1Hand: gameState.hands[socket.id][selectedHandIndex], player2Hand: gameState.hands[gameState.opponentId][selectedHandIndex] });
    }

    function calculateHandSum(hand) {
        return hand.reduce((sum, card) => {
            if (card === 'A') return sum + 11; // Aは11としてカウント
            if (['J', 'Q', 'K'].includes(card)) return sum + 10;
            return sum + parseInt(card);
        }, 0);
    }

    socket.on('roundResult', (result) => {
        waitingContainer.style.display = 'none';
        showdownContainer.style.display = 'block';
        
        const message = result.winner ? `勝者: ${result.winner}, 獲得チップ: ${result.chipsWon}` : result.message;
        alert(message);

        if (result.winner === socket.id) {
            chips += result.chipsWon;
        } else if (result.winner) {
            chips -= result.chipsWon;
        }
        updateChips();
        
        // 自分のハンドと相手のハンドを表示
        displayShowdown(result);

        // 次のラウンドに進む
        checkRoundCompletion();
    });

    function handleFold(handIndex) {
        const winner = socket.id;
        const chipsWon = currentBet; // 勝った側が現在のベット額を獲得

        socket.emit('roundResult', {
            winner: winner,
            chipsWon: chipsWon,
            player1Hand: handContainers[handIndex].querySelectorAll('.card').map(card => card.textContent),
            player2Hand: []
        });
    }
});
