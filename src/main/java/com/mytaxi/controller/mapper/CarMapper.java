package com.mytaxi.controller.mapper;

import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.domainobject.CarDO;

public class CarMapper {
    public static CarDO makeCarDO(CarDTO carDTO) {
        CarDO carDO = new CarDO();
        carDO.setLicensePlate(carDTO.getLicensePlate());
        carDO.setSeatCount(carDTO.getSeatCount());
        carDO.setConvertible(carDTO.getConvertible());
        carDO.setRating(carDTO.getRating());
        carDO.setEngineType(carDTO.getEngineType());
        carDO.setManufacturer(carDTO.getManufacturer());
        return carDO;
    }

    public static CarDTO makeCarDTO(CarDO carDO) {
        CarDTO carDTO = new CarDTO();
        carDTO.setLicensePlate(carDO.getLicensePlate());
        carDTO.setSeatCount(carDO.getSeatCount());
        carDTO.setConvertible(carDO.getConvertible());
        carDTO.setRating(carDO.getRating());
        carDTO.setEngineType(carDO.getEngineType());
        carDTO.setManufacturer(carDO.getManufacturer());
        carDTO.setDeleted(carDO.getDeleted());
        return carDTO;
    }
}
