package com.tenniscourts.schedules;

import com.tenniscourts.config.BaseRestController;
import lombok.AllArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/schedule")
@Api(description = "Set of endpoints for Creating and Retrieving of Schedules.")
public class ScheduleController extends BaseRestController {

    private final ScheduleService scheduleService;

    @PostMapping("/add")
    @ApiOperation("Creates a new schedule.")
    public ResponseEntity<Void> addScheduleTennisCourt(@RequestBody CreateScheduleRequestDTO createScheduleRequestDTO) {
        return ResponseEntity.created(locationByEntity(scheduleService.addSchedule(createScheduleRequestDTO.getTennisCourtId(), createScheduleRequestDTO).getId())).build();
    }

    @GetMapping("/{id}")
    @ApiOperation("Returns a specific schedule by their identifier. 404 if does not exist.")
    public ResponseEntity<ScheduleDTO> findByScheduleId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(scheduleService.findSchedule(id));
    }

    @GetMapping()
    @ApiOperation("Returns a schedule list between a start date and a end date.")
    public ResponseEntity<List<ScheduleDTO>> findSchedulesByDates(
        @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(scheduleService.findSchedulesByDates(LocalDateTime.of(startDate, LocalTime.of(0, 0)), LocalDateTime.of(endDate, LocalTime.of(23, 59))));
    }

}
