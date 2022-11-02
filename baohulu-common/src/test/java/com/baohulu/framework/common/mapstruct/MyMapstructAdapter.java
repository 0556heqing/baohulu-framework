package com.baohulu.framework.common.mapstruct;

import com.baohulu.framework.common.adapter.MapstructAdapter;
import org.mapstruct.Named;

import java.util.List;

/**
 * @author heqing
 * @date 2022/10/21 11:52
 */
public class MyMapstructAdapter extends MapstructAdapter {

    @Named("setFirstName")
    public String setFirstName(List<String> nameParts) {
        return nameParts.get(0);
    }
}
