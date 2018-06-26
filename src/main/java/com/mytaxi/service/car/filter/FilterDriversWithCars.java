package com.mytaxi.service.car.filter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.mytaxi.controller.mapper.DriverMapper;
import com.mytaxi.datatransferobject.DriverDTO;
import com.mytaxi.domainobject.DriverDO;

@Component("filterDriversWithCars")
public class FilterDriversWithCars implements FilterDrivers {
    @Override
    public List<DriverDTO> filter(Iterable<DriverDO> driverDOIterable) {
        List<DriverDTO> driversWithCars = new ArrayList<>();
        driverDOIterable.forEach(driverDO -> {
            if (driverDO.getCar() != null) {
                DriverDTO driverDTO = DriverMapper.makeDriverDTO(driverDO);
                driversWithCars.add(driverDTO);
            }
        });
        return driversWithCars;
    }
}
