package com.chundengtai.base.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DistributionEvent {

    public int userId;
    public int referrerId;

}
