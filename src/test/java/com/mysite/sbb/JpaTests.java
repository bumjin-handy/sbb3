package com.mysite.sbb;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerRepository;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("JPA 연결 테스트")
@SpringBootTest
class JpaTests {

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@AfterAll
	@DisplayName("DB 데이터 클리어(삭제)")
	static void clean(@Autowired QuestionRepository questionRepository, @Autowired AnswerRepository answerRepository) {
		/*questionRepository.deleteAll();
		answerRepository.deleteAll();*/
	}
	@BeforeAll
	@DisplayName("초기 데이터 입력")
	static void setup(@Autowired QuestionRepository questionRepository, @Autowired AnswerRepository answerRepository) {
		/*Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다.");
		q1.setCreateDate(LocalDateTime.now());
		questionRepository.save(q1);  // 첫번째 질문 저장
		System.out.println("setup q1 id: "+q1.getId());
		Question q2 = new Question();
		q2.setSubject("스프링부트 모델 질문입니다.");
		q2.setContent("id는 자동으로 생성되나요?");
		q2.setCreateDate(LocalDateTime.now());
		questionRepository.save(q2);  // 두번째 질문 저장
		System.out.println("setup q2 id: "+q2.getId());*/
	}

	@AfterEach
	@DisplayName("DB 데이터 클리어(삭제)")
	void cleanEach() {
		questionRepository.deleteAll();
		answerRepository.deleteAll();
	}
	@BeforeEach
	@DisplayName("초기 데이터 입력")
	void setupEach() {
		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다.");
		q1.setCreateDate(LocalDateTime.now());
		questionRepository.save(q1);  // 첫번째 질문 저장

		Question q2 = new Question();
		q2.setSubject("스프링부트 모델 질문입니다.");
		q2.setContent("id는 자동으로 생성되나요?");
		q2.setCreateDate(LocalDateTime.now());
		questionRepository.save(q2);  // 두번째 질문 저장

	}

	@Test
	@DisplayName("모든 질문 데이터 조회 후 첫번쨰 질문의 제목 비교")
	void givenNothingWhenFindingAllThenReturnAllQuestion() {
		List<Question> all = questionRepository.findAll();
		assertEquals(2, all.size());

		Question q = all.get(0);
		assertEquals("sbb가 무엇인가요?", q.getSubject());
	}

	@Test
	@DisplayName("주어진 질문ID로 해당 질문 데이터 조회 후 제목비교")
	void givenQuestionIdWhenFindingQuestionThenReturnQuestion() {
		Optional<Question> oq = questionRepository.findById(1);
		if(oq.isPresent()) {
			Question q = oq.get();
			assertEquals("sbb가 무엇인가요?", q.getSubject());
		}
	}

	@Test
	@DisplayName("주어진 질문의 제목으로 해당 질문 데이터 조회 후 id비교")
	void givenQuestionSubjectWhenFindingQuestionThenReturnQuestion() {
		Question q = questionRepository.findBySubject("sbb가 무엇인가요?");
		System.out.println("q Id="+q.getId());
		assertEquals(1, q.getId());
	}

