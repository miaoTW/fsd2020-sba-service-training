package com.fsd.sba.web.rest;

import com.fsd.sba.domain.Training;
import com.fsd.sba.security.SecurityUtils;
import com.fsd.sba.service.TrainingService;
import com.fsd.sba.web.rest.errors.BadRequestAlertException;

import com.fsd.sba.web.rest.vm.training.ProposalVM;
import com.fsd.sba.web.rest.vm.training.UserTrainingVM;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.fsd.sba.domain.Training}.
 */
@RestController
@RequestMapping("/api")
public class TrainingResource {

    private final Logger log = LoggerFactory.getLogger(TrainingResource.class);

    private static final String ENTITY_NAME = "trainingTraining";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrainingService trainingService;

    public TrainingResource(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    /**
     * {@code POST  /trainings} : Create a new training.
     *
     * @param training the training to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new training, or with status {@code 400 (Bad Request)} if the training has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trainings")
    public ResponseEntity<Training> createTraining(@RequestBody Training training) throws URISyntaxException {
        log.debug("REST request to save Training : {}", training);
        if (training.getId() != null) {
            throw new BadRequestAlertException("A new training cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Training result = trainingService.save(training);
        return ResponseEntity.created(new URI("/api/trainings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /trainings} : Updates an existing training.
     *
     * @param training the training to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated training,
     * or with status {@code 400 (Bad Request)} if the training is not valid,
     * or with status {@code 500 (Internal Server Error)} if the training couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trainings")
    public ResponseEntity<Training> updateTraining(@RequestBody Training training) throws URISyntaxException {
        log.debug("REST request to update Training : {}", training);
        if (training.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Training result = trainingService.save(training);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, training.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /trainings} : get all the trainings.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trainings in body.
     */
    @GetMapping("/trainings")
    public ResponseEntity<List<Training>> getAllTrainings(Pageable pageable) {
        log.debug("REST request to get a page of Trainings");
        Page<Training> page = trainingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /trainings/:id} : get the "id" training.
     *
     * @param id the id of the training to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the training, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trainings/{id}")
    public ResponseEntity<Training> getTraining(@PathVariable Long id) {
        log.debug("REST request to get Training : {}", id);
        Optional<Training> training = trainingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(training);
    }

    /**
     * {@code DELETE  /trainings/:id} : delete the "id" training.
     *
     * @param id the id of the training to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trainings/{id}")
    public ResponseEntity<Void> deleteTraining(@PathVariable Long id) {
        log.debug("REST request to delete Training : {}", id);
        trainingService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @PostMapping(value = "/training/proposal")
    public Long sendProposal(@RequestBody ProposalVM proposal) {
        return trainingService.sendProposal(proposal);
    }


    @GetMapping(value = "/trainings/findByUser")
    public List<UserTrainingVM> findByUserId() {
        String userName = SecurityUtils.getCurrentUserLogin().orElseThrow( () ->  new RuntimeException("authenticate failed"));
        return trainingService.findByUserName(userName);
    }
}
