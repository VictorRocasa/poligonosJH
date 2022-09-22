package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Forma;
import com.mycompany.myapp.service.dto.FormaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Forma} and its DTO {@link FormaDTO}.
 */
@Mapper(componentModel = "spring")
public interface FormaMapper extends EntityMapper<FormaDTO, Forma> {
    @Mapping(target = "agrupamento", source = "agrupamento", qualifiedByName = "formaId")
    FormaDTO toDto(Forma s);

    @Named("formaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FormaDTO toDtoFormaId(Forma forma);
}
