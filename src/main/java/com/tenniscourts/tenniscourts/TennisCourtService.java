package com.tenniscourts.tenniscourts;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.schedules.CreateScheduleRequestDTO;
import com.tenniscourts.schedules.ScheduleDTO;
import com.tenniscourts.schedules.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TennisCourtService {

    private final TennisCourtRepository tennisCourtRepository;

    private final ScheduleService scheduleService;

    private final TennisCourtMapper tennisCourtMapper;

    public TennisCourtDTO addTennisCourt(TennisCourtDTO tennisCourt) {
        return tennisCourtMapper.map(tennisCourtRepository.saveAndFlush(tennisCourtMapper.map(tennisCourt)));
    }

    public TennisCourtDTO addScheduleSlotsToTennisCourt(Long id, LocalDateTime startDate, LocalDateTime endDate) {
        TennisCourtDTO tennisCourt = findTennisCourtById(id);
        long hours = ChronoUnit.HOURS.between(startDate, endDate);

        tennisCourt.setTennisCourtSchedules(new ArrayList<>());

        for (int hoursIndex = 0; hoursIndex <= hours; hoursIndex++) {
            LocalDateTime date = startDate;
            CreateScheduleRequestDTO createDTO = CreateScheduleRequestDTO.builder().tennisCourtId(id).startDateTime(date.plusHours(hoursIndex)).build();
            ScheduleDTO schedule = scheduleService.addSchedule(id, createDTO);
            tennisCourt.getTennisCourtSchedules().add(schedule);
        }

        return tennisCourt;
    }

    public TennisCourtDTO findTennisCourtById(Long id) {
        return tennisCourtRepository.findById(id).map(tennisCourtMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Tennis Court " + id + " not found.");
        });
    }

    public TennisCourtDTO findTennisCourtWithSchedulesById(Long tennisCourtId) {
        TennisCourtDTO tennisCourtDTO = findTennisCourtById(tennisCourtId);
        tennisCourtDTO.setTennisCourtSchedules(scheduleService.findSchedulesByTennisCourtId(tennisCourtId));
        return tennisCourtDTO;
    }
}
