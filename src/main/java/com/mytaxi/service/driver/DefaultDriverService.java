package com.mytaxi.service.driver;

import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mytaxi.dataaccessobject.DriverRepository;
import com.mytaxi.datatransferobject.DriverDTO;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.DriverAction;
import com.mytaxi.domainvalue.GeoCoordinate;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.CarAlreadyInUseException;
import com.mytaxi.exception.CarNotAssignedToDriver;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.DriverAlreadyHasACarException;
import com.mytaxi.exception.DriverNotOnline;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.helper.FilterDriversHelper;
import com.mytaxi.service.car.CarService;

/**
 * Service to encapsulate the link between DAO and controller and to have business logic for some driver specific things.
 * <p/>
 */
@Service
public class DefaultDriverService implements DriverService
{

    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(DefaultDriverService.class);

    private final DriverRepository driverRepository;
    private final CarService carService;
    private final FilterDriversHelper filterDriversHelper;


    public DefaultDriverService(final DriverRepository driverRepository, final CarService carService, final FilterDriversHelper filterDriversHelper)
    {
        this.driverRepository = driverRepository;
        this.carService = carService;
        this.filterDriversHelper = filterDriversHelper;
    }


    /**
     * Selects a driver by id.
     *
     * @param driverId
     * @return found driver
     * @throws EntityNotFoundException if no driver with the given id was found.
     */
    @Override
    public DriverDO find(Long driverId) throws EntityNotFoundException
    {
        return findDriverChecked(driverId);
    }


    /**
     * Creates a new driver.
     *
     * @param driverDO
     * @return
     * @throws ConstraintsViolationException if a driver already exists with the given username, ... .
     */
    @Override
    public DriverDO create(DriverDO driverDO) throws ConstraintsViolationException
    {
        DriverDO driver;
        try
        {
            driver = driverRepository.save(driverDO);
        }
        catch (DataIntegrityViolationException e)
        {
            LOG.warn("Some constraints are thrown due to driver creation", e);
            throw new ConstraintsViolationException(e.getMessage());
        }
        return driver;
    }


    /**
     * Deletes an existing driver by id.
     *
     * @param driverId
     * @throws EntityNotFoundException if no driver with the given id was found.
     */
    @Override
    @Transactional
    public void delete(Long driverId) throws EntityNotFoundException
    {
        DriverDO driverDO = findDriverChecked(driverId);
        driverDO.setDeleted(true);
    }


    /**
     * Update the location for a driver.
     *
     * @param driverId
     * @param longitude
     * @param latitude
     * @throws EntityNotFoundException
     */
    @Override
    @Transactional
    public void updateLocation(long driverId, double longitude, double latitude) throws EntityNotFoundException
    {
        DriverDO driverDO = findDriverChecked(driverId);
        driverDO.setCoordinate(new GeoCoordinate(latitude, longitude));
    }


    /**
     * Find all drivers by online state.
     *
     * @param onlineStatus
     */
    @Override
    public List<DriverDO> find(OnlineStatus onlineStatus)
    {
        return driverRepository.findByOnlineStatus(onlineStatus);
    }

    @Override
    @Transactional
    public void assignOrUnassignCar(long driverId, long carId, DriverAction driverAction)
            throws CarAlreadyInUseException, EntityNotFoundException, DriverAlreadyHasACarException, DriverNotOnline, CarNotAssignedToDriver {

        DriverDO driverDO = findDriverChecked(driverId);;
        CarDO carDO = carService.find(carId);

        switch (driverAction) {
            case SELECT:
                if (driverDO.getCar() != null) {
                    LOG.info("Driver with id {} is already assigned a car with id {}", driverDO.getId(), driverDO.getCar().getId());
                    throw new DriverAlreadyHasACarException("This driver is already assigned a car");
                } else if (carDO.getDriver() != null) {
                    LOG.info("Car with id {} is already assigned a driver with id {}", carId, carDO.getDriver().getId());
                    throw new CarAlreadyInUseException("Car with id " + carId + " is already assigned to another driver");
                } else {
                    if (driverDO.getOnlineStatus() == OnlineStatus.ONLINE) {
                        driverDO.setCar(carDO);
                    } else {
                        LOG.info("Driver with id {} is not ONLINE", driverDO.getId());
                        throw new DriverNotOnline("Cannot assign the car as driver with id " + driverId + " is not online");
                    }
                }
                break;
            case DESELECT:
                if (driverDO.getCar() != null && driverDO.getCar().getId() == carId && driverDO.getId() == driverId) {
                    driverDO.setCar(null);
                } else {
                    LOG.info("Car with id {} is not assigned to driver with id {}", carId, driverId);
                    throw new CarNotAssignedToDriver("This car is not assigned to this driver");
                }
                break;
        }
    }

    @Override
    public Map<String, List<DriverDTO>> getOnlineDriversWithAndWithoutCars() {
        Iterable<DriverDO> driverDOIterable = driverRepository.findAll();
        return filterDriversHelper.filter(driverDOIterable);
    }

    private DriverDO findDriverChecked(Long driverId) throws EntityNotFoundException
    {
        DriverDO driverDO = driverRepository.findOne(driverId);
        if (driverDO == null)
        {
            throw new EntityNotFoundException("Could not find entity with id: " + driverId);
        }
        return driverDO;
    }

}
