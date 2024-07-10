const socket = io();

document.addEventListener('DOMContentLoaded', () => {
    const lobbyContainer = document.getElementById('lobby');
    const setupContainer = document.getElementById('setup');
    const waitingContainer = document.getElementById('waiting');
    const gameContainer = document.getElementById('game');
    const actionContainer = document.getElementById('action');
    const cardContainer = document.getElementById('cards');
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

    let selectedHand = [];
    let currentBet = 0;
    let isFolded = false;

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

    const cardValues = ['A', '2', '3', '4', '5', '6', '7', '8', '9', '10', 'J', 'Q', 'K', '0', '0'];
    cardValues.forEach(value => {
        const card = document.createElement('div');
        card.classList.add('card');
        card.textContent = value;
        card.dataset.value = value === 'A' ? '1' : value;
        if (['J', 'Q', 'K'].includes(value)) card.dataset.value = '10';
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

    document.getElementById('startgame').addEventListener('click', () => {
        const name = prompt('名前を入力してください:');
        if (name) {
            console.log(`${name}がゲームに参加しました`);
            socket.emit('joinGame', name);
            socket.on('waiting', (message) => {
                console.log('Waiting for another player...');
                document.getElementById('waitingMessage').textContent = message;
                lobbyContainer.style.display = 'none';
                waitingContainer.style.display = 'block';
            });
        }
    });

    document.getElementById('start').addEventListener('click', () => {
        const handsCompleted = Object.values(handContainers).every(container => container.children.length === 3);
        if (handsCompleted) {
            console.log('Starting game...');
            const hands = {};
            Object.keys(handContainers).forEach(hand => {
                hands[hand] = Array.from(handContainers[hand].children).map(card => card.textContent);
            });
            socket.emit('readyToStart', hands);
            socket.on('waiting', (message) => {
                document.getElementById('waitingMessage').textContent = message;
                setupContainer.style.display = 'none';
                waitingContainer.style.display = 'block';
            });
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

    socket.on('gameStart', (gameState) => {
        console.log('ゲームが開始されました:', gameState);
        lobbyContainer.style.display = 'none';
        waitingContainer.style.display = 'none';
        setupContainer.style.display = 'block';
    });

    socket.on('bothReady', (gameState) => {
        console.log('両プレイヤーが準備完了:', gameState);
        const hands = gameState.hands[socket.id];
        if (hands) {
            Object.keys(hands).forEach((hand, index) => {
                const container = handContainers[`hand${index + 1}Container`];
                hands[hand].forEach(cardValue => {
                    const card = document.createElement('div');
                    card.classList.add('card');
                    card.textContent = cardValue;
                    container.appendChild(card);
                });
                updateHandSum(`hand${index + 1}`);
            });
        }
        waitingContainer.style.display = 'none';
        setupContainer.style.display = 'none';
        gameContainer.style.display = 'block';
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
    const chipsDisplay = document.getElementById('chips');
    const betButton = document.getElementById('bet');
    const foldButton = document.getElementById('fold');
    const confirmButton = document.getElementById('confirm');

    let currentHandIndex = 0;
    let chips = 5;
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
                const selectedHandIndex = hand;
                selectedHand = Array.from(handContainers[hand].querySelectorAll('.card'))
                    .map(card => card.textContent);
                console.log(`${hand} のカード情報:`, selectedHand);
                if (confirm(`${hand} で勝負しますか？`)) {
                    actionContainer.style.display = 'block';
                    gameContainer.style.display = 'none';
                }
            });
        });
    }

    betButton.addEventListener('click', () => {
        if (chips > 0) {
            currentBet += 1;
            chips -= 1;
            chipsDisplay.textContent = chips;
            console.log(`Bet placed: ${currentBet}`);
        } else {
            alert('これ以上ベットできません');
        }
    });

    foldButton.addEventListener('click', () => {
        isFolded = true;
        console.log('フォールドしました');
    });

    confirmButton.addEventListener('click', () => {
        socket.emit('handSelected', { hand: selectedHand, bet: currentBet, folded: isFolded });
        console.log('ハンド確定:', selectedHand, currentBet, isFolded);
        waitingContainer.style.display = 'block';
        actionContainer.style.display = 'none';
    });

    socket.on('showdown', (data) => {
        console.log('対戦結果:', data);

        const yourHandContainer = document.getElementById('yourSelectedHandContainer');
        yourHandContainer.innerHTML = '';
        data.yourHand.forEach(cardValue => {
            const card = document.createElement('div');
            card.classList.add('card');
            card.textContent = cardValue;
            yourHandContainer.appendChild(card);
        });

        const opponentHandContainer = document.getElementById('opponentHandContainer');
        opponentHandContainer.innerHTML = '';

        if (!data.opponentFolded) {
            data.opponentHand.forEach(cardValue => {
                const card = document.createElement('div');
                card.classList.add('card');
                card.textContent = cardValue;
                opponentHandContainer.appendChild(card);
            });
        } else {
            const foldedMessage = document.createElement('p');
            foldedMessage.textContent = '相手はフォールドしました';
            opponentHandContainer.appendChild(foldedMessage);
        }

        document.getElementById('showdown').style.display = 'block';
        document.getElementById('game').style.display = 'none';
        waitingContainer.style.display = 'none';
    });

    socket.on('bothReady', (gameState) => {
        console.log('両プレイヤーが準備完了:', gameState);
        waitingContainer.style.display = 'none';
        gameContainer.style.display = 'block';
        displayYourHands();
    });

    socket.on('opponentBet', (handIndex) => {
        console.log(`相手がベットしました。ハンド: ${handIndex}`);
    });

    socket.on('opponentFold', (handIndex) => {
        opponentFolded = true;
        console.log(`相手がフォールドしました。ハンド: ${handIndex}`);
    });

    socket.on('roundResult', (result) => {
        if (result.winner === socket.id) {
            chips += result.chipsWon;
        }
        alert(`ラウンドの結果: ${result.message}`);
        chipsDisplay.textContent = chips;
    });
});