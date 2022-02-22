package com.tenniscourts.tenniscourts;

public class TennisCourtMapperImpl implements TennisCourtMapper {
    public TennisCourt map(TennisCourtDTO source) {
        TennisCourt target = null;
        if (source != null) {
            target = TennisCourt.builder().name(source.getName()).build();
            target.setId(source.getId());
        }
        return target;
    }

    public TennisCourtDTO map(TennisCourt source) {
        return (source != null) ? TennisCourtDTO.builder().id(source.getId()).name(source.getName()).build() : null;
    }
}
