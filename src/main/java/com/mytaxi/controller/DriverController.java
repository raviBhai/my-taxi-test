package com.mytaxi.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mytaxi.controller.mapper.DriverMapper;
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
import com.mytaxi.service.driver.DriverService;

/**
 * All operations with a driver will be routed by this controller.
 * <p/>
 */
@RestController
@RequestMapping("v1/drivers")
public class DriverController
{

    private final DriverService driverService;

    @Autowired
    public DriverController(final DriverService driverService)
    {
        this.driverService = driverService;
    }

    @GetMapping("/{driverId}")
    @PreAuthorize("hasRole('ROLE_DRIVER') OR hasRole('ROLE_DRIVER_ADMIN')")
    public DriverDTO getDriver(@Valid @PathVariable long driverId) throws EntityNotFoundException
    {
        return DriverMapper.makeDriverDTO(driverService.find(driverId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_DRIVER_ADMIN')")
    public DriverDTO createDriver(@Valid @RequestBody DriverDTO driverDTO) throws ConstraintsViolationException
    {
        DriverDO driverDO = DriverMapper.makeDriverDO(driverDTO);
        return DriverMapper.makeDriverDTO(driverService.create(driverDO));
    }

    @DeleteMapping("/{driverId}")
    @PreAuthorize("hasRole('ROLE_DRIVER_ADMIN')")
    public void deleteDriver(@Valid @PathVariable long driverId) throws EntityNotFoundException
    {
        driverService.delete(driverId);
    }

    @PutMapping("/{driverId}")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public void updateLocation(
        @Valid @PathVariable long driverId, @RequestParam double longitude, @RequestParam double latitude)
        throws ConstraintsViolationException, EntityNotFoundException
    {
        driverService.updateLocation(driverId, longitude, latitude);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_DRIVER_ADMIN')")
    public List<DriverDTO> findDrivers(@RequestParam OnlineStatus onlineStatus)
        throws ConstraintsViolationException, EntityNotFoundException
    {
        return DriverMapper.makeDriverDTOList(driverService.find(onlineStatus));
    }

    @PutMapping("/{driverId}/car/{carId}")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public void assignOrUnassignCar(
            @Valid @PathVariable long driverId,
            @Valid @PathVariable long carId,
            @RequestParam DriverAction driverAction)
            throws CarAlreadyInUseException, EntityNotFoundException, DriverAlreadyHasACarException, DriverNotOnline, CarNotAssignedToDriver {
        driverService.assignOrUnassignCar(driverId, carId, driverAction);
    }

    /**
     * This API is used to get a list of ONLINE drivers with and without cars.
     * @return
     */
    @GetMapping("/driversWithAndWithoutCars")
    @PreAuthorize("hasRole('ROLE_DRIVER_ADMIN')")
    public Map<String, List<DriverDTO>> getOnlineDriversWithAndWithoutCars() {
        return driverService.getOnlineDriversWithAndWithoutCars();
    }

}
