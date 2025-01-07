package com.example.demo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffSalaryEntity {

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "职称")
    private String post;

    @ApiModelProperty(value = "月薪")
    private BigDecimal mouthSalary;

    @ApiModelProperty(value = "时薪")
    private BigDecimal hourSalary;

    @ApiModelProperty(value = "应出勤天数")
    private Double shouldAttend;

    @ApiModelProperty(value = "出勤天数")
    private Double actualAttend;

    @ApiModelProperty(value = "平时加班时数")
    private Double overtime;

    @ApiModelProperty(value = "周末加班时数")
    private Double weekOvertime;

    @ApiModelProperty(value = "国假天数")
    private Double holiday;

    @ApiModelProperty(value = "正班薪资")
    private BigDecimal normalSalary;

    @ApiModelProperty(value = "平时加班薪资")
    private BigDecimal overtimeSalary;

    @ApiModelProperty(value = "周末加班薪资")
    private BigDecimal weekOvertimeSalary;

    @ApiModelProperty(value = "国假薪资")
    private BigDecimal holidaySalary;

    @ApiModelProperty(value = "岗位津贴")
    private BigDecimal postSubsidy;

    @ApiModelProperty(value = "全勤补贴")
    private BigDecimal fullAttendSubsidy;

    @ApiModelProperty(value = "全勤奖")
    private BigDecimal award;

    @ApiModelProperty(value = "事假缺勤扣款")
    private BigDecimal deduction;

    @ApiModelProperty(value = "社保费用")
    private BigDecimal social;

    @ApiModelProperty(value = "应得薪资")
    private BigDecimal shouldSalary;

    @ApiModelProperty(value = "个人扣税")
    private BigDecimal selfTax;

    @ApiModelProperty(value = "实发薪资")
    private BigDecimal actualSalary;

    @ApiModelProperty(value = "员工签名")
    private BigDecimal sign;
}
