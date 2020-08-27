package com.fsd.sba.web.rest;

import com.fsd.sba.TrainingApp;
import com.fsd.sba.domain.Training;
import com.fsd.sba.repository.TrainingRepository;
import com.fsd.sba.service.TrainingService;
import com.fsd.sba.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.fsd.sba.web.rest.TestUtil.sameInstant;
import static com.fsd.sba.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TrainingResource} REST controller.
 */
@SpringBootTest(classes = TrainingApp.class)
public class TrainingResourceIT {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Integer DEFAULT_PROGRESS = 1;
    private static final Integer UPDATED_PROGRESS = 2;

    private static final Float DEFAULT_FEES = 1F;
    private static final Float UPDATED_FEES = 2F;

    private static final Float DEFAULT_COMMISSION_AMOUNT = 1F;
    private static final Float UPDATED_COMMISSION_AMOUNT = 2F;

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Float DEFAULT_AMOUNT_RECEIVED = 1F;
    private static final Float UPDATED_AMOUNT_RECEIVED = 2F;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Long DEFAULT_MENTOR_ID = 1L;
    private static final Long UPDATED_MENTOR_ID = 2L;

    private static final Long DEFAULT_SKILL_ID = 1L;
    private static final Long UPDATED_SKILL_ID = 2L;

    private static final String DEFAULT_RAZORPAY_PAYMENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_RAZORPAY_PAYMENT_ID = "BBBBBBBBBB";

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restTrainingMockMvc;

