package com.baohulu.framework.common.mapstruct;

import com.baohulu.framework.common.mapstruct.domain.PersonBO;
import com.baohulu.framework.common.mapstruct.domain.PersonDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author heqing
 * @date 2022/10/19 11:25
 */
@Mapper(uses= MyMapstructAdapter.class)
public interface PersonMapstructConverter {

    PersonMapstructConverter INSTANCE = Mappers.getMapper(PersonMapstructConverter.class);

    /**
     * 对象类型转化
     * @param personBo
     * @return
     */
    @Mappings({
            @Mapping(source = "nameParts", target = "firstName", qualifiedByName="setFirstName"),
            @Mapping(constant="庆", target = "lastName"),
            @Mapping(source = "sex", target = "gender")
    })
    PersonDTO boToDto(PersonBO personBo);

    /**
     * 对象类型转化
     * @param personDTO
     * @return
     */
    @Mappings({
            @Mapping(source = "gender", target = "sex")
    })
    PersonBO dtoToBo(PersonDTO personDTO);

}
