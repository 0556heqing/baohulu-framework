package com.baohulu.framework.common.domain;

import com.baohulu.framework.common.utils.file.pdf.document.AbstractDocumentVo;
import lombok.Data;

import java.io.Serializable;

/**
 * @author heqing
 * @date 2022/09/19 09:49
 */
@Data
public class RecipeVo extends AbstractDocumentVo implements Serializable {
    private static final long serialVersionUID = -9177662149705377710L;
    private String hospitalName;
    private String recipeCode;
    private String recipeTime;
    private String patientName;
    private String patientSex;
    private String patientBirthDay;
    private String archiveId;
    private String payType;
    private String address;
    private String patientTel;
    private String departmentName;
    private String doctorDesc;
    private String doctorSeal;
    private String dosagePharmacistSeal;
    private String confirmPharmacistSeal;
    private String remark;
    private String age;
    private String ageType;
    private String time1;
    private String time2;
    private String time3;
    private String doctor1;
    private String doctor2;
    private String doctor3;

    @Override
    public String findPrimaryKey() {
        return this.recipeCode;
    }
}