    private Training training;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TrainingResource trainingResource = new TrainingResource(trainingService);
        this.restTrainingMockMvc = MockMvcBuilders.standaloneSetup(trainingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Training createEntity(EntityManager em) {
        Training training = new Training()
            .status(DEFAULT_STATUS)
            .progress(DEFAULT_PROGRESS)
            .fees(DEFAULT_FEES)
            .commissionAmount(DEFAULT_COMMISSION_AMOUNT)
            .rating(DEFAULT_RATING)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .amountReceived(DEFAULT_AMOUNT_RECEIVED)
            .userId(DEFAULT_USER_ID)
            .mentorId(DEFAULT_MENTOR_ID)
            .skillId(DEFAULT_SKILL_ID)
            .razorpayPaymentId(DEFAULT_RAZORPAY_PAYMENT_ID);
        return training;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Training createUpdatedEntity(EntityManager em) {
        Training training = new Training()
            .status(UPDATED_STATUS)
            .progress(UPDATED_PROGRESS)
            .fees(UPDATED_FEES)
            .commissionAmount(UPDATED_COMMISSION_AMOUNT)
            .rating(UPDATED_RATING)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .amountReceived(UPDATED_AMOUNT_RECEIVED)
            .userId(UPDATED_USER_ID)
            .mentorId(UPDATED_MENTOR_ID)
            .skillId(UPDATED_SKILL_ID)
            .razorpayPaymentId(UPDATED_RAZORPAY_PAYMENT_ID);
        return training;
    }

    @BeforeEach
    public void initTest() {
        training = createEntity(em);
    }

    @Test
    @Transactional
    public void createTraining() throws Exception {
        int databaseSizeBeforeCreate = trainingRepository.findAll().size();

        // Create the Training
        restTrainingMockMvc.perform(post("/api/trainings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(training)))
            .andExpect(status().isCreated());

        // Validate the Training in the database
        List<Training> trainingList = trainingRepository.findAll();
        assertThat(trainingList).hasSize(databaseSizeBeforeCreate + 1);
        Training testTraining = trainingList.get(trainingList.size() - 1);
        assertThat(testTraining.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTraining.getProgress()).isEqualTo(DEFAULT_PROGRESS);
        assertThat(testTraining.getFees()).isEqualTo(DEFAULT_FEES);
        assertThat(testTraining.getCommissionAmount()).isEqualTo(DEFAULT_COMMISSION_AMOUNT);
        assertThat(testTraining.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testTraining.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testTraining.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testTraining.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testTraining.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testTraining.getAmountReceived()).isEqualTo(DEFAULT_AMOUNT_RECEIVED);
        assertThat(testTraining.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testTraining.getMentorId()).isEqualTo(DEFAULT_MENTOR_ID);
        assertThat(testTraining.getSkillId()).isEqualTo(DEFAULT_SKILL_ID);
        assertThat(testTraining.getRazorpayPaymentId()).isEqualTo(DEFAULT_RAZORPAY_PAYMENT_ID);
    }

    @Test
    @Transactional
    public void createTrainingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = trainingRepository.findAll().size();

        // Create the Training with an existing ID
        training.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrainingMockMvc.perform(post("/api/trainings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(training)))
            .andExpect(status().isBadRequest());

        // Validate the Training in the database
        List<Training> trainingList = trainingRepository.findAll();
        assertThat(trainingList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTrainings() throws Exception {
        // Initialize the database
        trainingRepository.saveAndFlush(training);

        // Get all the trainingList
        restTrainingMockMvc.perform(get("/api/trainings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(training.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].progress").value(hasItem(DEFAULT_PROGRESS)))
            .andExpect(jsonPath("$.[*].fees").value(hasItem(DEFAULT_FEES.doubleValue())))
            .andExpect(jsonPath("$.[*].commissionAmount").value(hasItem(DEFAULT_COMMISSION_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(sameInstant(DEFAULT_END_TIME))))
            .andExpect(jsonPath("$.[*].amountReceived").value(hasItem(DEFAULT_AMOUNT_RECEIVED.doubleValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].mentorId").value(hasItem(DEFAULT_MENTOR_ID.intValue())))
            .andExpect(jsonPath("$.[*].skillId").value(hasItem(DEFAULT_SKILL_ID.intValue())))
            .andExpect(jsonPath("$.[*].razorpayPaymentId").value(hasItem(DEFAULT_RAZORPAY_PAYMENT_ID)));
    }
    
    @Test
    @Transactional
    public void getTraining() throws Exception {
        // Initialize the database
        trainingRepository.saveAndFlush(training);

        // Get the training
        restTrainingMockMvc.perform(get("/api/trainings/{id}", training.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(training.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.progress").value(DEFAULT_PROGRESS))
            .andExpect(jsonPath("$.fees").value(DEFAULT_FEES.doubleValue()))
            .andExpect(jsonPath("$.commissionAmount").value(DEFAULT_COMMISSION_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.startTime").value(sameInstant(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.endTime").value(sameInstant(DEFAULT_END_TIME)))
            .andExpect(jsonPath("$.amountReceived").value(DEFAULT_AMOUNT_RECEIVED.doubleValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.mentorId").value(DEFAULT_MENTOR_ID.intValue()))
            .andExpect(jsonPath("$.skillId").value(DEFAULT_SKILL_ID.intValue()))
            .andExpect(jsonPath("$.razorpayPaymentId").value(DEFAULT_RAZORPAY_PAYMENT_ID));
    }

    @Test
    @Transactional
    public void getNonExistingTraining() throws Exception {
        // Get the training
        restTrainingMockMvc.perform(get("/api/trainings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTraining() throws Exception {
        // Initialize the database
        trainingService.save(training);

        int databaseSizeBeforeUpdate = trainingRepository.findAll().size();

        // Update the training
        Training updatedTraining = trainingRepository.findById(training.getId()).get();
        // Disconnect from session so that the updates on updatedTraining are not directly saved in db
        em.detach(updatedTraining);
        updatedTraining
            .status(UPDATED_STATUS)
            .progress(UPDATED_PROGRESS)
            .fees(UPDATED_FEES)
            .commissionAmount(UPDATED_COMMISSION_AMOUNT)
            .rating(UPDATED_RATING)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .amountReceived(UPDATED_AMOUNT_RECEIVED)
            .userId(UPDATED_USER_ID)
            .mentorId(UPDATED_MENTOR_ID)
            .skillId(UPDATED_SKILL_ID)
            .razorpayPaymentId(UPDATED_RAZORPAY_PAYMENT_ID);

        restTrainingMockMvc.perform(put("/api/trainings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTraining)))
            .andExpect(status().isOk());

        // Validate the Training in the database
        List<Training> trainingList = trainingRepository.findAll();
        assertThat(trainingList).hasSize(databaseSizeBeforeUpdate);
        Training testTraining = trainingList.get(trainingList.size() - 1);
        assertThat(testTraining.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTraining.getProgress()).isEqualTo(UPDATED_PROGRESS);
        assertThat(testTraining.getFees()).isEqualTo(UPDATED_FEES);
        assertThat(testTraining.getCommissionAmount()).isEqualTo(UPDATED_COMMISSION_AMOUNT);
        assertThat(testTraining.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testTraining.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testTraining.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testTraining.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testTraining.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testTraining.getAmountReceived()).isEqualTo(UPDATED_AMOUNT_RECEIVED);
        assertThat(testTraining.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testTraining.getMentorId()).isEqualTo(UPDATED_MENTOR_ID);
        assertThat(testTraining.getSkillId()).isEqualTo(UPDATED_SKILL_ID);
        assertThat(testTraining.getRazorpayPaymentId()).isEqualTo(UPDATED_RAZORPAY_PAYMENT_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingTraining() throws Exception {
        int databaseSizeBeforeUpdate = trainingRepository.findAll().size();

        // Create the Training

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainingMockMvc.perform(put("/api/trainings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(training)))
            .andExpect(status().isBadRequest());

        // Validate the Training in the database
        List<Training> trainingList = trainingRepository.findAll();
        assertThat(trainingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTraining() throws Exception {
        // Initialize the database
        trainingService.save(training);

        int databaseSizeBeforeDelete = trainingRepository.findAll().size();

        // Delete the training
        restTrainingMockMvc.perform(delete("/api/trainings/{id}", training.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Training> trainingList = trainingRepository.findAll();
        assertThat(trainingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Training.class);
        Training training1 = new Training();
        training1.setId(1L);
        Training training2 = new Training();
        training2.setId(training1.getId());
        assertThat(training1).isEqualTo(training2);
        training2.setId(2L);
        assertThat(training1).isNotEqualTo(training2);
        training1.setId(null);
        assertThat(training1).isNotEqualTo(training2);
    }
}
