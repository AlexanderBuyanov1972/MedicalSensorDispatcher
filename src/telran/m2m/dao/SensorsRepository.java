package telran.m2m.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.m2m.dto.Sensor;

public interface SensorsRepository extends MongoRepository<Sensor, String> {

}
