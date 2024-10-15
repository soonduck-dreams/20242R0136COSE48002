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