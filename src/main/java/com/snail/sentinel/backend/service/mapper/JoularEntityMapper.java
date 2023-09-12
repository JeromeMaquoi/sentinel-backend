package com.snail.sentinel.backend.service.mapper;

import com.snail.sentinel.backend.domain.JoularEntity;
import com.snail.sentinel.backend.service.dto.joular.JoularEntityDTO;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class JoularEntityMapper implements EntityMapper<JoularEntityDTO, JoularEntity> {
    @Override
    public JoularEntity toEntity(JoularEntityDTO dto) {
        if ( dto == null ) {
            return null;
        }

        JoularEntity joularEntity = new JoularEntity();

        joularEntity.setId( dto.getId() );
        joularEntity.setValue( dto.getValue() );
        joularEntity.setScope( dto.getScope() );
        joularEntity.setMonitoringType( dto.getMonitoringType() );
        joularEntity.setMeasurableElement(dto.getMethodElementDTO());
        joularEntity.setIteration(dto.getIterationDTO());
        joularEntity.setCommit(dto.getCommitSimpleDTO());

        return joularEntity;
    }

    @Override
    public List<JoularEntity> toEntity(List<JoularEntityDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<JoularEntity> list = new ArrayList<JoularEntity>( dtoList.size() );
        for ( JoularEntityDTO joularEntityDTO : dtoList ) {
            list.add( toEntity( joularEntityDTO ) );
        }

        return list;
    }
}
