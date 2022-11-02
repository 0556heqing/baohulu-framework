package com.baohulu.framework.common.mapstruct.domain;

import com.baohulu.framework.common.adapter.XmlDataAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author heqing
 */
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
@Data
@ToString
public class Address {

    @XmlElement(name = "province")
    @XmlJavaTypeAdapter(XmlDataAdapter.class)
    private String province;

    @XmlElement(name = "city")
    @XmlJavaTypeAdapter(XmlDataAdapter.class)
    private String city;

    @XmlElement(name = "area")
    @XmlJavaTypeAdapter(XmlDataAdapter.class)
    private String area;

}
