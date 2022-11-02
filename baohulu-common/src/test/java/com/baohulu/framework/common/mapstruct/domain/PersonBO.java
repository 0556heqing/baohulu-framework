package com.baohulu.framework.common.mapstruct.domain;

import com.baohulu.framework.common.adapter.XmlDataAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author heqing
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
@NoArgsConstructor
@Data
@ToString
public class PersonBO implements Serializable {

    @XmlElement(name = "id")
    private Long id;

    @XmlElement(name = "name")
    @XmlJavaTypeAdapter(XmlDataAdapter.class)
    private String name;

    @XmlElement(name = "remarkJson")
    @XmlJavaTypeAdapter(XmlDataAdapter.class)
    private String remarkJson;

    /**
     * 测试类型不一样 （Date -- String）
     */
    @XmlElement(name = "date")
    private Date date;

    /**
     * 测试属性名不一样 （sex -- gender）
     */
    @XmlElement(name = "sex")
    private Integer sex;

    /**
     * 测试list
     */
    @XmlElement(name = "nameParts")
    private List<String> nameParts;

    /**
     * 测试map
     */
    @XmlElement(name = "remarkMap")
    private Map<String, Object> remarkMap;

    /**
     * 测试对象
     */
    @XmlElement(name = "address")
    private Address address;

    /**
     * 测试枚举
     */
    @XmlElement(name = "type")
    private PersonEnum type;
}
