package com.lyjava.domain.vos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkVo {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    private String name;

    /**
     *
     */
    private String logo;

    /**
     * 描述
     */
    private String description;

    /**
     * 网站地址
     */
    private String address;
}
