package com.fsd.sba.service;

import com.fsd.sba.client.GatewayClient;
import com.fsd.sba.client.TechnologiesClient;
import com.fsd.sba.client.UserClient;
import com.fsd.sba.domain.Technologies;
import com.fsd.sba.domain.Training;
import com.fsd.sba.repository.TrainingRepository;
import com.fsd.sba.service.dto.UserDTO;
import com.fsd.sba.web.rest.vm.training.ProposalVM;
import com.fsd.sba.web.rest.vm.training.UserTrainingVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Training}.
 */
@Service
@Transactional
public class TrainingService {

    private final Logger log = LoggerFactory.getLogger(TrainingService.class);

    private final TrainingRepository trainingRepository;

    private final GatewayClient gatewayClient;
    private final TechnologiesClient technologiesClient;
    private UserClient userClient;

    public TrainingService(TrainingRepository trainingRepository, GatewayClient gatewayClient, UserClient userClient, TechnologiesClient technologiesClient) {
        this.trainingRepository = trainingRepository;
        this.userClient = userClient;
        this.gatewayClient = gatewayClient;
        this.technologiesClient = technologiesClient;
    }

    /**
     * Save a training.
     *
     * @param training the entity to save.
     * @return the persisted entity.
     */
    public Training save(Training training) {
        log.debug("Request to save Training : {}", training);
        return trainingRepository.save(training);
    }

    /**
     * Get all the trainings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Training> findAll(Pageable pageable) {
        log.debug("Request to get all Trainings");
        return trainingRepository.findAll(pageable);
    }


    /**
     * Get one training by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Training> findOne(Long id) {
        log.debug("Request to get Training : {}", id);
        return trainingRepository.findById(id);
    }

    /**
     * Delete the training by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Training : {}", id);
        trainingRepository.deleteById(id);
    }

    public Long sendProposal(ProposalVM proposal) {
        Training training = new Training();
        training.setMentorId(proposal.getMentorId());
        training.setSkillId(proposal.getSkillId());
        training.setUserId(proposal.getUserId());
        training.setStartDate(LocalDateTime.ofInstant(proposal.getStartDatetime(), ZoneOffset.UTC).toLocalDate());
        training.setStartTime(ZonedDateTime.ofInstant(proposal.getStartDatetime(), ZoneOffset.UTC));
        training.setEndDate(LocalDateTime.ofInstant(proposal.getEndDatetime(), ZoneOffset.UTC).toLocalDate());
        training.setEndTime(ZonedDateTime.ofInstant(proposal.getEndDatetime(), ZoneOffset.UTC));
        training.setProgress(0);
        training.setStatus("Proposal");
        training = trainingRepository.save(training);
        return training.getId();
    }

    public List<UserTrainingVM> findByUserName(String userName) {
        UserDTO user = gatewayClient.getUser(userName);
        return
            trainingRepository.findByUserIdAndStatus(user.getId(), "started").stream()
                .map(training -> {
                        UserTrainingVM userTrainingVM = new UserTrainingVM();
                        userTrainingVM.setMentor(userClient.findMentorNameById(training.getMentorId()));
                        ResponseEntity<Technologies> responseEntity = technologiesClient.getTechnologies(training.getSkillId());
                        if (responseEntity.getBody() != null) {
                            userTrainingVM.setSkill(responseEntity.getBody().getName());
                        }
                        userTrainingVM.setProgress(training.getProgress());
                        userTrainingVM.setRating(training.getRating());
                        userTrainingVM.setStatus(training.getStatus());
                        if (training.getStartTime() != null) {
                            userTrainingVM.setStart(training.getStartTime().format(DateTimeFormatter.ofPattern("MM/dd HH:mm")));
                        }
                        if (training.getEndTime() != null) {
                            userTrainingVM.setEnd(training.getEndTime().format(DateTimeFormatter.ofPattern("MM/dd HH:mm")));
                        }
                        return userTrainingVM;
                    }
                ).collect(Collectors.toList());
    }
}
