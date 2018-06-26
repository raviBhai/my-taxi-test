package com.mytaxi.service.car;

import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mytaxi.dataaccessobject.CarRepository;
import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;

@Service
public class DefaultCarService implements CarService {

    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(DefaultCarService.class);

    private final CarRepository carRepository;

    public DefaultCarService(final CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    /**
     * Selects a car by id.
     *
     * @param carId
     * @return
     * @throws EntityNotFoundException if no car with given id is found
     */
    @Override
    public CarDO find(Long carId) throws EntityNotFoundException {
        return findCarChecked(carId);
    }

    /**
     *
     * Creates a new car.
     *
     * @param carDO
     * @return
     * @throws ConstraintsViolationException if car already exists with given license
     */
    @Override
    public CarDO create(CarDO carDO) throws ConstraintsViolationException {
        CarDO car;
        try {
            car = carRepository.save(carDO);
        } catch (DataIntegrityViolationException e) {
            LOG.warn("Some constraints are thrown due to car creation", e);
            throw new ConstraintsViolationException(e.getMessage());
        }
        return car;
    }

    /**
     *
     * Deletes an existing car.
     *
     * @param carId
     * @throws EntityNotFoundException if no car with given id is found
     */
    @Override
    @Transactional
    public void delete(Long carId) throws EntityNotFoundException {
        CarDO carDO = findCarChecked(carId);
        carDO.setDeleted(true);
    }

    /**
     *
     * Updates an existing car.
     * Updating Licence and Manufacturer is NOT allowed.
     *
     * @param carDTO
     * @throws EntityNotFoundException if no car with given id is found
     */
    @Override
    @Transactional
    public void update(CarDTO carDTO) throws EntityNotFoundException {
        CarDO carDO = findCarChecked(carDTO.getId());
        carDO.setSeatCount(carDTO.getSeatCount());
        carDO.setConvertible(carDTO.getConvertible());
        carDO.setRating(carDTO.getRating());
        carDO.setEngineType(carDTO.getEngineType());
    }

    private CarDO findCarChecked(Long carId) throws EntityNotFoundException {
        CarDO carDO = carRepository.findOne(carId);
        if (carDO == null) {
            throw new EntityNotFoundException("Could not find entity with id: " + carId);
        }
        return carDO;
    }
}
