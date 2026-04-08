package com.transport.tracking.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DocStatusVO {

    public String DocumentNo;
    public String DocumentStatus;
}
