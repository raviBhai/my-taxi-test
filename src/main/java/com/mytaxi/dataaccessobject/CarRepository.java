package com.mytaxi.dataaccessobject;

import org.springframework.data.repository.CrudRepository;

import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;

public interface CarRepository extends CrudRepository<CarDO, Long> {
}
