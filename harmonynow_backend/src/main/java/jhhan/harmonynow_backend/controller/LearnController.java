/**
 * LearnController
 *
 * [1. 역할]
 * - 학습하기 페이지에서 코드(Chord) 및 코드 진행(Progression)의 데이터를 사용자에게 제공하는 컨트롤러.
 * - 난이도별 코드 목록, 코드 상세 정보, 코드 진행 상세 정보를 제공.
 *
 * [2. 주요 기능]
 * - 난이도(Level)별 코드 목록을 조회하여 사용자 인터페이스에 표시.
 * - 특정 코드의 상세 정보 및 해당 코드가 포함된 코드 진행 목록을 제공.
 * - 특정 코드 진행의 상세 정보를 제공.
 *
 * [3. 사용 사례]
 * - 학습하기 페이지에서 난이도별로 코드를 탐색하거나, 코드와 관련된 코드 진행을 확인할 때 사용.
 */


package jhhan.harmonynow_backend.controller;

import jhhan.harmonynow_backend.dto.ReadChordDTO;
import jhhan.harmonynow_backend.dto.ReadProgressionDTO;
import jhhan.harmonynow_backend.repository.ChordProgressionMapRepository;
import jhhan.harmonynow_backend.repository.ChordRepository;
import jhhan.harmonynow_backend.service.ChordService;
import jhhan.harmonynow_backend.service.ProgressionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LearnController {

    private final ChordService chordService;
    private final ChordRepository chordRepository;
    private final ProgressionService progressionService;

    @GetMapping("/learn/chords")
    public String chordList(@RequestParam(required = false) String level, Model model) {
        if (level == null) {
            return "redirect:/learn/chords?level=beginner";
        }

        model.addAttribute("level", level);
        model.addAttribute("chordDtoList", chordService.findPublicChordsByLevel(level));

        return "learn/chordList";
    }

    @GetMapping("/learn/chords/{chordId}")
    public String chordDetail(@PathVariable Long chordId, Model model) {
        ReadChordDTO dto = chordService.findChordById(chordId);
        List<ReadProgressionDTO> progressionDTOList = progressionService.getProgressionsByChordId(chordId);

        model.addAttribute("chord", dto);
        model.addAttribute("progressions", progressionDTOList);

        return "learn/chordDetail";
    }

    @GetMapping("/learn/progressions/{progressionId}")
    public String progressionDetail(@PathVariable Long progressionId, Model model) {
        ReadProgressionDTO dto = progressionService.getProgressionById(progressionId, false);
        List<ReadChordDTO> chordDTOList = chordRepository.findChordsByProgressionId(progressionId);

        model.addAttribute("progression", dto);
        model.addAttribute("chords", chordDTOList);

        return "learn/progressionDetail";
    }
}
