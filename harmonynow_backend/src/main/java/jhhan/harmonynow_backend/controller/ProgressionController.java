/**
 * ProgressionController
 *
 * [1. 역할]
 * - 관리자 페이지에서 코드 진행(Progression) 관련 데이터를 관리하는 컨트롤러.
 * - 코드 진행의 목록 조회, 생성, 수정, 삭제 요청을 처리하며, 해당 정보를 사용자 인터페이스에 전달.
 *
 * [2. 주요 기능]
 * - 코드 진행 목록을 조회하여 관리자 페이지에서 표시.
 * - 새로운 코드 진행 생성 폼 표시 및 생성 처리.
 * - 코드 진행 수정 폼 표시 및 수정 처리.
 * - 코드 진행 삭제 요청 처리.
 *
 * [3. 사용 사례]
 * - 관리자 페이지에서 코드 진행 목록을 확인하고, 새로운 코드 진행을 추가하거나 기존 코드 진행을 수정/삭제할 때 사용.
 */


package jhhan.harmonynow_backend.controller;

import jakarta.validation.Valid;
import jhhan.harmonynow_backend.domain.Chord;
import jhhan.harmonynow_backend.domain.Progression;
import jhhan.harmonynow_backend.dto.CreateChordDTO;
import jhhan.harmonynow_backend.dto.CreateProgressionDTO;
import jhhan.harmonynow_backend.dto.EditProgressionDTO;
import jhhan.harmonynow_backend.dto.ReadProgressionDTO;
import jhhan.harmonynow_backend.repository.ChordRepository;
import jhhan.harmonynow_backend.repository.ProgressionRepository;
import jhhan.harmonynow_backend.service.ProgressionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProgressionController {

    private final ChordRepository chordRepository;
    private final ProgressionRepository progressionRepository;
    private final ProgressionService progressionService;

    /* admin 전용 */
    @GetMapping("/admin/progressions")
    public String adminProgressionList(Model model) {
        List<Progression> progressions = progressionRepository.findAll();

        List<ReadProgressionDTO> dtoList = new ArrayList<>();
        for (Progression progression : progressions) {
            dtoList.add(new ReadProgressionDTO(progression.getId(), progressionService.getProgressionName(progression),
                        progression.getDescription(), progression.getAudioUrl(), progression.getSampleMidiUrl(), progression.getIsCadence()));
        }

        model.addAttribute("dtoList", dtoList);
        return "admin/progressionList";
    }

    @GetMapping("/admin/progressions/new")
    public String adminCreateProgressionForm(Model model) {
        model.addAttribute("form", new CreateProgressionDTO());

        List<Chord> chords = chordRepository.findAll();
        model.addAttribute("chords", chords);

        model.addAttribute("chordNoneValue", -1); // 코드 진행 선택 UI에서 "없음" 선택 시 DTO로 넘어올 값을 설정

        return "admin/createProgressionForm";
    }

    @PostMapping("/admin/progressions/new")
    public String adminCreateProgression(@Valid @ModelAttribute("form") CreateProgressionDTO dto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<Chord> chords = chordRepository.findAll();
            model.addAttribute("chords", chords);

            model.addAttribute("chordNoneValue", -1); // 코드 진행 선택 UI에서 "없음" 선택 시 DTO로 넘어올 값을 설정

            return "admin/createProgressionForm";
        }

        progressionService.saveProgression(dto);
        return "redirect:/admin/progressions";
    }

    @GetMapping("/admin/progressions/edit/{progressionId}")
    public String adminEditProgressionForm(@PathVariable Long progressionId, Model model) {
        Progression progression = progressionRepository.findOne(progressionId);
        List<Long> chordIds = progressionService.getChordIdsInProgression(progression);
        List<Chord> chords = chordRepository.findAll();

        EditProgressionDTO dto = new EditProgressionDTO(progressionId, chordIds, progression.getDescription(),
                                                        progression.getAudioUrl(), progression.getSampleMidiUrl(), progression.getIsCadence());

        model.addAttribute("form", dto);
        model.addAttribute("chords", chords);
        model.addAttribute("chordNoneValue", -1);

        return "admin/editProgressionForm";
    }

    @PostMapping("/admin/progressions/edit/{progressionId}")
    public String adminEditProgression(@PathVariable Long progressionId,
                                       @Valid @ModelAttribute("form") EditProgressionDTO dto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            Progression progression = progressionRepository.findOne(progressionId);
            List<Long> chordIds = progressionService.getChordIdsInProgression(progression);
            List<Chord> chords = chordRepository.findAll();

            dto.setId(progressionId);
            dto.setIsCadence(progression.getIsCadence());
            dto.setChordIds(chordIds);
            dto.setAudioUrl(progression.getAudioUrl());
            dto.setSampleMidiUrl(progression.getSampleMidiUrl());

            model.addAttribute("chords", chords);
            model.addAttribute("chordNoneValue", -1);

            return "admin/editProgressionForm";
        }

        progressionService.updateProgression(progressionId, dto);

        return "redirect:/admin/progressions";
    }

    @PostMapping("/admin/progressions/delete/{progressionId}")
    public String adminDeleteProgression(@PathVariable Long progressionId, Model model) {
        progressionService.deleteProgression(progressionId);

        return "redirect:/admin/progressions";
    }
}
