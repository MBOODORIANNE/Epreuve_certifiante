package com.ecomove.ecoMove.service;

import com.ecomove.ecoMove.dto.request.VehicleRequest;
import com.ecomove.ecoMove.dto.response.VehicleResponse;
import java.util.List;

/**
 * Interface du service de gestion des véhicules.
 *
 * @author MBOO
 * @version 1.0.0
 * @since 2026-03-06
 */
public interface VehicleService {

    VehicleResponse addVehicle(Long ownerId, VehicleRequest request);

    VehicleResponse getVehicleById(Long id);

    List<VehicleResponse> getVehiclesByOwner(Long ownerId);

    VehicleResponse updateVehicle(Long id, Long ownerId, VehicleRequest request);

    void deleteVehicle(Long id, Long ownerId);
}
