package com.baohulu.framework.common.domain;

import com.baohulu.framework.common.mapstruct.domain.PersonBO;
import com.baohulu.framework.common.utils.validate.annotation.StringValidation;
import com.baohulu.framework.common.utils.validate.enums.RegexTypeEnum;
import lombok.Data;

/**
 * @author heqing
 * @date 2022/10/27 10:36
 */
@Data
public class StudentBO extends PersonBO {

    @StringValidation(nullAble=false, blackAble=false, regexType= RegexTypeEnum.NUMBER, min=100, max=200, description="编号")
    private String no;
}
