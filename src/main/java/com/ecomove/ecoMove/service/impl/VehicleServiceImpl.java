package com.ecomove.ecoMove.service.impl;

import com.ecomove.ecoMove.dto.request.VehicleRequest;
import com.ecomove.ecoMove.dto.response.VehicleResponse;
import com.ecomove.ecoMove.entity.User;
import com.ecomove.ecoMove.entity.Vehicle;
import com.ecomove.ecoMove.exception.BadRequestException;
import com.ecomove.ecoMove.exception.ResourceNotFoundException;
import com.ecomove.ecoMove.exception.UnauthorizedException;
import com.ecomove.ecoMove.mapper.EntityMapper;
import com.ecomove.ecoMove.repository.UserRepository;
import com.ecomove.ecoMove.repository.VehicleRepository;
import com.ecomove.ecoMove.service.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des véhicules.
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@Service
public class VehicleServiceImpl implements VehicleService {

    private static final Logger logger = LoggerFactory.getLogger(VehicleServiceImpl.class);

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository, UserRepository userRepository) {
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public VehicleResponse addVehicle(Long ownerId, VehicleRequest request) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", ownerId));

        if (vehicleRepository.existsByLicensePlate(request.getLicensePlate())) {
            throw new BadRequestException("Un véhicule avec cette immatriculation existe déjà : " + request.getLicensePlate());
        }

        Vehicle vehicle = new Vehicle(request.getBrand(), request.getModel(), request.getLicensePlate(),
                request.getSeats(), request.getVehicleType(), owner);
        vehicle.setColor(request.getColor());
        vehicle.setFuelConsumption(request.getFuelConsumption());

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        logger.info("Véhicule ajouté - ID: {}, Propriétaire: {}", savedVehicle.getId(), owner.getFullName());

        return toVehicleResponse(savedVehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleResponse getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule", "id", id));
        return toVehicleResponse(vehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleResponse> getVehiclesByOwner(Long ownerId) {
        return vehicleRepository.findByOwnerId(ownerId).stream()
                .map(this::toVehicleResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VehicleResponse updateVehicle(Long id, Long ownerId, VehicleRequest request) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule", "id", id));

        if (!vehicle.getOwner().getId().equals(ownerId)) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à modifier ce véhicule");
        }

        vehicle.setBrand(request.getBrand());
        vehicle.setModel(request.getModel());
        vehicle.setColor(request.getColor());
        vehicle.setSeats(request.getSeats());
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setFuelConsumption(request.getFuelConsumption());

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        logger.info("Véhicule mis à jour - ID: {}", id);

        return toVehicleResponse(updatedVehicle);
    }

    @Override
    @Transactional
    public void deleteVehicle(Long id, Long ownerId) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule", "id", id));

        if (!vehicle.getOwner().getId().equals(ownerId)) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à supprimer ce véhicule");
        }

        vehicleRepository.delete(vehicle);
        logger.info("Véhicule supprimé - ID: {}", id);
    }

    private VehicleResponse toVehicleResponse(Vehicle vehicle) {
        VehicleResponse response = new VehicleResponse();
        response.setId(vehicle.getId());
        response.setBrand(vehicle.getBrand());
        response.setModel(vehicle.getModel());
        response.setColor(vehicle.getColor());
        response.setLicensePlate(vehicle.getLicensePlate());
        response.setSeats(vehicle.getSeats());
        response.setVehicleType(vehicle.getVehicleType().name());
        response.setFuelConsumption(vehicle.getFuelConsumption());
        response.setCreatedAt(vehicle.getCreatedAt());
        if (vehicle.getOwner() != null) {
            response.setOwnerId(vehicle.getOwner().getId());
            response.setOwnerName(vehicle.getOwner().getFullName());
        }
        return response;
    }
}
