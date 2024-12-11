/**
 * ChordController
 *
 * [1. 역할]
 * - 관리자 페이지에서 코드(Chord) 관련 데이터를 관리하는 컨트롤러.
 * - 코드의 목록 조회, 생성, 수정, 삭제 요청을 처리하며, 해당 정보를 사용자 인터페이스에 전달.
 *
 * [2. 주요 기능]
 * - 코드 목록을 조회하여 관리자 페이지에서 표시.
 * - 새로운 코드 생성 폼 표시 및 생성 처리.
 * - 코드 수정 폼 표시 및 수정 처리.
 * - 코드 삭제 요청 처리.
 *
 * [3. 사용 사례]
 * - 관리자 페이지에서 코드 목록을 확인하고, 새로운 코드를 추가하거나 기존 코드를 수정/삭제할 때 사용.
 */

package jhhan.harmonynow_backend.controller;

import jakarta.validation.Valid;
import jhhan.harmonynow_backend.domain.Chord;
import jhhan.harmonynow_backend.dto.CreateChordDTO;
import jhhan.harmonynow_backend.dto.EditChordDTO;
import jhhan.harmonynow_backend.dto.ReadChordDTO;
import jhhan.harmonynow_backend.exception.ChordNotDeletableException;
import jhhan.harmonynow_backend.repository.ChordRepository;
import jhhan.harmonynow_backend.service.ChordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ChordController {

    private final ChordService chordService;
    private final ChordRepository chordRepository;

    /* admin 전용 */
    @GetMapping("/admin/chords")
    public String adminChordList(Model model) {
        List<Chord> chords = chordRepository.findAll();
        List<ReadChordDTO> dtoList = chords.stream().map(ReadChordDTO::new).toList();
        model.addAttribute("dtoList", dtoList);
        return "admin/chordList";
    }

    @GetMapping("/admin/chords/new")
    public String adminCreateChordForm(Model model) {
        model.addAttribute("form", new CreateChordDTO());
        return "admin/createChordForm";
    }

    @PostMapping("/admin/chords/new")
    public String adminCreateChord(@Valid @ModelAttribute("form") CreateChordDTO dto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "admin/createChordForm";
        }

        chordService.saveChord(dto);
        return "redirect:/admin/chords";
    }

    @GetMapping("/admin/chords/edit/{chordId}")
    public String adminEditChordForm(@PathVariable Long chordId, Model model) {
        Chord chord = chordRepository.findOne(chordId);
        EditChordDTO dto = new EditChordDTO(chord.getId(), chord.getName(), chord.getDescription(),
                chord.getLevel(), chord.getIsPublic(), chord.getImageUrl(), chord.getAudioUrl());

        model.addAttribute("form", dto);

        return "admin/editChordForm";
    }

    @PostMapping("/admin/chords/edit/{chordId}")
    public String adminEditChord(@PathVariable Long chordId,
                                 @Valid @ModelAttribute("form") EditChordDTO dto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            Chord chord = chordRepository.findOne(chordId);
            dto.setId(chordId);
            dto.setImageUrl(chord.getImageUrl());
            dto.setAudioUrl(chord.getAudioUrl());

            return "admin/editChordForm";
        }

        chordService.updateChord(chordId, dto);
        return "redirect:/admin/chords";
    }

    @PostMapping("/admin/chords/delete/{chordId}")
    public String adminDeleteChord(@PathVariable Long chordId, Model model, RedirectAttributes redirectAttributes) {
        try {
            chordService.deleteChord(chordId);
        } catch (ChordNotDeletableException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/admin/chords";
    }
}
