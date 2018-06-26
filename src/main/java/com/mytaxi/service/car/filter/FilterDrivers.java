package com.mytaxi.service.car.filter;

import java.util.List;

import com.mytaxi.datatransferobject.DriverDTO;
import com.mytaxi.domainobject.DriverDO;

public interface FilterDrivers {
    List<DriverDTO> filter(Iterable<DriverDO> driverDOIterable);
}
