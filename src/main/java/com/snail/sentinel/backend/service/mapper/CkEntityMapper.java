package com.snail.sentinel.backend.service.mapper;

import com.snail.sentinel.backend.domain.CkEntity;
import com.snail.sentinel.backend.service.dto.ck.CkEntityDTO;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CkEntityMapper implements EntityMapper<CkEntityDTO, CkEntity> {
    @Override
    public CkEntity toEntity(CkEntityDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CkEntity ckEntity = new CkEntity();

        ckEntity.setId(dto.getId());
        ckEntity.setName(dto.getName());
        ckEntity.setValue(dto.getValue());
        ckEntity.setToolVersion(dto.getToolVersion());
        ckEntity.setCommit(dto.getCommit());
        ckEntity.setMeasurableElement(dto.getMeasurableElementDTO());

        return ckEntity;
    }

    @Override
    public List<CkEntity> toEntity(List<CkEntityDTO> dtoList) {
        if ( dtoList == null ) {
            return Collections.emptyList();
        }

        List<CkEntity> list = new ArrayList<CkEntity>( dtoList.size() );
        for ( CkEntityDTO ckEntityDTO : dtoList ) {
            list.add( toEntity( ckEntityDTO ) );
        }

        return list;
    }
}
