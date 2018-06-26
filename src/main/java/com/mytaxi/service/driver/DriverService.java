package com.mytaxi.service.driver;

import com.mytaxi.datatransferobject.DriverDTO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.DriverAction;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.CarAlreadyInUseException;
import com.mytaxi.exception.CarNotAssignedToDriver;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.DriverAlreadyHasACarException;
import com.mytaxi.exception.DriverNotOnline;
import com.mytaxi.exception.EntityNotFoundException;
import java.util.List;
import java.util.Map;

public interface DriverService
{

    DriverDO find(Long driverId) throws EntityNotFoundException;

    DriverDO create(DriverDO driverDO) throws ConstraintsViolationException;

    void delete(Long driverId) throws EntityNotFoundException;

    void updateLocation(long driverId, double longitude, double latitude) throws EntityNotFoundException;

    List<DriverDO> find(OnlineStatus onlineStatus);

    void assignOrUnassignCar(long driverId, long carId, DriverAction driverAction)
            throws CarAlreadyInUseException, EntityNotFoundException, DriverAlreadyHasACarException, DriverNotOnline, CarNotAssignedToDriver;

    Map<String, List<DriverDTO>> getOnlineDriversWithAndWithoutCars();
}
