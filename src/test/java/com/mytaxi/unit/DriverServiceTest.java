package com.mytaxi.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mytaxi.dataaccessobject.DriverRepository;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.DriverAction;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.CarAlreadyInUseException;
import com.mytaxi.exception.CarNotAssignedToDriver;
import com.mytaxi.exception.DriverAlreadyHasACarException;
import com.mytaxi.exception.DriverNotOnline;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.helper.FilterDriversHelper;
import com.mytaxi.service.car.CarService;
import com.mytaxi.service.driver.DefaultDriverService;

@RunWith(MockitoJUnitRunner.class)
public class DriverServiceTest {

    @Mock
    DriverRepository driverRepository;
    @Mock
    CarService carService;
    @Mock
    FilterDriversHelper filterDriversHelper;

    DefaultDriverService defaultDriverService;

    @Before
    public void setUp() {
        defaultDriverService = new DefaultDriverService(driverRepository, carService, filterDriversHelper);
    }

    @Test
    public void successfullyAssignDriverToACar() throws EntityNotFoundException, CarNotAssignedToDriver, CarAlreadyInUseException, DriverAlreadyHasACarException, DriverNotOnline {
        DriverDO driverDO = new DriverDO("uname", "pwd");
        driverDO.setId(1l);
        driverDO.setOnlineStatus(OnlineStatus.ONLINE);

        CarDO carDO = new CarDO();
        carDO.setId(1l);

        long driverId = 1;
        long carId = 1;
        when(driverRepository.findOne(driverId)).thenReturn(driverDO);
        when(carService.find(carId)).thenReturn(carDO);

        defaultDriverService.assignOrUnassignCar(driverId, carId, DriverAction.SELECT);
        assertThat(driverDO.getCar().getId()).isEqualTo(1);
    }

    @Test(expected = DriverNotOnline.class)
    public void cannotAssignCarAsDriverNotOnline() throws EntityNotFoundException, CarNotAssignedToDriver, CarAlreadyInUseException, DriverAlreadyHasACarException, DriverNotOnline {
        DriverDO driverDO = new DriverDO("uname", "pwd");
        driverDO.setId(1l);
        driverDO.setOnlineStatus(OnlineStatus.OFFLINE);

        CarDO carDO = new CarDO();
        carDO.setId(1l);

        long driverId = 1;
        long carId = 1;
        when(driverRepository.findOne(driverId)).thenReturn(driverDO);
        when(carService.find(carId)).thenReturn(carDO);

        defaultDriverService.assignOrUnassignCar(driverId, carId, DriverAction.SELECT);
    }

    @Test(expected = CarAlreadyInUseException.class)
    public void cannotAssignCarAsItIsAlreadyInUse() throws EntityNotFoundException, CarNotAssignedToDriver, CarAlreadyInUseException, DriverAlreadyHasACarException, DriverNotOnline {
        DriverDO driverDO1 = new DriverDO("uname1", "pwd1");
        driverDO1.setId(1l);
        driverDO1.setOnlineStatus(OnlineStatus.OFFLINE);

        DriverDO driverDO2 = new DriverDO("uname2", "pwd2");
        driverDO2.setId(2l);

        CarDO carDO = new CarDO();
        carDO.setId(1l);
        carDO.setDriver(driverDO2);

        long driverId = 1;
        long carId = 1;
        when(driverRepository.findOne(driverId)).thenReturn(driverDO1);
        when(carService.find(carId)).thenReturn(carDO);

        defaultDriverService.assignOrUnassignCar(driverId, carId, DriverAction.SELECT);
    }

    @Test(expected = DriverAlreadyHasACarException.class)
    public void cannotAssignCarDriverAlreadyHasACar() throws EntityNotFoundException, CarNotAssignedToDriver, CarAlreadyInUseException, DriverAlreadyHasACarException, DriverNotOnline {
        DriverDO driverDO = new DriverDO("uname", "pwd");
        driverDO.setId(1l);
        driverDO.setOnlineStatus(OnlineStatus.OFFLINE);


        CarDO carDO = new CarDO();
        carDO.setId(1l);

        CarDO carDO2 = new CarDO();
        carDO2.setId(2l);

        driverDO.setCar(carDO2);

        long driverId = 1;
        long carId = 1;
        when(driverRepository.findOne(driverId)).thenReturn(driverDO);
        when(carService.find(carId)).thenReturn(carDO);

        defaultDriverService.assignOrUnassignCar(driverId, carId, DriverAction.SELECT);
    }

    @Test
    public void successfullyUnassignACarFromADriver() throws EntityNotFoundException, CarNotAssignedToDriver, CarAlreadyInUseException, DriverAlreadyHasACarException, DriverNotOnline {
        DriverDO driverDO = new DriverDO("uname", "pwd");
        driverDO.setId(1l);

        CarDO carDO = new CarDO();
        carDO.setId(1l);

        driverDO.setCar(carDO);

        long driverId = 1;
        long carId = 1;
        when(driverRepository.findOne(driverId)).thenReturn(driverDO);
        when(carService.find(carId)).thenReturn(carDO);

        defaultDriverService.assignOrUnassignCar(driverId, carId, DriverAction.DESELECT);
        assertThat(driverDO.getCar()).isNull();
    }

    @Test(expected = CarNotAssignedToDriver.class)
    public void cannotUnassignAsCarIsNotAssignedToDriver() throws EntityNotFoundException, CarNotAssignedToDriver, CarAlreadyInUseException, DriverAlreadyHasACarException, DriverNotOnline {
        DriverDO driverDO = new DriverDO("uname", "pwd");
        driverDO.setId(1l);

        CarDO carDO1 = new CarDO();
        carDO1.setId(1l);

        CarDO carDO2 = new CarDO();
        carDO2.setId(2l);

        driverDO.setCar(carDO2);

        long driverId = 1;
        long carId = 1;
        when(driverRepository.findOne(driverId)).thenReturn(driverDO);
        when(carService.find(carId)).thenReturn(carDO1);

        defaultDriverService.assignOrUnassignCar(driverId, carId, DriverAction.DESELECT);
    }

}
