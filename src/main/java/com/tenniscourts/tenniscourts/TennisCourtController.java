package com.tenniscourts.tenniscourts;

import java.time.LocalDateTime;

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

@AllArgsConstructor
@RestController
@RequestMapping("/tennisCourt")
@Api(description = "Set of endpoints for Creating and Retrieving of Tennis Courts.")
public class TennisCourtController extends BaseRestController {

    private final TennisCourtService tennisCourtService;

    @PostMapping("/add")
    @ApiOperation("Creates a new tennis court.")
    public ResponseEntity<Void> addTennisCourt(@RequestBody TennisCourtDTO tennisCourtDTO) {
        return ResponseEntity.created(locationByEntity(tennisCourtService.addTennisCourt(tennisCourtDTO).getId())).build();
    }

    @PostMapping("/tennisCourt/createScheduleSlots/{id}")
    @ApiOperation("Creates schedule slots from start date time to end date time and returns a tennis court with a list of this slots.")
    public ResponseEntity<TennisCourtDTO> addScheduleSlotsToTennisCourt(
        @PathVariable("id") Long id,
        @RequestParam("startDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
        @RequestParam("endDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime
    ) {
        return ResponseEntity.ok(tennisCourtService.addScheduleSlotsToTennisCourt(id, startDateTime, endDateTime));
    }

    @GetMapping("/{id}")
    @ApiOperation("Returns a specific tennis court by their identifier. 404 if does not exist.")
    public ResponseEntity<TennisCourtDTO> findTennisCourtById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(tennisCourtService.findTennisCourtById(id));
    }

    @GetMapping("/withSchedules/{id}")
    @ApiOperation("Returns a specific tennis court with their schedule list by their identifier. 404 if does not exist.")
    public ResponseEntity<TennisCourtDTO> findTennisCourtWithSchedulesById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(tennisCourtService.findTennisCourtWithSchedulesById(id));
    }
}
