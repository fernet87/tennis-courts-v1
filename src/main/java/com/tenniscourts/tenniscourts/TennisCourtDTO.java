package com.tenniscourts.tenniscourts;

import com.tenniscourts.schedules.ScheduleDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Class representing a tennis court tracked by the application.")
public class TennisCourtDTO {

    @ApiModelProperty(notes = "Unique identifier of the tennis court. No two tennis courts can have the same id.", example = "1", required = true, position = 0)
    private Long id;

    @NotNull
    @ApiModelProperty(notes = "Name of the tennis court.", example = "Court Philippe-Chatrier", required = true, position = 1)
    private String name;

    @ApiModelProperty(notes = "List of schedules of the tennis court.", required = false, position = 2)
    private List<ScheduleDTO> tennisCourtSchedules;

}
