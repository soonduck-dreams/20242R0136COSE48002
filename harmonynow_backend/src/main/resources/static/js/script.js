// Progression 등록 UI를 위한 업데이트 함수
function updateDropdowns() {
    // "없음" 값 상수 읽기
    var noneValue = document.getElementById('chord-fields').dataset.noneValue;

    var numSelects = 4;
    var foundFirstNone = false;

    for (var n = 0; n < numSelects; n++) {
        var select = document.getElementById('chords' + n);
        var selectDiv = document.getElementById('chord-div-' + n);
        var value = select.value;

        if (value === noneValue) {  // "없음" 값을 상수로 처리
            if (!foundFirstNone) {
                selectDiv.style.display = 'block';
                foundFirstNone = true;
            } else {
                // 이후의 "없음" 선택지는 숨김
                selectDiv.style.display = 'none';
            }
            // 다음 선택지들을 초기화하고 숨김
            for (var m = n + 1; m < numSelects; m++) {
                var nextSelect = document.getElementById('chords' + m);
                var nextSelectDiv = document.getElementById('chord-div-' + m);
                nextSelect.value = noneValue;
                nextSelectDiv.style.display = 'none';
            }
            break;
        } else {
            // "없음"이 아닌 값인 경우 계속 보여줌
            selectDiv.style.display = 'block';
        }
    }
}

// Chord 수정, Progression 수정 UI에 쓰임
function toggleFileInput(fileInputId, checkboxId) {
    var fileInput = document.getElementById(fileInputId);
    var checkbox = document.getElementById(checkboxId);

    // 체크박스가 체크되어 있으면 파일 업로드 비활성화
    if (checkbox.checked) {
        fileInput.disabled = true;
    } else {
        fileInput.disabled = false;
    }
}

// Chord 목록 조회 UI에서 삭제 에러 toast 메시지를 띄우는 데 쓰임
function showToastMessage(toastId) {
    const toastLiveExample = document.getElementById(toastId);

    const toastBootstrap = bootstrap.Toast.getOrCreateInstance(toastLiveExample);
    console.log(toastBootstrap);
    toastBootstrap.show();
}


/* 채팅 UI에 쓰이는 기능 */
function getAllMessages() {
    const messages = [];
    const messageElements = document.querySelectorAll(".chat-messages .message");

    messageElements.forEach((element) => {
        const messageText = element.textContent;
        const role = element.classList.contains("user") ? "user" : "assistant";
        messages.push({role, content: messageText});
    });

    return messages;
}

function sendMessage() {
    const input = document.getElementById("chat-input");
    const userMessage = input.value.trim();
    if (userMessage === "") return;

    const learnMainContent = document.getElementById("learn-main-content").textContent.trim();
    const systemMessage = [
        {
            "role": "system",
            "content": "The user is now seeing the following content. You can refer to the content to answer user's question:" + learnMainContent
        },
    ];

    // 사용자 메시지 UI에 추가
    const userMessageDiv = document.createElement("div");
    userMessageDiv.className = "message user";
    userMessageDiv.textContent = userMessage;
    document.getElementById("chat-content").appendChild(userMessageDiv);

    // 기존 메시지 배열 가져오기
    const allMessages = getAllMessages();

    const chatContent = document.getElementById("chat-content");
    chatContent.scrollTop = chatContent.scrollHeight;

    fetch('/chat/prompt', {
        method: 'POST',
        body: JSON.stringify({messages: allMessages.concat(systemMessage)}),
        headers: {'Content-Type': 'application/json'}
    })
        .then(response => response.json())
        .then(data => {
            const assistantMessageDiv = document.createElement("div");
            const assistantMessage = data.result[0].message.content; // 서버 응답 내용
            assistantMessageDiv.className = "message assistant";
            assistantMessageDiv.innerHTML = assistantMessage.replace(/\n/g, "<br>");
            chatContent.appendChild(assistantMessageDiv);
            chatContent.scrollTop = chatContent.scrollHeight;

            saveChatHistory(userMessage, assistantMessage);
            sendDocument();
        });

    input.value = "";
    input.focus();
}

function sendDocument() {
    const learnMainContent = document.getElementById("learn-main-content").innerText.trim();

    const allMessages = getAllMessages();

    const chatContent = document.getElementById("chat-content");

    fetch('/chat/starter', {
        method: 'POST',
        body: JSON.stringify({
            messages: [
                {"role": "system", "content": "You can refer to the following conversation history:"},
                ...allMessages.slice(-4),
                {"role": "system", "content": "You can refer to the following document content:"},
                {"role": "user", "content": learnMainContent},

            ]
        }),
        headers: {'Content-Type': 'application/json'}
    })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            const starterQuestions = [document.getElementById("starter-question-1"), document.getElementById("starter-question-2")];

            for (let i = 0; i < starterQuestions.length; i++) {
                starterQuestions[i].textContent = data.result[i].message.content;
                starterQuestions[i].style.display = "inline-block";
            }

            chatContent.scrollTop = chatContent.scrollHeight;
        });
}

function sendStarterMessage(starterButtonId) {
    const inputField = document.getElementById("chat-input");
    const starterButton = document.getElementById(starterButtonId);
    const starterQuestions = [document.getElementById("starter-question-1"), document.getElementById("starter-question-2")];

    for (let i = 0; i < starterQuestions.length; i++) {
        starterQuestions[i].style.display = "none";
    }

    inputField.value = starterButton.textContent; // 버튼 클릭 시 입력창에 미리 입력
    sendMessage(); // 전송
}

const initialAssistantMessage = [
    {"role": "assistant", "content": "안녕! 학습하다가 모르는 거나 궁금한 거 있으면 물어봐. 내가 도와줄게."}
]

function saveChatHistory(userMessage, assistantMessage) {
    let chatHistory = JSON.parse(sessionStorage.getItem("chatHistory")) || [...initialAssistantMessage];

    chatHistory.push({ role: "user", content: userMessage });
    chatHistory.push({ role: "assistant", content: assistantMessage });

    sessionStorage.setItem("chatHistory", JSON.stringify(chatHistory));
    console.log("hi");
}

function loadChatHistory() {
    const chatHistory = JSON.parse(sessionStorage.getItem("chatHistory")) || [...initialAssistantMessage];

    const chatContent = document.getElementById("chat-content");

    chatHistory.forEach(entry => {
        const messageElement = document.createElement("div");
        messageElement.className = `message ${entry.role}`;
        messageElement.innerHTML = entry.content.replace(/\n/g, "<br>");
        chatContent.appendChild(messageElement);
    });

    chatContent.scrollTop = chatContent.scrollHeight;
}

function clearChatHistory() {
    // 대화 내역 지우기
    const chatContent = document.getElementById("chat-content");

    chatContent.innerHTML = ""; // 화면에서 대화 내역 제거

    // 세션 스토리지나 로컬 스토리지에서 저장된 대화 내역 삭제
    sessionStorage.removeItem("chatHistory");

    loadChatHistory();
    sendDocument();
}

/* 커스텀 플레이 버튼 */
function playAudio(button) {
    const audioSrc = button.getAttribute('data-audio-src');
    let audio = new Audio(audioSrc);
    audio.play();
}
