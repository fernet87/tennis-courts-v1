package com.tenniscourts.schedules;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.tenniscourts.TennisCourtMapper;
import com.tenniscourts.tenniscourts.TennisCourtRepository;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ScheduleService {

    private ScheduleRepository scheduleRepository;

    private ScheduleMapper scheduleMapper;

    private TennisCourtRepository tennisCourtRepository;

    private TennisCourtMapper tennisCourtMapper;

    public ScheduleDTO addSchedule(Long tennisCourtId, CreateScheduleRequestDTO createScheduleRequestDTO) {
        ScheduleDTO newSchedule = ScheduleDTO.builder()
            .tennisCourtId(tennisCourtId)
            .startDateTime(createScheduleRequestDTO.getStartDateTime())
            .endDateTime(createScheduleRequestDTO.getStartDateTime().plusHours(1L))
            .tennisCourt(tennisCourtRepository.findById(tennisCourtId).map(tennisCourtMapper::map).orElseThrow(() -> {
                throw new EntityNotFoundException("Tennis Court not found.");
            }))
            .build();
        
        return scheduleMapper.map(scheduleRepository.saveAndFlush(scheduleMapper.map(newSchedule)));
    }

    public List<ScheduleDTO> findSchedulesByDates(LocalDateTime startDate, LocalDateTime endDate) {
        return scheduleRepository.findAllByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(startDate, endDate).stream().map(schedule -> { return scheduleMapper.map(schedule); }).collect(Collectors.toList());
    }

    public ScheduleDTO findSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).map(scheduleMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Schedule " + scheduleId + " not found.");
        });
    }

    public List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId) {
        return scheduleMapper.map(scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(tennisCourtId));
    }
}
