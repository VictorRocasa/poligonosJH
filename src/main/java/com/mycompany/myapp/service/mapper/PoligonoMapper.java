package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Forma;
import com.mycompany.myapp.domain.Poligono;
import com.mycompany.myapp.service.dto.FormaDTO;
import com.mycompany.myapp.service.dto.PoligonoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Poligono} and its DTO {@link PoligonoDTO}.
 */
@Mapper(componentModel = "spring")
public interface PoligonoMapper extends EntityMapper<PoligonoDTO, Poligono> {
    @Mapping(target = "forma", source = "forma", qualifiedByName = "formaId")
    PoligonoDTO toDto(Poligono s);

    @Named("formaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FormaDTO toDtoFormaId(Forma forma);
}
