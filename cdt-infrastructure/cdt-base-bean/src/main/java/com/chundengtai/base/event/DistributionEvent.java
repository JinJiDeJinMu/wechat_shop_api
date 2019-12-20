package com.chundengtai.base.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistributionEvent {

    public Long userId;
    public int referrerId;
    public String encryptCode;

}
