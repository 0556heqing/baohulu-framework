package com.baohulu.framework.common.adapter;

import com.baohulu.framework.basic.consts.Separators;
import com.baohulu.framework.basic.consts.Time;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

/**
 * mapstruct 不同类型间的转换适配
 * 常用注解解释
 *  @Mapper         —— 表示该接口作为映射接口，编译时MapStruct处理器的入口
 *      uses：外部引入的转换类；
 *      componentModel：就是依赖注入，类似于在spring的servie层用@servie注入，那么在其他地方可以使用@Autowired取到值。
 *          默认：这个就是经常使用的 xxxMapper.INSTANCE.xxx;
 *          cdi：使用该属性，则在其他地方可以使用@Inject取到值；
 *          spring：使用该属性，则在其他地方可以使用@Autowired取到值；
 *          jsr330/Singleton：使用者两个属性，可以再其他地方使用@Inject取到值;
 *  @Mappings       —— 一组映射关系，值为一个数组，元素为@Mapping
 *  @Mapping        —— 一对映射关系
 *      target：目标属性，赋值的过程是把“源属性”赋值给“目标属性”；
 *      source：源属性，赋值的过程是把“源属性”赋值给“目标属性”；
 *      dateFormat：用于源属性是Date，转化为String;
 *      numberFormat：用户数值类型与String类型之间的转化;
 *      constant：不管源属性，直接将“目标属性”置为常亮；
 *      expression：使用表达式进行属性之间的转化;
 *      ignore：忽略某个属性的赋值;
 *      qualifiedByName：根据自定义的方法进行赋值;
 *      defaultValue：默认值;
 *  @MappingTarget  ——  用在方法参数的前面。使用此注解，源对象同时也会作为目标对象，用于更新。
 *  @Named          ——  定义类/方法的名称
 *  @InheritConfiguration   ——  指定映射方法
 *  @InheritInverseConfiguration    ——  表示方法继承相应的反向方法的反向配置
 *
 *
 * @author heqing
 * @date 2022/10/19 14:33
 */
public class MapstructAdapter {

    public String dateToString(Date date) {
        if(date == null) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Time.YYYY_MM_DD_HH_MM_SS);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Date stringToDate(String dateStr) {
        if(dateStr == null) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Time.YYYY_MM_DD_HH_MM_SS);
            return sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Long dateToLong(Date date) {
        return date.getTime();
    }

    public Date longToDate(Long date) {
        return new Date(date);
    }

    public String listToString(List<String> list) {
        return String.join(Separators.COMMA, list);
    }

    public List<String> stringToList(String str) {
        return Arrays.asList(str.split(Separators.COMMA));
    }

    public String longListToString(List<Long> list) {
        return list.stream().filter(Objects::nonNull).map(Object::toString).collect(joining(Separators.COMMA));
    }

    public List<Long> stringToLongList(String str) {
        return Arrays.stream(str.split(Separators.COMMA))
                .filter(s -> StringUtils.isNotBlank(s) && StringUtils.isNumeric(s))
                .map(Long::parseLong)
                .collect(Collectors.toList()
                );
    }
}
