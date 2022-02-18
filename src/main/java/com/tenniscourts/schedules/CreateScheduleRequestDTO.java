package com.tenniscourts.schedules;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel(description = "Class representing a request object to create a new schedule.")
public class CreateScheduleRequestDTO {

    @NotNull
    @ApiModelProperty(notes = "Tennis court identifier required to create a schedule.", example = "1", required = true, position = 0)
    private Long tennisCourtId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull
    @ApiModelProperty(notes = "Start schedule date and time.", example = "2022-02-18T14:00", required = true, position = 1)
    private LocalDateTime startDateTime;

}
