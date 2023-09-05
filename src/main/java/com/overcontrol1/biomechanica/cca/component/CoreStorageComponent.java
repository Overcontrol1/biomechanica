package com.overcontrol1.biomechanica.cca.component;

import com.overcontrol1.biomechanica.item.util.CoreType;
import dev.onyxstudios.cca.api.v3.component.Component;

public interface CoreStorageComponent extends Component {
    CoreType getCoreType();
    void setCoreType(CoreType type);
}
