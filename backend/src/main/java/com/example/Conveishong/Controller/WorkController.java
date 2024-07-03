package com.example.Conveishong.Controller;

import com.example.Conveishong.Dto.WorkDTO;
import com.example.Conveishong.Model.Work;
import com.example.Conveishong.Repository.WorkRepository;
import com.example.Conveishong.Service.WorkService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class WorkController {
    @Autowired
    private WorkService workService;
    @Autowired
    private WorkRepository workRepository;
    @PostMapping("/api/v1/createWorkTime/{userId}")
    public String createUserWorkTime(@PathVariable String userId, @RequestBody WorkDTO workDTO) {
        try {
            WorkDTO createdWork = workService.createUserWorkTime(Long.valueOf(userId), workDTO);
            return ("유저 근무시간 반영 성공!");
        } catch (Exception e) {
            return ("유저 근무시간 반영 실패!");
        }
    }
    @GetMapping("/api/v1/getWorkTime/{userId}")
    public ResponseEntity<?> getUserWorkTime(@PathVariable String userId) {
        try {
            Long userIdLong = Long.parseLong(userId);
            List<Work> workTimes = workService.getUserWorkTimes(userIdLong);

            if (workTimes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 사용자의 근무 시간 정보가 없습니다.");
            }

            List<WorkDTO> workDTOs = workTimes.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(workDTOs);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 사용자 ID 형식입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }
    private WorkDTO convertToDTO(Work work) {
        WorkDTO dto = new WorkDTO();
        dto.setWorkId(work.getWorkId());
        dto.setWorkDay(work.getWorkDay());
        dto.setWorkStartTime(work.getWorkStartTime());
        dto.setWorkEndTime(work.getWorkEndTime());
        dto.setUserId(work.getUserId());
        return dto;
    }
}

