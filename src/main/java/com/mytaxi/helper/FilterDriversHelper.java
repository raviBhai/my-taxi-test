package com.mytaxi.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.mytaxi.datatransferobject.DriverDTO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.service.car.filter.FilterDrivers;

@Component
public class FilterDriversHelper {
    private final FilterDrivers filterDriversWithCars;
    private final FilterDrivers filterDriversWithoutCars;

    @Autowired
    public FilterDriversHelper(
            @Qualifier("filterDriversWithCars") FilterDrivers filterDriversWithCars,
            @Qualifier("filterDriversWithoutCars") FilterDrivers filterDriversWithoutCars
    ) {
        this.filterDriversWithCars = filterDriversWithCars;
        this.filterDriversWithoutCars = filterDriversWithoutCars;
    }

    public Map<String, List<DriverDTO>> filter(Iterable<DriverDO> driverDOIterable) {
        Map<String, List<DriverDTO>> drivers = new HashMap<>();
        drivers.put("driversWithCars", filterDriversWithCars.filter(driverDOIterable));
        drivers.put("driversWithoutCars", filterDriversWithoutCars.filter(driverDOIterable));
        return drivers;
    }

}
