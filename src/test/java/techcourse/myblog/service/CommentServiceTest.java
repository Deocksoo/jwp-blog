package techcourse.myblog.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.controller.dto.CommentDto;
import techcourse.myblog.model.Article;
import techcourse.myblog.model.Comment;
import techcourse.myblog.model.User;
import techcourse.myblog.repository.CommentRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class CommentServiceTest {
    private static final String COMMENTS_CONTENTS = "comment_contents";
    private static final String COMMENTS_CONTENTS_2 = "comment_contents2";
    private static final Long TEST_COMMENT_ID = 1l;
    private static final Long TEST_ARTICLE_ID = 2l;
    private static final String USER_NAME = "test";
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "password!1";
    private static final String TITLE = "title";
    private static final String COVER_URL = "cover_url";
    private static final String CONTENTS = "contents";
    private static final User USER = new User(USER_NAME, EMAIL, PASSWORD);
    private static final Article ARTICLE = new Article(TITLE, COVER_URL, CONTENTS, USER);

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Test
    @DisplayName("comment 잘 저장한다.")
    void save() {
        CommentDto commentDto = new CommentDto();
        commentDto.setContents(COMMENTS_CONTENTS);
        commentService.save(USER, ARTICLE, commentDto);

        verify(commentRepository, atLeast(1)).save(new Comment(USER, ARTICLE, COMMENTS_CONTENTS));
    }

    @Test
    @DisplayName("Comment를 잘 조회한다.")
    void findById() {
        given(commentRepository.findById(TEST_COMMENT_ID))
                .willReturn(Optional.of(new Comment(USER, ARTICLE, COMMENTS_CONTENTS)));
        Comment foundComment = commentService.findById(TEST_COMMENT_ID);

        assertThat(foundComment).isEqualTo(new Comment(USER, ARTICLE, COMMENTS_CONTENTS));
    }

    @Test
    @DisplayName("comment를 업데이트 한다.")
    void update() {
        CommentDto commentDto = new CommentDto();
        commentDto.setArticleId(TEST_ARTICLE_ID);
        commentDto.setContents(COMMENTS_CONTENTS_2);

        given(commentRepository.findById(TEST_COMMENT_ID))
                .willReturn(Optional.of(new Comment(USER, ARTICLE, COMMENTS_CONTENTS)));

        Comment updatedComment = commentService.update(commentDto, TEST_COMMENT_ID);

        assertThat(updatedComment.getContents()).isEqualTo(COMMENTS_CONTENTS_2);
    }
}