package cn.egenie.architect.demo.product.domain;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Test {
    private Integer id;

    private String name;

    private Integer valid;

    private Date createTime;

    private Date updateTime;
}