	@Test
	@Order(4)
	@DisplayName("주어진 질문의 제목과 내용으로 해당 질문 데이터 조회 후 id비교")
	void givenQuestionSubjectAndContentWhenFindingQuestionThenReturnQuestion() {
		Question q = questionRepository.findBySubjectAndContent(
				"sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
		assertEquals(1, q.getId());
	}

	@Test
	@DisplayName("주어진 질문의 제목으로 해당 질문 데이터 Like 검색 후 id비교")
	void givenQuestionSubjectWhenFindingQuestionThenReturnSubjectLikeQuestion() {
		List<Question> qList = questionRepository.findBySubjectLike("sbb%");
		Question q = qList.get(0);
		assertEquals("sbb가 무엇인가요?", q.getSubject());
	}

	@Disabled
	@Test
	@DisplayName("주어진 id에 해당하는 질문을 조회 후 해당 질문의 제목 수정")
	void givenQuestionIdWhenModifyQuestionSubjectThenWorksFine() {
		Optional<Question> oq = questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		q.setSubject("수정된 제목");
		questionRepository.save(q);
	}

	@Test
	@DisplayName("주어진 id에 해당하는 질문을 조회 후 해당 질문삭제")
	void givenQuestionIdWhenDeletingQuestionThenWorksFine() {
		assertEquals(2, questionRepository.count());
		Optional<Question> oq = questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		questionRepository.delete(q);
		assertEquals(1, questionRepository.count());
	}

	@Test
	@Order(8)
	@DisplayName("주어진 id에 해당하는 질문을 조회 후 해당 질문에 댓글 추가")
	void givenQuestionIdWhenAddAnswerThenWorksFine() {
		Optional<Question> oq = questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		assertEquals(2, q.getId());

		Answer a = new Answer();
		a.setContent("네 자동으로 생성됩니다.");
		a.setQuestion(q);  // 어떤 질문의 답변인지 알기위해서 Question 객체가 필요하다.
		a.setCreateDate(LocalDateTime.now());
		answerRepository.save(a);//두번째 질문에 대한 답변 저장
	}


	@Test
	@Order(9)
	@DisplayName("답변 데이터를 통해 질문 데이터 찾기 vs 질문 데이터를 통해 답변 데이터 찾기")
	void givenQuestionIdWhenFindAnswerListThenReturnAnswerList() {
		insertAnswer();

		try {
			Optional<Question> oq = questionRepository.findById(2);
			assertTrue(oq.isPresent());
			Question q = oq.get();
			System.out.println(q.getAnswerList());
			//q.getId()
			List<Answer> answerList = q.getAnswerList();
			assertEquals(1, q.getAnswerList().size());
			assertEquals("네 자동으로 생성됩니다.", q.getAnswerList().get(0).getContent());

		} catch (RuntimeException e) {
			Assertions.assertEquals("failed to lazily initialize a collection of role: com.mysite.sbb.question.Question.answerList: could not initialize proxy - no Session", e.getMessage());
		}
		//org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role: com.mysite.sbb.question.Question.answerList: could not initialize proxy - no Session

	}

	@Test
	@Order(10)
	@DisplayName("답변 데이터를 통해 질문 데이터 찾기 vs 질문 데이터를 통해 답변 데이터 찾기")
	@Transactional
	void givenQuestionIdWhenTransactionalFindAnswerListThenReturnAnswerList() {
		insertAnswer2();

		Optional<Question> oq = questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		System.out.println(q.getAnswerList().size());
		//q.getId()
		List<Answer> answerList = q.getAnswerList();
		assertEquals(1, q.getAnswerList().size());
		assertEquals("네 자동으로 생성됩니다.", q.getAnswerList().get(0).getContent());

		//org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role: com.mysite.sbb.question.Question.answerList: could not initialize proxy - no Session

	}

	private void insertAnswer() {
		Optional<Question> oq = questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		assertEquals(2, q.getId());

		Answer a = new Answer();
		a.setContent("네 자동으로 생성됩니다.");
		a.setQuestion(q);  // 어떤 질문의 답변인지 알기위해서 Question 객체가 필요하다.
		a.setCreateDate(LocalDateTime.now());
		answerRepository.save(a);//두번째 질문에 대한 답변 저장
	}

	private void insertAnswer2() {
		Optional<Question> oq = questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		assertEquals(2, q.getId());

		Answer a = new Answer();
		a.setContent("네 자동으로 생성됩니다.");
		a.setQuestion(q);  // 어떤 질문의 답변인지 알기위해서 Question 객체가 필요하다.
		a.setCreateDate(LocalDateTime.now());
		answerRepository.save(a);//두번째 질문에 대한 답변 저장

		q.addAnswer(a);

		questionRepository.save(q);


	}
}
