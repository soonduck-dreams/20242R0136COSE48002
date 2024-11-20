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
let activeAudio = null;

function playAudio(button) {
    if (activeAudio && !activeAudio.paused) {
        activeAudio.pause();
        activeAudio.currentTime = 0;
    }

    const audioSrc = button.getAttribute('data-audio-src');
    activeAudio = new Audio(audioSrc);
    activeAudio.play();
}

function stopAllAudio() {
    // 모든 오디오 요소를 찾음
    const audioElements = document.querySelectorAll('audio');

    // 각 오디오 요소를 순회하며 재생 중인 오디오를 멈춤
    audioElements.forEach(audio => {
        if (!audio.paused) { // 재생 중인 오디오만 멈춤
            audio.pause();
            audio.currentTime = 0; // 재생 위치를 처음으로 리셋
        }
    });
}


/* 음악 생성 기능 - 폴링 */
function pollForResult(taskId) {
    const endpoint = `/api/learn/progressions/generate-music-result/${taskId}`;
    const interval = 3000; // 3초 간격으로 폴링

    const intervalId = setInterval(() => {
        fetch(endpoint, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            }
        })
            .then(response => {
                if (response.status === 202) {
                    console.log("Task still in progress...");
                    return null; // 아직 완료되지 않음
                }
                if (response.status === 500) {
                    console.error("Task failed!");
                    clearInterval(intervalId);
                    return null;
                }
                if (response.ok) {
                    return response.blob(); // 완료된 파일 데이터 가져오기
                }
            })
            .then(blob => {
                if (blob) {
                    clearInterval(intervalId);
                    const url = window.URL.createObjectURL(blob);
                    document.getElementById("generate-music-loading-spinner").style.display = "none";

                    document.getElementById("generate-music-button").innerText = "더 만들어봐!";
                    document.getElementById("generate-music-button").style.display = "inline";

                    processZip(blob);
                }
            })
            .catch(error => {
                alert("음악 생성에 실패했습니다.")

                document.getElementById("generate-music-loading-spinner").style.display = "none";
                document.getElementById("generate-music-button").innerText = "다시 시도";

                clearInterval(intervalId);
            });
    }, interval);
}

async function processZip(zipBlob) {
    const jszip = new JSZip();

    // ZIP 파일 읽기
    const zip = await jszip.loadAsync(zipBlob);

    // 결과 표시 영역
    const resultContainer = document.createElement("div");
    resultContainer.classList.add("generated-results");
    document.querySelector(".generate-music-result-container").appendChild(resultContainer);

    // ZIP 파일 내용 처리
    const files = Object.entries(zip.files);

    // 파일을 WAV → MIDI 순으로 정렬
    const sortedFiles = files.sort(([filenameA], [filenameB]) => {
        if (filenameA.endsWith(".wav")) return -1;
        if (filenameB.endsWith(".wav")) return 1;
        return 0;
    });

    // WAV, MIDI 각각 표시
    const resultBox = document.createElement("div"); // 결과 박스 컨테이너
    resultBox.classList.add("result-box"); // 스타일링용 클래스

    // 오디오와 다운로드 버튼을 담을 컨테이너 생성
    const contentContainer = document.createElement("div");
    contentContainer.classList.add("content-container");

    for (const [filename, file] of sortedFiles) {
        if (filename.endsWith(".wav")) {
            // WAV 파일 처리
            const audioBlob = await file.async("blob");
            const audioUrl = URL.createObjectURL(audioBlob);

            const audioPlayer = document.createElement("audio");
            audioPlayer.controls = true;
            audioPlayer.src = audioUrl;
            audioPlayer.style.display = "inline";

            contentContainer.appendChild(audioPlayer);
        } else if (filename.endsWith(".mid")) {
            // MIDI 파일 처리
            const midiBlob = await file.async("blob");
            const midiUrl = URL.createObjectURL(midiBlob);

            const downloadLink = document.createElement("a");
            downloadLink.classList.add("btn");
            downloadLink.classList.add("btn-primary");
            downloadLink.href = midiUrl;
            downloadLink.download = filename;
            downloadLink.textContent = `💾 MIDI 파일 다운로드`;

            contentContainer.appendChild(downloadLink);
        }
    }

    resultBox.appendChild(contentContainer); // 결과 박스에 컨텐츠 컨테이너 추가
    resultContainer.appendChild(resultBox); // 결과 컨테이너에 박스 추가
}
