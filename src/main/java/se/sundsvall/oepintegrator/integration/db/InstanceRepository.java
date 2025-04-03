package se.sundsvall.oepintegrator.integration.db;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import se.sundsvall.oepintegrator.integration.db.model.InstanceEntity;
import se.sundsvall.oepintegrator.util.enums.IntegrationType;

@CircuitBreaker(name = "instance-repository")
public interface InstanceRepository extends JpaRepository<InstanceEntity, String> {

	List<InstanceEntity> findByMunicipalityId(String municipalityId);

	Optional<InstanceEntity> findByMunicipalityIdAndId(String municipalityId, String id);

	List<InstanceEntity> findByIntegrationType(IntegrationType integrationType);

	boolean existsByIdAndMunicipalityId(String instanceId, String municipalityId);
}
