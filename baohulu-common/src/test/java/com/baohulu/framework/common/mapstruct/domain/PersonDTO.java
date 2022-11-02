package com.baohulu.framework.common.mapstruct.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author heqing
 */
@NoArgsConstructor
@Data
@ToString
public class PersonDTO implements Serializable {

    private Long id;
    private String name;
    private String remarkJson;
    private String date;
    private String gender;
    private String firstName;
    private String lastName;
    private List<String> nameParts;
    private Map<String, Object> remarkMap;
    private Address address;
    private PersonEnum type;
}
