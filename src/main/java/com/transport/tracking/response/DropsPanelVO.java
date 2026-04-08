package com.transport.tracking.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DropsPanelVO {

    public List<DropsVO> drops = new ArrayList<>();
    public List<PickUPVO> pickUps = new ArrayList<>();
}
