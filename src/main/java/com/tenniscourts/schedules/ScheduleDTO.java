package com.tenniscourts.schedules;

import com.tenniscourts.tenniscourts.TennisCourtDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Class representing a schedule tracked by the application.")
public class ScheduleDTO {

    @ApiModelProperty(notes = "Unique identifier of the schedule. No two schedules can have the same id.", example = "1", required = true, position = 0)
    private Long id;

    @ApiModelProperty(notes = "Tennis court.", required = false, position = 1)
    private TennisCourtDTO tennisCourt;

    @NotNull
    @ApiModelProperty(notes = "Tennis court identifier.", example = "1", required = true, position = 2)
    private Long tennisCourtId;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm")
    @NotNull
    @ApiModelProperty(notes = "Start date and time.", example = "2022-02-18T14:00", required = true, position = 3)
    private LocalDateTime startDateTime;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm")
    @ApiModelProperty(notes = "End date and time.", example = "2022-02-18T15:00", required = false, position = 4)
    private LocalDateTime endDateTime;

}
