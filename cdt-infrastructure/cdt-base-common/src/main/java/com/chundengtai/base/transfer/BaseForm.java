package com.chundengtai.base.transfer;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author royalmac
 */
@Data
@NoArgsConstructor
public class BaseForm<T> {

    private Integer pageIndex;
    private Integer pageSize;
    private T data;

    private String sortField;
    private String order;

}
