package com.platform.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;


/**
 * @date 2017-08-15 08:03:41
 */
@Data
@Slf4j
@NoArgsConstructor
public class AttributeCategoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private String bannerUrl;
    private Integer showStyle;
}
