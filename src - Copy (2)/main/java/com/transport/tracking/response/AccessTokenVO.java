package com.transport.tracking.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccessTokenVO {

    public String accessToken;
    public List<String> permissions;
    public UserVO userVO;
}
