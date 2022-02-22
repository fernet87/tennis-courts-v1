package com.tenniscourts.schedules;

import java.util.List;
import java.util.stream.Collectors;

import com.tenniscourts.tenniscourts.TennisCourtMapperImpl;

public class ScheduleMapperImpl implements ScheduleMapper {

    public Schedule map(ScheduleDTO source) {
        Schedule target = null;
        if (source != null) {
            target = Schedule.builder()
                .tennisCourt(new TennisCourtMapperImpl().map(source.getTennisCourt()))
                .startDateTime(source.getStartDateTime())
                .endDateTime(source.getEndDateTime())
                .build();
            target.setId(source.getId());
        }
        return target;
    }

    public ScheduleDTO map(Schedule source) {
        return (source != null) ? 
            ScheduleDTO.builder()
                .id(source.getId())
                .tennisCourt(new TennisCourtMapperImpl().map(source.getTennisCourt()))
                .tennisCourtId((source.getTennisCourt() != null) ? source.getTennisCourt().getId() : 0)
                .startDateTime(source.getStartDateTime())
                .endDateTime(source.getEndDateTime())
                .build() :
            null;
    }

    public List<ScheduleDTO> map(List<Schedule> source) {
        return source.stream().map(schedule -> map(schedule)).collect(Collectors.toList());
    }
}
